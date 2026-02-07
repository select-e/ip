package aerith;

import aerith.exception.StorageException;

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
        try {
            taskList = storage.getTaskList(SAVE_FILE, ui);
        } catch (StorageException e) {
            ui.showExceptionMessage(e);
        }
        parser = new Parser(taskList, ui);
    }

    private void run() {
        ui.showOpeningMessage();
        ui.readInput(parser);
        ui.showClosingMessage();
    }

    // Visible for testing
    Parser getParser() {
        return parser;
    }
}