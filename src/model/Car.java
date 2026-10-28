package model;

import exception.InvalidInputException;

public class Car extends Vehicle implements Maintainable {

    public static final double INSURANCE_FEE_PER_DAY = 15.0;

    private final int seats;
    private final String transmission;

    public Car(String vehicleId, String brand, String model, int year, double dailyPrice,
               int seats, String transmission) throws InvalidInputException {
        super(vehicleId, brand, model, year, dailyPrice);
        if (seats <= 0 || seats > 60) {
            throw new InvalidInputException("Number of seats must be between 1 and 60.");
        }
        if (transmission == null) {
            throw new InvalidInputException("Transmission type is required (Manual or Automatic).");
        }
        String t = transmission.trim();
        if (t.equalsIgnoreCase("manual")) {
            this.transmission = "Manual";
        } else if (t.equalsIgnoreCase("automatic")) {
            this.transmission = "Automatic";
        } else {
            throw new InvalidInputException("Transmission must be Manual or Automatic.");
        }
        this.seats = seats;
    }

    /** Overloaded constructor: standard 5-seat automatic car. */
    public Car(String vehicleId, String brand, String model, int year, double dailyPrice)
            throws InvalidInputException {
        this(vehicleId, brand, model, year, dailyPrice, 5, "Automatic");
    }

    @Override
    public double calculateRentalCost(int days, boolean optionalService) {
        double total = baseCost(days);
        if (optionalService) {
            total += INSURANCE_FEE_PER_DAY * days;
        }
        return total;
    }

    @Override
    public String getOptionalServiceName() {
        return "Insurance ($" + INSURANCE_FEE_PER_DAY + "/day)";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("      Type: Car | Seats: " + seats + " | Transmission: " + transmission);
    }

    @Override
    public void performMaintenance() {
        System.out.println("  Car " + getVehicleId() + ": oil changed, brakes checked, tyres rotated.");
        clearMaintenanceFlag();
    }

    @Override
    public boolean needsMaintenance() {
        return isMaintenanceRequired();
    }

    public int getSeats() { return seats; }
    public String getTransmission() { return transmission; }
}