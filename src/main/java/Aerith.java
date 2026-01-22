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
            aerith.handleInput(input);
            input = scanner.nextLine();
        }

        System.out.println("✧ I await your return. ✧");
        System.out.println("\n○ ✧ ⚬ .");
    }

    private void handleInput(String input) {
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
                    taskNum = Integer.parseInt(arr[1]);
                    tasks[taskNum - 1].markDone(true);
                    System.out.println("✧ I have marked this task as done: ✧");
                    System.out.println(taskNum + ". " + tasks[taskNum - 1] + "\n");
                    break;
                case "unmark":
                    taskNum = Integer.parseInt(arr[1]);
                    tasks[taskNum - 1].markDone(false);
                    System.out.println("✧ I have marked this task as not done yet: ✧");
                    System.out.println(taskNum + ". " + tasks[taskNum - 1] + "\n");
                    break;
                case "todo":
                    // Add to do
                    taskDesc = arr[1];
                    Todo todo = new Todo(taskDesc);
                    tasks[taskCount] = todo;
                    taskCount++;
                    System.out.println("✧ I have added a new todo: ✧");
                    System.out.println(todo + "\n");
                    break;
                case "deadline":
                    // Add deadline task
                    parts = arr[1].split("/");
                    taskDesc = parts[0].trim();
                    Deadline deadline = getDeadline(parts, taskDesc);
                    tasks[taskCount] = deadline;
                    taskCount++;
                    System.out.println("✧ I have added a new task: ✧");
                    System.out.println(deadline + "\n");
                    break;
                case "event":
                    // Add event
                    parts = arr[1].split("/");
                    taskDesc = parts[0].trim();
                    Event event = getEvent(parts, taskDesc);
                    tasks[taskCount] = event;
                    taskCount++;
                    System.out.println("✧ I have added a new event: ✧");
                    System.out.println(event + "\n");
                    break;
                default:
                    // Add item
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                    System.out.println("✧ Added: " + input + " ✧\n");
                    break;
            }
        }
    }

    private static Deadline getDeadline(String[] parts, String taskDesc) {
        String[] cmd = parts[1].split(" ", 2);
        // Get date
        String date = "";
        if (cmd[0].equals("by")) {
            date = cmd[1].trim();
        }
        return new Deadline(taskDesc, date);
    }

    private static Event getEvent(String[] parts, String taskDesc) {
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
        return new Event(taskDesc, from, to);
    }
}