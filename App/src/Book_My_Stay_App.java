public class Book_My_Stay_App {

    public static void main(String[] args) {

        // Creating room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Availability stored using simple variables
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        System.out.println("Hotel Room Availability\n");

        // Display details
        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailability + "\n");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailability + "\n");

        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailability + "\n");

        System.out.println("Application terminated.");
    }
}

/**
 * Abstract Room class defining common attributes and behavior
 */
abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqm");
        System.out.println("Price per night: $" + price);
    }
}

/**
 * Single Room implementation
 */
class SingleRoom extends Room {

    public SingleRoom() {
        roomType = "Single Room";
        beds = 1;
        size = 20;
        price = 100;
    }
}

/**
 * Double Room implementation
 */
class DoubleRoom extends Room {

    public DoubleRoom() {
        roomType = "Double Room";
        beds = 2;
        size = 30;
        price = 150;
    }
}

/**
 * Suite Room implementation
 */
class SuiteRoom extends Room {

    public SuiteRoom() {
        roomType = "Suite Room";
        beds = 3;
        size = 50;
        price = 300;
    }
}