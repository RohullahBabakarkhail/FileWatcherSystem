import java.util.List;

/**
 * Separate test class for testing date range database queries.
 * This test saves events with different dates and checks that only
 * records inside the selected date range are returned.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class DateRangeQueryTest {

    /**
     * Runs the date range query test.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running DateRangeQuery test...");

        DatabaseManager databaseManager = new DatabaseManager("date_range_test.db");

        FileEvent oldEvent = new FileEvent(
                "old-file.txt",
                "C:\\DateTestFolder\\old-file.txt",
                "CREATE",
                "2026-06-01T10:00:00"
        );

        FileEvent middleEvent = new FileEvent(
                "middle-file.txt",
                "C:\\DateTestFolder\\middle-file.txt",
                "CHANGE",
                "2026-06-08T12:00:00"
        );

        FileEvent newerEvent = new FileEvent(
                "newer-file.txt",
                "C:\\DateTestFolder\\newer-file.txt",
                "DELETE",
                "2026-06-10T02:00:00"
        );

        try {
            databaseManager.clearDatabase();

            databaseManager.saveEvent(oldEvent);
            databaseManager.saveEvent(middleEvent);
            databaseManager.saveEvent(newerEvent);

            List<FileEvent> results = databaseManager.getEventsByDateRange(
                    "2026-06-08",
                    "2026-06-10"
            );

            System.out.println("Date range query results: " + results.size());

            for (FileEvent event : results) {
                System.out.println("------------------------------");
                System.out.println(event);
            }

            if (results.size() == 2) {
                System.out.println("DateRangeQuery test passed.");
            } else {
                System.out.println("DateRangeQuery test failed.");
            }

        } catch (Exception e) {
            System.out.println("DateRangeQuery test error: " + e.getMessage());
        }

        System.out.println("DateRangeQuery test completed.");
    }
}