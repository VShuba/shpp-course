package shuba.practice;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DriverTimeoutException;
import com.datastax.oss.driver.api.core.servererrors.SyntaxError;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.config.Config;
import shuba.practice.connect.ConnectDatabase;
import shuba.practice.ddl.DDLDatabase;
import shuba.practice.dml.DMLDatabase;
import shuba.practice.repository.DTORepository;
import shuba.practice.setters.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static Config config;
    private static DTORepository repository;

    public static void main(String[] args) {

        logger.info("Start of the program FINAL");

        StopWatch globalStopWatch = new StopWatch();
        globalStopWatch.start();

        config = new Config();
        ConnectDatabase database = new ConnectDatabase(config);

        database.createSessionWithCustomTimeout(); // там 3 методи для створення (різниці не відчув)

        StopWatch ddlStopWatch = new StopWatch();
        ddlStopWatch.start();

        try {
            logger.info("------------------------- DDL PHASE -------------------------");

            CqlSession session = database.getSession();

            DDLDatabase ddlDatabase = new DDLDatabase(config, session);
            ddlDatabase.deleteTables();
            ddlDatabase.createTables();

            ddlStopWatch.stop();
            logger.info("DDL phase completed in: {} ms", ddlStopWatch.getTime(TimeUnit.MILLISECONDS));
            logger.info("KeySpace: {} Number of available nods: {}", session.getKeyspace(), database.getNumberOfNodes());

            logger.info("------------------------- THE END OF DDL PHASE -------------------------");

            StopWatch insertStopWatch = new StopWatch();
            insertStopWatch.start();
            logger.info("------------------------- INSERT PHASE -------------------------");

            repository = new DTORepository(config, session);

            Runnable insert = config.getIsParallelInsert() ?
                    Main::insertDataParallel :
                    Main::insertDataOneByOne;

            insert.run();

            insertStopWatch.stop();
            long totalRowsInserted = config.getTotalRows();
            double rps = totalRowsInserted / (insertStopWatch.getTime() / 1000.0);
            logger.info("Insert phase completed in: {} ms, RPS: {}", insertStopWatch.getTime(), rps);

            logger.info("------------------------- THE END OF INSERT PHASE -------------------------");
            logger.info("------------------------- QUERY PHASE -------------------------");

            DMLDatabase dmlDatabase = new DMLDatabase(config, session);

            StopWatch queryStopWatch = new StopWatch();
            queryStopWatch.start();
            dmlDatabase.countAllRecords();

            queryStopWatch.stop();
            logger.info("Query \"countAllRecords\" phase completed in: {} ms", queryStopWatch.getTime(TimeUnit.MILLISECONDS));
            queryStopWatch.reset();

            queryStopWatch.start();
            dmlDatabase.executeHardQuery();

            queryStopWatch.stop();
            logger.info("Query \"executeHardQuery\" phase completed in: {} ms", queryStopWatch.getTime(TimeUnit.MILLISECONDS));

            logger.info("------------------------- THE END OF QUERY PHASE -------------------------");

            globalStopWatch.stop();
            logger.info("Program completed in: {} ms", globalStopWatch.getTime(TimeUnit.MILLISECONDS));
        } catch (DriverTimeoutException | SyntaxError e) {
            logger.error("Smth going wrong: ", e);
        } finally {
            database.closeSession();
        }


    }

    private static void insertDataOneByOne() {
        insertData(new CategorySetter(config));
        insertData(new StoreSetter(config));
        insertData(new StoreProductSetter(config));
        insertData(new ProductTypeByStoreSetter(config));
    }

    public static void insertDataParallel() {

        try (ExecutorService executor = Executors.newFixedThreadPool(config.getNumOfThreads())) {

            executor.submit(() -> insertData(new CategorySetter(config)));
            executor.submit(() -> insertData(new StoreSetter(config)));
            executor.submit(() -> insertData(new StoreProductSetter(config)));
            executor.submit(() -> insertData(new ProductTypeByStoreSetter(config)));

            executor.shutdown();
            boolean terminated = executor.awaitTermination(5, TimeUnit.MINUTES);
            if (!terminated) {
                logger.warn("Not all tasks completed before timeout. Remaining tasks will be stopped.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("Executor interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }

    private static void insertData(Setter setter) {

        Runnable insert = config.getIsMultiThreadInsert() ?
                () -> repository.insertAllInSeveralThreads(setter) :
                () -> repository.insertAll(setter);

        logger.info("Starting to insert {}...", setter.getTableName());

        insert.run();
        logger.info("Finished inserting {}!", setter.getTableName());
    }
}
