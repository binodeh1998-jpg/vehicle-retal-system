package model;

import exception.InvalidInputException;

public class Truck extends Vehicle implements Maintainable {

    public static final double SERVICE_FEE = 50.0;  // fixed, once per rental

    private final double maxLoadTons;

    public Truck(String vehicleId, String brand, String model, int year, double dailyPrice,
                 double maxLoadTons) throws InvalidInputException {
        super(vehicleId, brand, model, year, dailyPrice);
        if (maxLoadTons <= 0) {
            throw new InvalidInputException("Maximum load capacity must be greater than zero.");
        }
        this.maxLoadTons = maxLoadTons;
    }

    @Override
    public double calculateRentalCost(int days, boolean optionalService) {
        // Trucks have no optional service; the fixed fee is always added once.
        return baseCost(days) + SERVICE_FEE;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("      Type: Truck | Max load: " + maxLoadTons + " tons | Service fee: $" + SERVICE_FEE);
    }

    @Override
    public void performMaintenance() {
        System.out.println("  Truck " + getVehicleId() + ": engine serviced, axles and load system inspected.");
        clearMaintenanceFlag();
    }

    @Override
    public boolean needsMaintenance() {
        return isMaintenanceRequired();
    }

    public double getMaxLoadTons() { return maxLoadTons; }
}