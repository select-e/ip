import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    private TaskList tasks;
    private final static String SAVE_FILE = "./data/save.txt";

    public Parser(TaskList tasks) {
        this.tasks = tasks;
    }

    public void handleInput(String input) throws InvalidInputException {
        if (input.equals("list")) {
            // List items
            System.out.println("✧ ✧ ✧");
            for (int i = 0; i < tasks.getLength(); i++) {
                System.out.println((i + 1) + ". " + tasks.getTask(i));
            }
            System.out.println("✧ ✧ ✧\n");
        } else {
            String[] arr = input.split(" ", 2);
            int taskNum;
            String taskDesc;
            String[] parts;
            switch(arr[0]) {
                case "mark":
                    if (arr.length < 2) {
                        throw new InvalidInputException("Please provide the task number you want to mark as done.");
                    }
                    tasks.markTask(arr[1]);
                    break;
                case "unmark":
                    if (arr.length < 2) {
                        throw new InvalidInputException("Please provide the task number you want to mark as not done yet.");
                    }
                    tasks.unmarkTask(arr[1]);
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
                    String emptyMessage = "The description of an event cannot be empty.";
                    if (arr.length < 2 || arr[1].isBlank()) {
                        throw new InvalidInputException(emptyMessage);
                    }

                    // Add event
                    parts = arr[1].split("/");
                    taskDesc = parts[0].trim();
                    if (taskDesc.isBlank()) {
                        throw new InvalidInputException(emptyMessage);
                    }
                    Event event = getEvent(parts, taskDesc);
                    saveNewTask(event);
                    tasks.addTask(event);
                    System.out.println("✧ I have added a new event: ✧");
                    System.out.println(event + "\n");
                    break;
                case "delete":
                    if (arr.length < 2 || arr[1].isBlank()) {
                        throw new InvalidInputException("Please specify the task you want to remove.");
                    }
                    taskNum = Integer.parseInt(arr[1]);
                    tasks.removeTask(taskNum - 1);
                    break;
                default:
                    throw new InvalidInputException("My apologies, I do not understand what that means.");
            }
        }
    }

    /**
     * Adds a todo to the list and displays the result.
     * @param command The user-inputted description
     */
    private void addTodo(String command) {
        String taskDesc = command.trim();
        Todo todo = new Todo(taskDesc);
        saveNewTask(todo);
        tasks.addTask(todo);
        System.out.println("✧ I have added a new todo: ✧");
        System.out.println(todo + "\n");
    }

    /**
     * Adds a deadline task to the list and displays the result.
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
        saveNewTask(deadline);
        tasks.addTask(deadline);
        System.out.println("✧ I have added a new task: ✧");
        System.out.println(deadline + "\n");
    }
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
                dateString = cmd[1].trim() + " 12:00";
            }
            try {
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

    private void saveNewTask(Task task) {
        // Write a new task to the data file
        try {
            FileWriter fw = new FileWriter(SAVE_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(task.toSaveFormat());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Event getEvent(String[] parts, String taskDesc) throws InvalidInputException {
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

        return new Event(taskDesc, from, to);
    }
}
