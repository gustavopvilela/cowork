package backend.coworking.exception;

public class EmailException extends RuntimeException {
    public EmailException(String message) {
        super(message);
    }
}
