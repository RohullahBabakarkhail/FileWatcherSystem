public class FileWatcherApp {
    private MainWindow mainWindow;

    public FileWatcherApp() {
        mainWindow = new MainWindow();
    }

    public void start() {
        mainWindow.showWindow();
    }

    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        FileWatcherApp app = new FileWatcherApp();
        app.start();
    }
}
