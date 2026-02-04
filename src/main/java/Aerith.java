import java.util.ArrayList;

public class Aerith {
    private TaskList taskList;
    private final static String SAVE_FILE = "./data/save.txt";
    private Storage storage;
    private Ui ui;
    private Parser parser;

    public static void main(String[] args) {
        new Aerith().run();
    }

    public Aerith() {
        ui = new Ui();
        storage = new Storage();
        taskList = storage.getTaskList(SAVE_FILE, ui);
        parser = new Parser(taskList, ui);
    }

    private void run() {
        ui.showOpeningMessage();
        ui.readInput(parser);
        ui.showClosingMessage();
    }
}