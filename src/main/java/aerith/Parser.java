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
     * Parses the user's input.
     * @param input The input string
     * @throws InvalidInputException If information is missing from the command
     */
    public String parse(String input) throws InvalidInputException, StorageException {
        // Split the input into command & body
        String[] commandParts = input.split(" ", 2);
        boolean isSingleWordCommand = commandParts.length < 2;
        String commandHead = commandParts[0].toLowerCase();
        String commandBody = "";
        if (!isSingleWordCommand) {
            commandBody = commandParts[1];
        }

        switch(commandHead) {
        case "list":
            // List the current tasks
            return ui.getListOfTasks(taskList);
        case "mark":
            if (isSingleWordCommand) {
                throw new InvalidInputException("Please provide the task number you want to mark as done.");
            }
            return parseMarkCommand(commandBody);
        case "unmark":
            if (isSingleWordCommand) {
                throw new InvalidInputException("Please provide the task number you want to mark as not done yet.");
            }
            return parseUnmarkCommand(commandBody);
        case "todo":
            if (isSingleWordCommand || commandBody.isBlank()) {
                throw new InvalidInputException("The description of a todo cannot be empty.");
            }
            return addTodo(commandBody);
        case "deadline":
            if (isSingleWordCommand || commandBody.isBlank()) {
                throw new InvalidInputException("The description of a deadline task cannot be empty.");
            }
            return addDeadline(commandBody);
        case "event":
            if (isSingleWordCommand || commandBody.isBlank()) {
                throw new InvalidInputException("The description of an event cannot be empty.");
            }
            return addEvent(commandBody);
        case "delete":
            if (isSingleWordCommand || commandBody.isBlank()) {
                throw new InvalidInputException("Please specify the task you want to remove.");
            }
            int taskNum;
            try {
                taskNum = Integer.parseInt(commandBody);
            } catch(NumberFormatException e) {
                throw new InvalidInputException(commandBody + " is not a valid task number.");
            }
            return taskList.removeTask(taskNum - 1);
        case "find":
            if (isSingleWordCommand || commandBody.isBlank()) {
                throw new InvalidInputException("Please specify the keyword you want to search for.");
            }
            return searchForKeyword(commandBody);
        case "edit":
            if (isSingleWordCommand || commandBody.isBlank()) {
                throw new InvalidInputException("Please provide the task number of the task you want to edit.");
            }
            return editTask(commandBody);
        case "bye":
            return MainWindow.EXIT_COMMAND;
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
    private String addTodo(String taskDesc) throws StorageException, InvalidInputException {
        assert !taskDesc.isBlank() : "Task description should not be blank";

        if (taskDesc.contains("|")) {
            throw new InvalidInputException("Task descriptions cannot contain the character '|'.");
        }

        Todo todo = new Todo(taskDesc.trim());
        taskList.addTask(todo);
        return ui.getNewTodoConfirmation(todo);
    }

    /**
     * Adds a deadline task to the list.
     * @param commandBody The user-inputted description and deadline
     */
    private String addDeadline(String commandBody) throws InvalidInputException, StorageException {
        String[] parts = commandBody.split("/");
        String taskDesc = parts[0].trim();

        // Handle the case where there is no description before the "/"
        if (taskDesc.isBlank()) {
            throw new InvalidInputException("The description of an event cannot be empty.");
        }

        if (taskDesc.contains("|")) {
            throw new InvalidInputException("Task descriptions cannot contain the character '|'.");
        }

        Deadline deadline = getDeadlineFromCommand(parts);
        taskList.addTask(deadline);
        return ui.getNewDeadlineConfirmation(deadline);
    }

    /**
     * Parses the user input and returns a deadline task.
     * @param parts An array of string parts of the command body, after "deadline", previously separated by "/".
     * @return A new deadline task.
     * @throws InvalidInputException If deadline is missing or format is invalid.
     */
    private static Deadline getDeadlineFromCommand(String[] parts) throws InvalidInputException {
        if (parts.length < 2) {
            throw new InvalidInputException("The task requires a deadline. Please specify it using the /by command.");
        }

        String[] cmd = parts[1].split(" ", 2);

        if (cmd.length != 2) {
            throw new InvalidInputException("The deadline of a deadline task cannot be empty.");
        }

        // Get date
        if (cmd[0].equals("by")) {
            String dateString = cmd[1].trim();
            boolean hasTime = dateString.contains(" ");
            LocalDateTime deadline = getDateTimeFromString(dateString, hasTime);
            String taskDesc = parts[0].trim();
            return new Deadline(taskDesc, deadline, hasTime);
        } else {
            throw new InvalidInputException("Please enter a deadline using the /by command.");
        }
    }

    /**
     * Parses a string into a LocalDateTime
     * @param text The string.
     * @param hasTime Whether text contains time information.
     * @return The LocalDateTime.
     * @throws InvalidInputException If the string does not fit the pattern "d-M-yyyy HH:mm".
     */
    private static LocalDateTime getDateTimeFromString(String text, boolean hasTime) throws InvalidInputException {
        DateTimeFormatter formatter;
        LocalDateTime date;
        String dateString = text;
        if (!hasTime) {
            // Set the time to an arbitrary if time is not provided
            dateString = text + " 12:00";
        }
        try {
            // Parse the string into a LocalDateTime
            formatter = DateTimeFormatter.ofPattern("d-M-yyyy HH:mm");
            date = LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Please enter a date in the format "
                    + "\"dd-MM-yyyy\" or \"dd-MM-yyyy HH:ss\".");
        }
        return date;
    }

    /**
     * Adds an event to the task list.
     * @param commandBody The user-inputted command body
     */
    private String addEvent(String commandBody) throws InvalidInputException, StorageException {
        String[] parts = commandBody.split("/");
        String taskDesc = parts[0].trim();
        if (taskDesc.isBlank()) {
            throw new InvalidInputException("The description of an event cannot be empty.");
        }

        if (taskDesc.contains("|")) {
            throw new InvalidInputException("Task descriptions cannot contain the character '|'.");
        }

        Event event = getEventFromCommand(parts);
        taskList.addTask(event);
        return ui.getNewEventConfirmation(event);
    }

    /**
     * Parses the user's input and returns an event.
     * @param parts An array of string parts of the command body, after "event", previously separated by "/"
     * @return The new Event
     */
    private static Event getEventFromCommand(String[] parts) throws InvalidInputException {
        // Get "from" and "to"
        String fromString = "";
        String toString = "";
        for (int i = 1; i < parts.length; i++) {
            String[] cmd = parts[i].split(" ", 2);
            if (cmd[0].equals("from")) {
                fromString = cmd[1].trim();
            } else if (cmd[0].equals("to")) {
                toString = cmd[1].trim();
            }
        }

        if (fromString.isBlank()) {
            throw new InvalidInputException("The event requires a starting date/time. "
                    + "Please specify it using the /from command.");
        }
        if (toString.isBlank()) {
            throw new InvalidInputException("The event requires an ending date/time. "
                    + "Please specify it using the /to command.");
        }

        if (fromString.contains("|") || toString.contains("|")) {
            throw new InvalidInputException("Argument cannot contain the character '|'.");
        }

        String taskDesc = parts[0].trim();
        return new Event(taskDesc, fromString, toString);
    }

    /**
     * Searches for a keyword in task list.
     * @param keyword The keyword provided
     */
    private String searchForKeyword(String keyword) {
        assert !keyword.isBlank() : "Keyword should not be blank";

        ArrayList<Task> relevantTasks = taskList.getTasksWithKeyword(keyword.trim().toLowerCase());
        return ui.getSearchResults(relevantTasks);
    }

    /**
     * Parses an edit command and updates the task accordingly.
     * @param commandBody The user-inputted command body.
     * @return An edited task confirmation message.
     * @throws InvalidInputException If the task number is invalid or the flags do not match the task type.
     * @throws StorageException If there is an error while updating the stored tasks.
     */
    private String editTask(String commandBody) throws InvalidInputException, StorageException {
        String[] parts = commandBody.split("/");

        if (parts.length < 2) {
            throw new InvalidInputException("Please specify the details you would like to edit.");
        }

        int taskNum;
        try {
            taskNum = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Please specify the task you want to update by its task number.");
        }

        assert taskNum != 0 : "A valid task number should be more than zero.";
        Task task = taskList.getTask(taskNum - 1);

        for (int i = 1; i < parts.length; i++) {
            String[] cmdParts = parts[i].split(" ", 2);

            if (cmdParts.length < 2) {
                throw new InvalidInputException("Please provide the details you want to update.");
            }

            String flag = cmdParts[0];
            String argument = cmdParts[1].trim();

            if (argument.isEmpty()) {
                throw new InvalidInputException("Please provide an argument for the flag /" + flag + ".");
            }

            switch (flag) {
            case "desc":
                if (argument.contains("|")) {
                    throw new InvalidInputException("Task descriptions cannot contain the character '|'.");
                }
                task.setDescription(argument);
                break;
            case "by":
                if (!(task instanceof Deadline deadlineTask)) {
                    throw new InvalidInputException("I cannot change the /by field of this task, as it is not a deadline task.");
                }
                LocalDateTime deadline = getDateTimeFromString(argument, argument.contains(" "));
                deadlineTask.setDeadline(deadline);
                break;
            case "from":
                if (!(task instanceof Event event)) {
                    throw new InvalidInputException("I cannot change the /from field of this task, as it is not an event.");
                }
                if (argument.contains("|")) {
                    throw new InvalidInputException("Argument cannot contain the character '|'.");
                }
                event.setStartDateTime(argument);
                break;
            case "to":
                if (!(task instanceof Event event)) {
                    throw new InvalidInputException("I cannot change the /to field of this task, as it is not an event.");
                }
                if (argument.contains("|")) {
                    throw new InvalidInputException("Argument cannot contain the character '|'.");
                }
                event.setEndDateTime(argument);
                break;
            default:
                throw new InvalidInputException("My apologies, I do not understand the command \"/" + cmdParts[0] + "\".");
            }
        }

        taskList.updateTasks();
        return ui.getEditedTaskConfirmation(task);
    }
}
