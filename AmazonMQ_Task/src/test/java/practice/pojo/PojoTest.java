package practice.pojo;

import jakarta.validation.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PojoTest {

    private static Validator validator;
    private static ValidatorFactory factory;

    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void close() {
        factory.close();
    }

    @Test
    void testName_Valid() {
        Pojo pojo = new Pojo();
        pojo.setName("Alexandra");
        pojo.setCount(10);

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testName_Null() {
        Pojo pojo = new Pojo();
        pojo.setName(null);
        pojo.setCount(10);

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertFalse(violations.isEmpty());
        assertEquals("First name is compulsory", violations.iterator().next().getMessage());
    }

    @Test
    void testName_TooShort() {
        Pojo pojo = new Pojo();
        pojo.setName("vova");
        pojo.setCount(10);


        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertFalse(violations.isEmpty());
        assertEquals("First name must be at least 7 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testName_NoLetterA() {
        Pojo pojo = new Pojo();
        pojo.setName("volodyw");
        pojo.setCount(10);

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertFalse(violations.isEmpty());
        assertEquals("First name must contain at least one 'a'", violations.iterator().next().getMessage());
    }

    @Test
    void testEddr_Valid() {
        Pojo pojo = new Pojo();
        pojo.setEddr("12345678");
        pojo.setCount(10);
        pojo.setName("volodya");

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testEddr_Invalid() {
        Pojo pojo = new Pojo();
        pojo.setEddr("12345");
        pojo.setCount(10);
        pojo.setName("volodya");

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertFalse(violations.isEmpty());
        assertEquals("Eddr has invalid format", violations.iterator().next().getMessage());
    }

    @Test
    void testCount_Valid() {
        Pojo pojo = new Pojo();
        pojo.setCount(10);
        pojo.setName("volodya");

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testCount_LessThanMin() {
        Pojo pojo = new Pojo();
        pojo.setCount(5);
        pojo.setName("volodya");

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertFalse(violations.isEmpty());
        assertEquals("Count must be bigger than or equal to 10", violations.iterator().next().getMessage());
    }

    @Test
    void testCreatedAt_NoValidation() {
        Pojo pojo = new Pojo();
        pojo.setCreatedAt(LocalDateTime.now());
        pojo.setName("volodya");
        pojo.setCount(10);

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testWhenTwoObjectsEquals() {
        Pojo pojo = new Pojo();
        pojo.setName("volodya");
        pojo.setEddr("12345678");
        pojo.setCount(10);
        pojo.setCreatedAt(LocalDateTime.of(2023, 12, 15, 10, 0));

        Pojo pojo2 = new Pojo();
        pojo2.setName("volodya");
        pojo2.setEddr("12345678");
        pojo2.setCount(10);
        pojo2.setCreatedAt(LocalDateTime.of(2023, 12, 15, 10, 0));

        assertEquals(pojo, pojo2);
    }

    @Test
    void testWhenTwoObjectsNotEquals() {
        Pojo pojo = new Pojo();
        pojo.setName("Volodya");
        pojo.setEddr("12345678");
        pojo.setCount(10);
        pojo.setCreatedAt(LocalDateTime.of(2023, 12, 15, 10, 0));

        Pojo pojo2 = new Pojo();
        pojo2.setName("NeVolodya");
        pojo2.setEddr("12345678");
        pojo2.setCount(10);
        pojo2.setCreatedAt(LocalDateTime.of(2023, 12, 15, 10, 0));

        assertNotEquals(pojo, pojo2);
    }

    @Test
    void testToString() {
        Pojo pojo = new Pojo();
        pojo.setName("Volodya");
        pojo.setEddr("12345678");
        pojo.setCount(10);
        pojo.setCreatedAt(LocalDateTime.of(2023, 12, 15, 10, 0));

        String expected = "Pojo{name='Volodya', eddr='12345678', count=10, createdAt=2023-12-15T10:00}";
        assertEquals(expected, pojo.toString()); // Проверяем формат строки
    }

    @Test
    void testHashCode_EqualObjects() {
        Pojo pojo1 = new Pojo();
        pojo1.setName("volodya");
        pojo1.setEddr("12345678");
        pojo1.setCount(10);
        pojo1.setCreatedAt(LocalDateTime.now());

        Pojo pojo2 = new Pojo();
        pojo2.setName(pojo1.getName());
        pojo2.setEddr(pojo1.getEddr());
        pojo2.setCount(pojo1.getCount());
        pojo2.setCreatedAt(pojo1.getCreatedAt());

        assertEquals(pojo1.hashCode(), pojo2.hashCode()); // Равные объекты имеют одинаковый hashCode
    }

    @Test
    void testHashCode_NotEqualObjects() {
        Pojo pojo1 = new Pojo();
        pojo1.setName("volodya");
        pojo1.setEddr("12345678");
        pojo1.setCount(10);
        pojo1.setCreatedAt(LocalDateTime.now());

        Pojo pojo2 = new Pojo();
        pojo2.setName("volodya");
        pojo2.setEddr("87654321");
        pojo2.setCount(20);
        pojo2.setCreatedAt(LocalDateTime.now());

        assertNotEquals(pojo1.hashCode(), pojo2.hashCode()); // Неравные объекты имеют разные hashCode
    }

}
