public class FileWatcherTest {
    public static void main(String[] args) {
        System.out.println("Running File Watcher System Iteration 3 test...");

        FileEvent createEvent = new FileEvent(
                "sample-create.txt",
                "C:\\SampleFolder\\sample-create.txt",
                "CREATE",
                "2026-05-10 7:30 PM"
        );

        FileEvent modifyEvent = new FileEvent(
                "sample-modify.txt",
                "C:\\SampleFolder\\sample-modify.txt",
                "MODIFY",
                "2026-05-10 7:31 PM"
        );

        FileEvent deleteEvent = new FileEvent(
                "sample-delete.txt",
                "C:\\SampleFolder\\sample-delete.txt",
                "DELETE",
                "2026-05-10 7:32 PM"
        );

        int eventCount = 0;

        System.out.println("\nTesting FileEvent objects:");
        printEvent(createEvent);
        eventCount++;

        printEvent(modifyEvent);
        eventCount++;

        printEvent(deleteEvent);
        eventCount++;

        System.out.println("\nTotal sample events created: " + eventCount);

        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", ".txt");

        System.out.println("\nTesting FileMonitor placeholder:");
        monitor.startMonitoring();
        System.out.println("Directory: " + monitor.getDirectoryPath());
        System.out.println("Extension: " + monitor.getExtensionFilter());
        System.out.println("Is Monitoring: " + monitor.isMonitoring());

        FileEvent monitorCreatedEvent = monitor.createEvent(
                "monitor-test.txt",
                "C:\\SampleFolder\\monitor-test.txt",
                "CREATE"
        );

        System.out.println("\nCreated by FileMonitor:");
        printEvent(monitorCreatedEvent);
        eventCount++;

        monitor.stopMonitoring();
        System.out.println("Is Monitoring: " + monitor.isMonitoring());

        DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");

        System.out.println("\nTesting DatabaseManager placeholder:");
        databaseManager.connect();
        databaseManager.saveEvent(createEvent);
        databaseManager.saveEvent(modifyEvent);
        databaseManager.saveEvent(deleteEvent);

        System.out.println("\nEvents that would be saved: " + eventCount);
        System.out.println("\nIteration 3 basic test completed successfully.");
    }

    private static void printEvent(FileEvent event) {
        System.out.println("------------------------------");
        System.out.println("File Name: " + event.getFileName());
        System.out.println("Path: " + event.getAbsolutePath());
        System.out.println("Event Type: " + event.getEventType());
        System.out.println("Date/Time: " + event.getEventDateTime());
    }
}
