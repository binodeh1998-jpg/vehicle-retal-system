package service;

import exception.InvalidInputException;
import exception.VehicleNotAvailableException;
import model.Car;
import model.Customer;
import model.Maintainable;
import model.Motorcycle;
import model.Rental;
import model.Truck;
import model.Vehicle;
import java.util.ArrayList;

/** All business logic lives here (Main only handles input/output). */
public class RentalSystem {

    private final ArrayList<Vehicle> vehicles = new ArrayList<>();
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ArrayList<Rental> rentals = new ArrayList<>();

    // =====================  Vehicles  =====================

    public void addCar(String id, String brand, String model, int year, double price,
                       int seats, String transmission) throws InvalidInputException {
        addVehicle(new Car(id, brand, model, year, price, seats, transmission));
    }

    public void addMotorcycle(String id, String brand, String model, int year, double price,
                              int engineCc, boolean helmetIncluded) throws InvalidInputException {
        addVehicle(new Motorcycle(id, brand, model, year, price, engineCc, helmetIncluded));
    }

    public void addTruck(String id, String brand, String model, int year, double price,
                         double maxLoadTons) throws InvalidInputException {
        addVehicle(new Truck(id, brand, model, year, price, maxLoadTons));
    }

    public void addVehicle(Vehicle vehicle) throws InvalidInputException {
        if (findVehicleById(vehicle.getVehicleId()) != null) {
            throw new InvalidInputException("Vehicle ID '" + vehicle.getVehicleId() + "' already exists.");
        }
        vehicles.add(vehicle);
    }

    public Vehicle findVehicleById(String id) {
        if (id == null) {
            return null;
        }
        for (Vehicle v : vehicles) {
            if (v.getVehicleId().equalsIgnoreCase(id.trim())) {
                return v;
            }
        }
        return null;
    }

    private Vehicle getVehicle(String id) throws InvalidInputException {
        Vehicle v = findVehicleById(id);
        if (v == null) {
            throw new InvalidInputException("Vehicle with ID '" + id + "' was not found.");
        }
        return v;
    }

    /** Overload 1: search by brand or model keyword. */
    public ArrayList<Vehicle> searchVehicles(String keyword) {
        ArrayList<Vehicle> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }
        String k = keyword.trim().toLowerCase();
        for (Vehicle v : vehicles) {
            if (v.getBrand().toLowerCase().contains(k) || v.getModel().toLowerCase().contains(k)) {
                result.add(v);
            }
        }
        return result;
    }

    /** Overload 2: search by daily price range. */
    public ArrayList<Vehicle> searchVehicles(double minPrice, double maxPrice) throws InvalidInputException {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new InvalidInputException("Invalid price range.");
        }
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v.getDailyPrice() >= minPrice && v.getDailyPrice() <= maxPrice) {
                result.add(v);
            }
        }
        return result;
    }

    public ArrayList<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    /** Vehicles that can be rented right now (free and not flagged for maintenance). */
    public ArrayList<Vehicle> getAvailableVehicles() {
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v.isRentable()) {
                result.add(v);
            }
        }
        return result;
    }

    public ArrayList<Vehicle> getRentedVehicles() {
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (!v.isAvailable()) {
                result.add(v);
            }
        }
        return result;
    }

    public void updateVehiclePrice(String vehicleId, double newPrice) throws InvalidInputException {
        getVehicle(vehicleId).setDailyPrice(newPrice);
    }

    // =====================  Customers  =====================

    public void registerCustomer(String id, String name, String phone, String email,
                                 String license) throws InvalidInputException {
        Customer c = new Customer(id, name, phone, email, license);
        if (findCustomerById(c.getCustomerId()) != null) {
            throw new InvalidInputException("Customer ID '" + c.getCustomerId() + "' already exists.");
        }
        customers.add(c);
    }

    public Customer findCustomerById(String id) {
        if (id == null) {
            return null;
        }
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(id.trim())) {
                return c;
            }
        }
        return null;
    }

    private Customer getCustomer(String id) throws InvalidInputException {
        Customer c = findCustomerById(id);
        if (c == null) {
            throw new InvalidInputException("Customer with ID '" + id + "' was not found.");
        }
        return c;
    }

    public ArrayList<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public void updateCustomerPhone(String customerId, String phone) throws InvalidInputException {
        getCustomer(customerId).setPhone(phone);
    }

    public void updateCustomerEmail(String customerId, String email) throws InvalidInputException {
        getCustomer(customerId).setEmail(email);
    }

    // =====================  Rentals  =====================

    public Rental rentVehicle(String customerId, String vehicleId, int days, boolean optionalService)
            throws InvalidInputException, VehicleNotAvailableException {
        if (days <= 0) {
            throw new InvalidInputException("Rental days must be a positive number.");
        }
        Customer customer = getCustomer(customerId);
        Vehicle vehicle = getVehicle(vehicleId);

        if (vehicle.isMaintenanceRequired()) {
            throw new VehicleNotAvailableException("Vehicle " + vehicle.getVehicleId()
                    + " is flagged for maintenance and cannot be rented.");
        }
        if (!vehicle.isAvailable() || hasActiveRental(vehicle)) {
            throw new VehicleNotAvailableException("Vehicle " + vehicle.getVehicleId()
                    + " is currently rented.");
        }

        // The optional service only applies if this vehicle type offers one.
        boolean useService = optionalService && vehicle.getOptionalServiceName() != null;

        Rental rental = new Rental(customer, vehicle, days, useService);
        rentals.add(rental);
        customer.addRental(rental);
        vehicle.setAvailable(false);
        return rental;
    }

    public Rental returnVehicle(String rentalId) throws InvalidInputException {
        Rental rental = findRentalById(rentalId);
        if (rental == null) {
            throw new InvalidInputException("Rental with ID '" + rentalId + "' was not found.");
        }
        rental.complete();  // throws if already completed
        rental.getVehicle().setAvailable(true);
        return rental;
    }

    public Rental findRentalById(String id) {
        if (id == null) {
            return null;
        }
        for (Rental r : rentals) {
            if (r.getRentalId().equalsIgnoreCase(id.trim())) {
                return r;
            }
        }
        return null;
    }

    private boolean hasActiveRental(Vehicle vehicle) {
        for (Rental r : rentals) {
            if (r.getVehicle() == vehicle && r.isActive()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Rental> getAllRentals() {
        return new ArrayList<>(rentals);
    }

    public ArrayList<Rental> getActiveRentals() {
        ArrayList<Rental> result = new ArrayList<>();
        for (Rental r : rentals) {
            if (r.isActive()) {
                result.add(r);
            }
        }
        return result;
    }

    public ArrayList<Rental> getRentalHistory(String customerId) throws InvalidInputException {
        return getCustomer(customerId).getRentals();
    }

    public ArrayList<Rental> getActiveRentalsOfCustomer(String customerId) throws InvalidInputException {
        return getCustomer(customerId).getActiveRentals();
    }

    /** Revenue counts completed rentals only. */
    public double calculateTotalRevenue() {
        double total = 0;
        for (Rental r : rentals) {
            if (r.isCompleted()) {
                total += r.getTotalCost();
            }
        }
        return total;
    }

    // =====================  Maintenance (interface polymorphism)  =====================

    private Maintainable asMaintainable(Vehicle vehicle) throws InvalidInputException {
        if (vehicle instanceof Maintainable m) {
            return m;
        }
        throw new InvalidInputException("Vehicle " + vehicle.getVehicleId() + " does not support maintenance.");
    }

    /** The whole fleet viewed only through the Maintainable interface. */
    public ArrayList<Maintainable> getMaintainableFleet() {
        ArrayList<Maintainable> fleet = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v instanceof Maintainable m) {
                fleet.add(m);
            }
        }
        return fleet;
    }

    /** Counts vehicles needing maintenance using Maintainable references. */
    public int countNeedingMaintenance() {
        int count = 0;
        for (Maintainable m : getMaintainableFleet()) {
            if (m.needsMaintenance()) {
                count++;
            }
        }
        return count;
    }

    public void flagVehicleForMaintenance(String vehicleId) throws InvalidInputException {
        Vehicle vehicle = getVehicle(vehicleId);
        if (vehicle.isMaintenanceRequired()) {
            throw new InvalidInputException("Vehicle " + vehicle.getVehicleId() + " is already flagged for maintenance.");
        }
        vehicle.flagForMaintenance();
    }

    public ArrayList<Vehicle> getVehiclesNeedingMaintenance() {
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v instanceof Maintainable m && m.needsMaintenance()) {
                result.add(v);
            }
        }
        return result;
    }

    public void performMaintenance(String vehicleId) throws InvalidInputException {
        Vehicle vehicle = getVehicle(vehicleId);
        Maintainable m = asMaintainable(vehicle);

        if (hasActiveRental(vehicle)) {
            throw new InvalidInputException("Cannot perform maintenance on " + vehicle.getVehicleId()
                    + " while it has an active rental. Return it first.");
        }
        if (!m.needsMaintenance()) {
            throw new InvalidInputException("Vehicle " + vehicle.getVehicleId() + " is not flagged for maintenance.");
        }
        m.performMaintenance();  // each subclass runs its own maintenance
    }
}