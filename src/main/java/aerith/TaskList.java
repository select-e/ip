package aerith;

import java.util.ArrayList;

import aerith.exception.StorageException;
import aerith.task.Task;

/**
 * Handles the list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    private final Storage storage;
    private final Ui ui;

    public TaskList(Storage storage, Ui ui) {
        tasks = new ArrayList<>(100);
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Adds a new task to the list.
     * @param task The new task
     */
    public void addTask(Task task) throws StorageException {
        tasks.add(task);
        storage.updateTasks(this);
    }

    /**
     * Returns the length of the list.
     * @return The number of tasks in the list
     */
    public int getLength() {
        return tasks.size();
    }

    /**
     * Returns a task specified by its index
     * @param index The index of the task
     * @return The task at that index
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Removes a task at a specified index.
     * @param index The index of the task
     */
    public String removeTask(int index) throws StorageException {
        assert index >= 0 && index < tasks.size() : "Index should be within bounds";

        Task task = tasks.get(index);
        tasks.remove(index);
        storage.updateTasks(this);
        return ui.getRemovedTaskConfirmation(task);
    }

    /**
     * Marks a task as done.
     * @param index The true task index
     * @throws StorageException If an error occurs while updating the task data
     */
    public String markTask(int index) throws StorageException {
        Task task = tasks.get(index);
        task.setIsDone(true);
        storage.updateTasks(this);
        return ui.getMarkedTaskConfirmation(index + 1, task);
    }

    /**
     * Marks a task as not done yet.
     * @param index The true task index
     * @throws StorageException If an error occurs while updating the task data
     */
    public String unmarkTask(int index) throws StorageException {
        Task task = tasks.get(index);
        task.setIsDone(false);
        storage.updateTasks(this);
        return ui.getUnmarkedTaskConfirmation(index + 1, task);
    }

    /**
     * Returns all tasks that contain the keyword.
     * @param keyword The keyword provided
     */
    public ArrayList<Task> getTasksWithKeyword(String keyword) {
        ArrayList<Task> results = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                results.add(task);
            }
        }
        return results;
    }

    /**
     * Updates the save file with the current list of tasks.
     * @throws StorageException If an error occurs while updating the task data
     */
    public void updateTasks() throws StorageException {
        storage.updateTasks(this);
    }
}
