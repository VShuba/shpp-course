package shpp.shuba.spring_jpa_first.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import shpp.shuba.spring_jpa_first.models.EmploymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeDTO(

        @Schema(description = "Name of employee", example = "Vladimir")
        @NotBlank(message = "Name can't be empty.")
        String fullName,

        @Schema(description = "Status of employee: WORKING, ON_VACATION, FIRED", example = "WORKING")
        @NotNull(message = "Static must have: WORKING, ON_VACATION, FIRED")
        EmploymentStatus status,

        @Schema(description = "Salary of employee", example = "999")
        @Min(value = 0, message = "Salary could not be negative.")
        BigDecimal salary,

        @Schema(description = "HireDate of employee", example = "2001-12-30")
        @NotNull(message = "The format of data must be like: YYYY-MM-DD")
        LocalDate hireDate,

        @Schema(description = "Email of employee (UNIQUE)", example = "test@gmail.com")
        @NotNull(message = "Email can't be empty")
        String email

        // @Hidden можно до поля, а можно до ендпоінта
) {
}
