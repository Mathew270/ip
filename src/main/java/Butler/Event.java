package Butler;

import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() { return from; }
    public LocalDateTime getTo() { return to; }

    @Override
    public String typeIcon() { return "[E]"; }

    @Override
    public String typeCode() { return "E"; }

    @Override
    public String serialize() {
        // Persist in ISO-8601 (LocalDateTime#toString -> 2025-08-25T18:00)
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description,
                from.toString(),
                to.toString()
        );
    }

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description
                + " (from: " + from.format(DISPLAY_DATETIME)
                + " to: " + to.format(DISPLAY_DATETIME) + ")";
    }
}


