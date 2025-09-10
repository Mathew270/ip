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

    /** Marks this task as done. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks this task as not done. */
    public void unmark() {
        this.isDone = false;
    }

    /** Returns the icon representing the done/not-done status. */
    protected String statusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /** Returns whether this task is marked as done. */
    public boolean isDone() {
        return isDone;
    }

    /** Returns the description of this task. */
    public String getDescription() {
        return description;
    }

    // ---- Polymorphic hooks for subclasses ----

    /** Returns the icon representing this task type. */
    public abstract String typeIcon();

    /** Returns the short code used to identify this task type in storage. */
    public abstract String typeCode();

    /** Serializes this task into a storable string format. */
    public abstract String serialize();

    /**
     * Reschedules this task using a task-specific argument format.
     * <p>
     * Default implementation: this task is not reschedulable.
     *
     * @param argsLine the argument string after the task index
     * @throws ButlerException if rescheduling is unsupported or arguments are invalid
     */
    public void reschedule(String argsLine) throws ButlerException {
        throw new ButlerException("This task has no date/time to reschedule.");
    }

    /** String representation including type, status, and description. */
    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description;
    }
}
