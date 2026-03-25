/**
 * BookingPersistenceApp
 *
 * Demonstrates persistence and recovery using serialization.
 *
 * Concepts:
 * - Stateful applications
 * - Serialization / Deserialization
 * - File persistence
 * - Recovery after restart
 * - Failure tolerance
 *
 * @author Aman Jain
 */

import java.io.*;
import java.util.*;

public class Book_My_Stay_App {

    private static final String FILE_NAME = "system_state.ser";

    public static void main(String[] args) {

        SystemState state;

        // 🔄 TRY TO LOAD PREVIOUS STATE
        state = PersistenceService.load();

        if (state == null) {
            System.out.println("No previous state found. Starting fresh...");
            state = new SystemState();
        } else {
            System.out.println("State restored successfully!");
        }

        BookingService bookingService = new BookingService(state);

        // Simulate new bookings
        bookingService.confirmBooking("Alice", "Single");
        bookingService.confirmBooking("Bob", "Double");

        // Show current state
        System.out.println("\nCurrent Inventory: " + state.inventory);
        System.out.println("Booking History: " + state.history);

        // 💾 SAVE STATE BEFORE SHUTDOWN
        PersistenceService.save(state);

        System.out.println("\nSystem state saved. Restart app to see recovery.");
    }
}

/**
 * System State (Serializable)
 */
class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory = new HashMap<>();
    List<String> history = new ArrayList<>();

    public SystemState() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }
}

/**
 * Booking Service
 */
class BookingService {

    private SystemState state;

    public BookingService(SystemState state) {
        this.state = state;
    }

    public void confirmBooking(String guest, String roomType) {

        if (!state.inventory.containsKey(roomType)) {
            System.out.println("Invalid room type");
            return;
        }

        if (state.inventory.get(roomType) <= 0) {
            System.out.println("No rooms available for " + roomType);
            return;
        }

        // Allocate
        state.inventory.put(roomType, state.inventory.get(roomType) - 1);

        String record = guest + " booked " + roomType;
        state.history.add(record);

        System.out.println("Booking Confirmed: " + record);
    }
}

/**
 * Persistence Service
 */
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // SAVE STATE
    public static void save(SystemState state) {

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("State saved to file.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // LOAD STATE
    public static SystemState load() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return null; // no previous state
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (SystemState) ois.readObject();

        } catch (Exception e) {
            System.out.println("Corrupted or invalid file. Starting fresh.");
            return null; // fail safely
        }
    }
}