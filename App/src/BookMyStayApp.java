import java.util.*;

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

class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public void displayQueue() {
        System.out.println("===== BOOKING REQUEST QUEUE =====");
        for (Reservation r : queue) {
            System.out.println("Guest: " + r.getGuestName() + " | Room: " + r.getRoomType());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Aryan", "Single Room"));
        bookingQueue.addRequest(new Reservation("Rohit", "Double Room"));
        bookingQueue.addRequest(new Reservation("Neha", "Suite Room"));

        bookingQueue.displayQueue();
    }
}