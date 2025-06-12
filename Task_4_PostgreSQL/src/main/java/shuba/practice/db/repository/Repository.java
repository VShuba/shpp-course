package shuba.practice.db.repository;

import shuba.practice.db.dto.ValidatableDTO;
import shuba.practice.db.setters.StatementSetter;

import java.sql.Connection;
import java.util.concurrent.BlockingQueue;


public interface Repository {
    int BATCH_SIZE = 10000;
    int COUNT_TO_LOG = 100000;

    void saveAll(BlockingQueue<ValidatableDTO> validDTOs,
                 Connection connection,
                 String tableName,
                 StatementSetter setter
    );
}
