package shpp.shuba.spring_jpa_first.exceptions;

public class UniqueEmailException extends RuntimeException { // todo
    public UniqueEmailException(String message) {
        super(message);
    }
}
