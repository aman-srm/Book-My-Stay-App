/**
 * BookingCancellationApp
 *
 * Demonstrates safe cancellation of bookings using rollback logic.
 * Uses Stack (LIFO) to track released room IDs.
 *
 * Concepts:
 * - State reversal
 * - Stack (LIFO rollback)
 * - Validation before mutation
 * - Inventory restoration
 * - Controlled state updates
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.*;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);
        CancellationService cancellationService =
                new CancellationService(inventory, bookingService);

        // Confirm bookings
        bookingService.confirmBooking(new Reservation("R101", "Alice", "Single"));
        bookingService.confirmBooking(new Reservation("R102", "Bob", "Double"));

        System.out.println("\n--- Cancelling Booking R101 ---");
        cancellationService.cancelBooking("R101");

        System.out.println("\n--- Attempt Invalid Cancellation ---");
        cancellationService.cancelBooking("R999"); // invalid

        System.out.println("\n--- Attempt Duplicate Cancellation ---");
        cancellationService.cancelBooking("R101"); // already cancelled
    }
}

/**
 * Reservation
 */
class Reservation {

    private String id;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getId() { return id; }
    public String getRoomType() { return roomType; }
}

/**
 * Inventory Service
 */
class InventoryService {

    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void increment(String type) {
        availability.put(type, availability.get(type) + 1);
    }
}

/**
 * Booking Service
 */
class BookingService {

    private InventoryService inventory;

    // reservationId → roomId
    private Map<String, String> allocatedRooms = new HashMap<>();

    // reservationId → roomType
    private Map<String, String> reservationRoomType = new HashMap<>();

    private int counter = 100;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation r) {

        String roomId = r.getRoomType().substring(0,1).toUpperCase() + counter++;

        allocatedRooms.put(r.getId(), roomId);
        reservationRoomType.put(r.getId(), r.getRoomType());

        inventory.decrement(r.getRoomType());

        System.out.println("Booking Confirmed: " + r.getId()
                + " | Room ID: " + roomId);
    }

    public boolean exists(String reservationId) {
        return allocatedRooms.containsKey(reservationId);
    }

    public String getRoomId(String reservationId) {
        return allocatedRooms.get(reservationId);
    }

    public String getRoomType(String reservationId) {
        return reservationRoomType.get(reservationId);
    }

    public void removeBooking(String reservationId) {
        allocatedRooms.remove(reservationId);
        reservationRoomType.remove(reservationId);
    }
}

/**
 * Cancellation Service
 */
class CancellationService {

    private InventoryService inventory;
    private BookingService bookingService;

    // Stack for rollback (released room IDs)
    private Stack<String> rollbackStack = new Stack<>();

    // Track cancelled reservations
    private Set<String> cancelled = new HashSet<>();

    public CancellationService(InventoryService inventory,
                               BookingService bookingService) {
        this.inventory = inventory;
        this.bookingService = bookingService;
    }

    public void cancelBooking(String reservationId) {

        // Validate existence
        if (!bookingService.exists(reservationId)) {
            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        // Prevent duplicate cancellation
        if (cancelled.contains(reservationId)) {
            System.out.println("Cancellation Failed: Already cancelled.");
            return;
        }

        // Get details
        String roomId = bookingService.getRoomId(reservationId);
        String roomType = bookingService.getRoomType(reservationId);

        // LIFO rollback → push released room ID
        rollbackStack.push(roomId);

        // Restore inventory immediately
        inventory.increment(roomType);

        // Remove booking
        bookingService.removeBooking(reservationId);

        // Mark as cancelled
        cancelled.add(reservationId);

        System.out.println("Booking Cancelled: " + reservationId
                + " | Released Room ID: " + roomId);
    }
}