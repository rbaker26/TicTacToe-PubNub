package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RoomListController extends AbstractSceneController{

    Stage window;
    TableView<RoomInfo> lobbyTable;


    private TextField nameField;
    private TextField roomField;
    private Button openButton;
    private Button joinButton;


    public RoomListController() {

        Label nameLabel = new Label("Name");
        nameField = new TextField();

        Label roomLabel = new Label("Room");
        roomField = new TextField();

        openButton = new Button("Open");
        joinButton = new Button("Join");

        //CREATING THE COLUMNS FOR THE TABLE

        // TODO Look into another way of populating this which doesn't use PropertyValueFactory

        TableColumn<RoomInfo, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        TableColumn<RoomInfo, String> lobbyStatColumn = new TableColumn<>("Lobby Status");
        lobbyStatColumn.setMinWidth(200);
        lobbyStatColumn.setCellValueFactory(new PropertyValueFactory<>("lobbyStatus"));

        TableColumn<RoomInfo, String> player1Column = new TableColumn<>("Player1");
        player1Column.setMinWidth(100);
        player1Column.setCellValueFactory(new PropertyValueFactory<>("player1"));

        TableColumn<RoomInfo, String> player2Column = new TableColumn<>("Player2");
        player2Column.setMinWidth(100);
        player2Column.setCellValueFactory(new PropertyValueFactory<>("player2"));

        TableColumn<RoomInfo, String> player1TokenColumn = new TableColumn<>("Player1 Token");
        player1TokenColumn.setMinWidth(100);
        player1TokenColumn.setCellValueFactory(new PropertyValueFactory<>("Player1Token"));

        TableColumn<RoomInfo, String> player2TokenColumn = new TableColumn<>("Player2 Token");
        player2TokenColumn.setMinWidth(100);
        player2TokenColumn.setCellValueFactory(new PropertyValueFactory<>("Player2Token"));

        lobbyTable = new TableView<>();

        lobbyTable.setItems(getRoomInfo());

        lobbyTable.getColumns().addAll(idColumn, lobbyStatColumn, player1Column, player2Column,
                player1TokenColumn, player2TokenColumn);

        VBox vbox = new VBox(lobbyTable);

        setRoot(vbox);



    }

    /**ABOUT THIS FUNCTION - THIS IS JUST A TESTING FUNCTION FOR TABLEVIEW CONTENTS
     *  Needs to be modified in order to become flexible.
     *  TableView populating option: we can change this function so that it
     *  can be passed in values instead of hard coding values.
     *  Player's name and token need to be taken from TextField and ToggleButton**/

    public ObservableList<RoomInfo> getRoomInfo() {
        ObservableList<RoomInfo> information = FXCollections.observableArrayList();
        information.add(new RoomInfo("34562", "Closed - Game in Play", "Bobby", "Keane", "X", "O"));
        information.add(new RoomInfo("67895", "Closed - Game in Play", "Daniel", "Naomi", "O", "X"));

        return information;
    }

}
