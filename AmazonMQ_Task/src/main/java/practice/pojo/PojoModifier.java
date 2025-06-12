package practice.pojo;

import com.github.javafaker.Faker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PojoModifier {

    private final ThreadLocal<Faker> threadLocalFaker = ThreadLocal.withInitial(() -> new Faker(new Locale("uk_UA")));
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Map<String, String> validatePojo(Pojo pojo) {
        Map<String, String> violationMap = new HashMap<>();
        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

        for (var violation : violations) {
            violationMap.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return violationMap;
    }

    public Pojo generateRandomPojo() {
        Pojo pojo = new Pojo();

        pojo.setName(generateRandomName());
        pojo.setEddr(generateRandomEddr());
        pojo.setCount(generateRandomCount());
        pojo.setCreatedAt(LocalDateTime.now());

        return pojo;
    }

    public Pojo deserializePojo(String message) {
        String[] fields = message.split(",");
        if (fields.length != 4) {
            throw new IllegalArgumentException("Pojo length is not 4");
        }

        Pojo pojo = new Pojo();
        pojo.setName(fields[0]);
        pojo.setEddr(fields[1]);
        pojo.setCount(Integer.parseInt(fields[2]));
        pojo.setCreatedAt(LocalDateTime.parse(fields[3]));

        return pojo;
    }

    public String serializePojo(Pojo pojo) {
        return String.format("%s,%s,%d,%s",
                pojo.getName(),
                pojo.getEddr(),
                pojo.getCount(),
                pojo.getCreatedAt());
    }

    private String generateRandomName() {
        Faker faker = threadLocalFaker.get();
        Random random = ThreadLocalRandom.current();

        if (random.nextDouble() < 0.2) {
            return faker.lorem().characters(3, 5);
        } else if (random.nextDouble() < 0.3) {
            return faker.lorem().characters(2, 6).replace("а", "б");
        }
        return faker.name().firstName() + "a";
    }

    private String generateRandomEddr() {
        Faker faker = threadLocalFaker.get();
        Random random = ThreadLocalRandom.current();

        if (random.nextDouble() < 0.3) {
            return faker.numerify("#####");
        }
        return faker.numerify("########");
    }

    private int generateRandomCount() {
        Random random = ThreadLocalRandom.current();

        if (random.nextDouble() < 0.2) {
            return random.nextInt(9);
        }
        return random.nextInt(100) + 10;
    }
}
