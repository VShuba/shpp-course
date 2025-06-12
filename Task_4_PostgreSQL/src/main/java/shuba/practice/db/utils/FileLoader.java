package shuba.practice.db.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class FileLoader {
    private static final Logger logger = LoggerFactory.getLogger(FileLoader.class);

    private final Properties properties = new Properties();

    private static final String PROPS_NAME = "config.properties";

    private final InputStream resourceAsStream =
           getClass().getClassLoader().getResourceAsStream(PROPS_NAME);

    public Properties getProperties() {
        return properties;
    }

    public FileLoader() {
        loadProps();
    }

    protected void loadProps() {
        logger.debug("Trying to load properties: \"{}\"", PROPS_NAME);

        if (resourceAsStream == null) {
            logger.warn("Failed to load properties: resource stream is null: \"{}\"", PROPS_NAME);
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
