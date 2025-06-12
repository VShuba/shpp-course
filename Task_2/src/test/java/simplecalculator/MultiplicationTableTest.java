package simplecalculator;

import org.junit.jupiter.api.Test;
import java.util.Properties;

import static simplecalculator.MultiplicationTable.checkFloatingPointOverflow;
import static simplecalculator.MultiplicationTable.checkIntegerOverflow;
import static org.junit.jupiter.api.Assertions.*;


class MultiplicationTableTest {

    @Test
    void testCheckOverflowForByte() {
        assertEquals((byte) 127, checkIntegerOverflow(1, 127, Byte.MIN_VALUE, Byte.MAX_VALUE, "byte"));
        assertEquals((byte) -128, checkIntegerOverflow(-1, 128, Byte.MIN_VALUE, Byte.MAX_VALUE, "byte"));

        ArithmeticException exception = assertThrows(ArithmeticException.class, () ->
                checkIntegerOverflow(10, 13, Byte.MIN_VALUE, Byte.MAX_VALUE, "byte"));
        assertTrue(exception.getMessage().contains("Overflow occurred for type byte"));
    }

    @Test
    void testCheckOverflowForShort() {
        assertEquals((short) 32767, checkIntegerOverflow(1, 32767, Short.MIN_VALUE, Short.MAX_VALUE, "short"));
        assertEquals((short) -32768, checkIntegerOverflow(-1, 32768, Short.MIN_VALUE, Short.MAX_VALUE, "short"));

        ArithmeticException exception = assertThrows(ArithmeticException.class, () ->
                checkIntegerOverflow(200, 200, Short.MIN_VALUE, Short.MAX_VALUE, "short"));
        assertTrue(exception.getMessage().contains("Overflow occurred for type short"));
    }

    @Test
    void testCheckOverflowForInt() {
        assertEquals(2147483647, checkIntegerOverflow(1, 2147483647, Integer.MIN_VALUE, Integer.MAX_VALUE, "int"));
        assertEquals(-2147483648, checkIntegerOverflow(-1, 2147483648L, Integer.MIN_VALUE, Integer.MAX_VALUE, "int"));

        ArithmeticException exception = assertThrows(ArithmeticException.class, () ->
                checkIntegerOverflow(50000, 50000, Integer.MIN_VALUE, Integer.MAX_VALUE, "int"));
        assertTrue(exception.getMessage().contains("Overflow occurred for type int"));
    }

    @Test
    void testCheckOverflowForFloatingPointTypes() {
        assertEquals(1.25f, checkFloatingPointOverflow(2.5f, 0.5f, -Float.MAX_VALUE, Float.MAX_VALUE, "float"));
        assertEquals(0.05, checkFloatingPointOverflow(0.1, 0.5, -Double.MAX_VALUE, Double.MAX_VALUE, "double"));

        ArithmeticException exception = assertThrows(ArithmeticException.class, () ->
                checkFloatingPointOverflow(Float.MAX_VALUE, 2.0f, -Float.MAX_VALUE, Float.MAX_VALUE, "float"));
        assertTrue(exception.getMessage().contains("Overflow occurred for type float"));
    }

    @Test
    void testPropertiesLoad() {
        Properties properties = new Properties();

        assertDoesNotThrow(() -> PropertiesModifier.load(properties));

        // Проверяем, что свойства загружены
        assertFalse(properties.isEmpty(), "Properties should not be empty after loading.");
        assertTrue(properties.containsKey("min"), "Properties should contain 'min' key.");
        assertTrue(properties.containsKey("max"), "Properties should contain 'max' key.");
        assertTrue(properties.containsKey("increment"), "Properties should contain 'increment' key.");
    }


    @Test
    void testDoubleValues() {

        assertDoesNotThrow(() ->
                MultiplicationTable.printTable("byte",0.1 , 1.0, 0.10));
    }
}
