import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DisasterReliefGUI extends JFrame {
    private AreaManager areaManager;
    private InventoryManager inventoryManager;
    private DispatchManager dispatchManager;

    // View Components
    private JTextArea queueDisplayArea;
    private JTextArea inventoryDisplayArea;

    // Process & Undo Buttons
    private JButton loadNextAreaButton;
    private JButton executeDispatchButton;
    private JButton undoButton;
    private JButton refreshButton;

    // Inputs for Adding Area
    private JTextField areaNameField;
    private JTextField populationField;
    private JTextField injuriesField;
    private JTextField severityField;
    private JButton addAreaButton;

    private JComboBox<String> supplyTypeCombo;
    private JTextField restockAmountField;
    private JButton restockButton;

    private JLabel activeTargetLabel;
    private JTextField customFoodField;
    private JTextField customMedField;
    private JTextField customWaterField;
    private DisasterArea currentLoadedArea = null;

    public DisasterReliefGUI() {
        this.inventoryManager = new InventoryManager();
        this.areaManager = new AreaManager();
        this.dispatchManager = new DispatchManager(inventoryManager);

        setTitle("Disaster Relief Control Center (Manual Dispatch Override Mode)");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel headerLabel = new JLabel("Real-Time Bounded Resource Allocation Engine");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel sideInputPanel = new JPanel();
        sideInputPanel.setLayout(new BoxLayout(sideInputPanel, BoxLayout.Y_AXIS));
        sideInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        sideInputPanel.setPreferredSize(new Dimension(340, 650));

        JPanel addAreaPanel = new JPanel(new GridLayout(5, 2, 5, 6));
        addAreaPanel.setBorder(BorderFactory.createTitledBorder("Register New Disaster Area"));
        addAreaPanel.add(new JLabel(" Area Name:"));
        areaNameField = new JTextField(); addAreaPanel.add(areaNameField);
        addAreaPanel.add(new JLabel(" Population:"));
        populationField = new JTextField(); addAreaPanel.add(populationField);
        addAreaPanel.add(new JLabel(" Injuries Count:"));
        injuriesField = new JTextField(); addAreaPanel.add(injuriesField);
        addAreaPanel.add(new JLabel(" Severity (1-10):"));
        severityField = new JTextField(); addAreaPanel.add(severityField);
        addAreaPanel.add(new JLabel(""));
        addAreaButton = new JButton("Register Area");
        addAreaButton.setBackground(new Color(39, 174, 96));
        addAreaButton.setForeground(Color.WHITE);
        addAreaPanel.add(addAreaButton);

        JPanel restockPanel = new JPanel(new GridLayout(3, 2, 5, 6));
        restockPanel.setBorder(BorderFactory.createTitledBorder("Restock Warehouse Inventory"));
        restockPanel.add(new JLabel(" Supply Item:"));
        String[] supplyItems = {"Food Boxes", "First Aid Kits", "Water Bottles"};
        supplyTypeCombo = new JComboBox<>(supplyItems); restockPanel.add(supplyTypeCombo);
        restockPanel.add(new JLabel(" Restock Qty:"));
        restockAmountField = new JTextField(); restockPanel.add(restockAmountField);
        restockPanel.add(new JLabel(""));
        restockButton = new JButton("Update Stock Balance");
        restockButton.setBackground(new Color(142, 68, 173));
        restockButton.setForeground(Color.WHITE);
        restockPanel.add(restockButton);

        JPanel dispatchOverridePanel = new JPanel(new GridLayout(5, 2, 5, 6));
        dispatchOverridePanel.setBorder(BorderFactory.createTitledBorder("Active Dispatch Parameters"));

        dispatchOverridePanel.add(new JLabel(" Active Target:"));
        activeTargetLabel = new JLabel("NONE (Load Next Area)");
        activeTargetLabel.setFont(new Font("Arial", Font.BOLD, 12));
        activeTargetLabel.setForeground(Color.RED);
        dispatchOverridePanel.add(activeTargetLabel);

        dispatchOverridePanel.add(new JLabel(" Food Boxes Qty:"));
        customFoodField = new JTextField();
        dispatchOverridePanel.add(customFoodField);

        dispatchOverridePanel.add(new JLabel(" First Aid Kits Qty:"));
        customMedField = new JTextField();
        dispatchOverridePanel.add(customMedField);

        dispatchOverridePanel.add(new JLabel(" Water Bottles Qty:"));
        customWaterField = new JTextField();
        dispatchOverridePanel.add(customWaterField);

        dispatchOverridePanel.add(new JLabel(""));
        executeDispatchButton = new JButton("Dispatch");
        executeDispatchButton.setBackground(new Color(230, 126, 34));
        executeDispatchButton.setForeground(Color.WHITE);
        executeDispatchButton.setEnabled(false);
        dispatchOverridePanel.add(executeDispatchButton);

        sideInputPanel.add(addAreaPanel);
        sideInputPanel.add(Box.createVerticalStrut(10));
        sideInputPanel.add(restockPanel);
        sideInputPanel.add(Box.createVerticalStrut(10));
        sideInputPanel.add(dispatchOverridePanel);
        add(sideInputPanel, BorderLayout.WEST);

        JPanel monitorPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        monitorPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        JPanel queuePanel = new JPanel(new BorderLayout());
        queuePanel.setBorder(BorderFactory.createTitledBorder("Pending Areas"));
        queueDisplayArea = new JTextArea();
        queueDisplayArea.setEditable(false);
        queueDisplayArea.setBackground(new Color(248, 249, 249));
        queueDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        queuePanel.add(new JScrollPane(queueDisplayArea), BorderLayout.CENTER);

        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Warehouse Stock"));
        inventoryDisplayArea = new JTextArea();
        inventoryDisplayArea.setEditable(false);
        inventoryDisplayArea.setBackground(new Color(248, 249, 249));
        inventoryDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inventoryPanel.add(new JScrollPane(inventoryDisplayArea), BorderLayout.CENTER);

        monitorPanel.add(queuePanel);
        monitorPanel.add(inventoryPanel);
        add(monitorPanel, BorderLayout.CENTER);

        JPanel controlActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        controlActionPanel.setBackground(new Color(234, 236, 238));

        loadNextAreaButton = new JButton("1. Process Dispatch");
        loadNextAreaButton.setFont(new Font("Arial", Font.BOLD, 12));
        undoButton = new JButton("2. Undo Last Dispatch");
        undoButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton = new JButton("3. Refresh System State");

        controlActionPanel.add(loadNextAreaButton);
        controlActionPanel.add(undoButton);
        controlActionPanel.add(refreshButton);
        add(controlActionPanel, BorderLayout.SOUTH);

        addAreaButton.addActionListener(e -> {
            try {
                String name = areaNameField.getText().trim();
                int pop = Integer.parseInt(populationField.getText().trim());
                int inj = Integer.parseInt(injuriesField.getText().trim());
                int sev = Integer.parseInt(severityField.getText().trim());

                if (name.isEmpty()) throw new Exception("Area name field cannot be blank!");

                DisasterArea newArea = new DisasterArea(name, pop, inj, sev);
                areaManager.addArea(newArea);

                JOptionPane.showMessageDialog(this, "Region '" + name + "' inserted successfully.\nSorted into its correct priority rank!");
                areaNameField.setText(""); populationField.setText(""); injuriesField.setText(""); severityField.setText("");
                updateDashboardView();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter valid integers for numerical inputs.", "Input Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Constraint Violation", JOptionPane.ERROR_MESSAGE);
            }
        });

        restockButton.addActionListener(e -> {
            try {
                String selectedItem = (String) supplyTypeCombo.getSelectedItem();
                int amount = Integer.parseInt(restockAmountField.getText().trim());
                if (amount <= 0) throw new Exception("Restock quantity must be greater than zero!");

                inventoryManager.restockSupply(selectedItem, amount);
                JOptionPane.showMessageDialog(this, "Warehouse balance updated for: " + selectedItem);
                restockAmountField.setText("");
                updateDashboardView();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Stock Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loadNextAreaButton.addActionListener(e -> {
            try {
                currentLoadedArea = areaManager.peekNextArea();
                int[] suggestions = dispatchManager.calculateRecommendedSupplies(currentLoadedArea);

                activeTargetLabel.setText(currentLoadedArea.getAreaName() + " (Sev: " + currentLoadedArea.getSeverityScore() + ")");
                activeTargetLabel.setForeground(new Color(39, 174, 96));

                customFoodField.setText(String.valueOf(suggestions[0]));
                customMedField.setText(String.valueOf(suggestions[1]));
                customWaterField.setText(String.valueOf(suggestions[2]));

                executeDispatchButton.setEnabled(true); // Unlock dispatch button
                JOptionPane.showMessageDialog(this, "Greedy estimates loaded for " + currentLoadedArea.getAreaName() + ".\nYou can now modify the numbers in the fields manually if needed!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Halted: " + ex.getMessage(), "Queue Alert", JOptionPane.WARNING_MESSAGE);
            }
        });

        executeDispatchButton.addActionListener(e -> {
            try {
                if (currentLoadedArea == null) return;

                int userFood = Integer.parseInt(customFoodField.getText().trim());
                int userMed = Integer.parseInt(customMedField.getText().trim());
                int userWater = Integer.parseInt(customWaterField.getText().trim());

                if (userFood < 0 || userMed < 0 || userWater < 0) {
                    throw new Exception("Dispatch quantities cannot be negative numbers.");
                }

                String confirmPrompt = String.format(
                        "Confirming Custom Deployment to %s:\n" +
                                "-> Food Boxes: %,d units\n" +
                                "-> First Aid Kits: %,d units\n" +
                                "-> Water Bottles: %,d units\n\n" +
                                "Process transaction?", currentLoadedArea.getAreaName(), userFood, userMed, userWater
                );

                int choice = JOptionPane.showConfirmDialog(this, confirmPrompt, "Authorization Gate", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    areaManager.serveNextArea();
                    int batchId = dispatchManager.startNewBatch();

                    dispatchManager.dispatch(currentLoadedArea, new Supply("Food Boxes", userFood), batchId);
                    dispatchManager.dispatch(currentLoadedArea, new Supply("First Aid Kits", userMed), batchId);
                    dispatchManager.dispatch(currentLoadedArea, new Supply("Water Bottles", userWater), batchId);

                    JOptionPane.showMessageDialog(this, "Custom transaction pushed cleanly onto LIFO history stack!");

                    currentLoadedArea = null;
                    activeTargetLabel.setText("NONE (Load Next Area)");
                    activeTargetLabel.setForeground(Color.RED);
                    customFoodField.setText(""); customMedField.setText(""); customWaterField.setText("");
                    executeDispatchButton.setEnabled(false);

                    updateDashboardView();
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter valid integers in the custom allocation fields.", "Value Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Transaction Aborted: " + ex.getMessage(), "Boundary Restriction", JOptionPane.ERROR_MESSAGE);
            }
        });

        undoButton.addActionListener(e -> {
            try {
                dispatchManager.undoLastDispatch(areaManager);
                JOptionPane.showMessageDialog(this, "LIFO Stack popped! Last custom batch reversed and area re-queued.");
                updateDashboardView();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Undo Blocked", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshButton.addActionListener(e -> updateDashboardView());
        updateDashboardView();
    }

    private void updateDashboardView() {
        queueDisplayArea.setText("");
        inventoryDisplayArea.setText("");

        List<DisasterArea> pending = areaManager.getPendingAreas();
        if (pending.isEmpty()) {
            queueDisplayArea.setText(" [STATUS]: No active regions left.\n All locations successfully served.");
        } else {
            int currentRank = 1;
            for (DisasterArea area : pending) {
                queueDisplayArea.append(String.format(
                        " Rank [%d] -> Severity Score: %d (%s)\n" +
                                "  Region: %s\n" +
                                "  Population: %,d | Injuries: %,d\n" +
                                "--------------------------------------------------\n",
                        currentRank++, area.getSeverityScore(), area.getSeverityType(),
                        area.getAreaName(), area.getPopulation(), area.getInjuries()
                ));
            }
        }

        List<Supply> currentStockList = inventoryManager.getInventory();
        for (Supply item : currentStockList) {
            inventoryDisplayArea.append(String.format(
                    " Item Type: %-18s\n Balanced Capacity: %,d units\n" +
                            "--------------------------------------------------\n",
                    item.getItemName(), item.getQuantity()
            ));
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DisasterReliefGUI().setVisible(true);
        });
    }
}