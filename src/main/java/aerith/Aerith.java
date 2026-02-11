package aerith;

import aerith.exception.StorageException;

/**
 * The starting point of the program.
 */

public class Aerith {
    private static final String SAVE_FILE = "./data/save.txt";
    private TaskList taskList;
    private Storage storage;
    private Ui ui;
    private Parser parser;

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

    public static void main(String[] args) {
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return ui.getResponse(parser, input);
    }

    /** Returns the opening message. */
    public String getOpeningMessage() {
        return ui.getOpeningMessage();
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
