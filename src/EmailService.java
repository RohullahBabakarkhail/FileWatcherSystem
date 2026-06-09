import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

/**
 * Sends generated CSV files through Gmail SMTP using Jakarta Mail.
 * The sender must use a Gmail app password, not the normal Gmail password.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class EmailService {

    /**
     * Sends an email with a file attachment.
     *
     * @param senderEmail    the Gmail sender address
     * @param appPassword    the Gmail app password
     * @param recipientEmail the recipient email address
     * @param attachment     the CSV file to attach
     * @throws Exception if sending fails
     */
    public void sendEmailWithAttachment(
            String senderEmail,
            String appPassword,
            String recipientEmail,
            File attachment
    ) throws Exception {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("File Watcher Query Results");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Attached is the generated File Watcher query results CSV file.");

        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource source = new FileDataSource(attachment);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(attachment.getName());

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}