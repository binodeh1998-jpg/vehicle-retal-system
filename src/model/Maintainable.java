package model;

/**
 * Contract for anything that can be serviced.
 * Implemented by Car, Motorcycle and Truck, each with its own behavior.
 */
public interface Maintainable {

    void performMaintenance();

    boolean needsMaintenance();
}