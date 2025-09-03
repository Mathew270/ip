package Butler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Butler butler;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image butlerImage = new Image(this.getClass().getResourceAsStream("/images/Butler.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Butler instance */
    public void setButler(Butler b) {
        butler = b;
        showWelcome(); // Show welcome when Duke is injected
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Butler's reply.
     * Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = butler.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getButlerDialog(response, butlerImage)
        );
        userInput.clear();
    }

    /**
     * Displays the welcome message shown when the chatbot starts.
     */
    public void showWelcome() {
        dialogContainer.getChildren().add(
                DialogBox.getButlerDialog("Hello! I'm Butler\nWhat can I do for you?", butlerImage)
        );
    }

    /**
     * Displays one or more lines of output in the GUI.
     *
     * @param lines the lines of text to display
     */
    public void showMessage(String... lines) {
        StringBuilder sb = new StringBuilder();
        for (String s : lines) {
            sb.append(s).append("\n");
        }
        dialogContainer.getChildren().add(
                DialogBox.getButlerDialog(sb.toString().trim(), butlerImage)
        );
    }

    /**
     * Displays an error message in the GUI.
     *
     * @param msg the error message to display
     */
    public void showError(String msg) {
        dialogContainer.getChildren().add(
                DialogBox.getButlerDialog("⚠ " + msg, butlerImage)
        );
    }

    /**
     * Displays a confirmation message when a task is added,
     * along with the total number of tasks now in the list.
     *
     * @param t     the task that was added
     * @param total the total number of tasks in the list after addition
     */
    public void printAdded(Task t, int total) {
        String message = "Got it. I've added this task:\n   " + t
                + "\nNow you have " + total + " tasks in the list.";
        dialogContainer.getChildren().add(
                DialogBox.getButlerDialog(message, butlerImage)
        );
    }

    /**
     * Displays the entire task list in the GUI.
     */
    public void showTaskList(String listText) {
        dialogContainer.getChildren().add(
                DialogBox.getButlerDialog(listText, butlerImage)
        );
    }

    /**
     * Displays search results in the GUI.
     */
    public void showTaskSearch(String resultsText) {
        dialogContainer.getChildren().add(
                DialogBox.getButlerDialog(resultsText, butlerImage)
        );
    }
}
