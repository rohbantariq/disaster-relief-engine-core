public class Supply {
    private String itemName;
    private int quantity;

    public Supply(String itemName, int quantity) throws IllegalArgumentException {
        if (quantity < 0) throw new IllegalArgumentException("Supply must be non-negative");
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void reduceQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deduction must be positive");
        }
        if (amount > quantity) throw new IllegalArgumentException("Not enough "+itemName+ " available.");
        this.quantity -= amount;
    }
    public void addQuantity(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Addition must be positive");
        quantity += amount;
    }

    @Override
    public String toString() {
        return String.format("%s: %d units", itemName, quantity);
    }
}