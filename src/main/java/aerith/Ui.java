package aerith;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

import aerith.exception.AerithException;
import aerith.task.Deadline;
import aerith.task.Event;
import aerith.task.Task;
import aerith.task.Todo;

/**
 * Deals with interactions with the user.
 */
public class Ui {
    public void showExceptionMessage(AerithException exception) {
        System.out.println(exception.getMessage() + "\n");
    }

    public void showLoadingError() {
        System.out.println("⚠ Something went wrong with the saved data. ⚠");
    }

    public void showSavingError() {
        System.out.println("⚠ Something went wrong while trying to create the save file. ⚠");
    }

    public String getOpeningMessage() {
        return """
                . ⚬ ✧ ○
                ✧ Greetings mage, I am Aerith! ✧
                """;
    }

    public String getResponse(Parser parser, String input) {
        try {
            return parser.parse(input);
        } catch (AerithException e) {
            return e.getMessage();
        }
    }

    public String getListOfTasks(TaskList taskList) {
        StringBuilder sb = new StringBuilder("✧ ✧ ✧\n");
        for (int i = 0; i < taskList.getLength(); i++) {
            sb.append((i + 1)).append(". ").append(taskList.getTask(i)).append("\n");
        }
        sb.append("✧ ✧ ✧");
        return sb.toString();
    }

    public String getNewTodoConfirmation(Todo todo) {
        return "✧ I have added a new todo: ✧\n" + todo;
    }

    public String getNewDeadlineConfirmation(Deadline deadline) {
        return "✧ I have added a new task: ✧\n" + deadline;
    }

    public String getNewEventConfirmation(Event event) {
        return "✧ I have added a new event: ✧\n" + event;
    }

    public String getMarkedTaskConfirmation(int taskNum, Task task) {
        return "✧ I have marked this task as done: ✧\n" + taskNum + ". " + task;
    }

    public String getUnmarkedTaskConfirmation(int taskNum, Task task) {
        return "✧ I have marked this task as not done yet: ✧\n" + taskNum + ". " + task;
    }

    public String getRemovedTaskConfirmation(Task task) {
        return "✧ I have removed this task: ✧\n" + task;
    }

    public String getSearchResults(ArrayList<Task> relevantTasks) {
        StringBuilder sb = new StringBuilder("✧ ✧ ✧\n");
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < relevantTasks.size(); i++) {
            sb.append(i + 1).append(". ").append(relevantTasks.get(i)).append("\n");
        }
        sb.append("✧ ✧ ✧");
        return sb.toString();
    }
}
