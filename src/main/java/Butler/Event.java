package Butler;

import java.time.LocalDateTime;

/**
 * A task that represents an event with a start and end date-time.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Creates an event task.
     *
     * @param description the event description
     * @param from the start date-time
     * @param to the end date-time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        assert from != null && to != null : "event times must not be null";
        assert !to.isBefore(from) : "event end must not be before start";
        this.from = from;
        this.to = to;
    }

    /** Returns the start date-time. */
    public LocalDateTime getFrom() {
        return from;
    }

    /** Returns the end date-time. */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Updates the start and end date-times of the event.
     *
     * @param newFrom the new start date-time
     * @param newTo the new end date-time
     */
    public void setSchedule(LocalDateTime newFrom, LocalDateTime newTo) {
        assert newFrom != null && newTo != null : "event times must not be null";
        assert !newTo.isBefore(newFrom) : "event end must not be before start";
        this.from = newFrom;
        this.to = newTo;
    }

    /**
     * Reschedules this event using {@code /from <start> /to <end>}.
     *
     * @param argsLine the argument string after the task index
     * @throws ButlerException if the arguments are invalid
     */
    @Override
    public void reschedule(String argsLine) throws ButlerException {
        String s = argsLine == null ? "" : argsLine.trim();

        // Accept "/from ... /to ..." or "... /from ... /to ..."
        String afterFrom;
        if (s.startsWith(Parser.DELIM_FROM)) {
            afterFrom = s.substring(Parser.DELIM_FROM.length());
        } else if (s.contains(Parser.DELIM_FROM)) {
            String[] p1 = Parser.splitOnce(s, Parser.DELIM_FROM);
            afterFrom = p1[1];
        } else {
            throw new ButlerException("Use '/from <start> /to <end>' to set new times.");
        }

        if (!afterFrom.contains(Parser.DELIM_TO)) {
            throw new ButlerException("Please include the end time using '/to <end>'.");
        }
        String[] p2 = Parser.splitOnce(afterFrom, Parser.DELIM_TO);
        var newFrom = Parser.parseLocalDateTime(p2[0].trim());
        var newTo   = Parser.parseLocalDateTime(p2[1].trim());
        assert newFrom != null && newTo != null : "event times must not be null";
        assert !newTo.isBefore(newFrom) : "event end must not be before start";
        setSchedule(newFrom, newTo);
    }

    @Override
    public String typeIcon() {
        return "[E]";
    }

    @Override
    public String typeCode() {
        return "E";
    }

    @Override
    public String serialize() {
        // E|done|desc|fromISO|toISO
        String doneFlag = isDone ? "1" : "0";
        return String.join("|", "E", doneFlag, description, from.toString(), to.toString());
    }

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description
                + " (from: " + DISPLAY_DATETIME.format(from)
                + ", to: "   + DISPLAY_DATETIME.format(to) + ")";
    }
}
