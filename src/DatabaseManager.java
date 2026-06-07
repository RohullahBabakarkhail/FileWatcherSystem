import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private String databasePath;

    public DatabaseManager(String databasePath) {
        this.databasePath = databasePath;
    }

    public Connection connect() throws Exception {
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }

    public void createTableIfNeeded() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS FileEvents (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fileName TEXT NOT NULL, " +
                "absolutePath TEXT NOT NULL, " +
                "eventType TEXT NOT NULL, " +
                "eventDateTime TEXT NOT NULL" +
                ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void saveEvent(FileEvent event) throws Exception {
        createTableIfNeeded();

        String sql = "INSERT INTO FileEvents " +
                "(fileName, absolutePath, eventType, eventDateTime) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getFileName());
            statement.setString(2, event.getAbsolutePath());
            statement.setString(3, event.getEventType());
            statement.setString(4, event.getEventDateTime());
            statement.executeUpdate();
        }
    }

    public List<FileEvent> getEventsByExtension(String extension) throws Exception {
        createTableIfNeeded();

        String cleanExtension = extension.trim();

        if (!cleanExtension.startsWith(".")) {
            cleanExtension = "." + cleanExtension;
        }

        String sql = "SELECT fileName, absolutePath, eventType, eventDateTime " +
                "FROM FileEvents WHERE fileName LIKE ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + cleanExtension);
            return getEventsFromStatement(statement);
        }
    }

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

    public List<FileEvent> getEventsByDate(String date) throws Exception {
        createTableIfNeeded();

        String sql = "SELECT fileName, absolutePath, eventType, eventDateTime " +
                "FROM FileEvents WHERE eventDateTime LIKE ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + date + "%");
            return getEventsFromStatement(statement);
        }
    }

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

    public void clearDatabase() throws Exception {
        createTableIfNeeded();

        String sql = "DELETE FROM FileEvents";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
}
