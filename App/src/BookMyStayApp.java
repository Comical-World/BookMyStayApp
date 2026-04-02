import java.util.*;

class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : services) {
            total += s.getPrice();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        System.out.println("Services for Reservation ID: " + reservationId);
        for (AddOnService s : services) {
            System.out.println(s.getName() + " - ₹" + s.getPrice());
        }
        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId1 = "SINGLEROOM-1";
        String reservationId2 = "DOUBLEROOM-1";

        manager.addService(reservationId1, new AddOnService("Breakfast", 300));
        manager.addService(reservationId1, new AddOnService("Airport Pickup", 800));
        manager.addService(reservationId2, new AddOnService("Extra Bed", 500));

        manager.displayServices(reservationId1);
        manager.displayServices(reservationId2);
    }
}