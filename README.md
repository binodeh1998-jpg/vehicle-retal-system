# Vehicle Rental Management System

A console-based Java application for a vehicle rental company that rents cars, motorcycles and trucks.
It demonstrates abstraction, inheritance, interfaces, both forms of polymorphism, overloading/overriding,
encapsulation, associations, collections and exception handling.

## Requirements
- JDK 17 or higher (the code uses `switch` arrows and pattern-matching `instanceof`)

## Project structure
```
VehicleRentalSystem/
├── src/
│   ├── exception/   InvalidInputException, VehicleNotAvailableException
│   ├── model/       Maintainable, Vehicle, Car, Motorcycle, Truck, Customer, Rental
│   ├── service/     RentalSystem   (all business logic)
│   └── main/        Main           (console menu only)
├── docs/
│   ├── UML_Class_Diagram.png
│   ├── Report.pdf
│   └── screenshots/
├── UML.md
└── README.md
```

## Compile and run

From the project root:

**Windows (CMD)**
```
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
java -cp out main.Main
```

**Linux / macOS**
```
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out main.Main
```

**Eclipse:** import the project, then right-click `main/Main.java` -> Run As -> Java Application.

## Features
- Add, list and search vehicles (by ID, brand/model keyword, or daily price range)
- Register and list customers; update phone/email
- Rent and return vehicles with automatic, polymorphic cost calculation
- Maintenance through the `Maintainable` interface: flag, list, perform
- Rental history per customer; revenue from completed rentals only

## Cost rules
| Vehicle | Cost |
|---|---|
| Car | price x days (+ $15/day insurance, optional) |
| Motorcycle | price x days (+ $5/day helmet, optional) |
| Truck | price x days + fixed $50 service fee per rental |

Examples: Car $60/day, 4 days, insurance = **$300**. Truck $100/day, 3 days = **$350**.

## Menu
1. Add New Vehicle
2. Display All Vehicles
3. Search for Vehicle (ID / keyword / price range)
4. Display Available Vehicles
5. Register New Customer
6. Display All Customers
7. Rent a Vehicle
8. Return a Vehicle
9. Display All Rentals
10. Display Customer Rental History
11. Display Total Revenue
12. Flag Vehicle for Maintenance
13. Display Vehicles Requiring Maintenance
14. Perform Vehicle Maintenance
15. Update Vehicle Daily Price *(extra)*
16. Update Customer Phone/Email *(extra)*
17. Display Vehicles Currently Rented by a Customer *(extra)*
0. Exit

## Business rules enforced
- Unique vehicle, customer and rental IDs
- Rental days must be positive
- A vehicle cannot have two active rentals; a customer may have several
- A rental can be completed only once
- A vehicle flagged for maintenance cannot be rented
- Maintenance cannot be performed on a vehicle with an active rental
- Unknown customers/vehicles/rentals are rejected

## Design notes
- `Vehicle` is abstract and declares `calculateRentalCost(...)`; each subclass overrides it (no if/else on vehicle type).
- `Maintainable` is an interface implemented by `Car`, `Motorcycle` and `Truck`; `RentalSystem` works with `Maintainable` references to check and perform maintenance.
- `Main` only reads input and prints output; all logic is in `RentalSystem`.

## Documentation
- UML class diagram: `docs/UML_Class_Diagram.png` (also as Mermaid in `UML.md`)
- Report: `docs/Report.pdf`
- Screenshots: `docs/screenshots/`
