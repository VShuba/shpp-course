package simplecalculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionsTest {
    @Test
    void testUnsupportedIntegerType() {
        // check for  IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            MultiplicationTable.checkIntegerOverflow(10L, 5L, -Long.MAX_VALUE, Long.MAX_VALUE, "unsupportedType");
        });
    }

    @Test
    void testUnsupportedFloatingPointType() {
        // check for IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            MultiplicationTable.checkFloatingPointOverflow(10.0, 5.0, -Double.MAX_VALUE, Double.MAX_VALUE, "unsupportedType");
        });
    }

    @Test
    void testZeroIncrement() {
        // increment == 0 >>> IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            MultiplicationTable.printTable("int", 1, 10, 0);  // increment == 0
        });
    }

    @Test
    void testInvalidTypeInPrintTable() {
        // unsupportedType в printTable
        assertThrows(IllegalArgumentException.class, () -> {
            MultiplicationTable.printTable("unsupportedType", 1, 10, 1);
        });
    }
}
