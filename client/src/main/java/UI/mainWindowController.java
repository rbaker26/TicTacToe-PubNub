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

public class mainWindowController extends AbstractSceneController{

    private Button multiPlayerButton;
    private Button easyAIButton;
    private Button hardAIButton;
    private Button gameHistoryButton;
    private Button logoutButton;

    public mainWindowController() {


        VBox vbox = new VBox(30);

        Label gameTitle = new Label("TIC    TAC    TOE");

        vbox.getChildren().addAll(gameTitle);

        multiPlayerButton = new Button("Multiplayer");
        easyAIButton = new Button("Against Computer - Easy");
        hardAIButton = new Button("Against Computer - Hard");
        gameHistoryButton = new Button("Game History");
        logoutButton = new Button("Logout");

        vbox.getChildren().addAll(multiPlayerButton, easyAIButton, hardAIButton, gameHistoryButton, logoutButton);


        vbox.setAlignment(Pos.CENTER);

        vbox.setMinSize(200, 500);

        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #66ccff, #ff9966)");


        gameTitle.setStyle("-fx-font: bold 30px 'impact'; -fx-text-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, #ff6600 0%, #0099ff 50%)");
        multiPlayerButton.setStyle("-fx-text-fill: #ff6600 ; -fx-font: bold 'impact'; -fx-font: normal bold 15px 'impact'");
        easyAIButton.setStyle("-fx-text-fill: #0066ff; -fx-font: bold 'impact'; -fx-font: normal bold 15px 'impact'");
        hardAIButton.setStyle("-fx-text-fill: #cc3300; -fx-font: bold 'impact'; -fx-font: normal bold 15px 'impact'");
        gameHistoryButton.setStyle("-fx-text-fill: #0099ff; -fx-font: bold 'impact'; -fx-font: normal bold 15px 'impact'");


        setRoot(vbox);

    }



    public Button getMultiPlayerButton() {
        return multiPlayerButton;
    }
    public Button getEasyAIButton() { return easyAIButton; }
    public Button getHardAIButton() { return hardAIButton; }
    public Button getGameHistoryButton() { return gameHistoryButton; }
    public Button getLogoutButton() { return logoutButton; }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMultiPlayerButton(), getEasyAIButton(), getHardAIButton(), getGameHistoryButton(), getLogoutButton());
    }

    @Override
    public String toString() {
        return "mainWindowController {" +
                "multiPlayerButton = " + multiPlayerButton +
                "easyAIButton = " + easyAIButton +
                "hardAIButton = " + hardAIButton +
                "gameHistoryButton = " + gameHistoryButton +
                "logoutButton = " + logoutButton +
                '}';
    }



}