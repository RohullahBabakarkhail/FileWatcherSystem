import java.util.List;

/**
 * Separate test class for testing the clear database feature.
 * This test saves sample events, clears the database, and confirms
 * that no records remain afterward.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class ClearDatabaseTest {

    /**
     * Runs the clear database test.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running ClearDatabase test...");

        DatabaseManager databaseManager = new DatabaseManager("clear_database_test.db");

        FileEvent createEvent = new FileEvent(
                "clear-test-create.txt",
                "C:\\ClearTestFolder\\clear-test-create.txt",
                "CREATE",
                "2026-06-08T12:00:00"
        );

        FileEvent changeEvent = new FileEvent(
                "clear-test-change.txt",
                "C:\\ClearTestFolder\\clear-test-change.txt",
                "CHANGE",
                "2026-06-08T12:05:00"
        );

        try {
            databaseManager.clearDatabase();

            databaseManager.saveEvent(createEvent);
            databaseManager.saveEvent(changeEvent);

            List<FileEvent> beforeClear = databaseManager.getEventsByPath("ClearTestFolder");
            System.out.println("Records before clear: " + beforeClear.size());

            databaseManager.clearDatabase();

            List<FileEvent> afterClear = databaseManager.getEventsByPath("ClearTestFolder");
            System.out.println("Records after clear: " + afterClear.size());

            if (beforeClear.size() == 2 && afterClear.size() == 0) {
                System.out.println("ClearDatabase test passed.");
            } else {
                System.out.println("ClearDatabase test failed.");
            }

        } catch (Exception e) {
            System.out.println("ClearDatabase test error: " + e.getMessage());
        }

        System.out.println("ClearDatabase test completed.");
    }
}