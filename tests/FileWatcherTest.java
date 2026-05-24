public class FileWatcherTest {
    public static void main(String[] args) {
        System.out.println("Running File Watcher System Iteration 4 test...");

        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", ".txt");

        System.out.println("\nTesting FileMonitor setup:");
        System.out.println("Directory: " + monitor.getDirectoryPath());
        System.out.println("Extension: " + monitor.getExtensionFilter());
        System.out.println("Matches sample.txt: " + monitor.matchesExtension("sample.txt"));
        System.out.println("Matches sample.java: " + monitor.matchesExtension("sample.java"));

        FileEvent createEvent = monitor.createEvent(
                "sample-create.txt",
                "C:\\SampleFolder\\sample-create.txt",
                "CREATE"
        );

        FileEvent modifyEvent = monitor.createEvent(
                "sample-modify.txt",
                "C:\\SampleFolder\\sample-modify.txt",
                "MODIFY"
        );

        FileEvent deleteEvent = monitor.createEvent(
                "sample-delete.txt",
                "C:\\SampleFolder\\sample-delete.txt",
                "DELETE"
        );

        System.out.println("\nTesting FileEvent objects:");
        printEvent(createEvent);
        printEvent(modifyEvent);
        printEvent(deleteEvent);

        System.out.println("\nTesting monitor start/stop placeholder:");
        monitor.startMonitoring();
        System.out.println("Is Monitoring: " + monitor.isMonitoring());
        monitor.stopMonitoring();
        System.out.println("Is Monitoring: " + monitor.isMonitoring());

        DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");

        System.out.println("\nTesting DatabaseManager placeholder:");
        databaseManager.connect();
        databaseManager.saveEvent(createEvent);
        databaseManager.saveEvent(modifyEvent);
        databaseManager.saveEvent(deleteEvent);

        System.out.println("\nIteration 4 basic test completed successfully.");
    }

    private static void printEvent(FileEvent event) {
        System.out.println("------------------------------");
        System.out.println("File Name: " + event.getFileName());
        System.out.println("Path: " + event.getAbsolutePath());
        System.out.println("Event Type: " + event.getEventType());
        System.out.println("Date/Time: " + event.getEventDateTime());
    }
}
