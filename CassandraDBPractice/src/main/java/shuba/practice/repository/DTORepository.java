package shuba.practice.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.config.Config;
import shuba.practice.dto.ValidatableDTO;
import shuba.practice.setters.Setter;
import shuba.practice.validation.ValidateDTO;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DTORepository implements Repository {

    private static final Logger logger = LoggerFactory.getLogger(DTORepository.class);
    private final Config config;
    private final CqlSession session;

    public DTORepository(Config config, CqlSession session) {
        this.session = session;
        this.config = config;
    }

    public void insertAllInSeveralThreads(Setter setter) {
        String sqlInsertScript = createCQLInsert(setter.getColumns(), setter.getTableName());
        PreparedStatement preparedStatement = session.prepare(sqlInsertScript);

        int totalCount = setter.getCount();
        int numThreads = config.getNumOfThreads();
        int batchSize = totalCount / numThreads;
        int remainder = totalCount % numThreads;

        AtomicInteger globalCounter = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newFixedThreadPool(numThreads)) {
            for (int i = 0; i < numThreads; i++) {
                int start = i * batchSize;
                int end = (i == numThreads - 1) ? start + batchSize + remainder : start + batchSize;

//                totalCount = 1010 -> numThreads -> 4 batchSize -> 1010 / 4 = 252 -> remainder -> 1010 % 4 = 2
//                The first thread processes data from 0 to 251 (252 items).
//                The second thread processes data from 252 to 503 (252 items).
//                The third thread processes data from 504 to 755 (252 items).
//                The fourth thread processes data from 756 to 1009 (254 elements, including the remainder).
                executor.submit(() ->
                        insertBatch(setter, start, end, preparedStatement, globalCounter)
                );
            }

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

    private void insertBatch(Setter setter, int start, int end, PreparedStatement preparedStatement, AtomicInteger globalCounter) {

        while (start < end) {
            ValidatableDTO dto = setter.getRandomDTO();

            if (ValidateDTO.validateDTO(dto).isEmpty()) {
                start++;
                BoundStatement boundStatement = setter.getBoundStatement(preparedStatement, dto);
                session.executeAsync(boundStatement);

                int count = globalCounter.incrementAndGet();
                logOut(setter, count);
            }
        }
    }

    @Override
    public void insertAll(Setter setter) {

        String sqlInsertScript = createCQLInsert(setter.getColumns(), setter.getTableName());

        PreparedStatement preparedStatement = session.prepare(sqlInsertScript);

        AtomicInteger aCounter = new AtomicInteger(0);

        while (aCounter.get() < setter.getCount()) {
            ValidatableDTO dto = setter.getRandomDTO();

            if (ValidateDTO.validateDTO(dto).isEmpty()) {
                aCounter.incrementAndGet();
                BoundStatement boundStatement = setter.getBoundStatement(preparedStatement, dto);

                session.execute(boundStatement);
                logOut(setter, aCounter.get());
            }
        }
    }

    private String createCQLInsert(String[] columns, String tableName) {
        String columnNames = String.join(", ", columns);
        String placeHolders = String.join(", ", Collections.nCopies(columns.length, "?"));

        return "INSERT INTO " + tableName + " ( " + columnNames + " ) VALUES ( " + placeHolders + " )";
    }

    private void logOut(Setter setter, int count) {
        if (count % config.getLogNumber() == 0 || count == setter.getCount()) {
            logger.info("Inserted {} {}", count, setter.getTableName());
        }
    }
}
