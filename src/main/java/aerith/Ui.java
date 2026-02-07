package aerith;

import aerith.exception.InvalidInputException;
import aerith.task.Deadline;
import aerith.task.Event;
import aerith.task.Task;
import aerith.task.Todo;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with interactions with the user.
 */
public class Ui {
    public void showLoadingError() {
        System.out.println("⚠ Something went wrong with the saved data. ⚠");
    }

    public void showSavingError() {
        System.out.println("⚠ Something went wrong while trying to create the save file. ⚠");
    }

    public void showOpeningMessage() {
        System.out.println("\n. ⚬ ✧ ○\n");
        System.out.println("✧ Greetings mage, I am Aerith! ✧\n");
    }

    public void showClosingMessage() {
        System.out.println("✧ I await your return. ✧");
        System.out.println("\n○ ✧ ⚬ .");
    }

    public void readInput(Parser parser) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            try {
                parser.handleInput(input);
            } catch (InvalidInputException e) {
                System.out.println("⚠ " + e.getMessage() + " ⚠\n");
            }
            input = scanner.nextLine();
        }
    }

    public void listTasks(TaskList taskList) {
        System.out.println("✧ ✧ ✧");
        for (int i = 0; i < taskList.getLength(); i++) {
            System.out.println((i + 1) + ". " + taskList.getTask(i));
        }
        System.out.println("✧ ✧ ✧\n");
    }

    public void displayNewTodo(Todo todo) {
        System.out.println("✧ I have added a new todo: ✧");
        System.out.println(todo + "\n");
    }

    public void displayNewDeadline(Deadline deadline) {
        System.out.println("✧ I have added a new task: ✧");
        System.out.println(deadline + "\n");
    }

    public void displayNewEvent(Event event) {
        System.out.println("✧ I have added a new event: ✧");
        System.out.println(event + "\n");
    }

    public void displayMarkedTask(int taskNum, Task task) {
        System.out.println("✧ I have marked this task as done: ✧");
        System.out.println(taskNum + ". " + task + "\n");
    }

    public void displayUnmarkedTask(int taskNum, Task task) {
        System.out.println("✧ I have marked this task as not done yet: ✧");
        System.out.println(taskNum + ". " + task + "\n");
    }

    public void displayRemovedTask(Task task) {
        System.out.println("✧ I have removed this task: ✧");
        System.out.println(task + "\n");
    }

    public void listSearchResults(ArrayList<Task> relevantTasks) {
        System.out.println("✧ ✧ ✧");
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < relevantTasks.size(); i++) {
            System.out.println((i + 1) + ". " + relevantTasks.get(i));
        }
        System.out.println("✧ ✧ ✧\n");
    }
}
