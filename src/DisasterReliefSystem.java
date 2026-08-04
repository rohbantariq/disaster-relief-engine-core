import java.util.Scanner;
public class DisasterReliefSystem {
    private static int safeIntInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventoryManager inventory = new InventoryManager();
        AreaManager areaManager = new AreaManager();
        DispatchManager dispatchManager = new DispatchManager(inventory);

        DataLogger.logAudit("System Initialized in Sandbox Memory Mode");
        System.out.println("\n=== DISASTER RELIEF MANAGEMENT SYSTEM ===");

        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Add Disaster Area");
            System.out.println("2. Process Next Pending Disaster Area");
            System.out.println("3. Undo Last Dispatch");
            System.out.println("4. Show Inventory State");
            System.out.println("5. Restock Supplies");
            System.out.println("6. Show Pending Disaster Areas (Queue State)");
            System.out.println("7. Exit");

            int choice = safeIntInput(sc, "Select an option (1-7): ");

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.println("\n--- ADD DISASTER AREA ---");
                        System.out.print("Enter region name: ");
                        String name = sc.nextLine();

                        int pop = safeIntInput(sc, "Enter population: ");
                        int inj = safeIntInput(sc, "Enter injuries: ");
                        int sev = safeIntInput(sc, "Enter priority severity (1-10): ");

                        DisasterArea newArea = new DisasterArea(name, pop, inj, sev);
                        areaManager.addArea(newArea);
                        System.out.println("Successfully queued: " + name);
                    }
                    case 2 -> {
                        System.out.println("\n--- GREEDY ALLOCATION RECOMMENDATIONS ---");
                        DisasterArea target = areaManager.peekNextArea();
                        System.out.println("Target Region Analysis: " + target);

                        int[] recommendations = dispatchManager.calculateRecommendedSupplies(target);

                        System.out.println("\n[RECOMMENDATIONS]");
                        System.out.println(" -> Suggested Food Boxes: " + recommendations[0]);
                        System.out.println(" -> Suggested First Aid Kits: " + recommendations[1]);
                        System.out.println(" -> Suggested Water Bottles: " + recommendations[2]);
                        System.out.println("------------------------------------------------");

                        System.out.println("Enter Dispatch amounts:");
                        int customFood  = safeIntInput(sc, "Enter Food Boxes to dispatch: ");
                        int customMed   = safeIntInput(sc, "Enter First Aid Kits to dispatch: ");
                        int customWater = safeIntInput(sc, "Enter Water Bottles to dispatch: ");

                        if (customFood < 0 || customMed < 0 || customWater < 0) {
                            System.out.println("Dispatch quantities cannot be negative. Action cancelled.");
                            break;
                        }

                        if (customFood == 0 && customMed == 0 && customWater == 0) {
                            System.out.println("No supplies selected. Action cancelled.");
                            break;
                        }

                        System.out.print("\nConfirm manual dispatch overrides? (Y/N): ");
                        String confirm = sc.nextLine().trim();

                        if (confirm.equalsIgnoreCase("Y")) {
                            areaManager.serveNextArea();
                            int batchId = dispatchManager.startNewBatch();
                            System.out.println("\nExecuting manual dispatch batch #" + batchId);
                            if (customFood > 0) {
                                dispatchManager.dispatch(target, new Supply("Food Boxes", customFood), batchId);
                            }
                            if (customMed > 0) {
                                dispatchManager.dispatch(target, new Supply("First Aid Kits", customMed), batchId);
                            }
                            if (customWater > 0) {
                                dispatchManager.dispatch(target, new Supply("Water Bottles", customWater), batchId);
                            }
                            System.out.println("Custom relief deployment complete.");
                        } else {
                            System.out.println("Dispatch cancelled. Area remains at the top of the queue.");
                        }
                    }
                    case 3 -> {
                        dispatchManager.undoLastDispatch(areaManager);
                        System.out.println("Timeline checkpoint reverted back safely.");
                    }
                    case 4 -> inventory.showInventory();
                    case 5 -> {
                        System.out.println("\n--- RESTOCK INVENTORY ---");
                        inventory.showInventory();

                        System.out.print("\nEnter item name to restock: ");
                        String itemName = sc.nextLine();

                        int amount = safeIntInput(sc, "Enter quantity to add: ");
                        if (amount <= 0) {
                            System.out.println("Amount must be positive.");
                            break;
                        }
                        inventory.restockSupply(itemName, amount);
                    }
                    case 6 -> {
                        System.out.println("\n--- CURRENT PENDING DISASTER QUEUE STATE ---");
                        java.util.List<DisasterArea> pending = areaManager.getPendingAreas();
                        if (pending.isEmpty()) {
                            System.out.println("No areas currently waiting in the queue.");
                        } else {
                            int rank = 1;
                            for (DisasterArea area : pending) {
                                System.out.println(" Position [" + (rank++) + "] -> " + area);
                            }
                        }
                        System.out.println("--------------------------------------------");
                    }
                    case 7 -> {
                        System.out.println("Exiting sandbox application environment. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice selection.");
                }
            } catch (Exception e) {
                System.out.println("Execution Error: " + e.getMessage());
            }
        }
    }
}