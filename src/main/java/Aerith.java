import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Aerith {
    private ArrayList<Task> tasks;
    private final static String SAVE_FILE = "./data/save.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Aerith aerith = new Aerith();
        aerith.tasks = loadTasks();

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

    private static ArrayList<Task> loadTasks() {
        // Open the save file
        try {
            BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE));
            try {
                ArrayList<Task> tasks = new ArrayList<>(100);
                String line = br.readLine();

                while (line != null) {
                    String[] taskInfo = line.split(" \\| ");
                    switch (taskInfo[0]) {
                    case "T":
                        Task todo = new Todo(taskInfo[2]);
                        todo.markDone(taskInfo[1].equals("1"));
                        tasks.add(todo);
                        break;
                    case "D":
                        Task deadline = new Deadline(taskInfo[2], taskInfo[3]);
                        deadline.markDone(taskInfo[1].equals("1"));
                        tasks.add(deadline);
                        break;
                    case "E":
                        Task event = new Event(taskInfo[2], taskInfo[3], taskInfo[4]);
                        event.markDone(taskInfo[1].equals("1"));
                        tasks.add(event);
                        break;
                    }
                    line = br.readLine();
                }
                br.close();
                return tasks;
            } catch (IOException e) {
                System.out.println("⚠ Something went wrong with the saved data. ⚠");
            }
        } catch (FileNotFoundException e) {
            // Create a new save file
            try {
                File saveFile = new File(SAVE_FILE);
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
                return new ArrayList<>(100);
            } catch (IOException ioException) {
                System.out.println("⚠ Something went wrong while trying to create the file. ⚠");
                ioException.printStackTrace();
            }
        }
        return null;
    }

    private void handleInput(String input) throws InvalidInputException {
        if (input.equals("list")) {
            // List items
            System.out.println("✧ ✧ ✧");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
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
                    if (taskNum <= 0 || taskNum > tasks.size()) {
                        throw new InvalidInputException("Please enter a valid task number.");
                    }
                    tasks.get(taskNum - 1).markDone(true);
                    updateTasks();
                    System.out.println("✧ I have marked this task as done: ✧");
                    System.out.println(taskNum + ". " + tasks.get(taskNum - 1) + "\n");
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
                    if (taskNum <= 0 || taskNum > tasks.size()) {
                        throw new InvalidInputException("Please enter a valid task number.");
                    }
                    tasks.get(taskNum - 1).markDone(false);
                    updateTasks();
                    System.out.println("✧ I have marked this task as not done yet: ✧");
                    System.out.println(taskNum + ". " + tasks.get(taskNum - 1) + "\n");
                    break;
                case "todo":
                    if (arr.length < 2 || arr[1].isBlank()) {
                        throw new InvalidInputException("The description of a todo cannot be empty.");
                    }

                    // Add to do
                    taskDesc = arr[1].trim();
                    Todo todo = new Todo(taskDesc);
                    saveNewTask(todo);
                    tasks.add(todo);
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
                    saveNewTask(deadline);
                    tasks.add(deadline);
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
                    saveNewTask(event);
                    tasks.add(event);
                    System.out.println("✧ I have added a new event: ✧");
                    System.out.println(event + "\n");
                    break;
                case "delete":
                    if (arr.length < 2 || arr[1].isBlank()) {
                        throw new InvalidInputException("Please specify the task you want to remove.");
                    }
                    taskNum = Integer.parseInt(arr[1]);
                    Task task = tasks.get(taskNum - 1);
                    tasks.remove(task);
                    System.out.println("✧ I have removed this task: ✧");
                    System.out.println(task + "\n");
                    break;
                default:
                    throw new InvalidInputException("My apologies, I do not understand what that means.");
            }
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

    private void updateTasks()
    {
        // Update the data file
        try {
            FileWriter fw = new FileWriter(SAVE_FILE, false);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                for (Task task : tasks) {
                    bw.write(task.toSaveFormat());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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