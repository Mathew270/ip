package Butler;

/**
 * Represents a simple todo task without any associated date or time.
 * <p>
 * A {@code Todo} has only a description and completion status.
 * It extends the {@link Task} base class and provides its own type code
 * and serialization format.
 */
public class Todo extends Task {

    /**
     * Constructs a {@code Todo} task with the given description.
     * The task is initially marked as not done.
     *
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the icon representing this task type.
     *
     * @return the string {@code "[T]"} for todos
     */
    @Override
    public String typeIcon() {
        return "[T]";
    }

    /**
     * Returns the code used to identify this task type in storage.
     *
     * @return the string {@code "T"} for todos
     */
    @Override
    public String typeCode() {
        return "T";
    }

    /**
     * Serializes this todo task into a storable string format.
     * <p>
     * Format: {@code T|<doneFlag>|<description>}
     *
     * @return the serialized representation of this todo
     */
    @Override
    public String serialize() {
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description
        );
    }
}
