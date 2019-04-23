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

public class GameScoreController extends AbstractSceneController {

    TableView<scoreInfo> playerHistoryTable;

    //region Table config
    /*
    TableColumn<scoreInfo, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));

    TableColumn<scoreInfo, Integer> lobbyStatColumn = new TableColumn<>("Room Status");
        lobbyStatColumn.setMinWidth(200);
        lobbyStatColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    TableColumn<scoreInfo, Integer> player1Column = new TableColumn<>("Player1");
        player1Column.setMinWidth(100);
        player1Column.setCellValueFactory(new PropertyValueFactory<>("player1Name"));

*/

    public GameScoreController() {

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

        //endregion


        VBox vbox = new VBox(playerHistoryTable);

        //setMasterScene(new Scene(vbox, 200, 200));
        setRoot(vbox);

    }

}
