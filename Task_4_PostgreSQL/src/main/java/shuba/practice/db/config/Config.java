package shuba.practice.db.config;

import shuba.practice.db.utils.FileLoader;

import java.util.Properties;

public class Config {
    private final Properties properties;

    private static final String DEFAULT_URL = "jdbc:postgresql://pract4.cvyk60cg4aa4.eu-north-1.rds.amazonaws.com:5432/epicenterDBAWS?reWriteBatchedInserts=true";
    private static final String DEFAULT_USER = "vladimir";
    private static final String DEFAULT_PASSWORD = "Djdxbr2001"; // upd

    public static final String DEFAULT_COUNT_CATEGORIES = "100";
    public static final String DEFAULT_COUNT_PRODUCTS = "5000";
    public static final String DEFAULT_COUNT_STORES = "200";
    public static final String DEFAULT_COUNT_STORE_PRODUCTS = "3000000";

    private static final String DEFAULT_PASS_TO_CREATE_SQL_SCRIPT = "config/createTables.sql";
    private static final String DEFAULT_PASS_TO_TRUNCATE_SQL_SCRIPT = "config/truncateTables.sql";
    private static final String DEFAULT_PASS_TO_DELETE_SQL_SCRIPT = "config/deleteTables.sql";
    private static final String DEFAULT_PASS_TO_INDEX_SQL_SCRIPT = "config/indexTables.sql";

    public static final String DEFAULT_N_THREADS = "4";

    public Config() {
        properties = new FileLoader().getProperties();
    }

    public String getUser() {
        return properties.getProperty("db.user", DEFAULT_USER);
    }

    public String getURL() {
        return properties.getProperty("db.url", DEFAULT_URL);
    }

    public String getPassword() {
        return properties.getProperty("db.password", DEFAULT_PASSWORD);
    }

    public String getPassToTruncateSQLScript() {
        return properties.getProperty("pass.to.truncate.script", DEFAULT_PASS_TO_TRUNCATE_SQL_SCRIPT);
    }

    public String getPassToCreateScript() {
        return properties.getProperty("pass.to.create.script", DEFAULT_PASS_TO_CREATE_SQL_SCRIPT);
    }

    public String getPassToDeleteSQLScript() {
        return properties.getProperty("pass.to.delete.script", DEFAULT_PASS_TO_DELETE_SQL_SCRIPT);
    }

    public String getPassToCreateIndexScript() {
        return properties.getProperty("pass.to.index.script", DEFAULT_PASS_TO_INDEX_SQL_SCRIPT);
    }

    public int getCountCategories() {
        return Integer.parseInt(properties.getProperty("count.categories", DEFAULT_COUNT_CATEGORIES));
    }

    public int getCountProducts() {
        return Integer.parseInt(properties.getProperty("count.products", DEFAULT_COUNT_PRODUCTS));
    }

    public int getCountStores() {
        return Integer.parseInt(properties.getProperty("count.stores", DEFAULT_COUNT_STORES));
    }

    public int getCountStoreProducts() {
        return Integer.parseInt(properties.getProperty("count.store.products", DEFAULT_COUNT_STORE_PRODUCTS));
    }

    public int getNumOfThreads() {
        return Integer.parseInt(properties.getProperty("n.threads", DEFAULT_N_THREADS));
    }
}
