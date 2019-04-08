package UI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WaitingForOpponentScene extends AbstractSceneController {

    Button cancelButton;

    public WaitingForOpponentScene() {
        Label waitingLabel = new Label("Waiting for an opponent...");
        Label waitingLabel2 = new Label("Patience");

        VBox box = new VBox(waitingLabel, waitingLabel2);
        box.setAlignment(Pos.CENTER);

        //setMasterScene(new Scene(box));
        setRoot(box);
    }
}
