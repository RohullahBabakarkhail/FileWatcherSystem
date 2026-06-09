import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Separate test class for testing the CsvExporter.
 * This test creates sample FileEvent objects and exports them to a CSV file.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class CsvExporterTest {

    /**
     * Runs the CSV export test.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running CSV Exporter test...");

        List<FileEvent> testEvents = new ArrayList<>();

        FileEvent createEvent = new FileEvent(
                "sample-create.txt",
                "C:\\SampleFolder\\sample-create.txt",
                "CREATE",
                java.time.LocalDateTime.now().toString()
        );

        FileEvent changeEvent = new FileEvent(
                "sample-change.txt",
                "C:\\SampleFolder\\sample-change.txt",
                "CHANGE",
                java.time.LocalDateTime.now().toString()
        );

        FileEvent deleteEvent = new FileEvent(
                "sample-delete.txt",
                "C:\\SampleFolder\\sample-delete.txt",
                "DELETE",
                java.time.LocalDateTime.now().toString()
        );

        FileEvent renameEvent = new FileEvent(
                "sample-renamed.txt",
                "C:\\SampleFolder\\sample-renamed.txt",
                "RENAME",
                java.time.LocalDateTime.now().toString()
        );

        testEvents.add(createEvent);
        testEvents.add(changeEvent);
        testEvents.add(deleteEvent);
        testEvents.add(renameEvent);

        try {
            File csvFile = new File("csv_export_test_results.csv");

            CsvExporter exporter = new CsvExporter();
            exporter.exportToCsv(
                    csvFile,
                    "Activity",
                    "CREATE, CHANGE, DELETE, RENAME",
                    testEvents
            );

            if (csvFile.exists() && csvFile.length() > 0) {
                System.out.println("CSV export test passed.");
                System.out.println("CSV file created at:");
                System.out.println(csvFile.getAbsolutePath());
            } else {
                System.out.println("CSV export test failed. File was not created correctly.");
            }

        } catch (Exception e) {
            System.out.println("CSV export test error: " + e.getMessage());
        }

        System.out.println("CSV Exporter test completed.");
    }
}