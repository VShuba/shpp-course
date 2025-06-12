package shuba.practice.db.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.db.dto.ValidatableDTO;
import shuba.practice.db.setters.StatementSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;

public class DTORepository implements Repository {

    private static final Logger logger = LoggerFactory.getLogger(DTORepository.class);

    @Override
    public void saveAll(BlockingQueue<ValidatableDTO> validDTOs,
                        Connection connection,
                        String tableName,
                        StatementSetter setter
    ) {
        String sql = createSQLInsert(tableName, setter.getColumns());

        int currentBatchSize = 0;
        int counterToLog = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            logger.info("Start to insert data into {}", tableName);

            while (!validDTOs.isEmpty()) {

                ValidatableDTO validDTO = validDTOs.poll();

                if (validDTO != null) {
                    setter.setParams(ps, validDTO);

                    ps.addBatch();

                    currentBatchSize++;
                    counterToLog++;

                    if (currentBatchSize == BATCH_SIZE) {
                        ps.executeBatch();
                        currentBatchSize = 0;
                    }

                    if (counterToLog == COUNT_TO_LOG) {
                        logger.info("Sent {} DTOs by: {}", counterToLog, Thread.currentThread().getName());
                        counterToLog = 0;
                    }
                }
            }
            if (currentBatchSize > 0 || counterToLog > 0) {
                ps.executeBatch();
                logger.info("Sent {} remaining DTOs by: {}", Math.max(currentBatchSize, counterToLog), Thread.currentThread().getName());
            }

            logger.info("Finish to insert data into {} by: {}", tableName, Thread.currentThread().getName());
        } catch (SQLException e) {
            logger.error("Failed to insert DTO into {}: ", tableName, e);
        }
    }

    private String createSQLInsert(String tableName, String[] columns) {
        String columnNames = String.join(", ", columns);
        String placeHolders = String.join(", ", Collections.nCopies(columns.length, "?"));

        return "INSERT INTO " + tableName + " ( " + columnNames + " ) VALUES ( " + placeHolders + " )";
    }
}