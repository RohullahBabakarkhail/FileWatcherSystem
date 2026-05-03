public class FileWatcherTest {

    public static void main(String[] args) {
        System.out.println("Running File Watcher System test...");

        FileEvent event = new FileEvent(
                "sample.txt",
                "C:\\TestFolder\\sample.txt",
                "CREATE",
                "2026-05-03 3:45 PM"
        );

        System.out.println("File Name: " + event.getFileName());
        System.out.println("Absolute Path: " + event.getAbsolutePath());
        System.out.println("Event Type: " + event.getEventType());
        System.out.println("Event Date/Time: " + event.getEventDateTime());

        FileMonitor monitor = new FileMonitor("C:\\TestFolder", "txt");

        monitor.startMonitoring();
        System.out.println("Monitoring status after start: " + monitor.isMonitoring());

        FileEvent createdEvent = monitor.createEvent(
                "test.txt",
                "C:\\TestFolder\\test.txt",
                "CREATE"
        );

        System.out.println("Created Event: " + createdEvent);

        monitor.stopMonitoring();
        System.out.println("Monitoring status after stop: " + monitor.isMonitoring());

        DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");
        databaseManager.connect();
        databaseManager.saveEvent(event);

        System.out.println("Test completed successfully.");
    }
}
