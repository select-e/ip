import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Aerith {
    private ArrayList<Task> tasks;
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
        tasks = storage.getTaskList(SAVE_FILE, ui);
        parser = new Parser(tasks);
    }

    private void run() {
        ui.showOpeningMessage();
        ui.readInput(parser);
        ui.showClosingMessage();
    }
}