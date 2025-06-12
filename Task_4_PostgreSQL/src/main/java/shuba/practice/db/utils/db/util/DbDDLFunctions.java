package shuba.practice.db.utils.db.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.db.config.Config;
import shuba.practice.db.utils.FileLoader;

import java.sql.*;

public class DbDDLFunctions extends DbConnection {

    private static final Logger logger = LoggerFactory.getLogger(DbDDLFunctions.class);

    public DbDDLFunctions(Config config) {
        super(config);
    }

    public void runDDLScript() {
        executeSQLScript(FileLoader.loadScript(config.getPassToCreateScript()), "Creating tables");
    }

    public void runDeleteSQLScript() {
        executeSQLScript(FileLoader.loadScript(config.getPassToDeleteSQLScript()), "Deleting tables");
    }

    public void runTruncateSQLScript() {
        executeSQLScript(FileLoader.loadScript(config.getPassToTruncateSQLScript()), "Truncating tables");
    }

    public void runCreateIndexScript() {
        executeSQLScript(FileLoader.loadScript(config.getPassToCreateIndexScript()), "Create Index");
    }

    protected void executeSQLScript(String script, String operation) {
        ensureConnection();

        if (script.isEmpty()) {
            logger.warn("{} script is empty, skipping execution.", operation);
            return;
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute(script);
            logger.info("{} script executed successfully.", operation);
        } catch (SQLException e) {
            logger.error("Error executing {} script: ", operation, e);
        }
    }
}
