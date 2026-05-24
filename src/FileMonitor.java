import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.time.LocalDateTime;

public class FileMonitor {
    private String directoryPath;
    private String extensionFilter;
    private boolean isMonitoring;
    private WatchService watchService;

    public FileMonitor(String directoryPath, String extensionFilter) {
        this.directoryPath = directoryPath;
        this.extensionFilter = extensionFilter;
        this.isMonitoring = false;
    }

    public void startMonitoring() {
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

            System.out.println("Basic WatchService setup complete.");
            System.out.println("Monitoring directory: " + directoryPath);
            System.out.println("Extension filter: " + extensionFilter);

        } catch (Exception e) {
            isMonitoring = false;
            System.out.println("Error starting WatchService: " + e.getMessage());
        }
    }

    public void stopMonitoring() {
        try {
            isMonitoring = false;

            if (watchService != null) {
                watchService.close();
            }

            System.out.println("Monitoring stopped.");

        } catch (Exception e) {
            System.out.println("Error stopping WatchService: " + e.getMessage());
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

    public FileEvent createSampleMonitoredEvent(String eventType) {
        String fileName = "iteration4-sample.txt";

        if (!matchesExtension(fileName)) {
            String cleanExtension = extensionFilter;

            if (cleanExtension == null || cleanExtension.trim().isEmpty()
                    || cleanExtension.equalsIgnoreCase("All Files")) {
                cleanExtension = ".txt";
            }

            if (!cleanExtension.startsWith(".")) {
                cleanExtension = "." + cleanExtension;
            }

            fileName = "iteration4-sample" + cleanExtension;
        }

        String path = directoryPath + File.separator + fileName;
        return createEvent(fileName, path, eventType);
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
