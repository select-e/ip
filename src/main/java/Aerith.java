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
    private Ui ui;
    private Parser parser;

    public static void main(String[] args) {
        new Aerith().run();
    }

    public Aerith() {
        ui = new Ui();
        tasks = loadTasks();
        parser = new Parser(tasks);
    }

    private void run() {
        ui.showOpeningMessage();
        ui.readInput(parser);
        ui.showClosingMessage();
    }

    private ArrayList<Task> loadTasks() {
        // Open the save file
        try {
            BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE));
            try {
                ArrayList<Task> tasks = new ArrayList<>(100);
                String line = br.readLine();

                while (line != null) {
                    String[] taskInfo = line.split(" \\| ", 2);
                    Task task = switch (taskInfo[0]) {
                    case "T" -> Todo.fromSaveFormat(taskInfo[1]);
                    case "D" -> Deadline.fromSaveFormat(taskInfo[1]);
                    case "E" -> Event.fromSaveFormat(taskInfo[1]);
                    default -> null;
                    };
                    tasks.add(task);
                    line = br.readLine();
                }
                br.close();
                return tasks;
            } catch (IOException e) {
                ui.showLoadingError();
            }
        } catch (FileNotFoundException e) {
            // Create a new save file
            try {
                File saveFile = new File(SAVE_FILE);
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
                return new ArrayList<>(100);
            } catch (IOException ioException) {
                ui.showSavingError();
                ioException.printStackTrace();
            }
        }
        return null;
    }
}