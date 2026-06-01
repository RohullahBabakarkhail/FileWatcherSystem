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

public class FileMonitor {
    private String directoryPath;
    private String extensionFilter;
    private boolean isMonitoring;
    private WatchService watchService;
    private Thread monitorThread;
    private FileEventListener listener;

    public FileMonitor(String directoryPath, String extensionFilter, FileEventListener listener) {
        this.directoryPath = directoryPath;
        this.extensionFilter = extensionFilter;
        this.listener = listener;
        this.isMonitoring = false;
    }

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

            sendMessage("Real WatchService monitoring started.");
            sendMessage("Directory: " + directoryPath);
            sendMessage("Extension filter: " + extensionFilter);

        } catch (Exception e) {
            isMonitoring = false;
            sendMessage("Error starting monitoring: " + e.getMessage());
        }
    }

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

                    String eventType = convertEventType(kind);
                    String absolutePath = directoryPath + File.separator + fileName;

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

    private String convertEventType(WatchEvent.Kind<?> kind) {
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            return "CREATE";
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            return "MODIFY";
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            return "DELETE";
        } else {
            return "UNKNOWN";
        }
    }

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

    public FileEvent createEvent(String fileName, String path, String eventType) {
        String dateTime = LocalDateTime.now().toString();
        return new FileEvent(fileName, path, eventType, dateTime);
    }

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

    private void sendFileEvent(FileEvent event) {
        if (listener != null) {
            SwingUtilities.invokeLater(() -> listener.onFileEvent(event));
        }
    }

    private void sendMessage(String message) {
        if (listener != null) {
            SwingUtilities.invokeLater(() -> listener.onMonitorMessage(message));
        }
    }

    public boolean isMonitoring() {
        return isMonitoring;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public String getExtensionFilter() {
        return extensionFilter;
    }
}
