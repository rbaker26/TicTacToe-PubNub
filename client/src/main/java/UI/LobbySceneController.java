package UI;

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

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LobbySceneController extends AbstractSceneController {

    private Button openButton;
    private Button backButton;


    private Consumer<RoomInfo> joinHandler;


    TableView<RoomInfo> lobbyTable;

    public LobbySceneController() {
        openButton = new Button("Open");
        backButton = new Button ("Back");

        //region Table config
        TableColumn<RoomInfo, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(150);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));

        TableColumn<RoomInfo, String> lobbyStatColumn = new TableColumn<>("Room Status");
        lobbyStatColumn.setMinWidth(150);
        lobbyStatColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<RoomInfo, String> player1Column = new TableColumn<>("Player1");
        player1Column.setMinWidth(150);
        player1Column.setCellValueFactory(new PropertyValueFactory<>("player1Name"));

        TableColumn<RoomInfo, String> player2Column = new TableColumn<>("Player2");
        player2Column.setMinWidth(150);
        player2Column.setCellValueFactory(new PropertyValueFactory<>("player2Name"));

        TableColumn<RoomInfo, String> player1TokenColumn = new TableColumn<>("Player1 Token");
        player1TokenColumn.setMinWidth(150);
        player1TokenColumn.setCellValueFactory(new PropertyValueFactory<>("player1Token"));

        TableColumn<RoomInfo, String> player2TokenColumn = new TableColumn<>("Player2 Token");
        player2TokenColumn.setMinWidth(150);
        player2TokenColumn.setCellValueFactory(new PropertyValueFactory<>("player2Token"));


        lobbyTable = new TableView<>();
        lobbyTable.getColumns().addAll(idColumn, lobbyStatColumn, player1Column, player2Column,
                player1TokenColumn, player2TokenColumn);

        lobbyTable.getSelectionModel().selectedIndexProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

                ObservableList<RoomInfo> selection = lobbyTable.getSelectionModel().getSelectedItems();

                if(selection.size() > 0 && joinHandler != null) {
                    joinHandler.accept( selection.get(0) );
                }
            }
        });

        //endregion


        VBox vbox = new VBox(lobbyTable, openButton, backButton);

        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #66ccff, #ff9966)");

        //setMasterScene(new Scene(vbox, 200, 200));
        setRoot(vbox);

    }

    /**ABOUT THIS FUNCTION - THIS IS JUST A TESTING FUNCTION FOR TABLEVIEW CONTENTS
     *  Needs to be modified in order to become flexible.
     *  TableView populating option: we can change this function so that it
     *  can be passed in values instead of hard coding values.
     *  Player's name and token need to be taken from TextField and ToggleButton**/

    @Deprecated
    public ObservableList<RoomInfo> getRoomInfo() {
        ObservableList<RoomInfo> information = FXCollections.observableArrayList();
        //information.add(new RoomInfo("34562", "Closed - Game in Play", "Bobby", "Keane", "X", "O"));
        //information.add(new RoomInfo("67895", "Closed - Game in Play", "Daniel", "Naomi", "O", "X"));
        //information.add(new RoomInfo(193, "Hi"));
        RoomInfo testRoom1 = new RoomInfo();
        testRoom1.setRoomID(1324);
        testRoom1.setPlayer1(new PlayerInfo("213", "Hi", "Oof"));
        information.add(testRoom1);

        //testing();

        return information;
    }


    /**
     * This is a proxy function to call setRoomInfo asynchronously. This means that the
     * given room info will not be applied instantaneously; it will be applied whenever
     * the JavaFX thread gets around to it.
     *
     * Use this if interacting with the lobby from another thread.
     * @param rooms The room list.
     */
    public void setRoomInfoAsync(List<Messages.RoomInfo> rooms) {
        Platform.runLater(() -> setRoomInfo(rooms));
    }

    /**
     * Updates the table of rooms with the new room info. Whatever was there
     * previously will be discarded.
     *
     * This is UNSAFE to call from another thread. Refer to setRoomInfoAsync if calling
     * from another thread.
     * @param rooms The room list.
     */
    public void setRoomInfo(List<Messages.RoomInfo> rooms) {

        // This code is just going to convert the Messages.RoomInfo objects into UI.RoomInfo objects.
        ObservableList<RoomInfo> information = FXCollections.observableArrayList(rooms);
        lobbyTable.setItems(information);

    }

    @Override
    public void applyScene(Stage targetStage) {
        super.applyScene(targetStage);

        // This causes us to automatically launch into the listening state when opening this scene.
        // It passes the setRoomInfoAsync function, which will get called whenever we get an
        // updated list of rooms.
        NetworkManager.getInstance().listenForRooms( this::setRoomInfoAsync );
    }

    //region Getters
//    public String getName() {
//        return nameField.getText();
//    }
    public Button getBackButton() {
        return backButton;
    }
    public void setOpenHandler(Runnable handler) {
        openButton.setOnAction(value -> handler.run());
    }
    public void setJoinHandler(Consumer<RoomInfo> handler) {
        joinHandler = handler;
    }
}
