package Butler;

import java.time.LocalDate;

/**
 * Represents a task with a specific deadline.
 * <p>
 * A {@code Deadline} has a description and a due date represented
 * as a {@link LocalDate}. It extends the {@link Task} base class.
 */
public class Deadline extends Task {
    private final LocalDate by;

    /**
     * Constructs a {@code Deadline} task with the given description and due date.
     *
     * @param description the description of the task
     * @param by          the due date of the task
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date of this deadline task.
     *
     * @return the {@link LocalDate} representing when the task is due
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the icon representing this task type.
     *
     * @return the string {@code "[D]"} for deadlines
     */
    @Override
    public String typeIcon() {
        return "[D]";
    }

    /**
     * Returns the code used to identify this task type in storage.
     *
     * @return the string {@code "D"} for deadlines
     */
    @Override
    public String typeCode() {
        return "D";
    }

    /**
     * Serializes this deadline task into a storable string format.
     * <p>
     * Format: {@code D|<doneFlag>|<description>|<dueDate>}
     * where {@code <dueDate>} is stored in ISO-8601 ({@code yyyy-MM-dd}).
     *
     * @return the serialized representation of this deadline
     */
    @Override
    public String serialize() {
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description,
                by.toString() // yyyy-MM-dd
        );
    }
}
