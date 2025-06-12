package shuba.practice.db;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.db.dto.*;
import shuba.practice.db.repository.Repository;
import shuba.practice.db.setters.*;
import shuba.practice.db.utils.db.util.DbDDLFunctions;
import shuba.practice.db.utils.db.util.DbQueries;
import shuba.practice.db.generation.GenerateRandomDTO;
import shuba.practice.db.repository.DTORepository;
import shuba.practice.db.config.Config;

import java.sql.Connection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static final String DEFAULT_SEARCHED_CATEGORY = "Одяг";

    public static void main(String[] args) {

        StopWatch totalWatch = new StopWatch();
        totalWatch.start();

        Config config = new Config();

        int nThreads = config.getNumOfThreads();
        logger.info("DB name: \"{}\"  Count of Threads: {}", config.getURL(), nThreads);


        logger.info("------------------- DDL PHASE -------------------");

        DbDDLFunctions ddl = new DbDDLFunctions(config);
        ddl.runDeleteSQLScript();
        ddl.runDDLScript();

        StopWatch dataGenerationWatch = new StopWatch();
        dataGenerationWatch.start();


        try (ExecutorService executor = Executors.newFixedThreadPool(nThreads)) {

            // 1: categories, stores  - PARALLEL
            // 2: products <depends on> categories
            // 3: store_product <depends on> products, stores

            GenerateRandomDTO generateRandomDTO = new GenerateRandomDTO(config);
            DTORepository dtoRepository = new DTORepository();

            BlockingQueue<ValidatableDTO> storeDTOS = new ArrayBlockingQueue<>(config.getCountStores()); // 500
            BlockingQueue<ValidatableDTO> categoryDTOS = new ArrayBlockingQueue<>(config.getCountCategories()); // 200
            BlockingQueue<ValidatableDTO> productDTOS = new ArrayBlockingQueue<>(config.getCountProducts()); // 10000
            BlockingQueue<ValidatableDTO> storeProductDTOS = new ArrayBlockingQueue<>(config.getCountStoreProducts()); // 3m
            // new LinkedBlockingQueue<>() THAT WAS TOO HARD FOR MEMORY IT STACKS IN 2.600.000


            logger.info("------------------- GENERATION PHASE -------------------");

            CountDownLatch generateStoreAndCategoryLatch = new CountDownLatch(2);

            StopWatch watch = new StopWatch();

            logger.info("Start of generating stores");
            watch.start();

            executor.submit(() -> generateRandomDTO.generateAndValidateDTOs(storeDTOS, config.getCountStores(), generateStoreAndCategoryLatch, "store"));

            logger.info("Start of generating categories");
            executor.submit(() -> generateRandomDTO.generateAndValidateDTOs(categoryDTOS, config.getCountCategories(), generateStoreAndCategoryLatch, "category"));

            generateStoreAndCategoryLatch.await(); // wait parallel for stores and categories tables

            watch.stop();
            logger.info("Finish of generating stores and categories tables in: {} ms", watch.getTime());

            CountDownLatch generateProductsLatch = new CountDownLatch(1);

            logger.info("Start of generating products table");
            watch.reset();
            watch.start();

            executor.submit(() -> generateRandomDTO.generateAndValidateDTOs(productDTOS, config.getCountProducts(), generateProductsLatch, "product"));

            generateProductsLatch.await(); // wait for products table

            watch.stop();
            logger.info("Finish of generating products table in: {} ms", watch.getTime());

            AtomicInteger totalGenerated = new AtomicInteger(0);

            CountDownLatch generateProductsStoreLatch = new CountDownLatch(nThreads);

            logger.info("Start of generating store_products table");
            watch.reset();
            watch.start();

            for (int i = 0; i < nThreads; i++) {
                executor.submit(() -> {
                    try {
                        while (true) {
                            int currentCount = totalGenerated.incrementAndGet();
                            if (currentCount > config.getCountStoreProducts()) {
                                break;
                            }
                            try {
                                storeProductDTOS.put(generateRandomDTO.generateStoreProductDTO());
                            } catch (Exception e) {
                                logger.error("Failed to generate into store_products: ", e);
                            }
                        }
                    } finally {
                        generateProductsStoreLatch.countDown();
                    }
                });
            }

            generateProductsStoreLatch.await(); // wait for store products table

            watch.stop();
            logger.info("Finish of generating storeProducts table in: {} ms", watch.getTime());

            dataGenerationWatch.stop();
            logger.info("Finished to generate all data in: {} ms", dataGenerationWatch.getTime());

            logger.info("Stores: {} , Products {} , Categories {} , StoreProducts {}",
                    storeDTOS.size(), productDTOS.size(), categoryDTOS.size(), storeProductDTOS.size());

            logger.info("------------------- INSERT PHASE -------------------");

            StopWatch insertWatch = new StopWatch();
            insertWatch.start();

            logger.info("Start of insert data into stores and categories");

            CountDownLatch insertStoreAndCategoryLatch = new CountDownLatch(2);

            executor.submit(() -> {
                dtoRepository.saveAll(storeDTOS, ddl.getConnection(), "stores", new StoreSetter());
                insertStoreAndCategoryLatch.countDown();
            });

            executor.submit(() -> {
                dtoRepository.saveAll(categoryDTOS, ddl.getConnection(), "categories", new CategorySetter());
                insertStoreAndCategoryLatch.countDown();
            });

            insertStoreAndCategoryLatch.await(); // wait for stores and categories insert

            logger.info("Finish of inserting data into stores and categories");

            logger.info("Start of inserting products table");

            CountDownLatch insertProductsLatch = new CountDownLatch(1);

            executor.submit(() -> {
                dtoRepository.saveAll(productDTOS, ddl.getConnection(), "products", new ProductSetter());
                insertProductsLatch.countDown();
            });

            insertProductsLatch.await(); // wait for products insert

            logger.info("Finish of inserting products table");

            logger.info("Start of inserting store_products table by {} threads", nThreads);

            CountDownLatch insertStoreProductsLatch = new CountDownLatch(nThreads);

            for (int i = 0; i < nThreads; i++) {
                executor.submit(() -> {
                    try (Connection connection = new DbQueries(config).getConnection()) {
                        dtoRepository.saveAll(storeProductDTOS, connection, "store_products", new StoreProductSetter());
                    } catch (Exception e) {
                        logger.error("Failed to insert into store_products: ", e);
                    } finally {
                        insertStoreProductsLatch.countDown();
                    }
                });
            }

            insertStoreProductsLatch.await();
            insertWatch.stop();

            logger.info("Finish of inserting store_products table in: {} ms", insertWatch.getTime());

            executor.shutdown();
            logger.info("The end of the insert to DataBase");

            if (!executor.awaitTermination(120, TimeUnit.SECONDS)) {
                logger.info("Force closing executors");
                executor.shutdownNow();
            }

            long totalRowsInserted = (long) config.getCountCategories()
                    + config.getCountStores() + config.getCountStoreProducts() + config.getCountProducts();

            double rps = totalRowsInserted / (insertWatch.getTime() / 1000.0);
            logger.info("Data inserted with a speed of RPS: {} rows per second", rps);

        } catch (Exception e) {
            logger.error("An error occurred during execution: ", e);
        } finally {

            logger.info("------------------- QUERY PHASE -------------------");

            String searchedCategory = System.getProperty("search", DEFAULT_SEARCHED_CATEGORY);

            StopWatch queryWatch = new StopWatch();
            queryWatch.start();

            logger.info("Start of searching the most COUNT of products with category: {}", searchedCategory);

            DbQueries queries = new DbQueries(config);
            String result = queries.getStoreAddressWithMostProducts(searchedCategory); // 5 s

            queryWatch.stop();
            logger.info("The end of the searching. Result: \"{}\" With executing time: {} ms",
                    result,
                    queryWatch.getTime()
            );
            queryWatch.reset();

            queryWatch.start();
            logger.info("Start of creating index");
            ddl.runCreateIndexScript(); // 100 s
            queryWatch.stop();
            logger.info("Finish of creating index with time: {} ms", queryWatch.getTime(TimeUnit.MILLISECONDS));

            queryWatch.reset();
            queryWatch.start();
            logger.info("Start of searching WITH INDEX the most COUNT of products with category: {} ", searchedCategory);
            String result2 = queries.getStoreAddressWithMostProducts(searchedCategory); // 100 s

            queryWatch.stop();
            logger.info("The end of the searching. Result: \"{}\" With executing time: {} ms",
                    result2,
                    queryWatch.getTime()
            );

            queryWatch.reset();
            queries.closeConnection();
            ddl.closeConnection();
            totalWatch.stop();
            logger.info("Program finished in: {} ms", totalWatch.getTime());
        }
    }
}
