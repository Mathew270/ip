import java.util.Scanner;

public class Butler {
    // ---------- Constants ----------
    private static final String LINE = "____________________________________________________________";

    // ---------- State ----------
    private static final Task[] tasks = new Task[100];
    private static int taskCount = 0;

    // ---------- Entry Point ----------
    public static void main(String[] args) {
        printBox(
                " Hello! I'm Butler",
                " What can I do for you?"
        );

        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) continue;

                try {
                    // Extract command (first word) and the rest as arguments
                    String[] parts = splitCommand(input);
                    String cmd = parts[0];
                    String argsLine = parts[1];

                    switch (cmd) {
                        case "bye":
                            printBox(" Bye. Hope to see you again soon!");
                            return;

                        case "list":
                            printList();
                            break;

                        case "mark":
                            handleMark(argsLine);
                            break;

                        case "unmark":
                            handleUnmark(argsLine);
                            break;

                        case "todo":
                            handleTodo(argsLine);
                            break;

                        case "deadline":
                            handleDeadline(argsLine);
                            break;

                        case "event":
                            handleEvent(argsLine);
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
        ensureCapacity();
        Task t = new Todo(desc);
        addTask(t);
        printAdded(t);
    }

    private static void handleDeadline(String argsLine) throws ButlerException {
        // Format: <desc> /by <by>
        ensureContains(argsLine, " /by ", "A deadline needs a '/by <time>' part.");
        String[] parts = splitOnce(argsLine, " /by ");
        String desc = parts[0].trim();
        String by = parts[1].trim();

        ensureNonEmpty(desc, "Deadline description cannot be empty.");
        ensureNonEmpty(by, "Tell me when it's due using '/by <time>'.");

        ensureCapacity();
        Task t = new Deadline(desc, by);
        addTask(t);
        printAdded(t);
    }

    private static void handleEvent(String argsLine) throws ButlerException {
        // Format: <desc> /from <from> /to <to>
        ensureContains(argsLine, " /from ", "An event needs '/from <start>' and '/to <end>'.");
        String[] p1 = splitOnce(argsLine, " /from ");
        String desc = p1[0].trim();
        String afterFrom = p1[1];

        ensureContains(afterFrom, " /to ", "Please include the end time using '/to <end>'.");
        String[] p2 = splitOnce(afterFrom, " /to ");
        String from = p2[0].trim();
        String to = p2[1].trim();

        ensureNonEmpty(desc, "Event description cannot be empty.");
        ensureNonEmpty(from, "Please specify the start time after '/from'.");
        ensureNonEmpty(to, "Please specify the end time after '/to'.");

        ensureCapacity();
        Task t = new Event(desc, from, to);
        addTask(t);
        printAdded(t);
    }

    private static void handleMark(String argsLine) throws ButlerException {
        int idx = parseIndex(argsLine);
        ensureIndexInRange(idx, "I can't find that task number.");
        tasks[idx - 1].mark();
        printBox(
                " Nice! I've marked this task as done:",
                "   " + tasks[idx - 1]
        );
    }

    private static void handleUnmark(String argsLine) throws ButlerException {
        int idx = parseIndex(argsLine);
        ensureIndexInRange(idx, "That task number is not in the list.");
        tasks[idx - 1].unmark();
        printBox(
                " OK, I've marked this task as not done yet:",
                "   " + tasks[idx - 1]
        );
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
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void printAdded(Task t) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    // ---------- Small Utilities ----------

    private static void addTask(Task t) {
        tasks[taskCount++] = t;
    }

    private static void ensureCapacity() throws ButlerException {
        if (taskCount >= tasks.length) {
            throw new ButlerException("I've reached my 100-task limit.");
        }
    }

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
        if (idx < 1 || idx > taskCount) throw new ButlerException(msg);
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
