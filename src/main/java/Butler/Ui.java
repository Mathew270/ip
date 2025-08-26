package Butler;

import java.util.Scanner;

public class Ui {
    private final Scanner sc;

    public Ui() {
        this.sc = new Scanner(System.in);
    }

    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Butler");
        System.out.println(" What can I do for you?");
        showLine();
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showMessage(String... lines) {
        showLine();
        for (String s : lines) System.out.println(s);
        showLine();
    }

    public void showError(String msg) {
        showMessage(" " + msg);
    }

    public void printAdded(Task t, int total) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + total + " tasks in the list.");
        showLine();
    }
}

