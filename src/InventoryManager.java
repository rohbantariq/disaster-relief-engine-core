import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private final List<Supply> inventory = new ArrayList<>();

    public InventoryManager() {
        initializeDemoInventory();
    }

    private void initializeDemoInventory() {
        addItem(new Supply("Food Boxes", 500000));
        addItem(new Supply("First Aid Kits", 500000));
        addItem(new Supply("Water Bottles", 1000000));
    }

    public List<Supply> getInventory() {
        return inventory;
    }

    public void addItem(Supply supply) {
        this.inventory.add(supply);
    }

    public Supply findItem(String itemName) {
        for (Supply supply : this.inventory) {
            if (supply.getItemName().equalsIgnoreCase(itemName)) {
                return supply;
            }
        }
        return null;
    }

    public void deductSupply(Supply supply) throws Exception {
        Supply s = findItem(supply.getItemName());
        if (s == null) throw new Exception("Requested supply item not found in inventory.");

        s.reduceQuantity(supply.getQuantity());
        DataLogger.logAudit("Deducted " + supply.getQuantity() + " of " + supply.getItemName());
        DataLogger.logInventory(inventory);
    }

    public void restoreSupply(Supply supply) throws Exception {
        Supply s = findItem(supply.getItemName());
        if (s == null) throw new Exception("Supply item not found to restore.");

        s.addQuantity(supply.getQuantity());
        DataLogger.logAudit("Restored " + supply.getQuantity() + " of " + supply.getItemName());
        DataLogger.logInventory(inventory);
    }

    public void restockSupply(String itemName, int amount) {
        Supply s = findItem(itemName);
        if (s != null) {
            s.addQuantity(amount);
        } else {
            addItem(new Supply(itemName, amount));
        }
        System.out.println("Successfully restocked " + amount + " units of " + itemName);
        DataLogger.logInventory(inventory);
    }

    public void showInventory() {
        System.out.println("\n--- CURRENT INVENTORY ---");
        if (inventory.isEmpty()) {
            System.out.println("No inventory available");
        } else {
            inventory.forEach(System.out::println);
        }
        System.out.println("-------------------------");
    }

    public void saveInventoryState() {
    }

    public void loadInventory() {
    }
}