import java.util.ArrayList;
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

class Car extends Vehicle {
    public Car(String vehicleId, String brand, String model, double basePricePerDay) {
        super(vehicleId, brand, model, basePricePerDay);
    }
}

interface Rentable {
    void rent();

    void returnVehicle();
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

    public void rentCar(Customer customer, int days) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n== Rent a Car ==\n");
        System.out.println("Available Cars: ");
        boolean availableCars = false;
        for (Vehicle vehicle : vehicles) {
            if (vehicle instanceof Car && vehicle.isAvailable()) {
                System.out.println(vehicle.getVehicleId() + " - " + vehicle.getBrand() + " " + vehicle.getModel());
                availableCars = true;
            }
        }

        if (!availableCars) {
            System.out.println("No cars available for rent.");
            return;
        }

        System.out.println("Enter the car ID you want to rent: ");
        String carId = sc.nextLine();

        Vehicle selectedCar = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle instanceof Car && vehicle.getVehicleId().equals(carId) && vehicle.isAvailable()) {
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
    public void returnCar(Customer customer) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n== Return a Car ==\n");
        System.out.println("Enter the car ID you want to return: ");
        String carId = sc.nextLine();

        Vehicle carToReturn = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle instanceof Car && vehicle.getVehicleId().equals(carId) && !vehicle.isAvailable()) {
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

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter your name: ");
                String customerName = sc.nextLine();

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = sc.nextInt();
                sc.nextLine();

                rentCar(newCustomer, rentalDays);
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
                    returnCar(customer);
                } else {
                    System.out.println("Customer not found.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        System.out.println("\nThank you for using the Car Rental System!");
    }
}

public class VehicleRental {
    public static void main(String[] args) {
        VehicleRentalSystem rentalSystem = new VehicleRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}