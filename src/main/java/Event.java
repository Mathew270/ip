public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }

    @Override
    public String typeIcon() { return "[E]"; }

    @Override
    public String typeCode() { return "E"; }

    @Override
    public String serialize() {
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description,
                from,
                to
        );
    }

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description
                + " (from: " + from + " to: " + to + ")";
    }
}

