package Butler;

/**
 * The main entry point for the Butler chatbot application.
 * <p>
 * Butler manages a list of tasks (todos, deadlines, events) and allows
 * users to add, mark, unmark, delete, list, and search tasks.
 * Tasks are persisted to disk between runs.
 */
public class Butler {

    // ---------- Collaborators ----------
    private final MainWindow ui;
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
        this.ui = new MainWindow();
        this.storage = new Storage(filePath);
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Problem loading tasks, starting fresh.");
            loaded = new TaskList();
        }
        this.tasks = loaded;
    }

    /**
     * Starts the main event loop of the chatbot.
     * <p>
     * Reads user commands, executes them, and displays results via {@link MainWindow}.
     * Exits when the user issues the "bye" command.
     */
    public void run(String input) {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = input.trim();
                if (fullCommand.isEmpty()) continue;

                String[] parts = Parser.splitCommand(fullCommand);
                Command cmd = Command.from(parts[0]);
                String argsLine = parts[1];

                switch (cmd) {
                case BYE:
                    ui.showMessage(" Bye. Hope to see you again soon!");
                    isExit = true;
                    break;

                case LIST:
                    tasks.printList(ui);
                    break;

                case MARK:
                    handleMark(argsLine);
                    break;

                case UNMARK:
                    handleUnmark(argsLine);
                    break;

                case TODO:
                    handleTodo(argsLine);
                    break;

                case DEADLINE:
                    handleDeadline(argsLine);
                    break;

                case EVENT:
                    handleEvent(argsLine);
                    break;

                case DELETE:
                    handleDelete(argsLine);
                    break;

                case FIND:
                    handleFind(argsLine);
                    break;

                default:
                    throw new ButlerException("Sorry, I don't recognize that command.");
                }
            } catch (ButlerException ex) {
                ui.showError(ex.getMessage());
            }
        }
    }

    // ---------- Command Handlers ----------
    // (private helpers are intentionally not documented with Javadoc)

    private void handleTodo(String argsLine) throws ButlerException {
        Checks.ensureNonEmpty(argsLine.trim(), "Please tell me what the todo is about.");
        Task t = new Todo(argsLine.trim());
        tasks.add(t);
        ui.printAdded(t, tasks.size());
        storage.save(tasks.all());
    }

    private void handleDeadline(String argsLine) throws ButlerException {
        Checks.ensureContains(argsLine, " /by ", "A deadline needs a '/by <date>' part (yyyy-MM-dd).");
        String[] parts = Parser.splitOnce(argsLine, " /by ");
        String desc = parts[0].trim();
        String byRaw = parts[1].trim();

        Checks.ensureNonEmpty(desc, "Deadline description cannot be empty.");
        var by = Parser.parseLocalDate(byRaw);

        Task t = new Deadline(desc, by);
        tasks.add(t);
        ui.printAdded(t, tasks.size());
        storage.save(tasks.all());
    }

    private void handleEvent(String argsLine) throws ButlerException {
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
        ui.printAdded(t, tasks.size());
        storage.save(tasks.all());
    }

    private void handleMark(String argsLine) throws ButlerException {
        int idx = Checks.parseIndex(argsLine);
        Checks.ensureIndexInRange(idx, tasks.size(), "I can't find that task number.");
        Task t = tasks.get(idx - 1);
        t.mark();
        ui.showMessage(" Nice! I've marked this task as done:", "   " + t);
        storage.save(tasks.all());
    }

    private void handleUnmark(String argsLine) throws ButlerException {
        int idx = Checks.parseIndex(argsLine);
        Checks.ensureIndexInRange(idx, tasks.size(), "That task number is not in the list.");
        Task t = tasks.get(idx - 1);
        t.unmark();
        ui.showMessage(" OK, I've marked this task as not done yet:", "   " + t);
        storage.save(tasks.all());
    }

    private void handleDelete(String argsLine) throws ButlerException {
        int idx = Checks.parseIndex(argsLine);
        Checks.ensureIndexInRange(idx, tasks.size(), "That task number is not in the list.");
        Task removed = tasks.remove(idx - 1);
        ui.showMessage(
                " Noted. I've removed this task:",
                "   " + removed,
                " Now you have " + tasks.size() + " tasks in the list."
        );
        storage.save(tasks.all());
    }

    private void handleFind(String argsLine) throws ButlerException {
        Checks.ensureNonEmpty(argsLine, "Please provide a keyword to search.");
        tasks.find(argsLine.trim(), ui);
    }
}
