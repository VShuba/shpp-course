package simplecalculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculateTest {

    @Test
    void testCalculateForByte() {
        assertEquals((byte) 10, MultiplicationTable.calculate("byte", 2, 5));
        assertThrows(ArithmeticException.class, () ->
                MultiplicationTable.calculate("byte", Byte.MAX_VALUE, 2)
        );
    }

    @Test
    void testCalculateForShort() {
        assertEquals((short) 100, MultiplicationTable.calculate("short", 10, 10));
        assertThrows(ArithmeticException.class, () ->
                MultiplicationTable.calculate("short", Short.MAX_VALUE, 2)
        );
    }

    @Test
    void testCalculateForInt() {
        assertEquals(1000, MultiplicationTable.calculate("int", 10, 100));
        assertThrows(ArithmeticException.class, () ->
                MultiplicationTable.calculate("int", Integer.MAX_VALUE, 2)
        );
    }

    @Test
    void testCalculateForFloat() {
        assertEquals(10.0f, MultiplicationTable.calculate("float", 2.0f, 5.0f));
        assertThrows(ArithmeticException.class, () ->
                MultiplicationTable.calculate("float", Float.MAX_VALUE, 2)
        );
    }

    @Test
    void testCalculateForDouble() {
        assertEquals(100.0, MultiplicationTable.calculate("double", 10.0, 10.0));
        assertThrows(ArithmeticException.class, () ->
                MultiplicationTable.calculate("double", Double.MAX_VALUE, 2)
        );
    }
}
