package model;

import exception.InvalidInputException;
import java.time.Year;

/**
 * Abstract base class for all vehicles.
 * Cost calculation is abstract so every subclass supplies its own rule (polymorphism).
 */
public abstract class Vehicle {

    private final String vehicleId;
    private String brand;
    private String model;
    private int year;
    private double dailyPrice;
    private boolean available;            // false while there is an active rental
    private boolean maintenanceRequired;  // independent from availability

    protected Vehicle(String vehicleId, String brand, String model, int year, double dailyPrice)
            throws InvalidInputException {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new InvalidInputException("Vehicle ID cannot be empty.");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new InvalidInputException("Brand cannot be empty.");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new InvalidInputException("Model cannot be empty.");
        }
        int maxYear = Year.now().getValue() + 1;
        if (year < 1950 || year > maxYear) {
            throw new InvalidInputException("Manufacturing year must be between 1950 and " + maxYear + ".");
        }
        if (dailyPrice <= 0) {
            throw new InvalidInputException("Daily price must be greater than zero.");
        }
        this.vehicleId = vehicleId.trim().toUpperCase();
        this.brand = brand.trim();
        this.model = model.trim();
        this.year = year;
        this.dailyPrice = dailyPrice;
        this.available = true;
        this.maintenanceRequired = false;
    }

    // ---------- abstract behavior ----------

    /** Each subclass defines its own rental cost rule. */
    public abstract double calculateRentalCost(int days, boolean optionalService);

    // ---------- shared behavior ----------

    /** Name of the optional service (with fee), or null if the vehicle has none. */
    public String getOptionalServiceName() {
        return null;
    }

    /** Base cost = daily price x days. Rejects non-positive days. */
    protected double baseCost(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Rental days must be positive.");
        }
        return dailyPrice * days;
    }

    /** Prints the common vehicle information. Subclasses override and call super. */
    public void displayInfo() {
        System.out.printf("ID: %-6s | %s %s (%d) | $%.2f/day | %s | Maintenance: %s%n",
                vehicleId, brand, model, year, dailyPrice,
                available ? "Available" : "Rented",
                maintenanceRequired ? "REQUIRED" : "OK");
    }

    public void flagForMaintenance() {
        this.maintenanceRequired = true;
    }

    protected void clearMaintenanceFlag() {
        this.maintenanceRequired = false;
    }

    /** A vehicle is rentable only if it is free AND not flagged for maintenance. */
    public boolean isRentable() {
        return available && !maintenanceRequired;
    }

    // ---------- getters / validated setters ----------

    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getDailyPrice() { return dailyPrice; }
    public boolean isAvailable() { return available; }
    public boolean isMaintenanceRequired() { return maintenanceRequired; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDailyPrice(double dailyPrice) throws InvalidInputException {
        if (dailyPrice <= 0) {
            throw new InvalidInputException("Daily price must be greater than zero.");
        }
        this.dailyPrice = dailyPrice;
    }
}