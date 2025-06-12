package shpp.shuba.spring_jpa_first.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // -- @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed"
        );

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    // -- Incorrect input like it's not real ENUM or smth
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidFormat(HttpMessageNotReadableException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Invalid request format: " + ex.getMessage()
        );
    }

    //  ----------- my ex in service -----------

    @ExceptionHandler(UniqueEmailException.class)
    public ProblemDetail handleUniqueTaxIdEx(UniqueEmailException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Employee with email like this is already exist! " + ex.getMessage());
    }

    @ExceptionHandler(EmptyDBException.class)
    public ProblemDetail handleEmptyDBEx(EmptyDBException ex){

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidEmployeeId.class)
    public ProblemDetail handleEmptyDBEx(InvalidEmployeeId ex){
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalEmployeeStatus.class)
    public ProblemDetail handleEmptyDBEx(IllegalEmployeeStatus ex){
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage()
        );
    }
}
