import javax.swing.*;
import java.awt.*;

public class QueryWindow extends JFrame {
    private JTextField searchField;
    private JComboBox<String> queryTypeBox;
    private JTextArea resultsArea;

    public QueryWindow() {
        setupWindow();
    }

    private void setupWindow() {
        setTitle("Query Database");
        setSize(650, 400);
        setLayout(new BorderLayout(10, 10));

        createMenuBar();
        createMainPanel();

        setLocationRelativeTo(null);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu databaseMenu = new JMenu("Database");

        JMenuItem clearItem = new JMenuItem("Clear Database");
        JMenuItem closeItem = new JMenuItem("Return to Main Window");

        clearItem.addActionListener(e -> clearDatabasePlaceholder());
        closeItem.addActionListener(e -> dispose());

        databaseMenu.add(clearItem);
        databaseMenu.add(closeItem);

        menuBar.add(databaseMenu);

        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        queryTypeBox = new JComboBox<>(new String[]{
                "Extension",
                "File Name",
                "Event Type",
                "Date"
        });
        queryTypeBox.setToolTipText("Choose the type of database query to run later.");

        searchField = new JTextField();
        searchField.setToolTipText("Enter the value to search for.");

        JButton searchButton = new JButton("Search");
        searchButton.setToolTipText("Placeholder search button for future database queries.");
        searchButton.addActionListener(e -> searchPlaceholder());

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Query Type:"));
        topPanel.add(queryTypeBox);
        topPanel.add(new JLabel("Search Value:"));
        topPanel.add(searchField);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setToolTipText("Future database query results will appear here.");
        resultsArea.setText(
                "Query Database Window\n" +
                        "Iteration 4 placeholder.\n" +
                        "Real SQLite query results will be added in a later iteration.\n"
        );

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        add(searchButton, BorderLayout.SOUTH);
    }

    private void searchPlaceholder() {
        String queryType = queryTypeBox.getSelectedItem().toString();
        String value = searchField.getText();

        resultsArea.append("\nSearch clicked.\n");
        resultsArea.append("Query Type: " + queryType + "\n");
        resultsArea.append("Search Value: " + value + "\n");
        resultsArea.append("Placeholder only. Real SQLite query logic will be added later.\n");
    }

    private void clearDatabasePlaceholder() {
        resultsArea.append("\nClear Database clicked.\n");
        resultsArea.append("Placeholder only. Real database clearing will be added later.\n");
    }
}
