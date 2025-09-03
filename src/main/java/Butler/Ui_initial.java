package Butler;

import java.util.Scanner;

/**
 * Handles all interactions with the user for the Butler chatbot.
 * <p>
 * The {@code Ui} class is responsible for displaying messages,
 * errors, task information, and reading user input from the console.
 */
public class Ui_initial {
    private final Scanner sc;

    /**
     * Constructs a {@code Ui} instance that reads input from {@code System.in}.
     */
    public Ui() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Displays the welcome message shown when the chatbot starts.
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Butler");
        System.out.println(" What can I do for you?");
        showLine();
    }

    /**
     * Reads the next line of user input from the console.
     *
     * @return the raw command string entered by the user
     */
    public String readCommand() {
        return sc.nextLine();
    }

    /**
     * Prints a horizontal divider line.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays one or more lines of output to the user,
     * wrapped between divider lines.
     *
     * @param lines the lines of text to display
     */
    public void showMessage(String... lines) {
        showLine();
        for (String s : lines) System.out.println(s);
        showLine();
    }

    /**
     * Displays an error message to the user,
     * wrapped in a standard error output format.
     *
     * @param msg the error message to display
     */
    public void showError(String msg) {
        showMessage(" " + msg);
    }

    /**
     * Displays a confirmation message when a task is added,
     * along with the total number of tasks now in the list.
     *
     * @param t     the task that was added
     * @param total the total number of tasks in the list after addition
     */
    public void printAdded(Task t, int total) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + total + " tasks in the list.");
        showLine();
    }
}
