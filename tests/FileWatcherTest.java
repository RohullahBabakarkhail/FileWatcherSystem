public class FileWatcherTest {
    public static void main(String[] args) {
        System.out.println("Running File Watcher System Iteration 5 test...");

        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", ".txt", null);

        System.out.println("\nTesting extension filtering:");
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

        System.out.println("\nTesting DatabaseManager methods:");
        DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");

        try {
            databaseManager.saveEvent(createEvent);
            databaseManager.saveEvent(modifyEvent);
            databaseManager.saveEvent(deleteEvent);
            System.out.println("Database save methods ran successfully.");
        } catch (Exception e) {
            System.out.println("Database test error: " + e.getMessage());
        }

        System.out.println("\nIteration 5 test completed.");
    }

    private static void printEvent(FileEvent event) {
        System.out.println("------------------------------");
        System.out.println("File Name: " + event.getFileName());
        System.out.println("Path: " + event.getAbsolutePath());
        System.out.println("Event Type: " + event.getEventType());
        System.out.println("Date/Time: " + event.getEventDateTime());
    }
}
