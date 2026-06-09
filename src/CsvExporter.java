import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Exports query results to a CSV file.
 * The CSV file includes query information at the top and labeled columns.
 *
 * @author Rohullah Babakarkhail
 * @author Kalsoom Babakarkhail
 */
public class CsvExporter {

    /**
     * Writes query results to a CSV file.
     *
     * @param file       the CSV file to create
     * @param queryType  the type of query that was run
     * @param queryValue the query value
     * @param results    the query results
     * @throws Exception if the file cannot be written
     */
    public void exportToCsv(File file, String queryType, String queryValue, List<FileEvent> results) throws Exception {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("File Watcher System Query Results\n");
            writer.write("Query Type," + escape(queryType) + "\n");
            writer.write("Query Value," + escape(queryValue) + "\n");
            writer.write("Generated," + java.time.LocalDateTime.now() + "\n");
            writer.write("\n");

            writer.write("File Name,Extension,Path,Activity,Date/Time\n");

            for (FileEvent event : results) {
                writer.write(escape(event.getFileName()) + ",");
                writer.write(escape(event.getFileExtension()) + ",");
                writer.write(escape(event.getAbsolutePath()) + ",");
                writer.write(escape(event.getEventType()) + ",");
                writer.write(escape(event.getEventDateTime()) + "\n");
            }
        }
    }

    /**
     * Escapes CSV text so commas and quotes are handled correctly.
     *
     * @param value the value to escape
     * @return escaped CSV value
     */
    private String escape(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}