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
        assert t != null : "cannot add null task";
        tasks.add(t);
    }

    /**
     * Retrieves the task at the given index.
     *
     * @param idx the index of the task (0-based)
     * @return the task at that index
     */
    public Task get(int idx) {
        assert idx >= 0 && idx < tasks.size() : "index out of bounds for get";
        return tasks.get(idx);
    }

    /**
     * Removes the task at the given index.
     *
     * @param idx the index of the task (0-based)
     * @return the task that was removed
     */
    public Task remove(int idx) {
        assert idx >= 0 && idx < tasks.size() : "index out of bounds for remove";
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
     * Returns whether the list has no tasks.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
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
     * Returns tasks whose description contains the given keyword.
     * This is a pure query (no UI), so callers can format the output
     * themselves. Keeps search logic in one place.
     *
     * @param keyword substring to match (case-sensitive)
     * @return a new {@link ArrayList} of matching tasks
     */
    public ArrayList<Task> findByDescriptionContains(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task t : tasks) {
            if (t != null && t.getDescription().contains(keyword)) {
                matches.add(t);
            }
        }
        return matches;
    }

    /**
     * Displays the full list of tasks in the user interface.
     * <p>
     * This method constructs a numbered list of all tasks currently stored
     * in the {@code TaskList} and passes the formatted output to the given
     * {@link MainWindow} instance for rendering in the GUI.
     *
     * @param ui the {@link MainWindow} responsible for displaying the task list
     */
    public void printList(MainWindow ui) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(".").append(tasks.get(i)).append("\n");
        }
        ui.showTaskList(sb.toString().trim());
    }

    /**
     * Searches for tasks whose description contains the given keyword
     * and displays the results in the user interface.
     * <p>
     * Matching tasks are numbered in the order they appear in the task list.
     * If no matches are found, a message indicating this is shown instead.
     * The formatted output is passed to the given {@link MainWindow} instance
     * for rendering in the GUI.
     *
     * @param keyword the keyword to search for within each task description
     * @param ui      the {@link MainWindow} responsible for displaying the results
     */
    public void find(String keyword, MainWindow ui) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        int count = 0;
        for (Task t : findByDescriptionContains(keyword)) {
            count++;
            sb.append(" ").append(count).append(".").append(t).append("\n");
        }
        if (count == 0) {
            sb.append(" (no matching tasks found)\n");
        }
        ui.showTaskSearch(sb.toString().trim());
    }
}
