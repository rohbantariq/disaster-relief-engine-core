import java.time.format.DateTimeFormatter;
public class DispatchRecord {
    private DisasterArea area;
    private Supply supply;
    private int quantityDispatched;
    private String timestamp;
    private int batchId;

    public DispatchRecord(DisasterArea area, Supply supply, int quantityDispatched, int batchId) {
        this.area = area;
        this.supply = supply;
        this.quantityDispatched = quantityDispatched;
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.batchId = batchId;
    }
    public DispatchRecord(DisasterArea area, Supply supply, int quantityDispatched, String timestamp, int batchId) {
        this.area = area;
        this.supply = supply;
        this.quantityDispatched = quantityDispatched;
        this.timestamp = timestamp;
        this.batchId = batchId;
    }

    public DisasterArea getArea() { return area; }
    public Supply getSupply() { return supply; }
    public int getQuantityDispatched() { return quantityDispatched; }
    public String getTimestamp() { return timestamp; }
    public int getBatchId() { return batchId; }

    public String formattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return java.time.LocalDateTime.parse(timestamp).format(formatter);
    }
    @Override
    public String toString() {
        return String.format("[%s] Dispatched %d units of %s to %s", formattedTimestamp(), quantityDispatched, supply.getItemName(), area.getAreaName());
    }
}