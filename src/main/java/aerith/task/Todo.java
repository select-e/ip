package aerith.task;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toSaveFormat() {
        // T | isDone | description
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Todo other)) {
            return false;
        }
        return other.description.equals(super.description) && other.isDone == super.isDone;
    }
}