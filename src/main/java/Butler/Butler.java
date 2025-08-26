package Butler;

public class Butler {
    // ---------- Constants ----------
    private static final String FILE_PATH = "data/butler.txt";

    // ---------- Collaborators ----------
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    // ---------- Commands Enum ----------
    private enum Command {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN;

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
            default: return UNKNOWN;
            }
        }
    }

    public Butler(String filePath) {
        this.ui = new Ui();
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

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand().trim();
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

                default:
                    throw new ButlerException("Sorry, I don't recognize that command.");
                }
            } catch (ButlerException ex) {
                ui.showError(ex.getMessage());
            }
        }
    }

    // ---------- Command Handlers ----------

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

    public static void main(String[] args) {
        new Butler(FILE_PATH).run();
    }
}

/*
 ======================
 Butler Error Handling
 ======================
 1. Empty description:
    - "todo" with nothing after → "Please tell me what the todo is about."\
    - "deadline" with no desc or /by part → error
    - "event" with no desc, /from, or /to parts → error

 2. Missing keywords:
    - deadline without "/by" → "A deadline needs a '/by <time>' part."
    - event without "/from" or "/to" → clear error messages

 3. Empty values after keywords:
    - "/by" but nothing after → "Tell me when it's due..."
    - "/from" with no time → "Please specify the start time..."
    - "/to" with no time → "Please specify the end time..."

 4. Invalid indices:
    - mark/unmark with non-numeric → "Please provide a valid task number."
    - mark/unmark with out-of-range index → "That task number is not in the list."

 5. Unknown command:
    - Any command not recognized (e.g., "blah") → "Sorry, I don't recognize that command."

 6. Storage limit:
    - More than 100 tasks → "I've reached my 100-task limit."
 */
