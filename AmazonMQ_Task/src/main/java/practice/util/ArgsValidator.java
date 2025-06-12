package practice.util;

public class ArgsValidator {

    public static final int DEFAULT_MESSAGE_COUNT = 10000;

    protected ArgsValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static int validateArgs(String[] args) {
        // cases: args = null  | args[0] = null | args[0] = "" | args[0] = "  "
        if (args != null && args.length != 0 && args[0] != null && !args[0].trim().isEmpty()) {
            try {
                int parsedValue = Integer.parseInt(args[0].trim());
                // Если число меньше 1, вернуть дефолтное значение
                if (parsedValue >= 1) {
                    return parsedValue;
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException();
            }
        }

        return DEFAULT_MESSAGE_COUNT;
    }
}
