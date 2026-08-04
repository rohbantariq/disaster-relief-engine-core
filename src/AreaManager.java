import java.util.ArrayList;
import java.util.List;

public class AreaManager {
    private final PriorityQueue<DisasterArea> areaQueue = new PriorityQueue<>();

    public AreaManager() {
        initializeDemoData();
    }

    private void initializeDemoData() {
        try {
            addArea(new DisasterArea("Landhi", 15000, 1, 2));
            addArea(new DisasterArea("Lahore", 90000, 900, 8));
            addArea(new DisasterArea("Korangi", 10000, 190, 7));
            addArea(new DisasterArea("Larkana", 20000, 80, 4));
            addArea(new DisasterArea("Gawadar", 45000, 680, 9));
            addArea(new DisasterArea("Thatta", 65000, 420, 6));
            addArea(new DisasterArea("Swat", 35000, 150, 5));
            addArea(new DisasterArea("Sukkur", 80000, 95, 3));
            addArea(new DisasterArea("Badin", 25000, 12, 2));
        } catch (Exception e) {
            System.out.println("Error initializing data.");
        }
    }

    public void addArea(DisasterArea area) {
        areaQueue.enqueuePriority(area, area.getSeverityScore());
    }

    public DisasterArea serveNextArea() throws Exception {
        if (areaQueue.isEmpty()) {
            throw new Exception("No disaster areas currently pending relief allocation.");
        }
        return areaQueue.dequeue();
    }

    public DisasterArea peekNextArea() throws Exception {
        if (areaQueue.isEmpty()) {
            throw new Exception("No disaster areas currently pending relief allocation.");
        }
        return areaQueue.peek();
    }

    public List<DisasterArea> getPendingAreas() {
        List<DisasterArea> result = new ArrayList<>();
        PriorityQueue<DisasterArea> tempQueue = new PriorityQueue<>();

        while (!areaQueue.isEmpty()) {
            try {
                DisasterArea a = areaQueue.dequeue();
                result.add(a);
                tempQueue.enqueuePriority(a, a.getSeverityScore());
            } catch (Exception e) { break; }
        }
        while (!tempQueue.isEmpty()) {
            try {
                DisasterArea a = tempQueue.dequeue();
                areaQueue.enqueuePriority(a, a.getSeverityScore());
            } catch (Exception e) { break; }
        }
        return result;
    }

    public void confirmAreasServed() {
    }
}