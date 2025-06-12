package simplecalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

class PropertiesModifier {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesModifier.class);

    private static InputStream resourceAsStream =
            Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties");

    private PropertiesModifier() {
        throw new IllegalStateException("Utility class");
    }

    public static void load(Properties properties) throws IOException {


        // Если файл не найден в classpath, попробуем загрузить его по фактическому пути
        if (resourceAsStream == null) {
            String actualPath = "src/main/resources/app.properties"; // Укажите фактический путь к файлу
            logger.warn("Could not find 'app.properties' in classpath. Trying to load from actual path: {}", actualPath);

            try {
                resourceAsStream = new FileInputStream(actualPath);
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Could not find 'app.properties' in classpath or at: " + actualPath);
            }
        }

        // Загружаем свойства из найденного InputStream
        try (InputStreamReader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
            logger.debug("Properties loaded successfully: {}", properties);
        } catch (IOException e) {
            throw new IOException("Failed to load properties file.", e);
        }
    }

    protected static Number getMin(Properties properties, String type) {
        return validateAndParse(properties.getProperty("min", "1"), type);
    }

    protected static Number getMax(Properties properties, String type) {
        return validateAndParse(properties.getProperty("max", "10"), type);
    }

    protected static Number getIncrement(Properties properties, String type) {
        return validateAndParse(properties.getProperty("increment", "1"), type);
    }


    private static Number validateAndParse(String value, String type) {
        try {
            return switch (type) {
                case "byte" -> Byte.valueOf(value);
                case "short" -> Short.valueOf(value);
                case "int" -> Integer.valueOf(value);
                case "float" -> Float.valueOf(value);
                case "double" -> Double.valueOf(value);
                default -> throw new IllegalArgumentException("Unsupported type: " + type);
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for type " + type + ": " + value);
        }
    }

    protected static void validateProperties(String type, Number min, Number max, Number increment) {

        logger.info("Checking all cases for valid properties");

        if (min == null || max == null || increment == null) {
            throw new IllegalArgumentException("Properties min, max, and increment must not be null.");
        }

        logger.debug("Validating properties: type={}, min={}, max={}, increment={}", type, min, max, increment);

        // bound of types
        double minBound;
        double maxBound;

        switch (type) {
            case "byte" -> {
                minBound = Byte.MIN_VALUE;
                maxBound = Byte.MAX_VALUE;
            }
            case "short" -> {
                minBound = Short.MIN_VALUE;
                maxBound = Short.MAX_VALUE;
            }
            case "int" -> {
                minBound = Integer.MIN_VALUE;
                maxBound = Integer.MAX_VALUE;
            }
            case "float" -> {
                minBound = -Float.MAX_VALUE;
                maxBound = Float.MAX_VALUE;
            }
            case "double" -> {
                minBound = -Double.MAX_VALUE;
                maxBound = Double.MAX_VALUE;
            }
            default -> {
                logger.error("Unsupported type: {}", type);
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }

        validateLogicalBounds(min, max, increment);

        // type matching check
        validateTypeCompatibility(type, min, "Min");
        validateTypeCompatibility(type, max, "Max");
        validateTypeCompatibility(type, increment, "Increment");

        // check for borders
        checkValueInRange(min, type, minBound, maxBound, "Min");
        checkValueInRange(max, type, minBound, maxBound, "Max");

        // check if increment = 0
        if (increment.doubleValue() == 0) {
            logger.error("Increment cannot be zero.");
            throw new IllegalArgumentException("Increment cannot be zero.");
        }

        logger.debug("Validation passed for type={}, min={}, max={}, increment={}", type, min, max, increment);
    }

    private static void validateLogicalBounds(Number min, Number max, Number increment) {
        double minValue = min.doubleValue();
        double maxValue = max.doubleValue();
        double incrementValue = increment.doubleValue();

        // Проверка на соответствие направления инкремента
        if ((incrementValue > 0 && minValue > maxValue) || (incrementValue < 0 && minValue < maxValue)) {
            logger.error("Logical mismatch: min={}, max={}, increment={}", minValue, maxValue, incrementValue);
            throw new IllegalArgumentException(
                    String.format("Logical mismatch: increment=%s does not align with min=%s and max=%s", incrementValue, minValue, maxValue));
        }
    }

    private static void checkValueInRange(Number value, String type, double minBound, double maxBound, String valueType) {
        if (value.doubleValue() < minBound || value.doubleValue() > maxBound) {
            logger.error("{} value out of range for type {}: {}={}, bounds=[{}, {}]", valueType, type, valueType, value, minBound, maxBound);
            throw new IllegalArgumentException(
                    String.format("%s value out of range for type %s: %s=%s, bounds=[%s, %s]", valueType, type, valueType, value, minBound, maxBound));
        }
    }

    private static void validateTypeCompatibility(String type, Number value, String valueType) {
        boolean isValid = switch (type) {
            case "byte" -> value instanceof Byte;
            case "short" -> value instanceof Short;
            case "int" -> value instanceof Integer;
            case "float" -> value instanceof Float;
            case "double" -> value instanceof Double;
            default -> false;
        };

        if (!isValid) {
            logger.error("{} does not match the expected type {}. Value: {}", valueType, type, value);
            throw new IllegalArgumentException(
                    String.format("%s value does not match the expected type %s. Provided: %s", valueType, type, value));
        }
    }
}
