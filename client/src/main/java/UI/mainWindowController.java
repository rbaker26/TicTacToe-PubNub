package UI;

// Create a VBox, add spaces
// between its elements, set an alignment
// and add it to the stage
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
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

public class mainWindowController extends AbstractSceneController{

    private Button multiPlayerButton;
    private Button easyAIButton;
    private Button hardAIButton;
    private Button gameHistoryButton;
    private TextFlow textTitle;

    public mainWindowController() {



        VBox vbox = new VBox(22);

        Label title = new Label("TIC TAC TOE");


        multiPlayerButton = new Button("Multiplayer");
        easyAIButton = new Button("Against Computer - Easy");
        hardAIButton = new Button("Against Computer - Hard");
        gameHistoryButton = new Button("Game History");


        title.setStyle("-fx-font: normal bold 20px 'impact'; -fx-text-fill: #3EC5F3 ");

        vbox.getChildren().addAll(title, multiPlayerButton, easyAIButton, hardAIButton, gameHistoryButton);


        vbox.setAlignment(Pos.CENTER);



        setRoot(vbox);

    }



    public Button getMultiPlayerButton() {
        return multiPlayerButton;
    }
    public Button getEasyAIButton() { return easyAIButton; }
    public Button getHardAIButton() { return hardAIButton; }
    public Button getGameHistoryButton() { return gameHistoryButton; }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMultiPlayerButton(), getEasyAIButton(), getHardAIButton(), getGameHistoryButton());
    }

    @Override
    public String toString() {
        return "mainWindowController {" +
                "multiPlayerButton = " + multiPlayerButton +
                "easyAIButton = " + easyAIButton +
                "hardAIButton = " + hardAIButton +
                "gameHistoryButton = " + gameHistoryButton +
                '}';
    }



}