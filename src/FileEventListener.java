/**
 * Listener interface used by FileMonitor to send detected events
 * and monitor messages back to the GUI.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public interface FileEventListener {
    /**
     * Called when a file event is detected.
     *
     * @param event the detected file event
     */
    void onFileEvent(FileEvent event);

    /**
     * Called when the monitor needs to display a message.
     *
     * @param message the message to display
     */
    void onMonitorMessage(String message);
}
