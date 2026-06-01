public interface FileEventListener {
    void onFileEvent(FileEvent event);

    void onMonitorMessage(String message);
}