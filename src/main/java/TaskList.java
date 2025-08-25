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
}

