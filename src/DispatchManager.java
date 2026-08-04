import java.util.Scanner;
public class DispatchManager {
    private InventoryManager inventory;
    private Stack<DispatchRecord> history;
    private int currentBatchId = 1;

    public DispatchManager(InventoryManager inventory) {
        this.inventory = inventory;
        this.history = new Stack<>();
    }

    public int startNewBatch() {
        return currentBatchId++;
    }

    public void dispatch(DisasterArea area, Supply supply, int batchId) throws Exception {
        inventory.deductSupply(supply);
        DispatchRecord record = new DispatchRecord(area, supply, supply.getQuantity(), batchId);
        history.push(record);
        DataLogger.logDispatch(record);
    }

    public int[] calculateRecommendedSupplies(DisasterArea area) {
        double severityRatio = area.getSeverityScore() / 10.0;

        Supply food  = inventory.findItem("Food Boxes");
        Supply med   = inventory.findItem("First Aid Kits");
        Supply water = inventory.findItem("Water Bottles");

        int availableFood  = (food  != null) ? food.getQuantity()  : 0;
        int availableMed   = (med   != null) ? med.getQuantity()   : 0;
        int availableWater = (water != null) ? water.getQuantity() : 0;

        int recommendedFood  = (int)(area.getPopulation() * 0.1  * severityRatio);
        int recommendedMed   = (int)(area.getInjuries()   * 2.0  * severityRatio);
        int recommendedWater = (int)(area.getPopulation() * 0.3  * severityRatio);

        recommendedFood  = Math.min(recommendedFood,  availableFood);
        recommendedMed   = Math.min(recommendedMed,   availableMed);
        recommendedWater = Math.min(recommendedWater, availableWater);

        DataLogger.logAudit("Greedy allocation calculated for " + area.getAreaName() + " | Food: " + recommendedFood + " | Medical: " + recommendedMed + " | Water: " + recommendedWater);
        return new int[]{recommendedFood, recommendedMed, recommendedWater};
    }

    public void undoLastDispatch(AreaManager areaManager) throws Exception {
        if (history.isEmpty())
            throw new Exception("No dispatch actions available to undo in this session.");

        int lastBatchId = history.peek().getBatchId();
        DisasterArea areaToRestore = history.peek().getArea();
        System.out.println("Undoing dispatch for: " + areaToRestore.getAreaName());

        while (!history.isEmpty() && history.peek().getBatchId() == lastBatchId) {
            DispatchRecord r = history.pop();
            inventory.restoreSupply(new Supply(r.getSupply().getItemName(), r.getQuantityDispatched()));
        }
        areaManager.addArea(areaToRestore);
        DataLogger.logAudit("UNDO completed for batch " + lastBatchId);
    }

    public void saveHistory() {
    }

    public void loadHistory() {
    }
}