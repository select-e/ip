package aerith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import aerith.exception.InvalidInputException;
import aerith.exception.StorageException;
import aerith.task.Deadline;
import aerith.task.Event;
import aerith.task.Task;
import aerith.task.Todo;

/**
 * Deals with making sense of the user's commands.
 */
public class Parser {
    private final TaskList taskList;
    private final Ui ui;

    public Parser(TaskList taskList, Ui ui) {
        this.taskList = taskList;
        this.ui = ui;
    }

    /**
     * Parse the user's input.
     * @param input The input string
     * @throws InvalidInputException If information is missing from the command
     */
    public String parse(String input) throws InvalidInputException, StorageException {
        // Split the input into command & body
        String[] arr = input.split(" ", 2);

        switch(arr[0]) {
        case "list":
            // List the current tasks
            return ui.getListOfTasks(taskList);
        case "mark":
            if (arr.length < 2) {
                throw new InvalidInputException("Please provide the task number you want to mark as done.");
            }
            return parseMarkCommand(arr[1]);
        case "unmark":
            if (arr.length < 2) {
                throw new InvalidInputException("Please provide the task number you want to mark as not done yet.");
            }
            return parseUnmarkCommand(arr[1]);
        case "todo":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("The description of a todo cannot be empty.");
            }
            return addTodo(arr[1]);
                case "deadline":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("The description of a deadline task cannot be empty.");
            }
            return addDeadline(arr[1]);
        case "event":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("The description of an event cannot be empty.");
            }
            return addEvent(arr[1]);
        case "delete":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("Please specify the task you want to remove.");
            }
            int taskNum = Integer.parseInt(arr[1]);
            return taskList.removeTask(taskNum - 1);
        case "find":
            if (arr.length < 2 || arr[1].isBlank()) {
                throw new InvalidInputException("Please specify the keyword you want to search for.");
            }
            return searchForKeyword(arr[1]);
        case "bye":
            return "EXIT_APPLICATION";
        default:
            throw new InvalidInputException("My apologies, I do not understand what that means.");
        }
    }

    /**
     * Parses a command to mark a task as done.
     * @param commandBody The user-inputted body after the command "mark"
     */
    private String parseMarkCommand(String commandBody) throws InvalidInputException, StorageException {
        int taskNum = getTaskNum(commandBody);
        return taskList.markTask(taskNum - 1);
    }

    /**
     * Parses a command to mark a task as not done yet.
     * @param commandBody The user-inputted body after the command "unmark"
     */
    private String parseUnmarkCommand(String commandBody) throws InvalidInputException, StorageException {
        int taskNum = getTaskNum(commandBody);
        return taskList.unmarkTask(taskNum - 1);
    }

    /**
     * Returns a task number from a string input.
     * @param input The user-inputted string we want to validate
     * @return The task number
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
    private String addTodo(String taskDesc) throws StorageException {
        assert !taskDesc.isBlank() : "Task description should not be blank";
        
        Todo todo = new Todo(taskDesc.trim());
        taskList.addTask(todo);
        return ui.getNewTodoConfirmation(todo);
    }

    /**
     * Adds a deadline task to the list.
     * @param command The user-inputted description and deadline
     */
    private String addDeadline(String command) throws InvalidInputException, StorageException {
        String[] parts = command.split("/");
        String taskDesc = parts[0].trim();

        // Handle the case where there is no description before the "/"
        if (taskDesc.isBlank()) {
            throw new InvalidInputException("The description of an event cannot be empty.");
        }

        Deadline deadline = getDeadline(parts, taskDesc);
        taskList.addTask(deadline);
        return ui.getNewDeadlineConfirmation(deadline);
    }

    /**
     * Parses the user input and returns a deadline task.
     * @param parts An array of string parts of the command body, after "deadline", previously separated by "/".
     * @param taskDesc The task description.
     * @return A new deadline task.
     * @throws InvalidInputException If deadline is missing or format is invalid.
     */
    private static Deadline getDeadline(String[] parts, String taskDesc) throws InvalidInputException {
        assert !taskDesc.isBlank() : "Task description should not be blank";

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
                throw new InvalidInputException("Please enter a date in the format "
                        + "\"dd-MM-yyyy\" or \"dd-MM-yyyy HH:ss\".");
            }
            return new Deadline(taskDesc, date, hasTime);
        } else {
            throw new InvalidInputException("Please enter a deadline using the /by command.");
        }
    }

    /**
     * Adds an event to the task list.
     * @param command The user-inputted command body
     */
    private String addEvent(String command) throws InvalidInputException, StorageException {
        String[] parts = command.split("/");
        String taskDesc = parts[0].trim();
        if (taskDesc.isBlank()) {
            throw new InvalidInputException("The description of an event cannot be empty.");
        }
        Event event = getEvent(parts);
        taskList.addTask(event);
        return ui.getNewEventConfirmation(event);
    }

    /**
     * Parses the user's input and returns an event.
     * @param parts An array of string parts of the command body, after "event", previously separated by "/"
     * @return The new aerith.Event
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
            throw new InvalidInputException("The event requires a starting date/time. "
                    + "Please specify it using the /from command.");
        }
        if (to.isBlank()) {
            throw new InvalidInputException("The event requires an ending date/time. "
                    + "Please specify it using the /to command.");
        }

        String taskDesc = parts[0].trim();
        return new Event(taskDesc, from, to);
    }

    private String searchForKeyword(String keyword) {
        assert !keyword.isBlank() : "Keyword should not be blank";

        ArrayList<Task> relevantTasks = taskList.getTasksWithKeyword(keyword);
        return ui.getSearchResults(relevantTasks);
    }
}
