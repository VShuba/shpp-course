package shpp.shuba.spring_jpa_first.exceptions;

public class EmptyDBException extends Throwable {
    public EmptyDBException(String message) {
        super(message);
    }
}
