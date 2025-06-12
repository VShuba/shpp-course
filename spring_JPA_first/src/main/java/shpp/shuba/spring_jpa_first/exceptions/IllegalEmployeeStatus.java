package shpp.shuba.spring_jpa_first.exceptions;

public class IllegalEmployeeStatus extends RuntimeException {
    public IllegalEmployeeStatus(String message) {
        super(message);
    }
}
