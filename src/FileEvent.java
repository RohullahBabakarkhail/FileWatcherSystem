/**
 * Represents a file system event detected by the File Watcher System.
 * Each FileEvent stores the file name, file extension, absolute path,
 * activity/event type, and date/time.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class FileEvent {
    private String fileName;
    private String fileExtension;
    private String absolutePath;
    private String eventType;
    private String eventDateTime;

    /**
     * Creates a new FileEvent.
     *
     * @param fileName      the name of the file
     * @param absolutePath  the absolute path of the file
     * @param eventType     the activity type, such as CREATE, CHANGE, DELETE, or RENAME
     * @param eventDateTime the date and time the event occurred
     */
    public FileEvent(String fileName, String absolutePath, String eventType, String eventDateTime) {
        this.fileName = fileName;
        this.fileExtension = extractExtension(fileName);
        this.absolutePath = absolutePath;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
    }

    /**
     * Extracts the file extension from a file name.
     *
     * @param fileName the file name
     * @return the file extension, or an empty string if no extension exists
     */
    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        int index = fileName.lastIndexOf(".");
        return fileName.substring(index);
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the file extension.
     *
     * @return the file extension
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Gets the absolute file path.
     *
     * @return the absolute path
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * Gets the event type.
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Gets the event date and time.
     *
     * @return the event date and time
     */
    public String getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Returns a readable string version of the file event.
     *
     * @return formatted file event details
     */
    @Override
    public String toString() {
        return "File Name: " + fileName +
                "\nExtension: " + fileExtension +
                "\nPath: " + absolutePath +
                "\nActivity: " + eventType +
                "\nDate/Time: " + eventDateTime;
    }
}
