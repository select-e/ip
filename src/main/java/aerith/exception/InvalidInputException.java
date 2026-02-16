package aerith.exception;

/**
 * The exception thrown when the user input is invalid.
 */
public class InvalidInputException extends AerithException {
    public InvalidInputException(String message) {
        super("⚠ " + message + " ⚠");
    }
}
