package aerith;

/**
 * The starting point of the program.
 */
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

    /**
     * Run the program.
     */
    private void run() {
        ui.showOpeningMessage();
        ui.readInput(parser);
        ui.showClosingMessage();
    }

    /**
     * For testing.
     * Returns the parser instance.
     * @return the parser tied to this instance of Aerith
     */
    Parser getParser() {
        return parser;
    }
}