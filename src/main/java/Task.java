public class Task {
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

    public String typeIcon() { return "[?]"; }

    @Override
    public String toString() {
        return typeIcon() + statusIcon() + " " + description;
    }
}

