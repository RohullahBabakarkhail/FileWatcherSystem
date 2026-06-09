import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main GUI window for the File Watcher System.
 * Users can choose a directory, select an extension, start/stop monitoring,
 * save events to SQLite, and open the Query Window.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class MainWindow extends JFrame implements FileEventListener {
    private JTextField directoryField;
    private JComboBox<String> extensionComboBox;
    private JTextArea eventDisplayArea;

    private JButton startButton;
    private JButton stopButton;
    private JButton saveButton;
    private JButton browseButton;
    private JButton sampleEventButton;
    private JButton clearEventsButton;

    private JLabel eventCountLabel;

    private List<FileEvent> displayedEvents;
    private FileMonitor fileMonitor;
    private boolean hasUnsavedEvents;

    /**
     * Creates the MainWindow.
     */
    public MainWindow() {
        displayedEvents = new ArrayList<>();
        hasUnsavedEvents = false;
        setupWindow();
    }

    /**
     * Sets up the main window.
     */
    private void setupWindow() {
        setTitle("File Watcher System");
        setSize(1000, 650);
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

    /**
     * Creates the menu strip.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu watcherMenu = new JMenu("File Watcher");
        JMenu databaseMenu = new JMenu("Database");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem startItem = new JMenuItem("Start Monitoring");
        JMenuItem stopItem = new JMenuItem("Stop Monitoring");
        JMenuItem clearEventsItem = new JMenuItem("Clear Events");
        JMenuItem saveItem = new JMenuItem("Write to Database");
        JMenuItem queryItem = new JMenuItem("Query Database");
        JMenuItem aboutItem = new JMenuItem("About");

        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
        startItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        stopItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
        clearEventsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
        queryItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        aboutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));

        exitItem.addActionListener(e -> handleExit());
        startItem.addActionListener(e -> startMonitoring());
        stopItem.addActionListener(e -> stopMonitoring());
        clearEventsItem.addActionListener(e -> clearEvents());
        saveItem.addActionListener(e -> saveEvents());
        queryItem.addActionListener(e -> openQueryWindow());
        aboutItem.addActionListener(e -> showAbout());

        fileMenu.add(exitItem);

        watcherMenu.add(startItem);
        watcherMenu.add(stopItem);
        watcherMenu.add(clearEventsItem);

        databaseMenu.add(saveItem);
        databaseMenu.add(queryItem);

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(watcherMenu);
        menuBar.add(databaseMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Creates toolbar buttons.
     */
    private void createToolbar() {
        JToolBar toolBar = new JToolBar();

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        saveButton = new JButton("Write DB");
        sampleEventButton = new JButton("Sample Events");
        clearEventsButton = new JButton("Clear Events");

        startButton.setToolTipText("Start real file monitoring for the selected directory.");
        stopButton.setToolTipText("Stop the current monitoring session.");
        saveButton.setToolTipText("Save displayed events to SQLite database.");
        sampleEventButton.setToolTipText("Add sample CREATE, CHANGE, DELETE, and RENAME file events.");
        clearEventsButton.setToolTipText("Clear all displayed events and reset the event count.");

        startButton.addActionListener(e -> startMonitoring());
        stopButton.addActionListener(e -> stopMonitoring());
        saveButton.addActionListener(e -> saveEvents());
        sampleEventButton.addActionListener(e -> addSampleEvents());
        clearEventsButton.addActionListener(e -> clearEvents());

        stopButton.setEnabled(false);

        toolBar.add(startButton);
        toolBar.add(stopButton);
        toolBar.add(saveButton);
        toolBar.add(sampleEventButton);
        toolBar.add(clearEventsButton);

        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Creates the main input and display panel.
     */
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

        gbc.insets = new Insets(8, 8, 8, 8);
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
                        "Final Version - Iteration 6\n\n" +
                        "Steps:\n" +
                        "1. Click Browse to choose a folder.\n" +
                        "2. Select or type a file extension.\n" +
                        "3. Click Start to begin monitoring.\n" +
                        "4. Create, change, delete, or rename a matching file in the folder.\n" +
                        "5. Click Write DB to save events.\n" +
                        "6. Use Database > Query Database to search, export, and email saved results.\n"
        );

        eventCountLabel = new JLabel("Event Count: 0");

        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(eventDisplayArea), BorderLayout.CENTER);
        panel.add(eventCountLabel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Shows the main window.
     */
    public void showWindow() {
        setVisible(true);
    }

    /**
     * Opens a folder chooser for selecting the monitored directory.
     */
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

    /**
     * Validates that a directory path exists and is a folder.
     *
     * @param directoryPath the directory path
     * @return true if the path is valid
     */
    private boolean isDirectoryValid(String directoryPath) {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please choose or enter a directory first.");
            return false;
        }

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            JOptionPane.showMessageDialog(this, "The selected directory does not exist.");
            return false;
        }

        if (!directory.isDirectory()) {
            JOptionPane.showMessageDialog(this, "The selected path is not a folder.");
            return false;
        }

        return true;
    }

    /**
     * Starts file monitoring.
     */
    private void startMonitoring() {
        String directory = directoryField.getText().trim();
        String extension = extensionComboBox.getSelectedItem().toString();

        if (!isDirectoryValid(directory)) {
            eventDisplayArea.append("\nMonitoring could not start because the directory is invalid.\n");
            return;
        }

        fileMonitor = new FileMonitor(directory, extension, this);
        fileMonitor.startMonitoring();

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        eventDisplayArea.append("\nMonitoring started.\n");
        eventDisplayArea.append("Directory: " + directory + "\n");
        eventDisplayArea.append("Extension: " + extension + "\n");
    }

    /**
     * Stops file monitoring.
     */
    private void stopMonitoring() {
        if (fileMonitor != null) {
            fileMonitor.stopMonitoring();
        }

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    /**
     * Handles a detected file event from FileMonitor.
     *
     * @param event the detected file event
     */
    @Override
    public void onFileEvent(FileEvent event) {
        displayedEvents.add(event);
        hasUnsavedEvents = true;

        eventDisplayArea.append("\nFile event detected:\n");
        displayFormattedEvent(event);
        updateEventCount();
    }

    /**
     * Handles monitor messages.
     *
     * @param message the monitor message
     */
    @Override
    public void onMonitorMessage(String message) {
        eventDisplayArea.append("\n" + message + "\n");
    }

    /**
     * Adds sample events for testing.
     */
    private void addSampleEvents() {
        String directory = directoryField.getText().trim();

        if (directory.isEmpty()) {
            directory = "C:\\SampleFolder";
        }

        FileEvent createEvent = new FileEvent("sample-create.txt", directory + File.separator + "sample-create.txt", "CREATE", java.time.LocalDateTime.now().toString());
        FileEvent changeEvent = new FileEvent("sample-change.txt", directory + File.separator + "sample-change.txt", "CHANGE", java.time.LocalDateTime.now().toString());
        FileEvent deleteEvent = new FileEvent("sample-delete.txt", directory + File.separator + "sample-delete.txt", "DELETE", java.time.LocalDateTime.now().toString());
        FileEvent renameEvent = new FileEvent("sample-renamed.txt", directory + File.separator + "sample-renamed.txt", "RENAME", java.time.LocalDateTime.now().toString());

        displayedEvents.add(createEvent);
        displayedEvents.add(changeEvent);
        displayedEvents.add(deleteEvent);
        displayedEvents.add(renameEvent);
        hasUnsavedEvents = true;

        eventDisplayArea.append("\nSample events added:\n");
        displayFormattedEvent(createEvent);
        displayFormattedEvent(changeEvent);
        displayFormattedEvent(deleteEvent);
        displayFormattedEvent(renameEvent);

        updateEventCount();
    }

    /**
     * Displays a file event in the event display area.
     *
     * @param event the file event to display
     */
    private void displayFormattedEvent(FileEvent event) {
        eventDisplayArea.append("------------------------------\n");
        eventDisplayArea.append("File Name: " + event.getFileName() + "\n");
        eventDisplayArea.append("Extension: " + event.getFileExtension() + "\n");
        eventDisplayArea.append("Path: " + event.getAbsolutePath() + "\n");
        eventDisplayArea.append("Activity: " + event.getEventType() + "\n");
        eventDisplayArea.append("Date/Time: " + event.getEventDateTime() + "\n");
    }

    /**
     * Updates the event count label.
     */
    private void updateEventCount() {
        eventCountLabel.setText("Event Count: " + displayedEvents.size());
    }

    /**
     * Clears displayed events.
     */
    private void clearEvents() {
        displayedEvents.clear();
        hasUnsavedEvents = false;
        eventDisplayArea.setText("Events cleared.\n");
        eventCountLabel.setText("Event Count: 0");
    }

    /**
     * Saves displayed events to the SQLite database.
     */
    private void saveEvents() {
        if (displayedEvents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no events to save.");
            return;
        }

        try {
            DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");

            for (FileEvent event : displayedEvents) {
                databaseManager.saveEvent(event);
            }

            hasUnsavedEvents = false;

            eventDisplayArea.append("\nSaved " + displayedEvents.size() + " event(s) to SQLite database.\n");
            JOptionPane.showMessageDialog(this, "Events saved successfully.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database save error: " + e.getMessage());
        }
    }

    /**
     * Opens the Query Window.
     */
    private void openQueryWindow() {
        QueryWindow queryWindow = new QueryWindow();
        queryWindow.setVisible(true);
    }

    /**
     * Shows the About window.
     */
    private void showAbout() {
        JOptionPane.showMessageDialog(
                this,
                "File Watcher System\n" +
                        "Version 6.0 - Final Iteration\n\n" +
                        "Developers:\n" +
                        "Rohullah Babakarkhail\n" +
                        "Kalsoom Babakarkhail\n\n" +
                        "Features:\n" +
                        "- Java WatchService monitoring\n" +
                        "- SQLite database saving\n" +
                        "- Query by extension, activity, path, and date range\n" +
                        "- CSV export\n" +
                        "- Email generated CSV files",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Handles application exit and asks the user to save unsaved events.
     */
    private void handleExit() {
        if (!hasUnsavedEvents) {
            System.exit(0);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "You have unsaved events. Do you want to save before exiting?",
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
