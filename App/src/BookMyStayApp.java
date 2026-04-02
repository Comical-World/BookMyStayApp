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

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementAvailability(String type) {
        if (inventory.containsKey(type) && inventory.get(type) > 0) {
            inventory.put(type, inventory.get(type) - 1);
        }
    }
}

class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> roomAllocations;
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
    }

    public void processRequests(BookingRequestQueue queue) {
        while (!queue.isEmpty()) {
            Reservation request = queue.getNextRequest();
            String type = request.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                String roomId = generateRoomId(type);

                allocatedRoomIds.add(roomId);

                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                inventory.decrementAvailability(type);

                System.out.println("Booking Confirmed for " + request.getGuestName() +
                        " | Room Type: " + type +
                        " | Room ID: " + roomId);
            } else {
                System.out.println("Booking Failed for " + request.getGuestName() +
                        " | Room Type: " + type + " (Not Available)");
            }
        }
    }

    private String generateRoomId(String type) {
        String roomId;
        do {
            roomId = type.replaceAll(" ", "").toUpperCase() + "-" + idCounter++;
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Aryan", "Single Room"));
        queue.addRequest(new Reservation("Rohit", "Single Room"));
        queue.addRequest(new Reservation("Neha", "Single Room"));
        queue.addRequest(new Reservation("Amit", "Double Room"));
        queue.addRequest(new Reservation("Sara", "Suite Room"));

        BookingService bookingService = new BookingService(inventory);
        bookingService.processRequests(queue);
    }
}