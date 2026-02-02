import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime deadline;
    boolean hasTime;

    public Deadline(String description, LocalDateTime deadline, boolean hasTime) {
        super(description);
        this.deadline = deadline;
        this.hasTime = hasTime;
    }

    @Override
    public String toSaveFormat() {
        return String.format("D | %d | %s | %s", super.isDone ? 1 : 0, super.description, this.deadline);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter;
        if (hasTime) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        } else {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        return "[" + super.getStatusIcon() + "] ⏱ " + super.getDescription()
                + " (by: " + deadline.format(formatter) + ")";
    }
}