package Butler;

import java.time.LocalDateTime;

/**
 * Represents an event task that spans a specific start and end date/time.
 * <p>
 * An {@code Event} has a description, a start time, and an end time.
 * It extends the {@link Task} base class.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an {@code Event} task with the given description, start time, and end time.
     *
     * @param description the description of the event
     * @param from        the starting date and time of the event
     * @param to          the ending date and time of the event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the starting date and time of the event.
     *
     * @return the {@link LocalDateTime} when the event begins
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the ending date and time of the event.
     *
     * @return the {@link LocalDateTime} when the event ends
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the icon representing this task type.
     *
     * @return the string {@code "[E]"} for events
     */
    @Override
    public String typeIcon() {
        return "[E]";
    }

    /**
     * Returns the code used to identify this task type in storage.
     *
     * @return the string {@code "E"} for events
     */
    @Override
    public String typeCode() {
        return "E";
    }

    /**
     * Serializes this event task into a storable string format.
     * <p>
     * Format: {@code E|<doneFlag>|<description>|<from>|<to>}
     * where {@code <from>} and {@code <to>} are stored in ISO-8601
     * (via {@link LocalDateTime#toString()}).
     *
     * @return the serialized representation of this event
     */
    @Override
    public String serialize() {
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description,
                from.toString(),
                to.toString()
        );
    }

    /**
     * Returns a string representation of this event task,
     * including its description, status, and formatted start and end times.
     *
     * @return the string representation of this event
     */
    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description
                + " (from: " + from.format(DISPLAY_DATETIME)
                + " to: " + to.format(DISPLAY_DATETIME) + ")";
    }
}
