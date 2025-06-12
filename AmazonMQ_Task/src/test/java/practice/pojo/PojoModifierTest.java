package practice.pojo;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PojoModifierTest {
    private PojoModifier pojoModifier;

    @BeforeEach
    void setUp() {
        pojoModifier = new PojoModifier();
    }

    @Test
    void testGenerateRandomPojo() {
        Pojo pojo = pojoModifier.generateRandomPojo();

        assertNotNull(pojo.getName());
        assertNotNull(pojo.getEddr());
        assertTrue(pojo.getCount() >= 0); // count не должен быть отрицательным
        assertNotNull(pojo.getCreatedAt());
    }

    @Test
    void testValidatePojo_ValidPojo() {
        Pojo pojo = new Pojo();
        pojo.setName("volodya");
        pojo.setEddr("12345678");
        pojo.setCount(10);
        pojo.setCreatedAt(LocalDateTime.now());

        assertDoesNotThrow(() -> pojoModifier.validatePojo(pojo));
    }


    @Test
    void testSerializePojo() {
        Pojo pojo = new Pojo();
        pojo.setName("volodya");
        pojo.setEddr("12345678");
        pojo.setCount(10);
        pojo.setCreatedAt(LocalDateTime.of(2023, 12, 15, 10, 0));

        String expected = "volodya,12345678,10,2023-12-15T10:00";
        assertEquals(expected, pojoModifier.serializePojo(pojo));
    }

    @Test
    void testDeserializePojo_ValidInput() {
        String message = "volodya,12345678,10,2023-12-15T10:00";

        Pojo pojo = pojoModifier.deserializePojo(message);

        assertEquals("volodya", pojo.getName());
        assertEquals("12345678", pojo.getEddr());
        assertEquals(10, pojo.getCount());
        assertEquals(LocalDateTime.of(2023, 12, 15, 10, 0), pojo.getCreatedAt());
    }

    @Test
    void testDeserializePojo_InvalidInputFormat() {
        String message = "volodya,12345678,10"; // Недостаточно полей

        assertThrows(IllegalArgumentException.class, () -> pojoModifier.deserializePojo(message));
    }

    @Test
    void testDeserializePojo_InvalidCount() {
        String message = "volodya,12345678,not_a_number,2023-12-15T10:00";

        assertThrows(NumberFormatException.class, () -> pojoModifier.deserializePojo(message));
    }

    @Test
    void testDeserializePojo_InvalidDateFormat() {
        String message = "volodya,12345678,10,invalid_date";

        assertThrows(DateTimeParseException.class, () -> pojoModifier.deserializePojo(message));
    }
}