import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path dataPath = Paths.get("data", "butler.txt");

    public ArrayList<Task> load() {
        ArrayList<Task> loaded = new ArrayList<>();
        try {
            Files.createDirectories(dataPath.getParent());
            if (!Files.exists(dataPath)) return loaded;

            List<String> lines = Files.readAllLines(dataPath, StandardCharsets.UTF_8);
            for (String raw : lines) {
                String[] p = raw.split("\\s*\\|\\s*");
                if (p.length < 3) continue; // skip corrupted lines
                char type = p[0].charAt(0);
                boolean done = "1".equals(p[1]);

                Task t = null;
                switch (type) {
                case 'T':
                    t = new Todo(p[2]);
                    break;
                case 'D':
                    if (p.length >= 4) t = new Deadline(p[2], p[3]);
                    break;
                case 'E':
                    if (p.length >= 5) t = new Event(p[2], p[3], p[4]);
                    break;
                }
                if (t != null && done) t.mark();
                if (t != null) loaded.add(t);
            }
        } catch (IOException e) {
            // ignore, start with empty
        }
        return loaded;
    }

    public void save(List<Task> tasks) {
        List<String> out = new ArrayList<>();
        for (Task t : tasks) {
            out.add(t.serialize());
        }
        try {
            Files.createDirectories(dataPath.getParent());
            Files.write(dataPath, out, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // ignore
        }
    }
}
