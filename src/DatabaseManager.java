import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles SQLite database operations for the File Watcher System.
 * This class creates the table, saves file events, runs queries,
 * and clears the database.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class DatabaseManager {
    private String databasePath;

    /**
     * Creates a DatabaseManager with the selected database path.
     *
     * @param databasePath the SQLite database file path
     */
    public DatabaseManager(String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * Opens a database connection.
     *
     * @return a database connection
     * @throws Exception if the connection fails
     */
    public Connection connect() throws Exception {
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }

    /**
     * Creates the FileEvents table if it does not exist.
     *
     * @throws Exception if table creation fails
     */
    public void createTableIfNeeded() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS FileEvents (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fileName TEXT NOT NULL, " +
                "fileExtension TEXT NOT NULL, " +
                "absolutePath TEXT NOT NULL, " +
                "eventType TEXT NOT NULL, " +
                "eventDateTime TEXT NOT NULL" +
                ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    /**
     * Saves one file event to the database.
     *
     * @param event the file event to save
     * @throws Exception if saving fails
     */
    public void saveEvent(FileEvent event) throws Exception {
        createTableIfNeeded();

        String sql = "INSERT INTO FileEvents " +
                "(fileName, fileExtension, absolutePath, eventType, eventDateTime) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getFileName());
            statement.setString(2, event.getFileExtension());
            statement.setString(3, event.getAbsolutePath());
            statement.setString(4, event.getEventType());
            statement.setString(5, event.getEventDateTime());
            statement.executeUpdate();
        }
    }

    /**
     * Gets events by file extension.
     *
     * @param extension the file extension to search for
     * @return matching events
     * @throws Exception if the query fails
     */
    public List<FileEvent> getEventsByExtension(String extension) throws Exception {
        createTableIfNeeded();

        String cleanExtension = extension.trim();

        if (!cleanExtension.startsWith(".")) {
            cleanExtension = "." + cleanExtension;
        }

        String sql = "SELECT fileName, absolutePath, eventType, eventDateTime " +
                "FROM FileEvents WHERE fileExtension = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cleanExtension);
            return getEventsFromStatement(statement);
        }
    }

    /**
     * Gets events by file name.
     *
     * @param fileName the file name search value
     * @return matching events
     * @throws Exception if the query fails
     */
    public List<FileEvent> getEventsByFileName(String fileName) throws Exception {
        createTableIfNeeded();

        String sql = "SELECT fileName, absolutePath, eventType, eventDateTime " +
                "FROM FileEvents WHERE fileName LIKE ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + fileName + "%");
            return getEventsFromStatement(statement);
        }
    }

    /**
     * Gets events by event/activity type.
     *
     * @param eventType the event type to search for
     * @return matching events
     * @throws Exception if the query fails
     */
    public List<FileEvent> getEventsByEventType(String eventType) throws Exception {
        createTableIfNeeded();

        String sql = "SELECT fileName, absolutePath, eventType, eventDateTime " +
                "FROM FileEvents WHERE eventType = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, eventType.toUpperCase());
            return getEventsFromStatement(statement);
        }
    }

    /**
     * Gets events by path/directory.
     *
     * @param path the directory or path search value
     * @return matching events
     * @throws Exception if the query fails
     */
    public List<FileEvent> getEventsByPath(String path) throws Exception {
        createTableIfNeeded();

        String sql = "SELECT fileName, absolutePath, eventType, eventDateTime " +
                "FROM FileEvents WHERE absolutePath LIKE ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + path + "%");
            return getEventsFromStatement(statement);
        }
    }

    /**
     * Gets events by a date range.
     * Dates should be entered in ISO format, such as 2026-06-01.
     *
     * @param startDate the early date
     * @param endDate   the later date
     * @return matching events
     * @throws Exception if the query fails
     */
    public List<FileEvent> getEventsByDateRange(String startDate, String endDate) throws Exception {
        createTableIfNeeded();

        String sql = "SELECT fileName, absolutePath, eventType, eventDateTime " +
                "FROM FileEvents WHERE substr(eventDateTime, 1, 10) BETWEEN ? AND ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, startDate);
            statement.setString(2, endDate);
            return getEventsFromStatement(statement);
        }
    }

    /**
     * Converts a prepared statement result into a list of FileEvent objects.
     *
     * @param statement the prepared statement to execute
     * @return list of file events
     * @throws Exception if the query fails
     */
    private List<FileEvent> getEventsFromStatement(PreparedStatement statement) throws Exception {
        List<FileEvent> events = new ArrayList<>();

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                FileEvent event = new FileEvent(
                        resultSet.getString("fileName"),
                        resultSet.getString("absolutePath"),
                        resultSet.getString("eventType"),
                        resultSet.getString("eventDateTime")
                );

                events.add(event);
            }
        }

        return events;
    }

    /**
     * Clears all records from the database.
     *
     * @throws Exception if clearing fails
     */
    public void clearDatabase() throws Exception {
        createTableIfNeeded();

        String sql = "DELETE FROM FileEvents";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
}
