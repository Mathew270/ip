public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() { return by; }

    @Override
    public String typeIcon() { return "[D]"; }

    @Override
    public String typeCode() { return "D"; }

    @Override
    public String serialize() {
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description,
                by
        );
    }

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description + " (by: " + by + ")";
    }
}

