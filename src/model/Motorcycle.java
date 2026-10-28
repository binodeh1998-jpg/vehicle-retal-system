package model;

import exception.InvalidInputException;

public class Motorcycle extends Vehicle implements Maintainable {

    public static final double HELMET_FEE_PER_DAY = 5.0;

    private final int engineCc;
    private final boolean helmetIncluded;  // true = a helmet can be added to the rental

    public Motorcycle(String vehicleId, String brand, String model, int year, double dailyPrice,
                      int engineCc, boolean helmetIncluded) throws InvalidInputException {
        super(vehicleId, brand, model, year, dailyPrice);
        if (engineCc <= 0) {
            throw new InvalidInputException("Engine capacity (CC) must be greater than zero.");
        }
        this.engineCc = engineCc;
        this.helmetIncluded = helmetIncluded;
    }

    /** Overloaded constructor: helmet available by default. */
    public Motorcycle(String vehicleId, String brand, String model, int year, double dailyPrice,
                      int engineCc) throws InvalidInputException {
        this(vehicleId, brand, model, year, dailyPrice, engineCc, true);
    }

    @Override
    public double calculateRentalCost(int days, boolean optionalService) {
        double total = baseCost(days);
        if (optionalService && helmetIncluded) {
            total += HELMET_FEE_PER_DAY * days;
        }
        return total;
    }

    @Override
    public String getOptionalServiceName() {
        return helmetIncluded ? "Helmet ($" + HELMET_FEE_PER_DAY + "/day)" : null;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("      Type: Motorcycle | Engine: " + engineCc + " CC | Helmet available: "
                + (helmetIncluded ? "Yes" : "No"));
    }

    @Override
    public void performMaintenance() {
        System.out.println("  Motorcycle " + getVehicleId() + ": chain lubricated, tyre pressure and brakes checked.");
        clearMaintenanceFlag();
    }

    @Override
    public boolean needsMaintenance() {
        return isMaintenanceRequired();
    }

    public int getEngineCc() { return engineCc; }
    public boolean isHelmetIncluded() { return helmetIncluded; }
}