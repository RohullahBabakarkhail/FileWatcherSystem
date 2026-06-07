public class FileWatcherTest {
    public static void main(String[] args) {
        System.out.println("Running File Watcher System final test...");

        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", ".txt", null);

        System.out.println("\nTesting extension filtering:");
        System.out.println("Matches sample.txt: " + monitor.matchesExtension("sample.txt"));
        System.out.println("Matches sample.java: " + monitor.matchesExtension("sample.java"));

        FileEvent createEvent = monitor.createEvent(
                "sample-create.txt",
                "C:\\SampleFolder\\sample-create.txt",
                "CREATE"
        );

        FileEvent changeEvent = monitor.createEvent(
                "sample-change.txt",
                "C:\\SampleFolder\\sample-change.txt",
                "CHANGE"
        );

        FileEvent deleteEvent = monitor.createEvent(
                "sample-delete.txt",
                "C:\\SampleFolder\\sample-delete.txt",
                "DELETE"
        );

        FileEvent renameEvent = monitor.createEvent(
                "sample-renamed.txt",
                "C:\\SampleFolder\\sample-renamed.txt",
                "RENAME"
        );

        System.out.println("\nTesting FileEvent objects:");
        printEvent(createEvent);
        printEvent(changeEvent);
        printEvent(deleteEvent);
        printEvent(renameEvent);

        System.out.println("\nTesting DatabaseManager methods:");
        DatabaseManager databaseManager = new DatabaseManager("filewatcher.db");

        try {
            databaseManager.saveEvent(createEvent);
            databaseManager.saveEvent(changeEvent);
            databaseManager.saveEvent(deleteEvent);
            databaseManager.saveEvent(renameEvent);

            System.out.println("Database save methods ran successfully.");

            System.out.println("\nTesting query by event type CREATE:");
            for (FileEvent event : databaseManager.getEventsByEventType("CREATE")) {
                printEvent(event);
            }

            System.out.println("\nTesting query by event type RENAME:");
            for (FileEvent event : databaseManager.getEventsByEventType("RENAME")) {
                printEvent(event);
            }

        } catch (Exception e) {
            System.out.println("Database test error: " + e.getMessage());
        }

        System.out.println("\nFinal test completed.");
    }

    private static void printEvent(FileEvent event) {
        System.out.println("------------------------------");
        System.out.println("File Name: " + event.getFileName());
        System.out.println("Path: " + event.getAbsolutePath());
        System.out.println("Event Type: " + event.getEventType());
        System.out.println("Date/Time: " + event.getEventDateTime());
    }
}
