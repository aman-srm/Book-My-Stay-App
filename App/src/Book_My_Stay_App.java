/**
 * HotelInventoryApp
 *
 * This program demonstrates centralized inventory management for hotel rooms
 * using a HashMap. Instead of storing availability in scattered variables,
 * all room availability is stored in a single data structure managed by
 * the RoomInventory class.
 *
 * The application initializes inventory, updates availability,
 * and displays the current inventory state.
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.HashMap;
import java.util.Map;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        System.out.println("Initial Room Inventory:");
        inventory.displayInventory();

        // Update availability
        inventory.updateAvailability("Single", -1); // One single room booked
        inventory.updateAvailability("Suite", 1);   // One suite added

        // Display updated inventory
        System.out.println("\nUpdated Room Inventory:");
        inventory.displayInventory();
    }
}

/**
 * RoomInventory
 *
 * Responsible for managing room availability across the system.
 * All availability data is stored in a centralized HashMap.
 */
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    /**
     * Constructor initializes room inventory
     */
    public RoomInventory() {

        roomAvailability = new HashMap<>();

        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    /**
     * Retrieve availability of a specific room type
     */
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    /**
     * Update availability in a controlled way
     */
    public void updateAvailability(String roomType, int change) {

        int current = roomAvailability.getOrDefault(roomType, 0);
        roomAvailability.put(roomType, current + change);
    }

    /**
     * Display full inventory state
     */
    public void displayInventory() {

        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}