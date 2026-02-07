package aerith.exception;

public class InvalidInputException extends AerithException {
    public InvalidInputException(String message) {
        super("⚠ " + message + " ⚠");
    }
}