public class FileMonitor {
    private String directoryPath;
    private String extensionFilter;
    private boolean isMonitoring;

    public FileMonitor(String directoryPath, String extensionFilter) {
        this.directoryPath = directoryPath;
        this.extensionFilter = extensionFilter;
        this.isMonitoring = false;
    }

    public void startMonitoring() {
        isMonitoring = true;
        System.out.println("Monitoring started for: " + directoryPath);
    }

    public void stopMonitoring() {
        isMonitoring = false;
        System.out.println("Monitoring stopped.");
    }

    public FileEvent createEvent(String fileName, String path, String eventType) {
        String dateTime = java.time.LocalDateTime.now().toString();
        return new FileEvent(fileName, path, eventType, dateTime);
    }

    public boolean isMonitoring() {
        return isMonitoring;
    }
}