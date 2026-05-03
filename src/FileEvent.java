public class FileEvent {
    private String fileName;
    private String absolutePath;
    private String eventType;
    private String eventDateTime;

    public FileEvent(String fileName, String absolutePath, String eventType, String eventDateTime) {
        this.fileName = fileName;
        this.absolutePath = absolutePath;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    @Override
    public String toString() {
        return eventDateTime + " | " + eventType + " | " + fileName + " | " + absolutePath;
    }
}