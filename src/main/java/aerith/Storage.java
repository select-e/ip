package aerith;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;

import aerith.exception.StorageException;
import aerith.task.Deadline;
import aerith.task.Event;
import aerith.task.Task;
import aerith.task.Todo;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the saved tasks.
     * @param ui The aerith.Ui instance.
     * @return A TaskList containing the loaded tasks.
     */
    public TaskList getTaskList(Ui ui) throws StorageException {
        TaskList taskList = new TaskList(this, ui);

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            try {
                getTasksFromFile(taskList, br);
            } catch (IOException | DateTimeParseException e) {
                throw new StorageException("There was an error loading the saved data: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            createNewFile();
        }
        return taskList;
    }

    /**
     * Reads data from a buffered reader and inserts tasks into a task list.
     * @param taskList The task list to be populated
     * @param br The buffered reader of the data file
     * @throws IOException If I/O errors occur
     * @throws StorageException If an error occurs when adding tasks
     */
    private void getTasksFromFile(TaskList taskList, BufferedReader br) throws IOException, StorageException {
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
    }

    /**
     * Creates a new data file.
     * @throws StorageException If an I/O exception occurs
     */
    private void createNewFile() throws StorageException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new StorageException("Something went wrong while trying to create the parent folder of the data file.");
        }

        try {
            boolean created = file.createNewFile();
            assert created; // created is true because we already know the file does not already exist
        } catch (IOException e) {
            throw new StorageException("Something went wrong while trying to create the data file.");
        }
    }

    /**
     * Updates the save file with the current list of tasks.
     * @param taskList The task list
     */
    public void updateTasks(TaskList taskList) throws StorageException {
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
}
