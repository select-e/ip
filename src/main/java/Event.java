import java.time.LocalDateTime;

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
        // E | isDone | description | from | to
        return String.format("E | %d | %s | %s | %s", super.isDone ? 1 : 0, super.description, this.from, this.to);
    }

    /**
     * Returns an event from a save file string
     * @param saveFormat the string
     * @return the event
     */
    public static Event fromSaveFormat(String saveFormat) {
        String[] taskInfo = saveFormat.split(" \\| ");
        Event event = new Event(taskInfo[1], taskInfo[2], taskInfo[3]);
        event.markDone(taskInfo[0].equals("1"));
        return event;
    }

    @Override
    public String toString() {
        return "[" + super.getStatusIcon() + "] @ " + super.getDescription()
                + " (from: " + from + " to: " + to + ")";
    }
}
