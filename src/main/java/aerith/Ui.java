package aerith;

import java.util.ArrayList;

import aerith.exception.AerithException;
import aerith.task.Deadline;
import aerith.task.Event;
import aerith.task.Task;
import aerith.task.Todo;

/**
 * Deals with interactions with the user.
 */
public class Ui {
    /**
     * Returns the opening message.
     */
    public String getOpeningMessage() {
        return """
                . ⚬ ✧ ○
                ✧ Greetings mage, I am Aerith! ✧
                """;
    }

    /**
     * Returns the response for a given input.
     * @param parser The parser instance
     * @param input The user's input
     */
    public String getResponse(Parser parser, String input) {
        try {
            return parser.parse(input);
        } catch (AerithException e) {
            return e.getMessage();
        }
    }

    /**
     * Returns the list of tasks as a string.
     * @param taskList The task list
     */
    public String getListOfTasks(TaskList taskList) {
        if (taskList.getLength() == 0) {
            return "✧ There are no tasks in your list. ✧";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n✧ ✧ ✧\n");
        for (int i = 0; i < taskList.getLength(); i++) {
            sb.append((i + 1)).append(". ").append(taskList.getTask(i)).append("\n");
        }
        sb.append("✧ ✧ ✧");
        return sb.toString();
    }

    /**
     * Returns a confirmation message for a new todo.
     * @param todo The new todo
     */
    public String getNewTodoConfirmation(Todo todo) {
        return "✧ I have added a new todo: ✧\n" + todo;
    }

    /**
     * Returns a confirmation message for a new deadline task.
     * @param deadline The new deadline task
     */
    public String getNewDeadlineConfirmation(Deadline deadline) {
        return "✧ I have added a new task: ✧\n" + deadline;
    }

    /**
     * Returns a confirmation message for a new event.
     * @param event The new event
     */
    public String getNewEventConfirmation(Event event) {
        return "✧ I have added a new event: ✧\n" + event;
    }

    /**
     * Returns a confirmation message for marking a task.
     * @param taskNum The user-facing number for the task
     * @param task The marked task
     */
    public String getMarkedTaskConfirmation(int taskNum, Task task) {
        return "✧ I have marked this task as done: ✧\n" + taskNum + ". " + task;
    }

    /**
     * Returns a confirmation message for unmarking a task.
     * @param taskNum The user-facing number for the task
     * @param task The unmarked task
     */
    public String getUnmarkedTaskConfirmation(int taskNum, Task task) {
        return "✧ I have marked this task as not done yet: ✧\n" + taskNum + ". " + task;
    }

    /**
     * Returns a confirmation message for removing a task.
     * @param task The removed task
     */
    public String getRemovedTaskConfirmation(Task task) {
        return "✧ I have removed this task: ✧\n" + task;
    }

    /**
     * Returns the formatted results for a search.
     * @param relevantTasks The search results
     */
    public String getSearchResults(ArrayList<Task> relevantTasks) {
        StringBuilder sb = new StringBuilder("✧ ✧ ✧\n");
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < relevantTasks.size(); i++) {
            sb.append(i + 1).append(". ").append(relevantTasks.get(i)).append("\n");
        }
        sb.append("✧ ✧ ✧");
        return sb.toString();
    }

    /**
     * Returns a confirmation message for editing a task
     * @param task The edited task
     */
    public String getEditedTaskConfirmation(Task task) {
        return "✧ I have updated this task: ✧\n" + task;
    }
}
