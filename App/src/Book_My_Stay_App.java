/**
 * HotelSearchApp
 *
 * Demonstrates a read-only room search operation in a hotel booking system.
 * Guests can view available room types and their details without modifying
 * system inventory.
 *
 * Concepts demonstrated:
 * - Read-only access
 * - Separation of concerns
 * - Defensive programming
 * - Domain model usage
 * - HashMap inventory management
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.HashMap;
import java.util.Map;

public class Book_My_Stay_App{

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room domain objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Register rooms in a domain map
        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single", single);
        roomCatalog.put("Double", doubleRoom);
        roomCatalog.put("Suite", suite);

        // Create search service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Guest searches for available rooms
        searchService.displayAvailableRooms();
    }
}

/**
 * Room domain model
 */
abstract class Room {

    protected String type;
    protected int beds;
    protected double price;

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: $" + price);
    }
}

class SingleRoom extends Room {

    public SingleRoom() {
        type = "Single";
        beds = 1;
        price = 100;
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        type = "Double";
        beds = 2;
        price = 150;
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        type = "Suite";
        beds = 3;
        price = 300;
    }
}

/**
 * Centralized inventory management
 */
class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 0); // Suite unavailable
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return availability;
    }
}

/**
 * Search Service
 *
 * Provides read-only access to room availability and room details.
 */
class SearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(RoomInventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    /**
     * Displays only available rooms
     */
    public void displayAvailableRooms() {

        System.out.println("Available Rooms:\n");

        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {

            String roomType = entry.getKey();
            int availableCount = entry.getValue();

            // Defensive check: only show rooms with availability > 0
            if (availableCount > 0) {

                Room room = roomCatalog.get(roomType);

                room.displayDetails();
                System.out.println("Available Rooms: " + availableCount);
                System.out.println();
            }
        }
    }
}