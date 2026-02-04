import java.io.*;
import java.util.ArrayList;

public class Storage {
    public ArrayList<Task> getTaskList(String filePath, Ui ui) {
        // Open the save file
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
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
                File saveFile = new File(filePath);
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
