package exception;

/**
 * Thrown when user input or a business rule is violated
 * (empty fields, duplicate IDs, unknown IDs, non-positive days, ...).
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }
}