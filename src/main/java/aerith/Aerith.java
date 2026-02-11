package aerith;

import aerith.exception.StorageException;

/**
 * The starting point of the program.
 */

public class Aerith {
    private static final String SAVE_FILE = "./data/save.txt";
    private TaskList taskList;
    private final Ui ui;
    private final Parser parser;

    public Aerith() {
        ui = new Ui();
        Storage storage = new Storage(SAVE_FILE);
        try {
            taskList = storage.getTaskList(ui);
        } catch (StorageException e) {
            System.out.println(e.getMessage() + "\n");
        }
        parser = new Parser(taskList, ui);
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
