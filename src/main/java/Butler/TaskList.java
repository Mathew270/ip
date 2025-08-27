package Butler;

import java.util.ArrayList;

/**
 * Represents a list of tasks in the Butler chatbot.
 * <p>
 * Provides operations to add, retrieve, remove, and print tasks.
 * Wraps an {@link ArrayList} of {@link Task} objects for storage.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list initialized with an existing collection of tasks.
     *
     * @param initial the list of tasks to start with
     */
    public TaskList(ArrayList<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Adds a new task to the list.
     *
     * @param t the task to add
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Retrieves the task at the given index.
     *
     * @param idx the index of the task (0-based)
     * @return the task at that index
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Removes the task at the given index.
     *
     * @param idx the index of the task (0-based)
     * @return the task that was removed
     */
    public Task remove(int idx) {
        return tasks.remove(idx);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return all tasks in this task list
     */
    public ArrayList<Task> all() {
        return tasks;
    }

    /**
     * Prints all tasks in the list with numbering, formatted through the given {@link Ui}.
     *
     * @param ui the user interface for showing divider lines
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
