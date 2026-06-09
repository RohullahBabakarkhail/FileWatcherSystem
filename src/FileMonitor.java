import javax.swing.SwingUtilities;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;

/**
 * Monitors a selected directory for file system events using Java WatchService.
 * This class detects create, change, delete, and basic rename behavior.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class FileMonitor {
    private String directoryPath;
    private String extensionFilter;
    private boolean isMonitoring;
    private WatchService watchService;
    private Thread monitorThread;
    private FileEventListener listener;

    private String lastDeletedFileName;
    private long lastDeletedTime;

    /**
     * Creates a FileMonitor for a directory and extension filter.
     *
     * @param directoryPath   the directory to monitor
     * @param extensionFilter the extension to watch, or All Files
     * @param listener        listener used to send events to the GUI
     */
    public FileMonitor(String directoryPath, String extensionFilter, FileEventListener listener) {
        this.directoryPath = directoryPath;
        this.extensionFilter = extensionFilter;
        this.listener = listener;
        this.isMonitoring = false;
        this.lastDeletedFileName = null;
        this.lastDeletedTime = 0;
    }

    /**
     * Starts monitoring the selected directory.
     */
    public void startMonitoring() {
        if (isMonitoring) {
            sendMessage("Monitoring is already running.");
            return;
        }

        try {
            Path path = Paths.get(directoryPath);
            watchService = FileSystems.getDefault().newWatchService();

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE
            );

            isMonitoring = true;

            monitorThread = new Thread(this::processEvents);
            monitorThread.setDaemon(true);
            monitorThread.start();

            sendMessage("Monitoring started successfully.");
            sendMessage("Directory: " + directoryPath);
            sendMessage("Extension filter: " + extensionFilter);

        } catch (Exception e) {
            isMonitoring = false;
            sendMessage("Error starting monitoring: " + e.getMessage());
        }
    }

    /**
     * Processes file system events while monitoring is active.
     */
    private void processEvents() {
        while (isMonitoring) {
            try {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    Path fileNamePath = (Path) event.context();
                    String fileName = fileNamePath.toString();

                    if (!matchesExtension(fileName)) {
                        continue;
                    }

                    String absolutePath = directoryPath + File.separator + fileName;
                    String eventType = convertEventType(kind);

                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        lastDeletedFileName = fileName;
                        lastDeletedTime = System.currentTimeMillis();
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE && isPossibleRename()) {
                        eventType = "RENAME";
                        sendMessage("Rename detected: " + lastDeletedFileName + " changed to " + fileName);
                        lastDeletedFileName = null;
                        lastDeletedTime = 0;
                    }

                    FileEvent fileEvent = createEvent(fileName, absolutePath, eventType);
                    sendFileEvent(fileEvent);
                }

                boolean valid = key.reset();

                if (!valid) {
                    sendMessage("Watch key is no longer valid.");
                    stopMonitoring();
                    break;
                }

            } catch (Exception e) {
                if (isMonitoring) {
                    sendMessage("Monitoring error: " + e.getMessage());
                }
                break;
            }
        }
    }

    /**
     * Checks if a create event happened soon after a delete event.
     * This is used as a simple way to detect rename behavior.
     *
     * @return true if the event may be a rename
     */
    private boolean isPossibleRename() {
        long currentTime = System.currentTimeMillis();
        return lastDeletedFileName != null && currentTime - lastDeletedTime <= 3000;
    }

    /**
     * Converts WatchService event kinds into project event labels.
     *
     * @param kind the WatchService event kind
     * @return a readable event type
     */
    private String convertEventType(WatchEvent.Kind<?> kind) {
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            return "CREATE";
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            return "CHANGE";
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            return "DELETE";
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * Stops monitoring.
     */
    public void stopMonitoring() {
        try {
            isMonitoring = false;

            if (watchService != null) {
                watchService.close();
            }

            sendMessage("Monitoring stopped.");

        } catch (Exception e) {
            sendMessage("Error stopping monitoring: " + e.getMessage());
        }
    }

    /**
     * Creates a FileEvent object.
     *
     * @param fileName  the file name
     * @param path      the absolute path
     * @param eventType the activity type
     * @return a new FileEvent object
     */
    public FileEvent createEvent(String fileName, String path, String eventType) {
        String dateTime = LocalDateTime.now().toString();
        return new FileEvent(fileName, path, eventType, dateTime);
    }

    /**
     * Checks whether a file name matches the selected extension filter.
     *
     * @param fileName the file name to check
     * @return true if the file should be displayed
     */
    public boolean matchesExtension(String fileName) {
        if (extensionFilter == null || extensionFilter.equalsIgnoreCase("All Files")) {
            return true;
        }

        String cleanExtension = extensionFilter.trim();

        if (cleanExtension.isEmpty()) {
            return true;
        }

        if (!cleanExtension.startsWith(".")) {
            cleanExtension = "." + cleanExtension;
        }

        return fileName.toLowerCase().endsWith(cleanExtension.toLowerCase());
    }

    /**
     * Sends a detected FileEvent to the listener.
     *
     * @param event the file event
     */
    private void sendFileEvent(FileEvent event) {
        if (listener != null) {
            SwingUtilities.invokeLater(() -> listener.onFileEvent(event));
        }
    }

    /**
     * Sends a message to the listener.
     *
     * @param message the message to display
     */
    private void sendMessage(String message) {
        if (listener != null) {
            SwingUtilities.invokeLater(() -> listener.onMonitorMessage(message));
        }
    }

    /**
     * Checks if monitoring is active.
     *
     * @return true if monitoring is active
     */
    public boolean isMonitoring() {
        return isMonitoring;
    }

    /**
     * Gets the monitored directory path.
     *
     * @return the directory path
     */
    public String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Gets the extension filter.
     *
     * @return the extension filter
     */
    public String getExtensionFilter() {
        return extensionFilter;
    }
}
