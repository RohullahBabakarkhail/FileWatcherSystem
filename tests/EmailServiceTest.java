import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Separate test class for testing the EmailService.
 * This test creates a small CSV file and sends it as an email attachment.
 *
 * IMPORTANT:
 * This test requires a Gmail App Password, not the normal Gmail password.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class EmailServiceTest {

    /**
     * Runs the EmailService test.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Running EmailService test...");

        Scanner scanner = new Scanner(System.in);

        try {
            File testCsv = createTestCsvFile();

            System.out.print("Enter sender Gmail address: ");
            String senderEmail = scanner.nextLine();

            System.out.print("Enter Gmail App Password: ");
            String appPassword = scanner.nextLine();

            System.out.print("Enter recipient email address: ");
            String recipientEmail = scanner.nextLine();

            EmailService emailService = new EmailService();

            emailService.sendEmailWithAttachment(
                    senderEmail,
                    appPassword,
                    recipientEmail,
                    testCsv
            );

            System.out.println("EmailService test passed.");
            System.out.println("Email sent successfully with CSV attachment.");

        } catch (Exception e) {
            System.out.println("EmailService test failed.");
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
        System.out.println("EmailService test completed.");
    }

    /**
     * Creates a sample CSV file for email testing.
     *
     * @return the created CSV file
     * @throws Exception if the file cannot be created
     */
    private static File createTestCsvFile() throws Exception {
        File file = new File("email_test_results.csv");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("File Watcher System Email Test\n");
            writer.write("Query Type,Activity\n");
            writer.write("Query Value,CREATE\n");
            writer.write("\n");
            writer.write("File Name,Extension,Path,Activity,Date/Time\n");
            writer.write("sample-create.txt,.txt,C:\\SampleFolder\\sample-create.txt,CREATE,2026-06-08T12:00:00\n");
        }

        System.out.println("Created test CSV file:");
        System.out.println(file.getAbsolutePath());

        return file;
    }
}