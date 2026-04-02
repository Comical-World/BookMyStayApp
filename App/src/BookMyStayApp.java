import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
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
        return inventory.getOrDefault(type, -1);
    }

    public void validateRoomType(String type) throws InvalidBookingException {
        if (!inventory.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
    }

    public void validateAvailability(String type) throws InvalidBookingException {
        int available = getAvailability(type);
        if (available <= 0) {
            throw new InvalidBookingException("No availability for room type: " + type);
        }
    }

    public void decrementAvailability(String type) throws InvalidBookingException {
        int available = getAvailability(type);
        if (available <= 0) {
            throw new InvalidBookingException("Cannot decrement. No rooms available for: " + type);
        }
        inventory.put(type, available - 1);
    }
}

class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(String guestName, String roomType) {
        try {
            inventory.validateRoomType(roomType);
            inventory.validateAvailability(roomType);

            inventory.decrementAvailability(roomType);

            System.out.println("Booking successful for " + guestName + " | Room Type: " + roomType);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed for " + guestName + " | Reason: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0);

        BookingService service = new BookingService(inventory);

        service.bookRoom("Aryan", "Single Room");
        service.bookRoom("Rohit", "Double Room");
        service.bookRoom("Neha", "Suite Room");
    }
}