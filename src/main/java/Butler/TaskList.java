package Butler;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public Task remove(int idx) {
        return tasks.remove(idx);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> all() {
        return tasks;
    }

    /**
     * Prints all tasks in the list with numbering.
     */
    public void printList(Ui ui) {
        ui.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        ui.showLine();
    }

    public void find(String keyword, Ui ui) {
        ui.showLine();
        System.out.println(" Here are the matching tasks in your list:");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.description.contains(keyword)) {
                count++;
                System.out.println(" " + count + "." + t);
            }
        }
        if (count == 0) {
            System.out.println(" (no matching tasks found)");
        }
        ui.showLine();
    }
}

