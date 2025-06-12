package shuba.practice.db.utils.db.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.db.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DbConnection {

    private static final Logger logger = LoggerFactory.getLogger(DbConnection.class);

    protected Connection connection;
    protected final Config config;

    protected DbConnection(Config config) {
        this.config = config;
        createConnectionToDB();
    }

    public void createConnectionToDB() {
        logger.info("Trying to connect to the database.");
        try {
            connection = DriverManager.getConnection(
                    config.getURL(),
                    config.getUser(),
                    config.getPassword()
            );
            logger.info("Successful connection to database.");
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed!", e);
        }
    }

    protected void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.warn("Connection is null or closed! Reconnecting...");
                createConnectionToDB();
            }
        } catch (SQLException e) {
            logger.error("Error while checking connection state: ", e);
        }
    }

    public void closeConnection() {
        logger.info("Trying to close connection...");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Connection closed successfully.");
            }
        } catch (SQLException e) {
            logger.error("Failed to close connection.", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
