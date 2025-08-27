package Butler;

import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all types of tasks managed by the Butler chatbot.
 * <p>
 * A {@code Task} has a description and a completion status (done/not done).
 * Subclasses such as {@link Todo}, {@link Deadline}, and {@link Event} provide
 * specific behaviors and additional fields where applicable.
 * <p>
 * This class defines common methods for marking/unmarking tasks,
 * as well as abstract methods for persistence and type representation.
 */
public abstract class Task {
    protected final String description;
    protected boolean isDone;

    /** Formatter for displaying plain dates (e.g., {@code Oct 15 2019}). */
    protected static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM d yyyy");

    /** Formatter for displaying date-times (e.g., {@code Oct 15 2019 18:00}). */
    protected static final DateTimeFormatter DISPLAY_DATETIME =
            DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    /**
     * Constructs a new {@code Task} with the given description.
     * Tasks are initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the icon representing the done/not-done status.
     *
     * @return {@code "[X]"} if done, {@code "[ ]"} otherwise
     */
    protected String statusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns whether this task is marked as done.
     *
     * @return {@code true} if done, {@code false} otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    // ---- Polymorphic hooks for subclasses ----

    /**
     * Returns the icon representing this task type.
     * For example: {@code "[T]"} for todos, {@code "[D]"} for deadlines.
     *
     * @return the string icon representing the type
     */
    public abstract String typeIcon();

    /**
     * Returns the short code used to identify this task type in storage.
     * For example: {@code "T"} for todos, {@code "D"} for deadlines.
     *
     * @return the type code string
     */
    public abstract String typeCode();

    /**
     * Serializes this task into a storable string format.
     * Subclasses define their own specific serialization format.
     *
     * @return the serialized representation of this task
     */
    public abstract String serialize();

    /**
     * Returns a string representation of this task,
     * including its type, status, and description.
     *
     * @return the string representation of this task
     */
    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description;
    }
}
