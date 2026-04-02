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

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void incrementAvailability(String type) {
        inventory.put(type, getAvailability(type) + 1);
    }

    public void decrementAvailability(String type) {
        if (getAvailability(type) > 0) {
            inventory.put(type, getAvailability(type) - 1);
        }
    }
}

class BookingHistory {
    private Map<String, Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new HashMap<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedBookings.put(reservation.getReservationId(), reservation);
    }

    public Reservation getReservation(String id) {
        return confirmedBookings.get(id);
    }

    public void removeReservation(String id) {
        confirmedBookings.remove(id);
    }

    public boolean exists(String id) {
        return confirmedBookings.containsKey(id);
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation failed: Reservation not found -> " + reservationId);
            return;
        }

        Reservation reservation = history.getReservation(reservationId);

        rollbackStack.push(reservationId);

        inventory.incrementAvailability(reservation.getRoomType());

        history.removeReservation(reservationId);

        System.out.println("Booking cancelled successfully for " + reservation.getGuestName() +
                " | Room Type: " + reservation.getRoomType() +
                " | Reservation ID: " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 1);

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("SINGLEROOM-1", "Aryan", "Single Room");
        Reservation r2 = new Reservation("DOUBLEROOM-1", "Rohit", "Double Room");

        history.addReservation(r1);
        history.addReservation(r2);

        inventory.decrementAvailability("Single Room");
        inventory.decrementAvailability("Double Room");

        CancellationService service = new CancellationService(inventory, history);

        service.cancelBooking("SINGLEROOM-1");
        service.cancelBooking("INVALID-ID");

        service.displayRollbackStack();
    }
}