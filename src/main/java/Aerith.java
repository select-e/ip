import java.util.Scanner;

public class Aerith {
    private final Task[] tasks = new Task[100];
    private int taskCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Aerith aerith = new Aerith();

        System.out.println("\n. ⚬ ✧ ○\n");
        System.out.println("✧ Greetings mage, I am Aerith! ✧\n");

        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            try {
                aerith.handleInput(input);
            } catch (InvalidInputException e) {
                System.out.println("⚠ " + e.getMessage() + " ⚠\n");
            }
            input = scanner.nextLine();
        }

        System.out.println("✧ I await your return. ✧");
        System.out.println("\n○ ✧ ⚬ .");
    }

    private void handleInput(String input) throws InvalidInputException {
        if (input.equals("list")) {
            // List items
            System.out.println("✧ ✧ ✧");
            for (int i = 0; i < taskCount; i++) {
                System.out.println((i + 1) + ". " + tasks[i]);
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
                    try {
                        taskNum = Integer.parseInt(arr[1]);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputException("Please specify tasks by their number.");
                    }
                    if (taskNum <= 0 || taskNum > taskCount) {
                        throw new InvalidInputException("Please enter a valid task number.");
                    }
                    tasks[taskNum - 1].markDone(true);
                    System.out.println("✧ I have marked this task as done: ✧");
                    System.out.println(taskNum + ". " + tasks[taskNum - 1] + "\n");
                    break;
                case "unmark":
                    if (arr.length < 2) {
                        throw new InvalidInputException("Please provide the task number you want to mark as not done yet.");
                    }
                    try {
                        taskNum = Integer.parseInt(arr[1]);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputException("Please specify tasks by their number.");
                    }
                    if (taskNum <= 0 || taskNum > taskCount) {
                        throw new InvalidInputException("Please enter a valid task number.");
                    }
                    tasks[taskNum - 1].markDone(false);
                    System.out.println("✧ I have marked this task as not done yet: ✧");
                    System.out.println(taskNum + ". " + tasks[taskNum - 1] + "\n");
                    break;
                case "todo":
                    if (arr.length < 2 || arr[1].isBlank()) {
                        throw new InvalidInputException("The description of a todo cannot be empty.");
                    }

                    // Add to do
                    taskDesc = arr[1].trim();
                    Todo todo = new Todo(taskDesc);
                    tasks[taskCount] = todo;
                    taskCount++;
                    System.out.println("✧ I have added a new todo: ✧");
                    System.out.println(todo + "\n");
                    break;
                case "deadline":
                    String emptyDescMessage = "The description of a deadline task cannot be empty.";

                    if (arr.length < 2 || arr[1].isBlank()) {
                        throw new InvalidInputException(emptyDescMessage);
                    }

                    // Add deadline task
                    parts = arr[1].split("/");
                    taskDesc = parts[0].trim();

                    if (taskDesc.isBlank()) {
                        throw new InvalidInputException(emptyDescMessage);
                    }

                    Deadline deadline = getDeadline(parts, taskDesc);
                    tasks[taskCount] = deadline;
                    taskCount++;
                    System.out.println("✧ I have added a new task: ✧");
                    System.out.println(deadline + "\n");
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
                    tasks[taskCount] = event;
                    taskCount++;
                    System.out.println("✧ I have added a new event: ✧");
                    System.out.println(event + "\n");
                    break;
                default:
                    throw new InvalidInputException("My apologies, I do not understand what that means.");
            }
        }
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
        String date = "";
        if (cmd[0].equals("by")) {
            date = cmd[1].trim();
        }
        return new Deadline(taskDesc, date);
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