import java.util.Scanner;

public class Butler {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        System.out.println(line);
        System.out.println(" Hello! I'm Butler");
        System.out.println(" What can I do for you?");
        System.out.println(line);

        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String input = sc.nextLine();

            if (input.equals("bye")) {
                System.out.println(line);
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (input.equals("list")) {
                System.out.println(line);
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i].toString());
                }
                System.out.println(line);
            } else if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5)) - 1;
                tasks[idx].mark();
                System.out.println(line);
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[idx].toString());
                System.out.println(line);
            } else if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7)) - 1;
                tasks[idx].unmark();
                System.out.println(line);
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[idx].toString());
                System.out.println(line);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(line);
                System.out.println(" added: " + input);
                System.out.println(line);
            }
        }
        sc.close();
    }
}
