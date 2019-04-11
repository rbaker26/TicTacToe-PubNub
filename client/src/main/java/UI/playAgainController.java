package UI;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class playAgainController extends AbstractSceneController {

    Button yesButton = new Button();
    Button noButton = new Button();

    public playAgainController() {

        Label playAgainLabel = new Label("Play Game Again?");

        yesButton.setText("Yes");
        noButton.setText("No");

        //container for buttons
        VBox vbox = new VBox(playAgainLabel, yesButton, noButton);
        setRoot(vbox);

    }


}
