public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toSaveFormat() {
        return String.format("T | %d | %s", super.isDone ? 1 : 0, super.description);
    }

    @Override
    public String toString() {
        return "[" + super.getStatusIcon() + "] ✎ " + super.getDescription();
    }
}