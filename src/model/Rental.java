package model;

import exception.InvalidInputException;

/** A rental links one Customer to one Vehicle (association). */
public class Rental {

    public enum Status { ACTIVE, COMPLETED }

    private static int rentalCounter = 0;  // shared across all rentals -> unique IDs

    private final String rentalId;
    private final Customer customer;
    private final Vehicle vehicle;
    private final int days;
    private final boolean optionalServiceUsed;
    private final double totalCost;
    private Status status;

    public Rental(Customer customer, Vehicle vehicle, int days, boolean optionalServiceUsed) {
        // Polymorphic call: the concrete vehicle decides how the cost is computed.
        double cost = vehicle.calculateRentalCost(days, optionalServiceUsed);
        this.rentalId = String.format("R%03d", ++rentalCounter);
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.optionalServiceUsed = optionalServiceUsed;
        this.totalCost = cost;
        this.status = Status.ACTIVE;
    }

    /** Completes the rental. A rental cannot be completed twice. */
    public void complete() throws InvalidInputException {
        if (status == Status.COMPLETED) {
            throw new InvalidInputException("Rental " + rentalId + " is already completed.");
        }
        status = Status.COMPLETED;
    }

    public boolean isActive() { return status == Status.ACTIVE; }
    public boolean isCompleted() { return status == Status.COMPLETED; }

    public void displayInfo() {
        String service = optionalServiceUsed ? vehicle.getOptionalServiceName() : "none";
        System.out.printf("Rental %s | Customer: %s (%s) | Vehicle: %s %s [%s] | Days: %d | Service: %s | Total: $%.2f | Status: %s%n",
                rentalId, customer.getFullName(), customer.getCustomerId(),
                vehicle.getBrand(), vehicle.getModel(), vehicle.getVehicleId(),
                days, service, totalCost, status);
    }

    public String getRentalId() { return rentalId; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public int getDays() { return days; }
    public boolean isOptionalServiceUsed() { return optionalServiceUsed; }
    public double getTotalCost() { return totalCost; }
    public Status getStatus() { return status; }
}