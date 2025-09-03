package Butler;

/**
 * The main logic class for the Butler chatbot application.
 * <p>
 * Butler manages a list of tasks (todos, deadlines, events) and allows
 * users to add, mark, unmark, delete, list, and search tasks.
 * Tasks are persisted to disk between runs.
 */
public class Butler {

    // ---------- Collaborators ----------
    private final Storage storage;
    private final TaskList tasks;

    // ---------- Commands Enum ----------
    private enum Command {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, UNKNOWN;

        static Command from(String s) {
            switch (s) {
            case "bye": return BYE;
            case "list": return LIST;
            case "mark": return MARK;
            case "unmark": return UNMARK;
            case "todo": return TODO;
            case "deadline": return DEADLINE;
            case "event": return EVENT;
            case "delete": return DELETE;
            case "find": return FIND;
            default: return UNKNOWN;
            }
        }
    }

    /**
     * Constructs a new Butler instance.
     *
     * @param filePath the path to the file used to persist tasks
     */
    public Butler(String filePath) {
        this.storage = new Storage(filePath);
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (Exception e) {
            loaded = new TaskList();
        }
        this.tasks = loaded;
    }

    /**
     * Processes a single user input and returns Butler's response as a string.
     * <p>
     * This replaces the old console-style event loop. Each call corresponds to
     * exactly one user command → one response for the GUI.
     *
     * @param input the raw user command string
     * @return Butler's response text
     */
    public String getResponse(String input) {
        try {
            String fullCommand = input.trim();
            if (fullCommand.isEmpty()) {
                return "";
            }

            String[] parts = Parser.splitCommand(fullCommand);
            Command cmd = Command.from(parts[0]);
            String argsLine = parts[1];

            switch (cmd) {
            case BYE:
                // Schedule app exit after 2 seconds (2000 ms)
                javafx.animation.PauseTransition delay = new javafx.animation.
                        PauseTransition(javafx.util.Duration.seconds(2));
                delay.setOnFinished(event -> javafx.application.Platform.exit());
                delay.play();
                return "Bye. Hope to see you again soon!";

            case LIST:
                return buildListString();

            case MARK:
                return handleMark(argsLine);

            case UNMARK:
                return handleUnmark(argsLine);

            case TODO:
                return handleTodo(argsLine);

            case DEADLINE:
                return handleDeadline(argsLine);

            case EVENT:
                return handleEvent(argsLine);

            case DELETE:
                return handleDelete(argsLine);

            case FIND:
                return handleFind(argsLine);

            default:
                throw new ButlerException("Sorry, I don't recognize that command.");
            }
        } catch (ButlerException ex) {
            return "⚠ " + ex.getMessage();
        }
    }

    // ---------- Command Handlers ----------

    private String handleTodo(String argsLine) throws ButlerException {
        Checks.ensureNonEmpty(argsLine.trim(), "Please tell me what the todo is about.");
        Task t = new Todo(argsLine.trim());
        tasks.add(t);
        storage.save(tasks.all());
        return "Got it. I've added this task:\n   " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDeadline(String argsLine) throws ButlerException {
        Checks.ensureContains(argsLine, " /by ", "A deadline needs a '/by <date>' part (yyyy-MM-dd).");
        String[] parts = Parser.splitOnce(argsLine, " /by ");
        String desc = parts[0].trim();
        String byRaw = parts[1].trim();

        Checks.ensureNonEmpty(desc, "Deadline description cannot be empty.");
        var by = Parser.parseLocalDate(byRaw);

        Task t = new Deadline(desc, by);
        tasks.add(t);
        storage.save(tasks.all());
        return "Got it. I've added this task:\n   " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleEvent(String argsLine) throws ButlerException {
        Checks.ensureContains(argsLine, " /from ", "An event needs '/from <start>' and '/to <end>'.");
        String[] p1 = Parser.splitOnce(argsLine, " /from ");
        String desc = p1[0].trim();
        String afterFrom = p1[1];

        Checks.ensureContains(afterFrom, " /to ", "Please include the end time using '/to <end>'.");
        String[] p2 = Parser.splitOnce(afterFrom, " /to ");
        String fromRaw = p2[0].trim();
        String toRaw = p2[1].trim();

        Checks.ensureNonEmpty(desc, "Event description cannot be empty.");
        var from = Parser.parseLocalDateTime(fromRaw);
        var to   = Parser.parseLocalDateTime(toRaw);

        Task t = new Event(desc, from, to);
        tasks.add(t);
        storage.save(tasks.all());
        return "Got it. I've added this task:\n   " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleMark(String argsLine) throws ButlerException {
        int idx = Checks.parseIndex(argsLine);
        Checks.ensureIndexInRange(idx, tasks.size(), "I can't find that task number.");
        Task t = tasks.get(idx - 1);
        t.mark();
        storage.save(tasks.all());
        return "Nice! I've marked this task as done:\n   " + t;
    }

    private String handleUnmark(String argsLine) throws ButlerException {
        int idx = Checks.parseIndex(argsLine);
        Checks.ensureIndexInRange(idx, tasks.size(), "That task number is not in the list.");
        Task t = tasks.get(idx - 1);
        t.unmark();
        storage.save(tasks.all());
        return "OK, I've marked this task as not done yet:\n   " + t;
    }

    private String handleDelete(String argsLine) throws ButlerException {
        int idx = Checks.parseIndex(argsLine);
        Checks.ensureIndexInRange(idx, tasks.size(), "That task number is not in the list.");
        Task removed = tasks.remove(idx - 1);
        storage.save(tasks.all());
        return "Noted. I've removed this task:\n   " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleFind(String argsLine) throws ButlerException {
        Checks.ensureNonEmpty(argsLine, "Please provide a keyword to search.");
        return buildFindString(argsLine.trim());
    }

    // ---------- Helpers for LIST / FIND ----------

    private String buildListString() {
        if (tasks.size() == 0) {
            return "Your task list is empty.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(".").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private String buildFindString(String keyword) {
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        int count = 0;
        for (Task t : tasks.all()) {
            if (t.description.contains(keyword)) {
                count++;
                sb.append(" ").append(count).append(".").append(t).append("\n");
            }
        }
        if (count == 0) {
            sb.append(" (no matching tasks found)\n");
        }
        return sb.toString().trim();
    }
}
