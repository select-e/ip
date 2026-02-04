import java.util.ArrayList;

public class TaskList {
    ArrayList<Task> tasks;
    Storage storage;

    public TaskList(Storage storage) {
        tasks = new ArrayList<>(100);
        this.storage = storage;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public int getLength() {
        return tasks.size();
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public void removeTask(int index) {
        Task task = tasks.get(index);
        tasks.remove(index);
        storage.updateTasks(this);
        System.out.println("✧ I have removed this task: ✧");
        System.out.println(task + "\n");
    }

    /**
     * Marks a task as done and displays the result.
     * @param command The user-inputted task number as a string
     * @throws InvalidInputException
     */
    public void markTask(String command) throws InvalidInputException {
        int taskNum;
        try {
            // Parse the command to int
            taskNum = Integer.parseInt(command);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Please specify tasks by their number.");
        }

        // Throw an exception if the task number is invalid
        if (taskNum <= 0 || taskNum > tasks.size()) {
            throw new InvalidInputException("Please enter a valid task number.");
        }

        tasks.get(taskNum - 1).markDone(true);
        storage.updateTasks(this);

        System.out.println("✧ I have marked this task as done: ✧");
        System.out.println(taskNum + ". " + tasks.get(taskNum - 1) + "\n");
    }

    /**
     * Marks a task as not done yet and displays the result.
     * @param command The user-inputted task number as a string
     * @throws InvalidInputException
     */
    public void unmarkTask(String command) throws InvalidInputException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(command);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Please specify tasks by their number.");
        }

        // Throw an exception if the task number is invalid
        if (taskNum <= 0 || taskNum > tasks.size()) {
            throw new InvalidInputException("Please enter a valid task number.");
        }

        tasks.get(taskNum - 1).markDone(false);
        storage.updateTasks(this);

        System.out.println("✧ I have marked this task as not done yet: ✧");
        System.out.println(taskNum + ". " + tasks.get(taskNum - 1) + "\n");
    }
}