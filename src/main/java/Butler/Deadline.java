package Butler;

import java.time.LocalDate;

public class Deadline extends Task {
    private final LocalDate by;

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    public LocalDate getBy() { return by; }

    @Override
    public String typeIcon() { return "[D]"; }

    @Override
    public String typeCode() { return "D"; }

    @Override
    public String serialize() {
        // persist in ISO-8601 for portability
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description,
                by.toString() // yyyy-MM-dd
        );
    }

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description
                + " (by: " + by.format(DISPLAY_DATE) + ")";
    }
}
