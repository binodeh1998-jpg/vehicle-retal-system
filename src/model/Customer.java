package model;

import exception.InvalidInputException;
import java.util.ArrayList;

public class Customer {

    private final String customerId;
    private final String fullName;
    private String phone;
    private String email;
    private final String licenseNumber;
    private final ArrayList<Rental> rentals = new ArrayList<>();

    public Customer(String customerId, String fullName, String phone, String email,
                    String licenseNumber) throws InvalidInputException {
        this.customerId = requireText(customerId, "Customer ID").toUpperCase();
        this.fullName = requireText(fullName, "Full name");
        this.phone = validatePhone(phone);
        this.email = validateEmail(email);
        this.licenseNumber = requireText(licenseNumber, "Driving license number");
    }

    // ---------- validation helpers ----------

    private static String requireText(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    private static String validatePhone(String phone) throws InvalidInputException {
        String p = requireText(phone, "Phone number");
        if (!p.matches("\\+?[0-9]{7,15}")) {
            throw new InvalidInputException("Phone must contain 7-15 digits (optional leading +).");
        }
        return p;
    }

    private static String validateEmail(String email) throws InvalidInputException {
        String e = requireText(email, "Email");
        if (!e.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new InvalidInputException("Invalid email format (example: name@mail.com).");
        }
        return e;
    }

    // ---------- rentals (association) ----------

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public ArrayList<Rental> getRentals() {
        return new ArrayList<>(rentals);
    }

    public ArrayList<Rental> getActiveRentals() {
        ArrayList<Rental> active = new ArrayList<>();
        for (Rental r : rentals) {
            if (r.isActive()) {
                active.add(r);
            }
        }
        return active;
    }

    public void displayInfo() {
        System.out.printf("ID: %-6s | %s | Phone: %s | Email: %s | License: %s%n",
                customerId, fullName, phone, email, licenseNumber);
    }

    // ---------- getters / validated setters ----------

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getLicenseNumber() { return licenseNumber; }

    public void setPhone(String phone) throws InvalidInputException {
        this.phone = validatePhone(phone);
    }

    public void setEmail(String email) throws InvalidInputException {
        this.email = validateEmail(email);
    }
}