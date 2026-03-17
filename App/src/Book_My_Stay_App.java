/**
 * AddOnServiceApp
 *
 * Demonstrates how optional services can be attached to existing reservations
 * without modifying core booking or inventory logic.
 *
 * Concepts demonstrated:
 * - One-to-many relationship (Reservation → Services)
 * - Map + List data structure
 * - Composition over inheritance
 * - Cost aggregation
 * - Extensible service design
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.*;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        // Example reservation (already confirmed earlier)
        Reservation reservation = new Reservation("R101", "Alice", "Single");

        // Service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects optional services
        serviceManager.addService(reservation.getReservationId(), new Service("Breakfast", 20));
        serviceManager.addService(reservation.getReservationId(), new Service("Airport Pickup", 40));
        serviceManager.addService(reservation.getReservationId(), new Service("Spa Access", 60));

        // Display services attached to reservation
        serviceManager.displayServices(reservation.getReservationId());

        // Calculate total additional cost
        double totalCost = serviceManager.calculateServiceCost(reservation.getReservationId());

        System.out.println("\nTotal Additional Service Cost: $" + totalCost);
    }
}

/**
 * Reservation
 * Represents a confirmed booking
 */
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }
}

/**
 * Service
 * Represents an optional add-on service
 */
class Service {

    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getServiceName() {
        return serviceName;
    }
}

/**
 * AddOnServiceManager
 *
 * Manages mapping between reservations and selected services.
 */
class AddOnServiceManager {

    // Map: ReservationID → List of Services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    /**
     * Add service to reservation
     */
    public void addService(String reservationId, Service service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println(service.getServiceName() +
                " added to reservation " + reservationId);
    }

    /**
     * Display services attached to reservation
     */
    public void displayServices(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        System.out.println("\nServices for Reservation " + reservationId + ":");

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (Service service : services) {
            System.out.println(service.getServiceName() + " - $" + service.getPrice());
        }
    }

    /**
     * Calculate total service cost
     */
    public double calculateServiceCost(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        if (services == null) {
            return 0;
        }

        double total = 0;

        for (Service service : services) {
            total += service.getPrice();
        }

        return total;
    }
}