//package shuba.practice.db.db.utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import shuba.practice.db.Main;
//import shuba.practice.db.dto.*;
//import shuba.practice.db.generation.GenerateRandomDTO;
//import shuba.practice.db.config.Config;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.*;
//
//public class DbInsertFunctions extends DbConnection {
//
//    private static final Logger logger = LoggerFactory.getLogger(DbInsertFunctions.class);
//
//    public static final int BATCH_SIZE = 10000;
//    public static final int TIMEOUT_FOR_POLL = 20;
//    public static final int N_THREADS = 3;
//
//    private final GenerateRandomDTO generator = new GenerateRandomDTO();
//
//    private final LinkedBlockingQueue<ProductDTO> productDTOS = new LinkedBlockingQueue<>();
//    private final LinkedBlockingQueue<CategoryDTO> categoriesDTOS = new LinkedBlockingQueue<>();
//    private final LinkedBlockingQueue<StoreDTO> storeDTOS = new LinkedBlockingQueue<>();
//    private final BlockingQueue<StoreProductDTO> storeProductDTOS = new LinkedBlockingQueue<>();
//
//    public DbInsertFunctions(Config config) {
//        super(config);
//    }
//
//    public int generateValidCategories(int COUNT) {
//        logger.info("Generating {} category records...", COUNT);
//        List<CategoryDTO> generatedCategories = generator.generateCategories(COUNT);
//        for (CategoryDTO category : generatedCategories) {
//            Map<String, String> violations = GenerateRandomDTO.validateDTO(category);
//            if (violations.isEmpty()) {
//                try {
//                    categoriesDTOS.put(category);
//                } catch (InterruptedException e) {
//                    logger.error("Error adding category to the queue: ", e);
//                }
//            }
//        }
//        logger.info("Finished generating valid category records. Total: {}", categoriesDTOS.size());
//        return categoriesDTOS.size();
//    }
//
//    public int generateValidProducts(int COUNT, int maxCategoryId) {
//        logger.info("Generating {} product records...", COUNT);
//        List<ProductDTO> generatedProducts = generator.generateProducts(COUNT, maxCategoryId);
//        for (ProductDTO product : generatedProducts) {
//            Map<String, String> violations = GenerateRandomDTO.validateDTO(product);
//            if (violations.isEmpty()) {
//                try {
//                    productDTOS.put(product); // Добавляем только валидные данные
//                } catch (InterruptedException e) {
//                    logger.error("Error adding product to the queue: ", e);
//                }
//            }
//        }
//        logger.info("Finished generating valid product records. Total: {}", productDTOS.size());
//        return productDTOS.size();
//    }
//
//    public int generateValidStores(int COUNT) {
//        logger.info("Generating {} store records...", COUNT);
//        List<StoreDTO> generatedStores = generator.generateStores(COUNT);
//        for (StoreDTO store : generatedStores) {
//            Map<String, String> violations = GenerateRandomDTO.validateDTO(store);
//            if (violations.isEmpty()) {
//                try {
//                    storeDTOS.put(store);
//                } catch (InterruptedException e) {
//                    logger.error("Error adding store to the queue: ", e);
//                }
//            }
//        }
//        logger.info("Finished generating valid store records. Total: {}", storeDTOS.size());
//        return storeDTOS.size();
//    }
//
//    public void generateStoreProducts(int COUNT, int maxStoreId, int maxProductId) {
//        logger.info("Generating {} store-product records...", COUNT);
//
//        int chunkSize = Math.max(1, COUNT / Main.N_THREADS);
//
//        try (ExecutorService genExecutor = Executors.newFixedThreadPool(Main.N_THREADS)) {
//
//            for (int i = 0; i < COUNT; i += chunkSize) {
//                int remainingRecords = COUNT - i;
//                int recordsToGenerate = Math.min(chunkSize, remainingRecords);
//                // 1s: 250.000 1.000.000  2s: 250.000 750.000  3s: 250.000 500.000  4s: 250.000 250.000  5s: if input was 1m.2 -> 250.000 2
//                genExecutor.execute(() -> generateStoreProductsChunk(recordsToGenerate, maxStoreId, maxProductId));
//            }
//
//            genExecutor.shutdown();
//            if (!genExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
//                genExecutor.shutdownNow();
//            }
//
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            logger.error("Error waiting for threads to finish: ", e);
//        }
//
//        logger.info("Finished generating store-product records. Total added to queue: {}", storeProductDTOS.size());
//    }
//
//    private void generateStoreProductsChunk(int COUNT, int maxStoreId, int maxProductId) {
//
//        LinkedBlockingQueue<StoreProductDTO> generatedProducts = generator.generateStoreProducts(COUNT, maxStoreId, maxProductId);
//
//        for (StoreProductDTO storeProduct : generatedProducts) {
//            try {
//                storeProductDTOS.put(storeProduct);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                logger.error("Error adding store-product to the queue: ", e);
//            }
//        }
//    }
//
//    public void insertCategories() {
//        logger.info("Starting insertion of {} records into categories...", categoriesDTOS.size());
//        ensureConnection();
//
//        String sql = "INSERT INTO categories (name) VALUES (?)";
//        int currentBatchSize = 0;
//
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            connection.setAutoCommit(false);
//
//            while (!categoriesDTOS.isEmpty()) {
//                CategoryDTO category = categoriesDTOS.poll(TIMEOUT_FOR_POLL, TimeUnit.MILLISECONDS);
//
//                if (category != null) {
//                    ps.setString(1, category.getName());
//                    ps.addBatch();
//                    currentBatchSize++;
//                }
//
//                if (currentBatchSize == BATCH_SIZE) {
//                    ps.executeBatch();
//                    connection.commit();
//                    currentBatchSize = 0;
//                }
//            }
//
//            if (currentBatchSize > 0) {
//                ps.executeBatch();
//                connection.commit();
//            }
//
//            logger.info("Inserted all records into categories.");
//        } catch (SQLException | InterruptedException e) {
//            connectionRollBack();
//            logger.error("Error inserting data into categories: ", e);
//        } finally {
//            connectionSetAutoCommitTrue();
//        }
//
//    }
//
//    public void insertProducts() {
//        logger.info("Starting insertion of {} records into products...", productDTOS.size());
//        ensureConnection();
//
//        String sql = "INSERT INTO products (name, category_id, price) VALUES (?, ?, ?)";
//        int currentBatchSize = 0;
//
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            connection.setAutoCommit(false);
//
//            while (!productDTOS.isEmpty()) {
//                ProductDTO product = productDTOS.poll(TIMEOUT_FOR_POLL, TimeUnit.MILLISECONDS);
//
//                if (product != null) {
//                    ps.setString(1, product.getName());
//                    ps.setInt(2, product.getCategoryId());
//                    ps.setBigDecimal(3, product.getPrice());
//                    ps.addBatch();
//                    currentBatchSize++;
//                }
//
//                if (currentBatchSize == BATCH_SIZE) {
//                    ps.executeBatch();
//                    connection.commit();
//                    currentBatchSize = 0;
//                }
//            }
//
//            if (currentBatchSize > 0) {
//                ps.executeBatch();
//                connection.commit();
//            }
//            logger.info("Inserted all records into products.");
//        } catch (SQLException | InterruptedException e) {
//            connectionRollBack();
//            logger.error("Error inserting data into products: ", e);
//        } finally {
//            connectionSetAutoCommitTrue();
//        }
//
//    }
//
//    public void insertStores() {
//        logger.info("Starting insertion of {} records into stores...", storeDTOS.size());
//        ensureConnection();
//
//        String sql = "INSERT INTO stores (name, location) VALUES (?, ?)";
//        int currentBatchSize = 0;
//
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            connection.setAutoCommit(false);
//
//            while (!storeDTOS.isEmpty()) {
////                StoreDTO store = storeDTOS.poll(TIMEOUT_FOR_POLL, TimeUnit.MILLISECONDS);
//                StoreDTO store = storeDTOS.take();
//
//                if (store == null && storeDTOS.isEmpty()) break;
//
//                if (store != null) {
//                    ps.setString(1, store.getName());
//                    ps.setString(2, store.getLocation());
//                    ps.addBatch();
//                    currentBatchSize++;
//                }
//
//                if (currentBatchSize == BATCH_SIZE) {
//                    ps.executeBatch();
//                    connection.commit();
//                    currentBatchSize = 0;
//                }
//            }
//
//            if (currentBatchSize > 0) {
//                ps.executeBatch();
//                connection.commit();
//            }
//            logger.info("Inserted all records into stores.");
//        } catch (SQLException | InterruptedException e) {
//            connectionRollBack();
//            logger.error("Error inserting data into stores: ", e);
//        } finally {
//            connectionSetAutoCommitTrue();
//        }
//
//    }
//
//    public void insertStoreProducts(Connection connection) {
//        logger.info("Starting insertion of {} records into store_products...", storeProductDTOS.size());
//        ensureConnection();
//
//        String sql = "INSERT INTO store_products (store_id, product_id, quantity) VALUES (?, ?, ?)";
//
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            connection.setAutoCommit(false);
//            int currentBatchSize = 0;
//
//            while (!storeProductDTOS.isEmpty()) {
//                StoreProductDTO storeProductDTO = storeProductDTOS.poll(TIMEOUT_FOR_POLL, TimeUnit.MILLISECONDS);
//                if (storeProductDTO == null) break;
//
//                ps.setInt(1, storeProductDTO.getStoreId());
//                ps.setInt(2, storeProductDTO.getProductId());
//                ps.setInt(3, storeProductDTO.getQuantity());
//                ps.addBatch();
//                currentBatchSize++;
//
//                if (currentBatchSize == BATCH_SIZE) {
//                    ps.executeBatch();
//                    connection.commit();
//                    currentBatchSize = 0;
//                    logger.info("Thread {} sent {} inserts into store products", Thread.currentThread().getName(), BATCH_SIZE);
//                }
//            }
//
//            if (currentBatchSize > 0) {
//                ps.executeBatch();
//                connection.commit();
//            }
//            logger.info("Inserted all records into stores.");
//
//        } catch (SQLException | InterruptedException e) {
//            connectionRollBack();
//            logger.error("Error inserting data into store_products: ", e);
//        } finally {
//            connectionSetAutoCommitTrue();
//        }
//    }
//
//    private void connectionSetAutoCommitTrue() {
//        try {
//            connection.setAutoCommit(true);
//        } catch (SQLException e) {
//            logger.error("Error resetting auto-commit: ", e);
//        }
//    }
//
//    private void connectionRollBack() {
//        try {
//            connection.rollback();
//        } catch (SQLException rollbackEx) {
//            logger.error("Error during transaction rollback: ", rollbackEx);
//        }
//    }
//
////    public void insertData(String sql, LinkedBlockingQueue<List<Object>> dataQueue) {
////        logger.info("Starting insertion of {} records...", dataQueue.size());
////        ensureConnection();
////
////        int currentBatchSize = 0;
////
////        try (PreparedStatement ps = connection.prepareStatement(sql)) {
////            connection.setAutoCommit(false);
////
////            while (!dataQueue.isEmpty()) {
////                List<Object> data = dataQueue.poll(TIMEOUT_FOR_POLL, TimeUnit.MILLISECONDS);
////
////                if (data != null) {
////                    for (int i = 0; i < data.size(); i++) {
////                        ps.setObject(i + 1, data.get(i));
////                    }
////                    ps.addBatch();
////                    currentBatchSize++;
////                }
////
////                if (currentBatchSize == BATCH_SIZE) {
////                    ps.executeBatch();
////                    currentBatchSize = 0;
////                }
////            }
////
////            if (currentBatchSize > 0) {
////                ps.executeBatch();
////            }
////
////            connection.commit();
////            logger.info("Inserted all records successfully.");
////        } catch (SQLException | InterruptedException e) {
////            connectionRollBack();
////            logger.error("Error inserting data: ", e);
////        } finally {
////            connectionSetAutoCommitTrue();
////        }
////    }
//
//}
