import java.io.*;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {
    String filePath;

    /**
     * Loads the saved tasks.
     * @param filePath The path to the save file
     * @param ui The Ui instance
     * @return A TaskList containing the loaded tasks
     */
    public TaskList getTaskList(String filePath, Ui ui) {
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
                File saveFile = new File(filePath);
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
                return taskList;
            } catch (IOException ioException) {
                ui.showSavingError();
                ioException.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Updates the save file with the current list of tasks.
     * @param taskList The task list
     */
    public void updateTasks(TaskList taskList)
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
            e.printStackTrace();
        }
    }

    /**
     * Writes a new task to the data file.
     * @param task The new task
     */
    public void saveNewTask(Task task) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(task.toSaveFormat());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
