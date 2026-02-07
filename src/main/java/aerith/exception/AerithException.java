package aerith.exception;

/**
 * The parent class for exceptions that are shown to the user.
 */
public class AerithException extends Exception {
    public AerithException(String message) {
        super(message);
    }
}