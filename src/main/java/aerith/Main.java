package aerith;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Aerith aerith;
    private static MainWindow mainWindow;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            stage.setMinHeight(417);
            stage.setMinWidth(220);
            stage.setScene(scene);
            mainWindow = fxmlLoader.getController();
            aerith = new Aerith();
            mainWindow.setAerith(aerith);  // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a message from Aerith, for exceptional cases
     * @param message The error message
     */
    public static void displayErrorMessage(String message) {
        mainWindow.displayAerithMessage(message);
    }
}
