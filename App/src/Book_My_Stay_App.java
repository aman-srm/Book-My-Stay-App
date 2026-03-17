/**
 * BookingAllocationApp
 *
 * Demonstrates safe booking confirmation by processing queued booking requests.
 * Rooms are allocated while ensuring unique room IDs and consistent inventory updates.
 *
 * Concepts demonstrated:
 * - Queue (FIFO booking requests)
 * - HashMap for inventory
 * - Set for uniqueness of room IDs
 * - Mapping room types to allocated room IDs
 * - Atomic logical allocation
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.*;

public class Book_My_Stay_App{

    public static void main(String[] args) {

        // Initialize inventory
        InventoryService inventory = new InventoryService();

        // Initialize booking queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        requestQueue.addRequest(new Reservation("Alice", "Single"));
        requestQueue.addRequest(new Reservation("Bob", "Double"));
        requestQueue.addRequest(new Reservation("Charlie", "Single"));
        requestQueue.addRequest(new Reservation("David", "Suite"));

        // Booking service
        BookingService bookingService = new BookingService(inventory);

        System.out.println("\nProcessing Booking Requests...\n");

        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.getNextRequest();
            bookingService.confirmBooking(reservation);
        }
    }
}

/**
 * Reservation represents a guest booking request
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * BookingRequestQueue manages FIFO booking requests
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

/**
 * InventoryService maintains room availability
 */
class InventoryService {

    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }
}

/**
 * BookingService processes booking requests and allocates rooms safely
 */
class BookingService {

    private InventoryService inventory;

    // Prevent duplicate room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room types to allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    /**
     * Confirms booking by allocating a unique room
     */
    public void confirmBooking(Reservation reservation) {

        String roomType = reservation.getRoomType();

        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("Booking Failed for " + reservation.getGuestName()
                    + " | No " + roomType + " rooms available.");
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Atomic allocation logic
        allocatedRoomIds.add(roomId);

        roomAllocations
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Update inventory immediately
        inventory.decrementRoom(roomType);

        System.out.println("Booking Confirmed for " + reservation.getGuestName()
                + " | Room Type: " + roomType
                + " | Room ID: " + roomId);
    }

    /**
     * Generates unique room ID
     */
    private String generateRoomId(String roomType) {

        String roomId;

        do {
            roomId = roomType.substring(0, 1).toUpperCase() + (100 + allocatedRoomIds.size());
        }
        while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}