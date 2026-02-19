package aerith;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    public final static String EXIT_COMMAND = "EXIT_APPLICATION";

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Aerith aerith;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/UserImage.png"));
    private Image aerithImage = new Image(this.getClass().getResourceAsStream("/images/AerithImage.png"));

    @FXML
    public void initialize() {
    }

    /** Injects the Aerith instance */
    public void setAerith(Aerith d) {
        aerith = d;

        dialogContainer.getChildren().add(DialogBox.getAerithDialog(aerith.getOpeningMessage(), aerithImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Aerith's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = aerith.getResponse(input);

        // If command is "bye", close the app
        if (response.equals(EXIT_COMMAND)) {
            Stage stage = (Stage) dialogContainer.getScene().getWindow();
            stage.close();
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getAerithDialog(response, aerithImage)
        );
        userInput.clear();
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
