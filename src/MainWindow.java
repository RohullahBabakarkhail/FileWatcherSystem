import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {
    private JTextField directoryField;
    private JComboBox<String> extensionComboBox;
    private JTextArea eventDisplayArea;

    private JButton startButton;
    private JButton stopButton;
    private JButton saveButton;
    private JButton browseButton;
    private JButton sampleEventButton;

    private JLabel eventCountLabel;

    private List<FileEvent> displayedEvents;
    private FileMonitor fileMonitor;

    public MainWindow() {
        displayedEvents = new ArrayList<>();
        setupWindow();
    }

    private void setupWindow() {
        setTitle("File Watcher System");
        setSize(850, 550);
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
        sampleEventButton = new JButton("Sample Events");

        startButton.setToolTipText("Start monitoring placeholder");
        stopButton.setToolTipText("Stop monitoring placeholder");
        saveButton.setToolTipText("Write current events to database placeholder");
        sampleEventButton.setToolTipText("Display sample file events");

        startButton.addActionListener(e -> startMonitoring());
        stopButton.addActionListener(e -> stopMonitoring());
        saveButton.addActionListener(e -> saveEvents());
        sampleEventButton.addActionListener(e -> addSampleEvents());

        stopButton.setEnabled(false);

        toolBar.add(startButton);
        toolBar.add(stopButton);
        toolBar.add(saveButton);
        toolBar.add(sampleEventButton);

        add(toolBar, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel directoryLabel = new JLabel("Directory to Monitor:");
        directoryField = new JTextField();

        browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> browseDirectory());

        JLabel extensionLabel = new JLabel("File Extension:");
        extensionComboBox = new JComboBox<>(new String[]{
                ".txt",
                ".java",
                ".csv",
                ".docx",
                ".pdf",
                "All Files"
        });
        extensionComboBox.setEditable(true);

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        inputPanel.add(directoryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        inputPanel.add(directoryField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        inputPanel.add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        inputPanel.add(extensionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        inputPanel.add(extensionComboBox, gbc);

        eventDisplayArea = new JTextArea();
        eventDisplayArea.setEditable(false);
        eventDisplayArea.setText(
                "File Watcher System ready.\n" +
                        "Choose a directory, select an extension, and click Start.\n" +
                        "Iteration 3 includes validation, sample event display, and event count.\n"
        );

        eventCountLabel = new JLabel("Event Count: 0");

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(eventDisplayArea), BorderLayout.CENTER);
        panel.add(eventCountLabel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
    }

    public void showWindow() {
        setVisible(true);
    }

    private void browseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose Directory to Monitor");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chooser.getSelectedFile();
            directoryField.setText(selectedFolder.getAbsolutePath());
            eventDisplayArea.append("\nSelected directory:\n");
            eventDisplayArea.append(selectedFolder.getAbsolutePath() + "\n");
        } else {
            eventDisplayArea.append("\nDirectory selection canceled.\n");
        }
    }

    private boolean isDirectoryValid(String directoryPath) {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please choose or enter a directory first.",
                    "Missing Directory",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "The selected directory does not exist.",
                    "Invalid Directory",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (!directory.isDirectory()) {
            JOptionPane.showMessageDialog(
                    this,
                    "The selected path is not a folder.",
                    "Invalid Directory",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }

    private void startMonitoring() {
        String directory = directoryField.getText().trim();
        String extension = extensionComboBox.getSelectedItem().toString();

        if (!isDirectoryValid(directory)) {
            eventDisplayArea.append("\nMonitoring could not start because the directory is invalid.\n");
            return;
        }

        fileMonitor = new FileMonitor(directory, extension);
        fileMonitor.startMonitoring();

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        eventDisplayArea.append("\nMonitoring started...\n");
        eventDisplayArea.append("Directory: " + directory + "\n");
        eventDisplayArea.append("Extension: " + extension + "\n");
        eventDisplayArea.append("Note: Real-time file monitoring will be completed in a later iteration.\n");
    }

    private void stopMonitoring() {
        if (fileMonitor != null) {
            fileMonitor.stopMonitoring();
        }

        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        eventDisplayArea.append("\nMonitoring stopped...\n");
    }

    private void addSampleEvents() {
        FileEvent createEvent = new FileEvent(
                "sample-create.txt",
                "C:\\SampleFolder\\sample-create.txt",
                "CREATE",
                LocalDateTime.now().toString()
        );

        FileEvent modifyEvent = new FileEvent(
                "sample-modify.txt",
                "C:\\SampleFolder\\sample-modify.txt",
                "MODIFY",
                LocalDateTime.now().toString()
        );

        FileEvent deleteEvent = new FileEvent(
                "sample-delete.txt",
                "C:\\SampleFolder\\sample-delete.txt",
                "DELETE",
                LocalDateTime.now().toString()
        );

        displayedEvents.add(createEvent);
        displayedEvents.add(modifyEvent);
        displayedEvents.add(deleteEvent);

        eventDisplayArea.append("\nSample File Events Added:\n");
        displayFormattedEvent(createEvent);
        displayFormattedEvent(modifyEvent);
        displayFormattedEvent(deleteEvent);

        updateEventCount();
    }

    private void displayFormattedEvent(FileEvent event) {
        eventDisplayArea.append("------------------------------\n");
        eventDisplayArea.append("File Name: " + event.getFileName() + "\n");
        eventDisplayArea.append("Path: " + event.getAbsolutePath() + "\n");
        eventDisplayArea.append("Event Type: " + event.getEventType() + "\n");
        eventDisplayArea.append("Date/Time: " + event.getEventDateTime() + "\n");
    }

    private void updateEventCount() {
        eventCountLabel.setText("Event Count: " + displayedEvents.size());
        eventDisplayArea.append("Current Event Count: " + displayedEvents.size() + "\n");
    }

    private void saveEvents() {
        if (displayedEvents.isEmpty()) {
            eventDisplayArea.append("\nWrite DB clicked, but there are no events to save yet.\n");
            eventDisplayArea.append("Event Count: 0\n");
            eventDisplayArea.append("Add sample events first or wait for real monitoring in a later iteration.\n");
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");
        databaseManager.connect();

        for (FileEvent event : displayedEvents) {
            databaseManager.saveEvent(event);
        }

        eventDisplayArea.append("\nWrite DB clicked.\n");
        eventDisplayArea.append("Placeholder save completed for " + displayedEvents.size() + " event(s).\n");
        eventDisplayArea.append("Real SQLite saving will be completed in a later iteration.\n");

        updateEventCount();
    }

    private void openQueryWindow() {
        QueryWindow queryWindow = new QueryWindow();
        queryWindow.setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
                this,
                "File Watcher System\n" +
                        "Version 3.0 - Iteration 3\n\n" +
                        "Developers:\n" +
                        "Rohullah Babakarkhail\n" +
                        "Kalsoom Babakarkhail\n\n" +
                        "Current Features:\n" +
                        "- Browse button for folder selection\n" +
                        "- Directory validation\n" +
                        "- Placeholder Start and Stop monitoring behavior\n" +
                        "- Sample CREATE, MODIFY, and DELETE file events\n" +
                        "- Basic event count display\n" +
                        "- Placeholder database save message\n\n" +
                        "Future Features:\n" +
                        "- Real-time Java WatchService monitoring\n" +
                        "- SQLite database saving\n" +
                        "- Real database query results",
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
