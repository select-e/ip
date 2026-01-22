import java.util.Scanner;

public class Aerith {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] items = new String[100];
        int itemCount = 0;

        System.out.println("\n. ⚬ ✧ ○\n");
        System.out.println("✧ Greetings mage, I am Aerith! ✧\n");

        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            if (input.equals("list")) {
                // List items
                System.out.println("✧ ✧ ✧");
                for (int i = 0; i < itemCount; i++) {
                    System.out.println((i+1) + ". " + items[i]);
                }
                System.out.println("✧ ✧ ✧\n");
            } else {
                // Add item
                System.out.println("✧ Added: " + input + " ✧\n");
                items[itemCount] = input;
                itemCount++;
            }
            input = scanner.nextLine();
        }

        System.out.println("✧ I await your return. ✧");
        System.out.println("\n○ ✧ ⚬ .\n");
    }
}