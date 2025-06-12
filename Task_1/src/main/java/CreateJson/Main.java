package CreateJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static final InputStream PATH =
            Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties");

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final String KEY = "username";

    public static void main(String[] args) {

        // + - * /  - - -
        logger.debug("Current file encoding: {}", Charset.defaultCharset().displayName());

        logger.info("Start of the program.");

        Properties properties = new Properties();

        loadProps(properties);

        Message message = checkingPropKey(properties);

        if (message == null) {
            logger.warn("Property {} not found in properties file", KEY);
            System.exit(-1);
        }

        printResult(message);

        logger.info("End of the program.");
    }

    private static Message checkingPropKey(Properties properties) {
        logger.debug("Checking for property key: {}", KEY);
        return properties.getProperty(KEY) == null ? null : new Message(properties.getProperty(KEY));
    }

    private static void printResult(Message message) {
        String outputFormat = System.getProperty("output.format", "json");
        logger.info(outputFormat);

        try {
            ObjectMapper obj = "xml".equalsIgnoreCase(outputFormat) ? new XmlMapper() : new ObjectMapper();
            logger.info("Result: {}", obj.writeValueAsString(message.sayHello()));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize message.", e);
            throw new RuntimeException(e);
        }
    }

    private static void loadProps(Properties properties) {
        logger.debug("Attempting to load properties from file.");
        try {
            assert PATH != null;
            try (InputStreamReader reader = new InputStreamReader(PATH, StandardCharsets.UTF_8)) {
                properties.load(reader);
                logger.debug("Properties loaded successfully: {}", properties);
            }
        } catch (IOException e) {
            logger.error("Failed to load properties.", e);
            throw new RuntimeException(e);
        }
    }
}
