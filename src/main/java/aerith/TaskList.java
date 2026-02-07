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
    public void removeTask(int index) throws StorageException {
        Task task = tasks.get(index);
        tasks.remove(index);
        storage.updateTasks(this);
        ui.displayRemovedTask(task);
    }

    /**
     * Marks a task as done.
     * @param index The true task index
     * @throws StorageException
     */
    public void markTask(int index) throws StorageException {
        Task task = tasks.get(index);
        task.setIsDone(true);
        storage.updateTasks(this);
        ui.displayMarkedTask(index + 1, task);
    }

    /**
     * Marks a task as not done yet.
     * @param index The true task index
     * @throws StorageException
     */
    public void unmarkTask(int index) throws StorageException {
        Task task = tasks.get(index);
        task.setIsDone(false);
        storage.updateTasks(this);
        ui.displayUnmarkedTask(index + 1, task);
    }

    public ArrayList<Task> getTasksWithKeyword(String keyword) {
        ArrayList<Task> results = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                results.add(task);
            }
        }
        return results;
    }
}
