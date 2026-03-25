/**
 * BookingValidationApp
 *
 * Demonstrates validation and error handling in booking system.
 * Ensures invalid inputs are detected early using custom exceptions.
 *
 * Concepts:
 * - Input validation
 * - Custom exceptions
 * - Fail-fast design
 * - Safe state handling
 * - Graceful error handling
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.*;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Test cases (valid + invalid)
        Reservation[] requests = {
                new Reservation("Alice", "Single"),     // valid
                new Reservation("Bob", "InvalidType"),  // invalid room
                new Reservation("Charlie", "Suite"),    // valid
                new Reservation("David", "Suite"),      // may fail (no availability)
                new Reservation("", "Double")           // invalid guest name
        };

        for (Reservation r : requests) {

            try {
                bookingService.confirmBooking(r);
            } catch (InvalidBookingException e) {
                System.out.println("Booking Failed: " + e.getMessage());
            }

            System.out.println("----------------------------------");
        }
    }
}

/**
 * Custom Exception for booking validation failures
 */
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Reservation (input from user)
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
 * Inventory Service (state holder)
 */
class InventoryService {

    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return availability.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }
}

/**
 * Validator Class (Fail-Fast)
 */
class InvalidBookingValidator {

    public static void validate(Reservation r, InventoryService inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "No available rooms for type: " + r.getRoomType());
        }
    }
}

/**
 * Booking Service
 */
class BookingService {

    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation r) throws InvalidBookingException {

        // FAIL FAST: validate before any state change
        InvalidBookingValidator.validate(r, inventory);

        // Safe allocation (only if validation passes)
        inventory.decrement(r.getRoomType());

        System.out.println("Booking Confirmed for " + r.getGuestName()
                + " | Room Type: " + r.getRoomType());
    }
}