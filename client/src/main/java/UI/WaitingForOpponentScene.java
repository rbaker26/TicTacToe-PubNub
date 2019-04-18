package UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


/**
 * A scene meant for waiting for another player.
 */
public class WaitingForOpponentScene extends AbstractSceneController {

    private Button cancelButton;

    public WaitingForOpponentScene() {
        Label waitingLabel = new Label("Waiting for an opponent...");
        Label waitingLabel2 = new Label("Patience");
        cancelButton = new Button("Cancel");

        VBox box = new VBox(waitingLabel, waitingLabel2, cancelButton);
        box.setAlignment(Pos.CENTER);

        //setMasterScene(new Scene(box));
        setRoot(box);
    }

    /**
     * Set the handler for the cancel button. There can only be one.
     * @param handler What will get called. Can be null, which clears old one.
     */
    public void setOnCancel(EventHandler<ActionEvent> handler) {
        cancelButton.setOnAction(handler);
    }
}
