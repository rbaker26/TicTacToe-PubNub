package UI;

// Create a VBox, add spaces
// between its elements, set an alignment
// and add it to the stage
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.geometry.Pos;

public class mainWindowController extends AbstractSceneController{

    public mainWindowController() {

        try {

            VBox vbox = new VBox(10);

            Label tic = new Label("TIC");
            Label tac = new Label("TAC");
            Label toe = new Label("TOE");

            vbox.getChildren().addAll(tic, tac, toe);

            Button multiPlayerButton = new Button("Multiplayer");
            Button easyAIButton = new Button("Against Computer - Easy");
            Button hardAIButton = new Button("Against Computer - Hard");

            vbox.getChildren().addAll(multiPlayerButton, easyAIButton, hardAIButton);

            vbox.setAlignment(Pos.CENTER);

            setRoot(vbox);

        }

        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


}
