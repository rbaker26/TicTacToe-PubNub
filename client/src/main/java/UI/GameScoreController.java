package UI;

import javafx.scene.control.*;

import Messages.PlayerInfo;
import Messages.RoomInfo;
import Network.NetworkManager;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Objects;

public class GameScoreController extends AbstractSceneController {

    TableView<scoreInfo> playerHistoryTable;

    private Button backButton;

    public GameScoreController() {

        backButton = new Button("Back");

        TableColumn<scoreInfo, String> nameColumn = new TableColumn<>("Player Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<scoreInfo, Integer> gamesWonColumn = new TableColumn<>("Games Won");
        gamesWonColumn.setMinWidth(100);
        gamesWonColumn.setCellValueFactory(new PropertyValueFactory<>("gamesWon"));

        TableColumn<scoreInfo, Integer> gamesPlayedColumn = new TableColumn<>("Games Played");
        gamesPlayedColumn.setMinWidth(100);
        gamesPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));



        playerHistoryTable = new TableView<>();
        playerHistoryTable.getColumns().addAll(nameColumn, gamesWonColumn, gamesPlayedColumn);


        VBox vbox = new VBox(playerHistoryTable, backButton);


        setRoot(vbox);

    }

    public Button getBackButton() {
        return backButton;
    }

    //region Object overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LobbySceneController that = (LobbySceneController) o;
        return Objects.equals(getBackButton(), that.getBackButton());


    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBackButton());
    }

    @Override
    public String toString() {
        return "GameScoreController{" +
                ", backButton=" + backButton +
                '}';
    }
    //endregion


}
