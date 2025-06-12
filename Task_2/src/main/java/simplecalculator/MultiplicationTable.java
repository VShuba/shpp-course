package simplecalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiplicationTable {

    private static final Logger logger = LoggerFactory.getLogger(MultiplicationTable.class);

    private MultiplicationTable() {
        throw new IllegalStateException("Utility class");
    }

    protected static void printTable(String numberType, Number min, Number max, Number increment) {
        logger.info("Generating multiplication table for type: {}", numberType);

        double minValue = min.doubleValue();
        double maxValue = max.doubleValue();
        double incrementValue = increment.doubleValue();

        for (double i = minValue; (incrementValue > 0 ? i <= maxValue : i >= maxValue); i += incrementValue) {
            for (double j = minValue; (incrementValue > 0 ? j <= maxValue : j >= maxValue); j += incrementValue) {
                try {
                    Number result = calculate(numberType, i, j);
                    // Форматирование вывода
                    String formattedOutput = formatOutput(numberType, i, j, result);
                    logger.info(formattedOutput);
                } catch (ArithmeticException e) {
                    logger.info("{} * {} = Overflow", i, j);
                }
            }
        }
    }

    private static String formatOutput(String numberType, double i, double j, Number result) {
        // Проверка типа для целочисленных типов
        boolean isIntegerType = switch (numberType) {
            case "byte", "short", "int" -> true;
            default -> false;
        };

        if (isIntegerType) {
            // Вывод в формате decimal
            return String.format("%d * %d = %d", (int) i, (int) j, result.intValue());
        } else {
            // Вывод в формате с плавающей точкой
            return String.format("%.2f * %.2f = %.2f", i, j, result.doubleValue());
        }
    }

    protected static Number calculate(String type, double a, double b) {
        return switch (type) {
            case "byte" -> checkIntegerOverflow((long) a, (long) b, Byte.MIN_VALUE, Byte.MAX_VALUE, "byte");
            case "short" -> checkIntegerOverflow((long) a, (long) b, Short.MIN_VALUE, Short.MAX_VALUE, "short");
            case "int" -> checkIntegerOverflow((long) a, (long) b, Integer.MIN_VALUE, Integer.MAX_VALUE, "int");
            case "float" ->
                    checkFloatingPointOverflow((float) a, (float) b, -Float.MAX_VALUE, Float.MAX_VALUE, "float");
            case "double" -> checkFloatingPointOverflow(a, b, -Double.MAX_VALUE, Double.MAX_VALUE, "double");
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    protected static Number checkIntegerOverflow(long a, long b, long min, long max, String type) {
        long result = a * b;

        if (result > max || result < min) {
            throw new ArithmeticException(String.format("Overflow occurred for type %s: %d * %d = %d", type, a, b, result));
        }

        return switch (type) {
            case "byte" -> (byte) result;
            case "short" -> (short) result;
            case "int" -> (int) result;
            default -> throw new IllegalArgumentException("Unsupported integer type: " + type);
        };
    }

    protected static Number checkFloatingPointOverflow(double a, double b, double min, double max, String type) {
        double result = a * b;

        if (result > max || result < min) {
            throw new ArithmeticException(String.format("Overflow occurred for type %s: %.2f * %.2f = %.2f", type, a, b, result));
        }

        return switch (type) {
            case "float" -> (float) result;
            case "double" -> result;
            default -> throw new IllegalArgumentException("Unsupported floating-point type: " + type);
        };
    }
}
