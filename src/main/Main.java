package main;

import exception.InvalidInputException;
import exception.VehicleNotAvailableException;
import model.Customer;
import model.Rental;
import model.Vehicle;
import service.RentalSystem;
import java.util.ArrayList;
import java.util.Scanner;

/** Console menu only: reads input, calls RentalSystem, prints results. */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RentalSystem system = new RentalSystem();

    public static void main(String[] args) {
        int choice = -1;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            try {
                handleChoice(choice);
            } catch (InvalidInputException | VehicleNotAvailableException e) {
                System.out.println("[ERROR] " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        } while (choice != 0);
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("========== Vehicle Rental Management System ==========");
        System.out.println(" 1. Add New Vehicle");
        System.out.println(" 2. Display All Vehicles");
        System.out.println(" 3. Search for Vehicle");
        System.out.println(" 4. Display Available Vehicles");
        System.out.println(" 5. Register New Customer");
        System.out.println(" 6. Display All Customers");
        System.out.println(" 7. Rent a Vehicle");
        System.out.println(" 8. Return a Vehicle");
        System.out.println(" 9. Display All Rentals");
        System.out.println("10. Display Customer Rental History");
        System.out.println("11. Display Total Revenue");
        System.out.println("12. Flag Vehicle for Maintenance");
        System.out.println("13. Display Vehicles Requiring Maintenance");
        System.out.println("14. Perform Vehicle Maintenance");
        System.out.println("--- Extra ---");
        System.out.println("15. Update Vehicle Daily Price");
        System.out.println("16. Update Customer Phone/Email");
        System.out.println("17. Display Vehicles Currently Rented by a Customer");
        System.out.println(" 0. Exit");
        System.out.println("======================================================");
    }

    private static void handleChoice(int choice) throws InvalidInputException, VehicleNotAvailableException {
        switch (choice) {
            case 1 -> addVehicle();
            case 2 -> printVehicles(system.getAllVehicles(), "No vehicles in the system.");
            case 3 -> searchVehicle();
            case 4 -> displayAvailableVehicles();
            case 5 -> registerCustomer();
            case 6 -> displayCustomers();
            case 7 -> rentVehicle();
            case 8 -> returnVehicle();
            case 9 -> printRentals(system.getAllRentals(), "No rentals yet.");
            case 10 -> displayRentalHistory();
            case 11 -> System.out.printf("Total revenue (completed rentals only): $%.2f%n",
                    system.calculateTotalRevenue());
            case 12 -> flagMaintenance();
            case 13 -> displayMaintenanceList();
            case 14 -> performMaintenance();
            case 15 -> updatePrice();
            case 16 -> updateCustomerContact();
            case 17 -> displayCustomerActiveRentals();
            case 0 -> System.out.println("Goodbye!");
            default -> System.out.println("[ERROR] Invalid choice. Please select a number from the menu.");
        }
    }

    // ---------------- Vehicles ----------------

    private static void addVehicle() throws InvalidInputException {
        System.out.println("Vehicle type: 1) Car  2) Motorcycle  3) Truck");
        int type = readInt("Type: ");
        if (type < 1 || type > 3) {
            System.out.println("[ERROR] Invalid vehicle type.");
            return;
        }
        String id = readLine("Vehicle ID: ");
        String brand = readLine("Brand: ");
        String model = readLine("Model: ");
        int year = readInt("Manufacturing year: ");
        double price = readDouble("Daily rental price ($): ");

        if (type == 1) {
            int seats = readInt("Number of seats: ");
            String transmission = readLine("Transmission (Manual/Automatic): ");
            system.addCar(id, brand, model, year, price, seats, transmission);
        } else if (type == 2) {
            int cc = readInt("Engine capacity (CC): ");
            boolean helmet = readYesNo("Helmet available for rent? (y/n): ");
            system.addMotorcycle(id, brand, model, year, price, cc, helmet);
        } else {
            double load = readDouble("Maximum load capacity (tons): ");
            system.addTruck(id, brand, model, year, price, load);
        }
        System.out.println("Vehicle added successfully.");
    }

    private static void searchVehicle() throws InvalidInputException {
        System.out.println("Search by: 1) Vehicle ID  2) Brand/Model keyword  3) Price range");
        int option = readInt("Option: ");
        if (option == 1) {
            Vehicle v = system.findVehicleById(readLine("Vehicle ID: "));
            if (v == null) {
                System.out.println("No vehicle found with that ID.");
            } else {
                v.displayInfo();
            }
        } else if (option == 2) {
            printVehicles(system.searchVehicles(readLine("Keyword: ")), "No matching vehicles.");
        } else if (option == 3) {
            double min = readDouble("Minimum daily price: ");
            double max = readDouble("Maximum daily price: ");
            printVehicles(system.searchVehicles(min, max), "No vehicles in that price range.");
        } else {
            System.out.println("[ERROR] Invalid search option.");
        }
    }

    private static void displayAvailableVehicles() {
        System.out.println("--- Available for rent ---");
        printVehicles(system.getAvailableVehicles(), "No vehicles are available right now.");
        System.out.println("--- Currently rented ---");
        printVehicles(system.getRentedVehicles(), "No vehicles are currently rented.");
    }

    private static void updatePrice() throws InvalidInputException {
        String id = readLine("Vehicle ID: ");
        double price = readDouble("New daily price ($): ");
        system.updateVehiclePrice(id, price);
        System.out.println("Price updated successfully.");
    }

    // ---------------- Customers ----------------

    private static void registerCustomer() throws InvalidInputException {
        String id = readLine("Customer ID: ");
        String name = readLine("Full name: ");
        String phone = readLine("Phone: ");
        String email = readLine("Email: ");
        String license = readLine("Driving license number: ");
        system.registerCustomer(id, name, phone, email, license);
        System.out.println("Customer registered successfully.");
    }

    private static void displayCustomers() {
        ArrayList<Customer> list = system.getAllCustomers();
        if (list.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }
        for (Customer c : list) {
            c.displayInfo();
        }
    }

    private static void updateCustomerContact() throws InvalidInputException {
        String id = readLine("Customer ID: ");
        System.out.println("Update: 1) Phone  2) Email");
        int option = readInt("Option: ");
        if (option == 1) {
            system.updateCustomerPhone(id, readLine("New phone: "));
            System.out.println("Phone updated successfully.");
        } else if (option == 2) {
            system.updateCustomerEmail(id, readLine("New email: "));
            System.out.println("Email updated successfully.");
        } else {
            System.out.println("[ERROR] Invalid option.");
        }
    }

    private static void displayCustomerActiveRentals() throws InvalidInputException {
        String id = readLine("Customer ID: ");
        printRentals(system.getActiveRentalsOfCustomer(id), "This customer has no active rentals.");
    }

    // ---------------- Rentals ----------------

    private static void rentVehicle() throws InvalidInputException, VehicleNotAvailableException {
        String customerId = readLine("Customer ID: ");
        String vehicleId = readLine("Vehicle ID: ");
        int days = readInt("Rental days: ");

        boolean service = false;
        Vehicle v = system.findVehicleById(vehicleId);
        if (v != null && v.getOptionalServiceName() != null) {
            service = readYesNo("Add " + v.getOptionalServiceName() + "? (y/n): ");
        }

        Rental rental = system.rentVehicle(customerId, vehicleId, days, service);
        System.out.println("Rental created successfully:");
        rental.displayInfo();
    }

    private static void returnVehicle() throws InvalidInputException {
        Rental rental = system.returnVehicle(readLine("Rental ID: "));
        System.out.println("Vehicle returned. Rental completed:");
        rental.displayInfo();
    }

    private static void displayRentalHistory() throws InvalidInputException {
        String id = readLine("Customer ID: ");
        printRentals(system.getRentalHistory(id), "This customer has no rentals.");
    }

    // ---------------- Maintenance ----------------

    private static void flagMaintenance() throws InvalidInputException {
        system.flagVehicleForMaintenance(readLine("Vehicle ID: "));
        System.out.println("Vehicle flagged for maintenance. It cannot be rented until maintenance is done.");
    }

    private static void displayMaintenanceList() {
        printVehicles(system.getVehiclesNeedingMaintenance(), "No vehicles require maintenance.");
        System.out.println("(Checked through Maintainable references: " + system.countNeedingMaintenance()
                + " of " + system.getMaintainableFleet().size() + " vehicles need maintenance.)");
    }

    private static void performMaintenance() throws InvalidInputException {
        system.performMaintenance(readLine("Vehicle ID: "));
        System.out.println("Maintenance completed. Flag cleared.");
    }

    // ---------------- Printing helpers ----------------

    private static void printVehicles(ArrayList<Vehicle> list, String emptyMessage) {
        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Vehicle v : list) {
            v.displayInfo();  // polymorphic call
        }
    }

    private static void printRentals(ArrayList<Rental> list, String emptyMessage) {
        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Rental r : list) {
            r.displayInfo();
        }
    }

    // ---------------- Input helpers (never crash on bad input) ----------------

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Please enter a valid number.");
            }
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("[ERROR] Please answer y or n.");
        }
    }
}