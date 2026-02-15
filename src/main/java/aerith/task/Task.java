package aerith.task;

/**
 * The blueprint for a task object.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns "X" if the task is done and " " otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the status of the task to done or not done.
     * @param value The new value to set to.
     */
    public void setIsDone(boolean value) {
        isDone = value;
    }

    /**
     * Returns the task as a string suitable to be placed in a text file.
     */
    public abstract String toSaveFormat();

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
