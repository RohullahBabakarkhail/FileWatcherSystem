/**
 * Main application starter for the File Watcher System.
 * This class creates and starts the main GUI window.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class FileWatcherApp {
    private MainWindow mainWindow;

    /**
     * Constructs the application and creates the main window.
     */
    public FileWatcherApp() {
        mainWindow = new MainWindow();
    }

    /**
     * Starts the application by showing the main window.
     */
    public void start() {
        mainWindow.showWindow();
    }

    /**
     * Stops the application.
     */
    public void stop() {
        System.exit(0);
    }

    /**
     * Main method used to run the File Watcher System.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        FileWatcherApp app = new FileWatcherApp();
        app.start();
    }
}
