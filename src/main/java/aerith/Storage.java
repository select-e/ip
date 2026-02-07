package aerith;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import aerith.exception.StorageException;
import aerith.task.Deadline;
import aerith.task.Event;
import aerith.task.Task;
import aerith.task.Todo;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {
    String filePath;

    /**
     * Loads the saved tasks.
     * @param filePath The path to the save file.
     * @param ui The aerith.Ui instance.
     * @return A TaskList containing the loaded tasks.
     */
    public TaskList getTaskList(String filePath, Ui ui) throws StorageException {
        this.filePath = filePath;
        TaskList taskList = new TaskList(this, ui);
        // Open the save file
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            try {

                String line = br.readLine();

                while (line != null) {
                    String[] taskInfo = line.split(" \\| ", 2);
                    Task task = switch (taskInfo[0]) {
                        case "T" -> Todo.fromSaveFormat(taskInfo[1]);
                        case "D" -> Deadline.fromSaveFormat(taskInfo[1]);
                        case "E" -> Event.fromSaveFormat(taskInfo[1]);
                        default -> null;
                    };
                    taskList.addTask(task);
                    line = br.readLine();
                }
                br.close();
                return taskList;
            } catch (IOException e) {
                ui.showLoadingError();
            }
        } catch (FileNotFoundException e) {
            // Create a new save file
            try {
                File file = new File(filePath);
                File parent = file.getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    ui.showSavingError();
                    return taskList;
                }

                boolean created = file.createNewFile();
                assert created; // created is true because we already know the file does not already exist
                return taskList;

            } catch (IOException ioException) {
                ui.showSavingError();
            }
        }
        return null;
    }

    /**
     * Updates the save file with the current list of tasks.
     * @param taskList The task list
     */
    public void updateTasks(TaskList taskList) throws StorageException
    {
        try {
            FileWriter fw = new FileWriter(filePath, false);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                for (int i = 0; i < taskList.getLength(); i++) {
                    bw.write(taskList.getTask(i).toSaveFormat());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new StorageException("Something went wrong while saving data. " + e.getMessage());
        }
    }

    /**
     * Writes a new task to the data file.
     * @param task The new task
     */
    public void saveNewTask(Task task) throws StorageException {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(task.toSaveFormat());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            throw new StorageException("Something went wrong while saving data. " + e.getMessage());
        }
    }
}
