import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path dataPath = Paths.get("data", "butler.txt"); // ./data/butler.txt

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
                if (p.length < 3) continue; // skip malformed

                String type = p[0];
                boolean done = "1".equals(p[1]);

                Task t = null;
                switch (type) {
                case "T": {
                    // T|done|desc
                    if (p.length >= 3) {
                        t = new Todo(p[2]);
                    }
                    break;
                }
                case "D": {
                    // D|done|desc|yyyy-MM-dd
                    if (p.length >= 4) {
                        LocalDate by = LocalDate.parse(p[3]); // ISO
                        t = new Deadline(p[2], by);
                    }
                    break;
                }
                case "E": {
                    // E|done|desc|yyyy-MM-ddTHH:mm[:ss]|yyyy-MM-ddTHH:mm[:ss]
                    if (p.length >= 5) {
                        LocalDateTime from = LocalDateTime.parse(p[3]); // ISO
                        LocalDateTime to   = LocalDateTime.parse(p[4]);
                        t = new Event(p[2], from, to);
                    }
                    break;
                }
                default:
                    // unknown line, skip
                }
                if (t != null && done) t.mark();
                if (t != null) loaded.add(t);
            }
        } catch (IOException e) {
            // ignore -> start empty
        } catch (Exception e) {
            // ignore corrupted lines -> keep what we have
        }
        return loaded;
    }

    public void save(List<Task> tasks) {
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
