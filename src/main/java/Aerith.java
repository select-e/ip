import java.util.Scanner;

public class Aerith {
    private Task[] tasks = new Task[100];
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
            int taskNum = -1;
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
                default:
                    // Add item
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                    System.out.println("✧ Added: " + input + " ✧\n");
                    break;
            }
        }
    }
}