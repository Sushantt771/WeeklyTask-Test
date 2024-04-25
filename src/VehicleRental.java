import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

abstract class Vehicle {
    protected String vehicleId;
    protected String brand;
    protected String model;
    protected double basePricePerDay;
    protected boolean isAvailable;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnVehicle() {
        isAvailable = true;
    }
}

interface Rentable {
    void rent();
    void returnVehicle();
}

interface Drivable {
    void drive();
    void stop();
}

class Car extends Vehicle implements Rentable, Drivable {
    public Car(String vehicleId, String brand, String model, double basePricePerDay) {
        super(vehicleId, brand, model, basePricePerDay);
    }

    @Override
    public void rent() {
        isAvailable = false;
    }

    @Override
    public void returnVehicle() {
        isAvailable = true;
    }

    @Override
    public void drive() {
        System.out.println("Car is being driven.");
    }

    @Override
    public void stop() {
        System.out.println("Car has stopped.");
    }
}

class Motorcycle extends Vehicle implements Rentable, Drivable {
    public Motorcycle(String vehicleId, String brand, String model, double basePricePerDay) {
        super(vehicleId, brand, model, basePricePerDay);
    }

    @Override
    public void rent() {
        isAvailable = false;
    }

    @Override
    public void returnVehicle() {
        isAvailable = true;
    }

    @Override
    public void drive() {
        System.out.println("Motorcycle is being driven.");
    }

    @Override
    public void stop() {
        System.out.println("Motorcycle has stopped.");
    }
}

class Truck extends Vehicle implements Rentable, Drivable {
    public Truck(String vehicleId, String brand, String model, double basePricePerDay) {
        super(vehicleId, brand, model, basePricePerDay);
    }

    @Override
    public void rent() {
        isAvailable = false;
    }

    @Override
    public void returnVehicle() {
        isAvailable = true;
    }

    @Override
    public void drive() {
        System.out.println("Truck is being driven.");
    }

    @Override
    public void stop() {
        System.out.println("Truck has stopped.");
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Vehicle vehicle;
    private Customer customer;
    private int days;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class VehicleRentalSystem {
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<Rental> rentals;

    public VehicleRentalSystem() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Customer customer, int days, String carId) {
        Vehicle selectedCar = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equalsIgnoreCase(carId) && vehicle.isAvailable()) {
                selectedCar = vehicle;
                break;
            }
        }

        if (selectedCar != null) {
            double totalPrice = selectedCar.calculatePrice(days);
            System.out.println("\n== Rental Information ==\n");
            System.out.println("Customer ID: " + customer.getCustomerId());
            System.out.println("Customer Name: " + customer.getName());
            System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
            System.out.println("Rental Days: " + days);
            System.out.printf("Total Price: $%.2f%n", totalPrice);

            System.out.print("\nConfirm rental (Y/N): ");
            Scanner sc = new Scanner(System.in);
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                selectedCar.rent();
                rentals.add(new Rental(selectedCar, customer, days));
                System.out.println("\nCar rented successfully.");
            } else {
                System.out.println("\nRental canceled.");
            }
        } else {
            System.out.println("\nInvalid car selection or car not available for rent.");
        }
    }

    public void returnCar(Customer customer, String carId) {
        Vehicle carToReturn = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equalsIgnoreCase(carId) && !vehicle.isAvailable()) {
                carToReturn = vehicle;
                break;
            }
        }

        if (carToReturn != null) {
            Customer customerOfRentedCar = null;
            for (Rental rental : rentals) {
                if (rental.getVehicle() == carToReturn && rental.getCustomer() == customer) {
                    customerOfRentedCar = rental.getCustomer();
                    break;
                }
            }
            if (customerOfRentedCar != null) {
                carToReturn.returnVehicle();
                Rental rentalToRemove = null;
                for (Rental rental : rentals) {
                    if (rental.getVehicle() == carToReturn && rental.getCustomer() == customer) {
                        rentalToRemove = rental;
                        break;
                    }
                }
                rentals.remove(rentalToRemove);
                System.out.println("Car returned successfully by " + customer.getName());
            } else {
                System.out.println("Car was not rented by this customer or rental information is missing.");
            }
        } else {
            System.out.println("Invalid car ID or car is not rented.");
        }
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline character

                if (choice == 1) {
                    System.out.print("Enter your name: ");
                    String customerName = sc.nextLine();

                    Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                    addCustomer(newCustomer);

                    System.out.print("Enter the number of days for rental: ");
                    int rentalDays = sc.nextInt();
                    sc.nextLine(); // Consume newline character

                    System.out.print("Enter the car ID you want to rent: ");
                    String carId = sc.nextLine();

                    rentCar(newCustomer, rentalDays, carId);
                } else if (choice == 2) {
                    System.out.print("Enter your name: ");
                    String customerName = sc.nextLine();

                    Customer customer = null;
                    for (Customer c : customers) {
                        if (c.getName().equals(customerName)) {
                            customer = c;
                            break;
                        }
                    }

                    if (customer != null) {
                        System.out.print("Enter the car ID you want to return: ");
                        String carId = sc.nextLine();

                        returnCar(customer, carId);
                    } else {
                        System.out.println("Customer not found.");
                    }
                } else if (choice == 3) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter a valid option (1, 2, or 3).");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                sc.nextLine(); // Clear the input buffer
            }
        }
        System.out.println("\nThank you for using the Car Rental System!");
    }
}

public class VehicleRental {
    public static void main(String[] args) {
        VehicleRentalSystem rentalSys = new VehicleRentalSystem();

        Car car1 = new Car("C01", "Hyundai", "Santro", 50.0);
        Car car2 = new Car("C02", "Tata", "Nexon", 75.0);
        Car car3 = new Car("C03", "Mahindra", "XUV", 140.0);

        Motorcycle bike1 = new Motorcycle("M01", "Honda", "CBR", 30.0);
        Motorcycle bike2 = new Motorcycle("M02", "Yamaha", "R1", 40.0);
        Motorcycle bike3 = new Motorcycle("M03", "Suzuki", "GSX", 35.0);

        Truck truck1 = new Truck("T01", "Ford", "F-150", 100.0);
        Truck truck2 = new Truck("T02", "Chevrolet", "Silverado", 120.0);
        Truck truck3 = new Truck("T03", "Dodge", "Ram", 130.0);

        rentalSys.addCar(car1);
        rentalSys.addCar(car2);
        rentalSys.addCar(car3);
        rentalSys.addCar(bike1);
        rentalSys.addCar(bike2);
        rentalSys.addCar(bike3);
        rentalSys.addCar(truck1);
        rentalSys.addCar(truck2);
        rentalSys.addCar(truck3);

        rentalSys.menu();
    }
}