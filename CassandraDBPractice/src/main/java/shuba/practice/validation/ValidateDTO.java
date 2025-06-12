package shuba.practice.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import shuba.practice.dto.ValidatableDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ValidateDTO {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidateDTO() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, String> validateDTO(ValidatableDTO validatableDTO) {
        Map<String, String> violationMap = new HashMap<>();
        Set<ConstraintViolation<ValidatableDTO>> violations = validator.validate(validatableDTO);

        for (var violation : violations) {
            violationMap.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return violationMap;
    }
}
