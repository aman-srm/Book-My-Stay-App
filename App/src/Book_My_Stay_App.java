/**
 * BookingHistoryApp
 *
 * Demonstrates historical tracking of confirmed bookings using a List.
 * Also shows how reporting is separated from storage logic.
 *
 * Concepts:
 * - List (ordered storage)
 * - Historical tracking
 * - Reporting service
 * - Separation of concerns
 * - Read-only reporting
 *
 * @author Aman Jain
 * @version 1.0
 */

import java.util.*;

public class Book_My_Stay_App {

    public static void main(String[] args) {

        // Booking history storage
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addBooking(new Reservation("R101", "Alice", "Single"));
        history.addBooking(new Reservation("R102", "Bob", "Double"));
        history.addBooking(new Reservation("R103", "Charlie", "Single"));
        history.addBooking(new Reservation("R104", "David", "Suite"));

        // Admin requests reports
        BookingReportService reportService = new BookingReportService(history);

        System.out.println("\n=== Booking History ===");
        reportService.displayAllBookings();

        System.out.println("\n=== Booking Summary Report ===");
        reportService.generateSummaryReport();
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

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

/**
 * BookingHistory
 * Stores confirmed bookings in insertion order
 */
class BookingHistory {

    private List<Reservation> bookings = new ArrayList<>();

    /**
     * Add confirmed booking to history
     */
    public void addBooking(Reservation reservation) {
        bookings.add(reservation);
    }

    /**
     * Retrieve all bookings (read-only usage)
     */
    public List<Reservation> getBookings() {
        return bookings;
    }
}

/**
 * BookingReportService
 *
 * Generates reports using booking history.
 * Does NOT modify stored data.
 */
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    /**
     * Display all bookings
     */
    public void displayAllBookings() {

        for (Reservation r : history.getBookings()) {
            r.display();
        }
    }

    /**
     * Generate summary report
     */
    public void generateSummaryReport() {

        Map<String, Integer> countMap = new HashMap<>();

        // Count bookings per room type
        for (Reservation r : history.getBookings()) {
            String type = r.getRoomType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        // Display summary
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Total Bookings: " + entry.getValue());
        }
    }
}