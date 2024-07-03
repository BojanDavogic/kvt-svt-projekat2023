package ftn.drustvenamreza_back.exceptionhandling.exception;

public class MalformedQueryException extends RuntimeException {
    public MalformedQueryException(String message) {
        super(message);
    }
}
