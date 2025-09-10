package Butler;

import java.time.LocalDate;

/**
 * A task that has a deadline (a specific date).
 */
public class Deadline extends Task {
    private LocalDate by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the date by which the task is due
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        assert by != null : "deadline date must not be null";
        this.by = by;
    }

    /** Returns the deadline date. */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Updates the deadline date.
     *
     * @param newBy the new due date
     */
    public void setBy(LocalDate newBy) {
        assert newBy != null : "new deadline date must not be null";
        this.by = newBy;
    }

    /**
     * Reschedules this deadline using {@code /by <yyyy-MM-dd>}.
     *
     * @param argsLine the argument string after the task index
     * @throws ButlerException if the arguments are invalid
     */
    @Override
    public void reschedule(String argsLine) throws ButlerException {
        String s = argsLine == null ? "" : argsLine.trim();

        // Accept "/by 2025-11-01" or "... /by 2025-11-01"
        String byRaw;
        if (s.startsWith(Parser.DELIM_BY)) {
            byRaw = s.substring(Parser.DELIM_BY.length()).trim();
        } else if (s.contains(Parser.DELIM_BY)) {
            String[] pp = Parser.splitOnce(s, Parser.DELIM_BY);
            byRaw = pp[1].trim();
        } else {
            throw new ButlerException("Use '/by <yyyy-MM-dd>' to set a new date.");
        }

        var newBy = Parser.parseLocalDate(byRaw);
        setBy(newBy);
    }

    @Override
    public String typeIcon() {
        return "[D]";
    }

    @Override
    public String typeCode() {
        return "D";
    }

    @Override
    public String serialize() {
        // D|done|desc|yyyy-MM-dd
        String doneFlag = isDone ? "1" : "0";
        return String.join("|", "D", doneFlag, description, by.toString());
    }

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description + " (by: " + DISPLAY_DATE.format(by) + ")";
    }
}
