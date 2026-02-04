import java.util.Scanner;

public class Ui {
    public void showLoadingError() {
        System.out.println("⚠ Something went wrong with the saved data. ⚠");
    }

    public void showSavingError() {
        System.out.println("⚠ Something went wrong while trying to create the save file. ⚠");
    }

    public void showOpeningMessage() {
        System.out.println("\n. ⚬ ✧ ○\n");
        System.out.println("✧ Greetings mage, I am Aerith! ✧\n");
    }

    public void showClosingMessage() {
        System.out.println("✧ I await your return. ✧");
        System.out.println("\n○ ✧ ⚬ .");
    }

    public void readInput(Parser parser) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            try {
                parser.handleInput(input);
            } catch (InvalidInputException e) {
                System.out.println("⚠ " + e.getMessage() + " ⚠\n");
            }
            input = scanner.nextLine();
        }
    }
}
