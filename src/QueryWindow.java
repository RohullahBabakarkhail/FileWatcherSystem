import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QueryWindow extends JFrame {
    private JTextField searchField;
    private JComboBox<String> queryTypeBox;
    private JTextArea resultsArea;
    private DatabaseManager databaseManager;

    public QueryWindow() {
        databaseManager = new DatabaseManager("filewatcher.db");
        setupWindow();
    }

    private void setupWindow() {
        setTitle("Query Database");
        setSize(700, 450);
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

        clearItem.addActionListener(e -> clearDatabase());
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

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> runQuery());

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Query Type:"));
        topPanel.add(queryTypeBox);
        topPanel.add(new JLabel("Search Value:"));
        topPanel.add(searchField);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setText("Query Database Window\nEnter a value and click Search.\n");

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        add(searchButton, BorderLayout.SOUTH);
    }

    private void runQuery() {
        String queryType = queryTypeBox.getSelectedItem().toString();
        String value = searchField.getText().trim();

        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search value.");
            return;
        }

        try {
            List<FileEvent> results;

            if (queryType.equals("Extension")) {
                results = databaseManager.getEventsByExtension(value);
            } else if (queryType.equals("File Name")) {
                results = databaseManager.getEventsByFileName(value);
            } else if (queryType.equals("Event Type")) {
                results = databaseManager.getEventsByEventType(value);
            } else {
                results = databaseManager.getEventsByDate(value);
            }

            displayResults(results);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Query error: " + e.getMessage());
        }
    }

    private void displayResults(List<FileEvent> results) {
        resultsArea.setText("");

        if (results.isEmpty()) {
            resultsArea.append("No matching records found.\n");
            return;
        }

        resultsArea.append("Results found: " + results.size() + "\n\n");

        for (FileEvent event : results) {
            resultsArea.append("------------------------------\n");
            resultsArea.append(event.toString());
            resultsArea.append("\n");
        }
    }

    private void clearDatabase() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear the database?",
                "Clear Database",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                databaseManager.clearDatabase();
                resultsArea.setText("Database cleared.\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Clear database error: " + e.getMessage());
            }
        }
    }
}
