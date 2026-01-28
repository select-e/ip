public class Event extends Task{
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toSaveFormat() {
        return String.format("E | %d | %s | %s | %s", super.isDone ? 1 : 0, super.description, this.from, this.to);
    }

    @Override
    public String toString() {
        return "[" + super.getStatusIcon() + "] @ " + super.getDescription()
                + " (from: " + from + " to: " + to + ")";
    }
}
