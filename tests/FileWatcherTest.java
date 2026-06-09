/**
 * Simple test class for the File Watcher System model classes.
 * This test checks FileEvent, FileMonitor filtering, database saving,
 * database queries, and CSV exporting.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class FileWatcherTest {
    /**
     * Runs basic model tests.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running File Watcher System final model tests...");

        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", ".txt", null);

        System.out.println("\nTesting extension filtering:");
        System.out.println("Matches sample.txt: " + monitor.matchesExtension("sample.txt"));
        System.out.println("Matches sample.java: " + monitor.matchesExtension("sample.java"));

        FileEvent createEvent = monitor.createEvent("sample-create.txt", "C:\\SampleFolder\\sample-create.txt", "CREATE");
        FileEvent changeEvent = monitor.createEvent("sample-change.txt", "C:\\SampleFolder\\sample-change.txt", "CHANGE");
        FileEvent deleteEvent = monitor.createEvent("sample-delete.txt", "C:\\SampleFolder\\sample-delete.txt", "DELETE");
        FileEvent renameEvent = monitor.createEvent("sample-renamed.txt", "C:\\SampleFolder\\sample-renamed.txt", "RENAME");

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
            System.out.println("CREATE results: " + databaseManager.getEventsByEventType("CREATE").size());
            System.out.println(".txt results: " + databaseManager.getEventsByExtension(".txt").size());
            System.out.println("Path results: " + databaseManager.getEventsByPath("SampleFolder").size());

        } catch (Exception e) {
            System.out.println("Database test error: " + e.getMessage());
        }

        System.out.println("\nFinal model tests completed.");
    }

    /**
     * Prints a FileEvent to the console.
     *
     * @param event the event to print
     */
    private static void printEvent(FileEvent event) {
        System.out.println("------------------------------");
        System.out.println(event);
    }
}
