import java.util.List;

/**
 * Separate test class for testing path/directory database queries.
 * This test saves events from different folder paths and checks that
 * only records from the selected path are returned.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class PathQueryTest {

    /**
     * Runs the path query test.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running PathQuery test...");

        DatabaseManager databaseManager = new DatabaseManager("path_query_test.db");

        FileEvent folderOneEvent1 = new FileEvent(
                "path-test-one.txt",
                "C:\\PathTestFolder\\path-test-one.txt",
                "CREATE",
                "2026-06-08T12:00:00"
        );

        FileEvent folderOneEvent2 = new FileEvent(
                "path-test-two.txt",
                "C:\\PathTestFolder\\path-test-two.txt",
                "CHANGE",
                "2026-06-08T12:05:00"
        );

        FileEvent otherFolderEvent = new FileEvent(
                "other-folder-file.txt",
                "C:\\OtherFolder\\other-folder-file.txt",
                "DELETE",
                "2026-06-08T12:10:00"
        );

        try {
            databaseManager.clearDatabase();

            databaseManager.saveEvent(folderOneEvent1);
            databaseManager.saveEvent(folderOneEvent2);
            databaseManager.saveEvent(otherFolderEvent);

            List<FileEvent> pathResults = databaseManager.getEventsByPath("PathTestFolder");

            System.out.println("Path query results: " + pathResults.size());

            for (FileEvent event : pathResults) {
                System.out.println("------------------------------");
                System.out.println(event);
            }

            if (pathResults.size() == 2) {
                System.out.println("PathQuery test passed.");
            } else {
                System.out.println("PathQuery test failed.");
            }

        } catch (Exception e) {
            System.out.println("PathQuery test error: " + e.getMessage());
        }

        System.out.println("PathQuery test completed.");
    }
}