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


import Messages.PlayerInfo;
import Messages.RoomInfo;
import Network.NetworkManager;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Objects;

public class MainWindowController extends AbstractSceneController{

    private Button multiPlayerButton;
    private Button easyAIButton;
    private Button hardAIButton;

    public MainWindowController() {


            VBox vbox = new VBox(10);

            Label tic = new Label("TIC");
            Label tac = new Label("TAC");
            Label toe = new Label("TOE");

            vbox.getChildren().addAll(tic, tac, toe);

             multiPlayerButton = new Button("Multiplayer");
             easyAIButton = new Button("Against Computer - Easy");
             hardAIButton = new Button("Against Computer - Hard");

            vbox.getChildren().addAll(multiPlayerButton, easyAIButton, hardAIButton);




            vbox.setAlignment(Pos.CENTER);

            setRoot(vbox);

        }



    public Button getMultiPlayerButton() {
        return multiPlayerButton;
    }

    //region Object overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LobbySceneController that = (LobbySceneController) o;
        return Objects.equals(getMultiPlayerButton(), that.getNameField());

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMultiPlayerButton());
    }

    @Override
    public String toString() {
        return "LobbySceneController{" +
                "multiPlayerButton = " + multiPlayerButton +
                '}';
    }



}
