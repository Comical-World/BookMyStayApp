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
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public synchronized boolean allocateRoom(String type) {
        int available = inventory.getOrDefault(type, 0);
        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue queue;
    private RoomInventory inventory;

    public ConcurrentBookingProcessor(BookingRequestQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r;
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getNextRequest();
            }

            if (r != null) {
                synchronized (inventory) {
                    if (inventory.allocateRoom(r.getRoomType())) {
                        System.out.println(Thread.currentThread().getName() +
                                " booked for " + r.getGuestName() +
                                " | Room: " + r.getRoomType());
                    } else {
                        System.out.println(Thread.currentThread().getName() +
                                " failed for " + r.getGuestName() +
                                " | Room: " + r.getRoomType());
                    }
                }
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        queue.addRequest(new Reservation("Aryan", "Single Room"));
        queue.addRequest(new Reservation("Rohit", "Single Room"));
        queue.addRequest(new Reservation("Neha", "Single Room"));
        queue.addRequest(new Reservation("Amit", "Double Room"));
        queue.addRequest(new Reservation("Sara", "Double Room"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory), "Thread-2");

        t1.start();
        t2.start();
    }
}