package aerith;

import aerith.exception.InvalidInputException;
import aerith.task.Deadline;
import aerith.task.Event;
import aerith.task.Todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deals with making sense of the user's commands.
 */
public class Parser {
    private TaskList taskList;
    private final static String SAVE_FILE = "./data/save.txt";
    private Ui ui;

    public Parser(TaskList taskList, Ui ui) {
        this.taskList = taskList;
        this.ui = ui;
    }

    /**
     * Parse the user's input.
     * @param input The input string
     * @throws InvalidInputException
     */
    public void handleInput(String input) throws InvalidInputException {
        // Split the input into command & body
        String[] arr = input.split(" ", 2);

        switch(arr[0]) {
        case "list":
            // List the current tasks
            ui.listTasks(taskList);
            break;
        case "mark":
            if (arr.length < 2) {
                throw new InvalidInputException("Please provide the task number you want to mark as done.");
            }
            parseMarkCommand(arr[1]);
            break;
        case "unmark":
            if (arr.length < 2) {
                throw new InvalidInputException("Please provide the task number you want to mark as not done yet.");
            }
            parseUnmarkCommand(arr[1]);
            break;
        case "todo":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("The description of a todo cannot be empty.");
            }
            addTodo(arr[1]);
            break;
        case "deadline":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("The description of a deadline task cannot be empty.");
            }
            addDeadline(arr[1]);
            break;
        case "event":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("The description of an event cannot be empty.");
            }
            addEvent(arr[1]);
            break;
        case "delete":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("Please specify the task you want to remove.");
            }
            int taskNum = Integer.parseInt(arr[1]);
            taskList.removeTask(taskNum - 1);
            break;
        default:
            throw new InvalidInputException("My apologies, I do not understand what that means.");
        }
    }

    /**
     * Parses a command to mark a task as done.
     * @param commandBody The user-inputted body after the command "mark"
     * @throws InvalidInputException
     */
    private void parseMarkCommand(String commandBody) throws InvalidInputException {
        int taskNum = getTaskNum(commandBody);
        taskList.markTask(taskNum - 1);
    }

    /**
     * Parses a command to mark a task as not done yet.
     * @param commandBody The user-inputted body after the command "unmark"
     * @throws InvalidInputException
     */
    private void parseUnmarkCommand(String commandBody) throws InvalidInputException {
        int taskNum = getTaskNum(commandBody);
        taskList.unmarkTask(taskNum - 1);
    }

    /**
     * Returns a task number from a string input.
     * @param input The user-inputted string we want to validate
     * @return The task number
     * @throws InvalidInputException
     */
    private int getTaskNum(String input) throws InvalidInputException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Please specify tasks by their number.");
        }

        // Throw an exception if the task number is invalid
        if (taskNum <= 0 || taskNum > taskList.getLength()) {
            throw new InvalidInputException("Please enter a valid task number.");
        }

        return taskNum;
    }

    /**
     * Adds a todo to the task list.
     * @param taskDesc The user-inputted description
     */
    private void addTodo(String taskDesc) {
        Todo todo = new Todo(taskDesc.trim());
        taskList.addTask(todo);
        ui.displayNewTodo(todo);
    }

    /**
     * Adds a deadline task to the list.
     * @param command The user-inputted description and deadline
     * @throws InvalidInputException
     */
    private void addDeadline(String command) throws InvalidInputException {
        String[] parts = command.split("/");
        String taskDesc = parts[0].trim();

        // Handle the case where there is no description before the "/"
        if (taskDesc.isBlank()) {
            throw new InvalidInputException("The description of an event cannot be empty.");
        }

        Deadline deadline = getDeadline(parts, taskDesc);
        taskList.addTask(deadline);
        ui.displayNewDeadline(deadline);
    }

    /**
     * Parses the user input and returns a deadline task.
     * @param parts An array of string parts of the command body, after "deadline", previously separated by "/".
     * @param taskDesc The task description.
     * @return A new deadline task.
     * @throws InvalidInputException If deadline is missing or format is invalid.
     */
    private static Deadline getDeadline(String[] parts, String taskDesc) throws InvalidInputException {
        if (parts.length < 2) {
            throw new InvalidInputException("The task requires a deadline. Please specify it using the /by command.");
        }

        String[] cmd = parts[1].split(" ", 2);

        if (cmd.length != 2) {
            throw new InvalidInputException("The deadline of a deadline task cannot be empty.");
        }

        // Get date
        if (cmd[0].equals("by")) {
            DateTimeFormatter formatter;
            LocalDateTime date;
            boolean hasTime = false;
            String dateString = cmd[1];
            if (dateString.contains(" ")) {
                hasTime = true;
            } else {
                // Set the time to an arbitrary if time is not provided
                dateString = cmd[1].trim() + " 12:00";
            }
            try {
                // Parse the string into a LocalDateTime
                formatter = DateTimeFormatter.ofPattern("d-M-yyyy HH:mm");
                date = LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("Please enter a date in the format \"dd-MM-yyyy\" or \"dd-MM-yyyy HH:ss\".");
            }
            return new Deadline(taskDesc, date, hasTime);
        } else {
            throw new InvalidInputException("Please enter a deadline using the /by command.");
        }
    }

    /**
     * Adds an event to the task list.
     * @param command The user-inputted command body
     * @throws InvalidInputException
     */
    private void addEvent(String command) throws InvalidInputException {
        String[] parts = command.split("/");
        String taskDesc = parts[0].trim();
        if (taskDesc.isBlank()) {
            throw new InvalidInputException("The description of an event cannot be empty.");
        }
        Event event = getEvent(parts);
        taskList.addTask(event);
        ui.displayNewEvent(event);
    }

    /**
     * Parses the user's input and returns an event.
     * @param parts An array of string parts of the command body, after "event", previously separated by "/"
     * @return The new aerith.Event
     * @throws InvalidInputException
     */
    private static Event getEvent(String[] parts) throws InvalidInputException {
        // Get "from" and "to"
        String from = "";
        String to = "";
        for (int i = 1; i < parts.length; i++) {
            String[] cmd = parts[i].split(" ", 2);
            if (cmd[0].equals("from")) {
                from = cmd[1].trim();
            } else if (cmd[0].equals("to")) {
                to = cmd[1].trim();
            }
        }

        if (from.isBlank()) {
            throw new InvalidInputException("The event requires a starting date/time. Please specify it using the /from command.");
        }
        if (to.isBlank()) {
            throw new InvalidInputException("The event requires an ending date/time. Please specify it using the /to command.");
        }

        String taskDesc = parts[0].trim();
        return new Event(taskDesc, from, to);
    }
}
