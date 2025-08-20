import java.util.Scanner;

public class Butler {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        System.out.println(line);
        System.out.println(" Hello! I'm Butler");
        System.out.println(" What can I do for you?");
        System.out.println(line);

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break;
            }
            String input = sc.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon");
                System.out.println(line);
                break;
            }
            System.out.println("" + input);
        }
        sc.close();
    }
}
