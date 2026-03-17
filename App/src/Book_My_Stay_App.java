/**
 * BookingQueueApp
 *
 * Demonstrates how booking requests are collected using a Queue
 * to preserve arrival order before any room allocation occurs.
 *
 * Concepts demonstrated:
 * - Queue Data Structure
 * - FIFO (First-In-First-Out)
 * - Fair request ordering
 * - Decoupling request intake from allocation
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.LinkedList;
import java.util.Queue;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submit booking requests
        bookingQueue.addRequest(new Reservation("Guest1", "Single"));
        bookingQueue.addRequest(new Reservation("Guest2", "Double"));
        bookingQueue.addRequest(new Reservation("Guest3", "Suite"));
        bookingQueue.addRequest(new Reservation("Guest4", "Single"));

        // Display queued requests
        System.out.println("\nCurrent Booking Request Queue:");
        bookingQueue.displayQueue();

        System.out.println("\nRequests are waiting for allocation...");
    }
}

/**
 * Reservation
 *
 * Represents a guest booking request.
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

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

/**
 * BookingRequestQueue
 *
 * Manages incoming booking requests using FIFO ordering.
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /**
     * Add booking request to queue
     */
    public void addRequest(Reservation reservation) {

        requestQueue.add(reservation);
        System.out.println("Booking request received from " + reservation.getGuestName());
    }

    /**
     * Display all queued requests
     */
    public void displayQueue() {

        for (Reservation reservation : requestQueue) {
            reservation.displayReservation();
        }
    }
}