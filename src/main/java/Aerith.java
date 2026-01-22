import java.util.Scanner;

public class Aerith {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n. ⚬ ✧ ○\n");
        System.out.println("✧ Greetings mage, I am Aerith! ✧\n");

        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            System.out.println("✧ " + input + " ✧\n");
            input = scanner.nextLine();
        }

        System.out.println("✧ I await your return. ✧");
        System.out.println("\n○ ✧ ⚬ .\n");
    }
}