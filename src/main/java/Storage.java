import java.io.*;

public class Storage {
    String filePath;

    public TaskList getTaskList(String filePath, Ui ui) {
        this.filePath = filePath;
        TaskList taskList = new TaskList(this);
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

    public void updateTasks(TaskList taskList)
    {
        // Update the data file
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
}
