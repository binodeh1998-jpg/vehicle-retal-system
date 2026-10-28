package exception;

/**
 * Thrown when a vehicle cannot be rented
 * (already rented or flagged for maintenance).
 */
public class VehicleNotAvailableException extends Exception {

    public VehicleNotAvailableException(String message) {
        super(message);
    }
}