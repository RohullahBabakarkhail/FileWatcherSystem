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
        sampleEventButton = new JButton("Sample Event");

        startButton.setToolTipText("Start monitoring placeholder");
        stopButton.setToolTipText("Stop monitoring placeholder");
        saveButton.setToolTipText("Write current events to database placeholder");
        sampleEventButton.setToolTipText("Display a sample file event");

        startButton.addActionListener(e -> startMonitoring());
        stopButton.addActionListener(e -> stopMonitoring());
        saveButton.addActionListener(e -> saveEvents());
        sampleEventButton.addActionListener(e -> addSampleEvent());

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
        eventDisplayArea.setText("File Watcher System ready.\nSelect a directory and extension, then click Start.\n");

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(eventDisplayArea), BorderLayout.CENTER);

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
            eventDisplayArea.append("Selected directory: " + selectedFolder.getAbsolutePath() + "\n");
        } else {
            eventDisplayArea.append("Directory selection canceled.\n");
        }
    }

    private void startMonitoring() {
        String directory = directoryField.getText();
        String extension = extensionComboBox.getSelectedItem().toString();

        if (directory.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please choose or enter a directory first.",
                    "Missing Directory",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        fileMonitor = new FileMonitor(directory, extension);
        fileMonitor.startMonitoring();

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        eventDisplayArea.append("Monitoring started...\n");
        eventDisplayArea.append("Directory: " + directory + "\n");
        eventDisplayArea.append("Extension: " + extension + "\n");
        eventDisplayArea.append("Note: Real file monitoring will be completed in a later iteration.\n");
    }

    private void stopMonitoring() {
        if (fileMonitor != null) {
            fileMonitor.stopMonitoring();
        }

        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        eventDisplayArea.append("Monitoring stopped...\n");
    }

    private void addSampleEvent() {
        FileEvent event = new FileEvent(
                "sample.txt",
                "C:\\SampleFolder\\sample.txt",
                "CREATE",
                LocalDateTime.now().toString()
        );

        displayedEvents.add(event);
        eventDisplayArea.append("Sample Event: " + event + "\n");
    }

    private void saveEvents() {
        DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");
        databaseManager.connect();

        for (FileEvent event : displayedEvents) {
            databaseManager.saveEvent(event);
        }

        eventDisplayArea.append("Write DB clicked. Database saving is currently a placeholder.\n");
        eventDisplayArea.append("Number of sample events prepared for saving: " + displayedEvents.size() + "\n");
    }

    private void openQueryWindow() {
        QueryWindow queryWindow = new QueryWindow();
        queryWindow.setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
                this,
                "File Watcher System\n" +
                        "Version 2.0 - Iteration 2\n\n" +
                        "Developers:\n" +
                        "Rohullah Babakarkhail\n" +
                        "Kalsoom Babakarkhail\n\n" +
                        "Usage:\n" +
                        "1. Choose a directory.\n" +
                        "2. Select or type a file extension.\n" +
                        "3. Click Start to begin monitoring placeholder behavior.\n" +
                        "4. Click Stop to stop monitoring.\n" +
                        "5. Use Sample Event to test event display.\n\n" +
                        "Note: Full file monitoring and SQLite database features will be completed in later iterations.",
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
