package practice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesLoader {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    private final Properties properties = new Properties();

    private static final String PATH = "config.properties";

    private final InputStream resourceAsStream =
            Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH);

    public Properties getProperties() {
        return properties;
    }

    public PropertiesLoader() {
        loadProps();
    }

    private void loadProps() {
        logger.debug("Trying to load properties from path: {}", PATH);

        if (resourceAsStream == null) {
            logger.warn("Failed to load properties: resource stream is null. Check the path: {}", PATH);
            return;
        }

        try (InputStreamReader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
            logger.debug("Properties loaded successfully: {}", properties);
        } catch (IOException e) {
            logger.error("Failed to load properties due to an IO exception: {}", e.getMessage());
        }
    }

}
