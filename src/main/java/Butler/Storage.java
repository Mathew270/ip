package Butler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of tasks to and from the local filesystem.
 * <p>
 * {@code Storage} loads tasks from a given file on startup and saves tasks
 * whenever the task list changes. Tasks are stored in a simple line-based
 * format that uses {@code |} as a delimiter.
 * <p>
 * Example formats:
 * <ul>
 *     <li>{@code T|1|read book}</li>
 *     <li>{@code D|0|return book|2019-12-02}</li>
 *     <li>{@code E|1|project meeting|2019-12-02T14:00|2019-12-02T16:00}</li>
 * </ul>
 */
public class Storage {
    private final Path dataPath;

    /**
     * Constructs a {@code Storage} object with the specified file path.
     *
     * @param filePath the relative or absolute path of the file to use for persistence,
     *                 e.g. {@code "data/butler.txt"}
     */
    public Storage(String filePath) {
        this.dataPath = Paths.get(filePath); // e.g., "data/butler.txt"
    }

    /**
     * Loads tasks from the backing file.
     * <p>
     * If the file does not exist, an empty list is returned.
     * Malformed lines or unknown task types are skipped silently.
     *
     * @return a list of {@link Task} objects loaded from disk
     */
    public ArrayList<Task> load() {
        ArrayList<Task> loaded = new ArrayList<>();
        try {
            Files.createDirectories(dataPath.getParent());
            if (!Files.exists(dataPath)) return loaded;

            List<String> lines = Files.readAllLines(dataPath, StandardCharsets.UTF_8);
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) continue;

                String[] p = line.split("\\s*\\|\\s*");
                if (p.length < 3) continue; // skip malformed lines

                String type = p[0];
                boolean done = "1".equals(p[1]);

                Task t = null;
                switch (type) {
                case "T": {
                    // T|done|desc
                    t = new Todo(p[2]);
                    break;
                }
                case "D": {
                    // D|done|desc|yyyy-MM-dd
                    LocalDate by = LocalDate.parse(p[3]);
                    t = new Deadline(p[2], by);
                    break;
                }
                case "E": {
                    // E|done|desc|fromISO|toISO
                    LocalDateTime from = LocalDateTime.parse(p[3]);
                    LocalDateTime to   = LocalDateTime.parse(p[4]);
                    t = new Event(p[2], from, to);
                    break;
                }
                default:
                    // unknown type -> skip line
                }
                if (t != null && done) t.mark();
                if (t != null) loaded.add(t);
            }
        } catch (IOException e) {
            // ignore -> start with empty list
        } catch (Exception e) {
            // ignore corrupted lines -> keep what we successfully parsed
        }
        return loaded;
    }

    /**
     * Saves the given list of tasks to the backing file.
     * <p>
     * Each task is serialized using its {@link Task#serialize()} method.
     * Existing content in the file will be replaced.
     *
     * @param tasks the list of tasks to persist
     */
    public void save(List<Task> tasks) {
        assert tasks != null : "tasks list to save must not be null";
        List<String> out = new ArrayList<>();
        for (Task t : tasks) {
            out.add(t.serialize());  // polymorphic, no instanceof
        }
        try {
            Files.createDirectories(dataPath.getParent());
            Files.write(dataPath, out, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // ignore write errors for now
        }
    }
}
