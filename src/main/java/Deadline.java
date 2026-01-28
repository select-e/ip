public class Deadline extends Task {
    protected String deadline;

    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toSaveFormat() {
        return String.format("D | %d | %s | %s", super.isDone ? 1 : 0, super.description, this.deadline);
    }

    @Override
    public String toString() {
        return "[" + super.getStatusIcon() + "] ⏱ " + super.getDescription()
                + " (by: " + deadline + ")";
    }
}