package practice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgsValidatorTest {

    private static final int DEFAULT_COUNT = ArgsValidator.DEFAULT_MESSAGE_COUNT;

    @Test
    void validateArgs_WhenArgsIsNull_ShouldReturnDefaultCount() {
        String[] args = null;

        int result = ArgsValidator.validateArgs(args);

        assertEquals(DEFAULT_COUNT, result, "Если аргумент args равен null, должно вернуть дефолтное значение");
    }

    @Test
    void validateArgs_WhenArgsIsEmpty_ShouldReturnDefaultCount() {
        String[] args = {};

        int result = ArgsValidator.validateArgs(args);

        assertEquals(DEFAULT_COUNT, result, "Если args пустой массив, должно вернуть дефолтное значение");
    }

    @Test
    void validateArgs_WhenFirstArgIsNull_ShouldReturnDefaultCount() {

        int result = ArgsValidator.validateArgs(null);

        assertEquals(DEFAULT_COUNT, result, "Если первый элемент массива args равен null, должно вернуть дефолтное значение");
    }

    @Test
    void validateArgs_WhenFirstArgIsEmptyString_ShouldReturnDefaultCount() {

        int result = ArgsValidator.validateArgs(new String[]{""});

        assertEquals(DEFAULT_COUNT, result, "Если первый элемент args - пустая строка, должно вернуть дефолтное значение");
    }

    @Test
    void validateArgs_WhenFirstArgIsWhitespace_ShouldReturnDefaultCount() {
        String[] args = {"   "};

        int result = ArgsValidator.validateArgs(args);

        assertEquals(DEFAULT_COUNT, result, "Если первый элемент args содержит только пробелы, должно вернуть дефолтное значение");
    }

    @Test
    void validateArgs_WhenFirstArgIsValidNumber_ShouldReturnParsedValue() {
        String[] args = {"500"};

        int result = ArgsValidator.validateArgs(args);

        assertEquals(500, result, "Должно возвращаться значение, корректно распарсенное из args.");
    }

    @Test
    void validateArgs_WhenFirstArgContainsLeadingAndTrailingSpaces_ShouldReturnParsedValue() {
        String[] args = {"   300   "};

        int result = ArgsValidator.validateArgs(args);

        assertEquals(300, result, "Должно корректно обрезать пробелы и распарсить число.");
    }

    @Test
    void validateArgs_WhenFirstArgIsNotANumber_ShouldThrowNumberFormatException() {
        String[] args = {"notANumber"};

        assertThrows(NumberFormatException.class, () -> ArgsValidator.validateArgs(args),
                "Если первый элемент args не является числом, должно выбрасываться NumberFormatException.");
    }

    @Test
    void validateArgs_WhenFirstArgIsSmallerThanBorder_ShouldReturnDefaultNumber() {
        String[] args = {"0"};

        int result = ArgsValidator.validateArgs(args);

        assertEquals(DEFAULT_COUNT, result,"Если первый элемент меньше 0, должно вернуть дефолтное значение");
    }

    @Test
    void tryToCreate_AnObjectOfUtilityClass(){
        assertThrows(IllegalStateException.class, ArgsValidator::new);
    }


}