import java.time.LocalDateTime;
import java.util.List;

public class DataLogger {
    private static String timeStamp() {
        return "[" + LocalDateTime.now() + "] ";
    }

    public static void logAudit(String message) {
        System.out.println(timeStamp() + "AUDIT: " + message);
    }

    public static void logDispatch(DispatchRecord record) {
        System.out.println(timeStamp() + "DISPATCH: " + record.toString());
    }

    public static void logInventory(List<Supply> inventory) {
        System.out.println("---- Inventory State Trace " + timeStamp() + " ----");
        for (Supply s : inventory) {
            System.out.println("  " + s.toString());
        }
        System.out.println("----------------------------------------\n");
    }
}