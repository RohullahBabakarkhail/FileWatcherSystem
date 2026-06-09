/**
 * Separate test class for testing the FileMonitor extension filtering logic.
 * This test checks .txt, .java, custom extensions, and All Files.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class FileMonitorTest {

    /**
     * Runs the FileMonitor test.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running FileMonitor test...");

        testTxtFilter();
        testJavaFilter();
        testCustomFilter();
        testAllFilesFilter();

        System.out.println("FileMonitor test completed.");
    }

    /**
     * Tests .txt extension filtering.
     */
    private static void testTxtFilter() {
        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", ".txt", null);

        boolean txtMatch = monitor.matchesExtension("notes.txt");
        boolean javaMatch = monitor.matchesExtension("Program.java");

        System.out.println("\nTesting .txt filter:");
        System.out.println("notes.txt should match: " + txtMatch);
        System.out.println("Program.java should not match: " + javaMatch);

        if (txtMatch && !javaMatch) {
            System.out.println(".txt filter test passed.");
        } else {
            System.out.println(".txt filter test failed.");
        }
    }

    /**
     * Tests .java extension filtering.
     */
    private static void testJavaFilter() {
        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", ".java", null);

        boolean javaMatch = monitor.matchesExtension("MainWindow.java");
        boolean txtMatch = monitor.matchesExtension("notes.txt");

        System.out.println("\nTesting .java filter:");
        System.out.println("MainWindow.java should match: " + javaMatch);
        System.out.println("notes.txt should not match: " + txtMatch);

        if (javaMatch && !txtMatch) {
            System.out.println(".java filter test passed.");
        } else {
            System.out.println(".java filter test failed.");
        }
    }

    /**
     * Tests a custom typed extension without a dot.
     */
    private static void testCustomFilter() {
        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", "csv", null);

        boolean csvMatch = monitor.matchesExtension("records.csv");
        boolean pdfMatch = monitor.matchesExtension("report.pdf");

        System.out.println("\nTesting custom csv filter:");
        System.out.println("records.csv should match: " + csvMatch);
        System.out.println("report.pdf should not match: " + pdfMatch);

        if (csvMatch && !pdfMatch) {
            System.out.println("Custom extension filter test passed.");
        } else {
            System.out.println("Custom extension filter test failed.");
        }
    }

    /**
     * Tests All Files option.
     */
    private static void testAllFilesFilter() {
        FileMonitor monitor = new FileMonitor("C:\\SampleFolder", "All Files", null);

        boolean txtMatch = monitor.matchesExtension("notes.txt");
        boolean javaMatch = monitor.matchesExtension("Program.java");
        boolean pdfMatch = monitor.matchesExtension("report.pdf");

        System.out.println("\nTesting All Files filter:");
        System.out.println("notes.txt should match: " + txtMatch);
        System.out.println("Program.java should match: " + javaMatch);
        System.out.println("report.pdf should match: " + pdfMatch);

        if (txtMatch && javaMatch && pdfMatch) {
            System.out.println("All Files filter test passed.");
        } else {
            System.out.println("All Files filter test failed.");
        }
    }
}