import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {
    private JTextField directoryField;
    private JComboBox<String> extensionComboBox;
    private JTextArea eventDisplayArea;
    private JButton startButton;
    private JButton stopButton;
    private JButton saveButton;

    private List<FileEvent> displayedEvents;

    public MainWindow() {
        displayedEvents = new ArrayList<>();
        setupWindow();
    }

    private void setupWindow() {
        setTitle("File Watcher System");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        createMenuBar();
        createToolbar();
        createMainPanel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                handleExit();
            }
        });
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu watcherMenu = new JMenu("File Watcher");
        JMenu databaseMenu = new JMenu("Database");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem startItem = new JMenuItem("Start Monitoring");
        JMenuItem stopItem = new JMenuItem("Stop Monitoring");
        JMenuItem saveItem = new JMenuItem("Write to Database");
        JMenuItem queryItem = new JMenuItem("Query Database");
        JMenuItem aboutItem = new JMenuItem("About");

        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
        startItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        stopItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
        queryItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        aboutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));

        exitItem.addActionListener(e -> handleExit());
        startItem.addActionListener(e -> startMonitoring());
        stopItem.addActionListener(e -> stopMonitoring());
        saveItem.addActionListener(e -> saveEvents());
        queryItem.addActionListener(e -> openQueryWindow());
        aboutItem.addActionListener(e -> showAbout());

        fileMenu.add(exitItem);
        watcherMenu.add(startItem);
        watcherMenu.add(stopItem);
        databaseMenu.add(saveItem);
        databaseMenu.add(queryItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(watcherMenu);
        menuBar.add(databaseMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        saveButton = new JButton("Write DB");

        startButton.setToolTipText("Start monitoring files");
        stopButton.setToolTipText("Stop monitoring files");
        saveButton.setToolTipText("Write current events to database");

        startButton.addActionListener(e -> startMonitoring());
        stopButton.addActionListener(e -> stopMonitoring());
        saveButton.addActionListener(e -> saveEvents());

        stopButton.setEnabled(false);

        toolBar.add(startButton);
        toolBar.add(stopButton);
        toolBar.add(saveButton);

        add(toolBar, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        directoryField = new JTextField();
        extensionComboBox = new JComboBox<>(new String[]{".txt", ".java", ".csv", ".docx", ".pdf", "All Files"});
        extensionComboBox.setEditable(true);

        inputPanel.add(new JLabel("Directory to Monitor:"));
        inputPanel.add(directoryField);
        inputPanel.add(new JLabel("File Extension:"));
        inputPanel.add(extensionComboBox);

        eventDisplayArea = new JTextArea();
        eventDisplayArea.setEditable(false);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(eventDisplayArea), BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    public void showWindow() {
        setVisible(true);
    }

    private void startMonitoring() {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        eventDisplayArea.append("Monitoring started...\n");
    }

    private void stopMonitoring() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        eventDisplayArea.append("Monitoring stopped...\n");
    }

    private void saveEvents() {
        eventDisplayArea.append("Events saved to database.\n");
    }

    private void openQueryWindow() {
        QueryWindow queryWindow = new QueryWindow();
        queryWindow.setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
                this,
                "File Watcher System\nVersion 1.0\nDevelopers: Rohullah Babakarkhail and Kalsoom Babakarkhail\n\nThis program monitors file events by extension and can save events to a SQLite database.",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleExit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save current events to the database before exiting?",
                "Exit",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            saveEvents();
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
}