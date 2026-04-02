import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void displayAllBookings() {
        System.out.println("===== BOOKING HISTORY =====");
        for (Reservation r : history.getAllReservations()) {
            System.out.println("ID: " + r.getReservationId() +
                    " | Guest: " + r.getGuestName() +
                    " | Room: " + r.getRoomType());
        }
    }

    public void generateSummary() {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            String type = r.getRoomType();
            summary.put(type, summary.getOrDefault(type, 0) + 1);
        }

        System.out.println("===== BOOKING SUMMARY =====");
        for (String type : summary.keySet()) {
            System.out.println(type + ": " + summary.get(type));
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("SINGLEROOM-1", "Aryan", "Single Room"));
        history.addReservation(new Reservation("DOUBLEROOM-1", "Rohit", "Double Room"));
        history.addReservation(new Reservation("SUITEROOM-1", "Neha", "Suite Room"));
        history.addReservation(new Reservation("SINGLEROOM-2", "Amit", "Single Room"));

        BookingReportService reportService = new BookingReportService(history);

        reportService.displayAllBookings();
        reportService.generateSummary();
    }
}