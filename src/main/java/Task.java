public abstract class Task {
    protected final String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() { this.isDone = true; }
    public void unmark() { this.isDone = false; }

    protected String statusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    public String getDescription() { return description; }
    public boolean isDone() { return isDone; }

    // Polymorphic type code (for file format)
    public abstract String typeCode();

    // Serialize to line
    public abstract String serialize();

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description;
    }

    // For subclasses to provide icon
    public abstract String typeIcon();
}
