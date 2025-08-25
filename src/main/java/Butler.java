import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Butler {
    // ---------- Constants ----------
    private static final String LINE = "____________________________________________________________";

    // ---------- State ----------
    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final Storage storage = new Storage(); // Level 7 persistence

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

    // ---------- Entry Point ----------
    public static void main(String[] args) {
        printBox(
                " Hello! I'm Butler",
                " What can I do for you?"
        );

        // Load tasks from disk
        tasks.addAll(storage.load());

        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) continue;

                try {
                    String[] parts = splitCommand(input);
                    Command cmd = Command.from(parts[0]);
                    String argsLine = parts[1];

                    switch (cmd) {
                    case BYE:
                        printBox(" Bye. Hope to see you again soon!");
                        return;

                    case LIST:
                        printList();
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
                    printBox(" " + ex.getMessage());
                }
            }
        }
    }

    // ---------- Command Handlers ----------

    private static void handleTodo(String argsLine) throws ButlerException {
        String desc = argsLine.trim();
        ensureNonEmpty(desc, "Please tell me what the todo is about.");
        Task t = new Todo(desc);
        tasks.add(t);
        printAdded(t);
        storage.save(tasks);
    }

    private static void handleDeadline(String argsLine) throws ButlerException {
        ensureContains(argsLine, " /by ", "A deadline needs a '/by <date>' part (yyyy-MM-dd).");
        String[] parts = splitOnce(argsLine, " /by ");
        String desc = parts[0].trim();
        String byRaw = parts[1].trim();

        ensureNonEmpty(desc, "Deadline description cannot be empty.");
        LocalDate by = parseLocalDate(byRaw); // yyyy-MM-dd only (minimal)

        Task t = new Deadline(desc, by);
        tasks.add(t);
        printAdded(t);
        storage.save(tasks);
    }

    private static void handleEvent(String argsLine) throws ButlerException {
        ensureContains(argsLine, " /from ", "An event needs '/from <start>' and '/to <end>'.");
        String[] p1 = splitOnce(argsLine, " /from ");
        String desc = p1[0].trim();
        String afterFrom = p1[1];

        ensureContains(afterFrom, " /to ", "Please include the end time using '/to <end>'.");
        String[] p2 = splitOnce(afterFrom, " /to ");
        String fromRaw = p2[0].trim();
        String toRaw = p2[1].trim();

        ensureNonEmpty(desc, "Event description cannot be empty.");
        LocalDateTime from = parseLocalDateTime(fromRaw);
        LocalDateTime to = parseLocalDateTime(toRaw);

        Task t = new Event(desc, from, to);
        tasks.add(t);
        printAdded(t);
        storage.save(tasks);
    }

    private static void handleMark(String argsLine) throws ButlerException {
        int idx = parseIndex(argsLine);
        ensureIndexInRange(idx, "I can't find that task number.");
        Task t = tasks.get(idx - 1);
        t.mark();
        printBox(" Nice! I've marked this task as done:", "   " + t);
        storage.save(tasks);
    }

    private static void handleUnmark(String argsLine) throws ButlerException {
        int idx = parseIndex(argsLine);
        ensureIndexInRange(idx, "That task number is not in the list.");
        Task t = tasks.get(idx - 1);
        t.unmark();
        printBox(" OK, I've marked this task as not done yet:", "   " + t);
        storage.save(tasks);
    }

    private static void handleDelete(String argsLine) throws ButlerException {
        int idx = parseIndex(argsLine);
        ensureIndexInRange(idx, "That task number is not in the list.");
        Task removed = tasks.remove(idx - 1);
        printBox(" Noted. I've removed this task:", "   " + removed,
                " Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks);
    }

    // ---------- Date Parsing Helpers (Level 8) ----------

    private static LocalDate parseLocalDate(String s) throws ButlerException {
        // Minimal requirement: accept yyyy-MM-dd only
        try {
            return LocalDate.parse(s); // ISO yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new ButlerException("Please use date format yyyy-MM-dd (e.g., 2019-10-15).");
        }
    }

    private static LocalDateTime parseLocalDateTime(String s) throws ButlerException {
        // Accept either "yyyy-MM-dd HHmm" or ISO "yyyy-MM-dd'T'HH:mm"
        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        try {
            if (s.contains("T")) {
                return LocalDateTime.parse(s); // ISO-8601 like 2019-10-15T18:00
            } else if (s.contains(":")) {
                // If colon present but no 'T', try "yyyy-MM-dd HH:mm"
                DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return LocalDateTime.parse(s, f2);
            } else {
                // Try "yyyy-MM-dd HHmm"
                return LocalDateTime.parse(s, f1);
            }
        } catch (DateTimeParseException e) {
            throw new ButlerException("Please use datetime format 'yyyy-MM-dd HHmm' or ISO 'yyyy-MM-ddTHH:mm'.");
        }
    }

    // ---------- Printing Helpers ----------

    private static void printBox(String... lines) {
        System.out.println(LINE);
        for (String s : lines) System.out.println(s);
        System.out.println(LINE);
    }

    private static void printList() {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    private static void printAdded(Task t) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(LINE);
    }

    // ---------- Small Utilities ----------

    private static void ensureNonEmpty(String s, String msg) throws ButlerException {
        if (s == null || s.isEmpty()) throw new ButlerException(msg);
    }

    private static void ensureContains(String s, String needle, String msg) throws ButlerException {
        if (!s.contains(needle)) throw new ButlerException(msg);
    }

    private static int parseIndex(String s) throws ButlerException {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new ButlerException("Please provide a valid task number.");
        }
    }

    private static void ensureIndexInRange(int idx, String msg) throws ButlerException {
        if (idx < 1 || idx > tasks.size()) throw new ButlerException(msg);
    }

    private static String[] splitOnce(String s, String delim) {
        int pos = s.indexOf(delim);
        return new String[] { s.substring(0, pos), s.substring(pos + delim.length()) };
    }

    private static String[] splitCommand(String input) {
        int space = input.indexOf(' ');
        if (space == -1) return new String[] { input, "" };
        return new String[] { input.substring(0, space), input.substring(space + 1) };
    }
}

/*
 ======================
 Butler Error Handling
 ======================
 1. Empty description:
    - "todo" with nothing after → "Please tell me what the todo is about."
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
