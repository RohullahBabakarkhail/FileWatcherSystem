/**
 * Separate test class for testing the FileEvent model class.
 * This test checks that file event data is stored and returned correctly.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class FileEventTest {

    /**
     * Runs the FileEvent test.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running FileEvent test...");

        FileEvent event = new FileEvent(
                "test-file.txt",
                "C:\\TestFolder\\test-file.txt",
                "CREATE",
                "2026-06-08T12:00:00"
        );

        boolean fileNameCorrect = event.getFileName().equals("test-file.txt");
        boolean extensionCorrect = event.getFileExtension().equals(".txt");
        boolean pathCorrect = event.getAbsolutePath().equals("C:\\TestFolder\\test-file.txt");
        boolean eventTypeCorrect = event.getEventType().equals("CREATE");
        boolean dateTimeCorrect = event.getEventDateTime().equals("2026-06-08T12:00:00");

        System.out.println("File name: " + event.getFileName());
        System.out.println("Extension: " + event.getFileExtension());
        System.out.println("Path: " + event.getAbsolutePath());
        System.out.println("Activity: " + event.getEventType());
        System.out.println("Date/Time: " + event.getEventDateTime());

        if (fileNameCorrect &&
                extensionCorrect &&
                pathCorrect &&
                eventTypeCorrect &&
                dateTimeCorrect) {
            System.out.println("FileEvent test passed.");
        } else {
            System.out.println("FileEvent test failed.");
        }

        System.out.println("FileEvent test completed.");
    }
}