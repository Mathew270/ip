package Butler;

import java.time.format.DateTimeFormatter;

public abstract class Task {
    protected final String description;
    protected boolean isDone;

    // For display (subclasses may also use their own)
    protected static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    protected static final DateTimeFormatter DISPLAY_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() { this.isDone = true; }
    public void unmark() { this.isDone = false; }

    protected String statusIcon() { return isDone ? "[X]" : "[ ]"; }
    public boolean isDone() { return isDone; }
    public String getDescription() { return description; }

    // Polymorphic hooks for persistence/printing
    public abstract String typeIcon();     // "[T]", "[D]", "[E]"
    public abstract String typeCode();     // "T", "D", "E"
    public abstract String serialize();    // e.g. T|1|read book

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description;
    }
}
