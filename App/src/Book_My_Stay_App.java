/**
 * ConcurrentBookingApp
 *
 * Demonstrates race conditions and synchronization in booking systems.
 *
 * Concepts:
 * - Multi-threading
 * - Race conditions
 * - Critical sections
 * - Synchronized methods
 * - Thread-safe inventory updates
 *
 * @author Aman Jain
 */

import java.util.*;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        System.out.println("=== WITHOUT SYNCHRONIZATION (RACE CONDITION) ===");
        runTest(false);

        System.out.println("\n=== WITH SYNCHRONIZATION (THREAD SAFE) ===");
        runTest(true);
    }

    private static void runTest(boolean useSync) {

        InventoryService inventory = new InventoryService();
        BookingQueue queue = new BookingQueue();

        // Only 1 room available → perfect to expose race condition
        inventory.setAvailability("Single", 1);

        // Add multiple requests
        queue.add(new Reservation("User1", "Single"));
        queue.add(new Reservation("User2", "Single"));
        queue.add(new Reservation("User3", "Single"));

        Runnable task = () -> {
            while (true) {
                Reservation r = queue.get();
                if (r == null) break;

                if (useSync) {
                    BookingProcessor.processSync(r, inventory);
                } else {
                    BookingProcessor.processUnsafe(r, inventory);
                }
            }
        };

        // Simulate multiple users (threads)
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start(); t2.start(); t3.start();

        try {
            t1.join(); t2.join(); t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Reservation
 */
class Reservation {
    String guest;
    String roomType;

    public Reservation(String guest, String roomType) {
        this.guest = guest;
        this.roomType = roomType;
    }
}

/**
 * Shared Queue (Thread-Safe)
 */
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void add(Reservation r) {
        queue.add(r);
    }

    public synchronized Reservation get() {
        return queue.poll();
    }
}

/**
 * Inventory (Shared Mutable State)
 */
class InventoryService {

    private Map<String, Integer> availability = new HashMap<>();

    public void setAvailability(String type, int count) {
        availability.put(type, count);
    }

    public int get(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

/**
 * Booking Processor
 */
class BookingProcessor {

    // ❌ UNSAFE METHOD (Race Condition)
    public static void processUnsafe(Reservation r, InventoryService inv) {

        if (inv.get(r.roomType) > 0) {

            // Simulate delay → increases race chance
            try { Thread.sleep(50); } catch (Exception e) {}

            inv.decrement(r.roomType);

            System.out.println(Thread.currentThread().getName()
                    + " BOOKED (UNSAFE): " + r.guest);
        } else {
            System.out.println(Thread.currentThread().getName()
                    + " FAILED: " + r.guest);
        }
    }

    // ✅ SAFE METHOD (SYNCHRONIZED CRITICAL SECTION)
    public static void processSync(Reservation r, InventoryService inv) {

        synchronized (inv) { // CRITICAL SECTION

            if (inv.get(r.roomType) > 0) {

                try { Thread.sleep(50); } catch (Exception e) {}

                inv.decrement(r.roomType);

                System.out.println(Thread.currentThread().getName()
                        + " BOOKED (SAFE): " + r.guest);
            } else {
                System.out.println(Thread.currentThread().getName()
                        + " FAILED: " + r.guest);
            }
        }
    }
}