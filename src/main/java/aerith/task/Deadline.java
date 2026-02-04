package aerith.task;

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
        // D | isDone | description | deadline | hasTime
        return String.format("D | %d | %s | %s | %d",
                super.isDone ? 1 : 0, super.description, deadline, hasTime ? 1 : 0);
    }

    /**
     * Returns a deadline task from a save file string
     * @param saveFormat the string
     * @return the deadline task
     */
    public static Deadline fromSaveFormat(String saveFormat) {
        String[] taskInfo = saveFormat.split(" \\| ");
        Deadline deadline = new Deadline(taskInfo[1], LocalDateTime.parse(taskInfo[2]), taskInfo[3].equals("1"));
        deadline.markDone(taskInfo[1].equals("1"));
        return deadline;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter;
        if (hasTime) {
            formatter = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a");
        } else {
            formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        }
        return "[" + super.getStatusIcon() + "] ⏱ " + super.getDescription()
                + " (by: " + deadline.format(formatter) + ")";
    }
}