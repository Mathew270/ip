public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String typeIcon() { return "[T]"; }

    @Override
    public String typeCode() { return "T"; }

    @Override
    public String serialize() {
        return String.join("|",
                typeCode(),
                isDone ? "1" : "0",
                description
        );
    }
}
