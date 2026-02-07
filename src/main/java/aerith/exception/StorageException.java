package aerith.exception;

/**
 * Exceptions relating to saving and loading of data.
 */
public class StorageException extends AerithException {
    public StorageException(String message) {
        super("⛏ " + message + " ⛏");
    }
}
