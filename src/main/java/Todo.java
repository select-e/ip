import java.time.LocalDateTime;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toSaveFormat() {
        // T | isDone | deadline
        return String.format("T | %d | %s", super.isDone ? 1 : 0, super.description);
    }

    /**
     * Returns a todo task from a save file string
     * @param saveFormat the string
     * @return the todo
     */
    public static Todo fromSaveFormat(String saveFormat) {
        String[] taskInfo = saveFormat.split(" \\| ");
        Todo todo = new Todo(taskInfo[1]);
        todo.markDone(taskInfo[0].equals("1"));
        return todo;
    }

    @Override
    public String toString() {
        return "[" + super.getStatusIcon() + "] ✎ " + super.getDescription();
    }
}