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
        setSize(600, 400);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu databaseMenu = new JMenu("Database");
        JMenuItem clearItem = new JMenuItem("Clear Database");
        JMenuItem closeItem = new JMenuItem("Return to Main Window");

        clearItem.addActionListener(e -> resultsArea.append("Database cleared.\n"));
        closeItem.addActionListener(e -> dispose());

        databaseMenu.add(clearItem);
        databaseMenu.add(closeItem);
        menuBar.add(databaseMenu);
        setJMenuBar(menuBar);

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        queryTypeBox = new JComboBox<>(new String[]{
                "Extension",
                "File Name",
                "Event Type",
                "Date"
        });

        searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> search());

        topPanel.add(new JLabel("Query Type:"));
        topPanel.add(queryTypeBox);
        topPanel.add(new JLabel("Search Value:"));
        topPanel.add(searchField);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        add(searchButton, BorderLayout.SOUTH);
    }

    private void search() {
        String queryType = queryTypeBox.getSelectedItem().toString();
        String value = searchField.getText();

        resultsArea.append("Searching by " + queryType + ": " + value + "\n");
    }
}