package shuba.practice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private final Properties properties = new Properties();

    private static final String PROPS_NAME = "config.properties";

    private static final String DEFAULT_REQUEST_TIME_OUT = "60";
    private static final String DEFAULT_QUERY_TIME_OUT = "60";
    private static final String DEFAULT_VALUE_IS_PARALLEL = "true";
    private static final String DEFAULT_VALUE_IS_MULTI_THREAD_INSERT = "false";

    private static final String DEFAULT_PASS_TO_TOKEN_JSON = "src/main/resources/cassandraDB-token.json";
    private static final String DEFAULT_PASS_TO_SECURE_CONNECT_BUNDLE = "src/main/resources/secure-connect-cassandradb.zip";
    private static final String DEFAULT_PASS_TO_CREATE_TABLES_SCRIPT = "src/main/resources/createTables.sql";
    private static final String DEFAULT_PASS_TO_DELETE_TABLES_SCRIPT = "src/main/resources/deleteTables.sql";
    private static final String DEFAULT_PASS_TO_COUNT_TABLES_SCRIPT = "src/main/resources/countTables.sql";
    private static final String DEFAULT_PASS_TO_QUERY_TABLES_SCRIPT = "src/main/resources/queryTables.sql";
    private static final String DEFAULT_KEYSPACE = "epicenter";

    public static final String DEFAULT_COUNT_CATEGORIES = "100";
    public static final String DEFAULT_COUNT_STORES = "1234";
    public static final String DEFAULT_COUNT_PRODUCT_TYPE_BY_STORE = "1000000";
    public static final String DEFAULT_COUNT_STORE_PRODUCTS = "2000000";

    public static final String DEFAULT_N_THREADS = "4";

    public static final String DEFAULT_LOG_NUMBER = "100000";

    private final InputStream resourceAsStream =
            getClass().getClassLoader().getResourceAsStream(PROPS_NAME);

    public Config() {
        loadProps();
    }

    public String getPassToCreateTablesScript() {
        return properties.getProperty("pass.to.create.script", DEFAULT_PASS_TO_CREATE_TABLES_SCRIPT);
    }

    public String getPassToDeleteTablesScript() {
        return properties.getProperty("pass.to.delete.script", DEFAULT_PASS_TO_DELETE_TABLES_SCRIPT);
    }

    public String getPassToCountRecordsScript() {
        return properties.getProperty("pass.to.count.script", DEFAULT_PASS_TO_COUNT_TABLES_SCRIPT);
    }

    public String getPassToQueryScript() {
        return properties.getProperty("pass.to.query.script", DEFAULT_PASS_TO_QUERY_TABLES_SCRIPT);
    }

    public String getPassTokenJson() {
        return properties.getProperty("pass.to.token", DEFAULT_PASS_TO_TOKEN_JSON);
    }

    public String getPassSecureConnectBundle() {
        return properties.getProperty("pass.to.secure.connect.bundle", DEFAULT_PASS_TO_SECURE_CONNECT_BUNDLE);
    }

    public String getKeySpace() {
        return properties.getProperty("key.space", DEFAULT_KEYSPACE);
    }

    public int getCountCategories() {
        return Integer.parseInt(properties.getProperty("count.categories", DEFAULT_COUNT_CATEGORIES));
    }

    public int getProductTypeByStore() {
        return Integer.parseInt(properties.getProperty("count.product.type.by.store", DEFAULT_COUNT_PRODUCT_TYPE_BY_STORE));
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

    public long getRequestTimeOut() {
        return Long.parseLong(properties.getProperty("request.time.out", DEFAULT_REQUEST_TIME_OUT));
    }

    public long getQueryTimeOut() {
        return Long.parseLong(properties.getProperty("query.time.out", DEFAULT_QUERY_TIME_OUT));
    }

    public long getTotalRows() {
        return (long) getCountCategories() + getCountStores() + getCountStoreProducts() + getProductTypeByStore();
    }

    public boolean getIsParallelInsert() {
        String value = properties.getProperty("is.parallel.insert", DEFAULT_VALUE_IS_PARALLEL);

        boolean isParallel = Boolean.parseBoolean(value);
        logger.info(isParallel ? "Using \"parallel\" insert." : "Using insert \"one by one\".");

        return isParallel;
    }

    public boolean getIsMultiThreadInsert() {
        String value = properties.getProperty("is.multi.thread.insert", DEFAULT_VALUE_IS_MULTI_THREAD_INSERT);

        boolean isParallel = Boolean.parseBoolean(value);
        logger.info(isParallel ? "Using \"multi thread\" insert." : "Using \"only one thread\" insert .");

        return isParallel;
    }

    public int getLogNumber() {
        return Integer.parseInt(properties.getProperty("log.number", DEFAULT_LOG_NUMBER));
    }

    private void loadProps() {
        logger.debug("Trying to load properties: \"{}\"", PROPS_NAME);

        if (resourceAsStream == null) {
            logger.warn("Failed to load properties: resource stream is null: \"{}\"", PROPS_NAME);
            logger.warn("Using DEFAULT VALUES");
            return;
        }

        try (InputStreamReader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
            logger.debug("Properties loaded successfully: {}", properties);
        } catch (IOException e) {
            logger.error("Failed to load properties due to an IO exception: {}", e.getMessage());
        }
    }

    public static String loadScript(String filePath) {
        logger.info("Trying to load script from \"{}\"", filePath);

        try {
            return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Failed to read file from \"{}\"", filePath, e);
            return "";
        }
    }
}
