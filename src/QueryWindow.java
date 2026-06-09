import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Query window for searching the SQLite database.
 * Users can query by extension, file name, activity type, path, or date range.
 * Results can be exported to CSV and emailed.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class QueryWindow extends JFrame {
    private JTextField searchField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JComboBox<String> queryTypeBox;
    private JTextArea resultsArea;

    private JButton searchButton;
    private JButton exportButton;
    private JButton emailButton;

    private DatabaseManager databaseManager;
    private List<FileEvent> currentResults;
    private File lastExportedFile;

    /**
     * Creates the Query Window.
     */
    public QueryWindow() {
        databaseManager = new DatabaseManager("filewatcher.db");
        currentResults = new ArrayList<>();
        setupWindow();
    }

    /**
     * Sets up the window.
     */
    private void setupWindow() {
        setTitle("Query Database");
        setSize(800, 550);
        setLayout(new BorderLayout(10, 10));

        createMenuBar();
        createMainPanel();

        setLocationRelativeTo(null);
    }

    /**
     * Creates the menu bar.
     */
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

    /**
     * Creates the main query panel.
     */
    private void createMainPanel() {
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        queryTypeBox = new JComboBox<>(new String[]{
                "Extension",
                "File Name",
                "Activity",
                "Path",
                "Date Range"
        });

        searchField = new JTextField();
        startDateField = new JTextField();
        endDateField = new JTextField();

        queryTypeBox.addActionListener(e -> updateDateRangeFields());

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Query Type:"));
        topPanel.add(queryTypeBox);

        topPanel.add(new JLabel("Search Value:"));
        topPanel.add(searchField);

        topPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        topPanel.add(startDateField);

        topPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        topPanel.add(endDateField);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setText("Query Database Window\nEnter query information and click Search.\n");

        JPanel buttonPanel = new JPanel();

        searchButton = new JButton("Search");
        exportButton = new JButton("Export CSV");
        emailButton = new JButton("Email CSV");

        searchButton.addActionListener(e -> runQuery());
        exportButton.addActionListener(e -> exportResults());
        emailButton.addActionListener(e -> emailResults());

        buttonPanel.add(searchButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(emailButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        updateDateRangeFields();
    }

    /**
     * Enables date range fields only when Date Range is selected.
     */
    private void updateDateRangeFields() {
        String queryType = queryTypeBox.getSelectedItem().toString();
        boolean isDateRange = queryType.equals("Date Range");

        startDateField.setEnabled(isDateRange);
        endDateField.setEnabled(isDateRange);
        searchField.setEnabled(!isDateRange);
    }

    /**
     * Runs the selected query.
     */
    private void runQuery() {
        String queryType = queryTypeBox.getSelectedItem().toString();
        String value = searchField.getText().trim();
        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();

        try {
            if (queryType.equals("Date Range")) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter both start and end dates.");
                    return;
                }

                currentResults = databaseManager.getEventsByDateRange(startDate, endDate);
                displayResults(currentResults, queryType, startDate + " to " + endDate);
                return;
            }

            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search value.");
                return;
            }

            if (queryType.equals("Extension")) {
                currentResults = databaseManager.getEventsByExtension(value);
            } else if (queryType.equals("File Name")) {
                currentResults = databaseManager.getEventsByFileName(value);
            } else if (queryType.equals("Activity")) {
                currentResults = databaseManager.getEventsByEventType(value);
            } else {
                currentResults = databaseManager.getEventsByPath(value);
            }

            displayResults(currentResults, queryType, value);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Query error: " + e.getMessage());
        }
    }

    /**
     * Displays query results clearly on the screen.
     *
     * @param results    query results
     * @param queryType  query type
     * @param queryValue query value
     */
    private void displayResults(List<FileEvent> results, String queryType, String queryValue) {
        resultsArea.setText("");

        resultsArea.append("Query Type: " + queryType + "\n");
        resultsArea.append("Query Value: " + queryValue + "\n");
        resultsArea.append("Results found: " + results.size() + "\n\n");

        if (results.isEmpty()) {
            resultsArea.append("No matching records found.\n");
            return;
        }

        resultsArea.append(String.format(
                "%-25s %-12s %-45s %-12s %-25s%n",
                "File Name", "Extension", "Path", "Activity", "Date/Time"
        ));
        resultsArea.append("---------------------------------------------------------------------------------------------\n");

        for (FileEvent event : results) {
            resultsArea.append(String.format(
                    "%-25s %-12s %-45s %-12s %-25s%n",
                    event.getFileName(),
                    event.getFileExtension(),
                    event.getAbsolutePath(),
                    event.getEventType(),
                    event.getEventDateTime()
            ));
        }
    }

    /**
     * Exports current query results to CSV.
     */
    private void exportResults() {
        if (currentResults == null || currentResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no query results to export.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Name and Save CSV File");
        chooser.setSelectedFile(new File("file_watcher_query_results.csv"));

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = chooser.getSelectedFile();

                if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
                }

                String queryType = queryTypeBox.getSelectedItem().toString();
                String queryValue = getCurrentQueryValue();

                CsvExporter exporter = new CsvExporter();
                exporter.exportToCsv(selectedFile, queryType, queryValue, currentResults);

                lastExportedFile = selectedFile;

                JOptionPane.showMessageDialog(this, "CSV file exported successfully.");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "CSV export error: " + e.getMessage());
            }
        }
    }

    /**
     * Gets the current query value for export information.
     *
     * @return query value
     */
    private String getCurrentQueryValue() {
        String queryType = queryTypeBox.getSelectedItem().toString();

        if (queryType.equals("Date Range")) {
            return startDateField.getText().trim() + " to " + endDateField.getText().trim();
        }

        return searchField.getText().trim();
    }

    /**
     * Emails the last generated CSV file.
     */
    private void emailResults() {
        if (lastExportedFile == null || !lastExportedFile.exists()) {
            JOptionPane.showMessageDialog(this, "Please export a CSV file before emailing.");
            return;
        }

        JTextField senderField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField recipientField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Sender Gmail:"));
        panel.add(senderField);
        panel.add(new JLabel("Gmail App Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Recipient Email:"));
        panel.add(recipientField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Email CSV File",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                EmailService emailService = new EmailService();
                emailService.sendEmailWithAttachment(
                        senderField.getText().trim(),
                        new String(passwordField.getPassword()),
                        recipientField.getText().trim(),
                        lastExportedFile
                );

                JOptionPane.showMessageDialog(this, "Email sent successfully.");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Email error: " + e.getMessage());
            }
        }
    }

    /**
     * Clears the database after user confirmation.
     */
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
                currentResults.clear();
                resultsArea.setText("Database cleared.\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Clear database error: " + e.getMessage());
            }
        }
    }
}
