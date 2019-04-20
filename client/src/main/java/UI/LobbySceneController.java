package UI;

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

import java.util.List;
import java.util.Objects;

public class LobbySceneController extends AbstractSceneController {

    private TextField nameField;
    private TextField roomField;
    private Button openButton;
    private Button joinButton;


    private ISceneController boardGUI;
    private ISceneController mainWindow;


    TableView<RoomInfo> lobbyTable;

    public LobbySceneController() {
        Label nameLabel = new Label("Name");
        nameField = new TextField();

        Label roomLabel = new Label("Room");
        roomField = new TextField();

        openButton = new Button("Open");
        joinButton = new Button("Join");

        //region Table config
        TableColumn<RoomInfo, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));

        TableColumn<RoomInfo, String> lobbyStatColumn = new TableColumn<>("Room Status");
        lobbyStatColumn.setMinWidth(200);
        lobbyStatColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<RoomInfo, String> player1Column = new TableColumn<>("Player1");
        player1Column.setMinWidth(100);
        player1Column.setCellValueFactory(new PropertyValueFactory<>("player1Name"));

        TableColumn<RoomInfo, String> player2Column = new TableColumn<>("Player2");
        player2Column.setMinWidth(100);
        player2Column.setCellValueFactory(new PropertyValueFactory<>("player2Name"));

        TableColumn<RoomInfo, String> player1TokenColumn = new TableColumn<>("Player1 Token");
        player1TokenColumn.setMinWidth(100);
        player1TokenColumn.setCellValueFactory(new PropertyValueFactory<>("player1Token"));

        TableColumn<RoomInfo, String> player2TokenColumn = new TableColumn<>("Player2 Token");
        player2TokenColumn.setMinWidth(100);
        player2TokenColumn.setCellValueFactory(new PropertyValueFactory<>("player2Token"));


        lobbyTable = new TableView<>();
        lobbyTable.getColumns().addAll(idColumn, lobbyStatColumn, player1Column, player2Column,
                player1TokenColumn, player2TokenColumn);

        lobbyTable.setItems(getRoomInfo());
        //endregion


        VBox vbox = new VBox(lobbyTable, nameLabel, nameField, roomLabel, roomField, openButton, joinButton);

        //setMasterScene(new Scene(vbox, 200, 200));
        setRoot(vbox);

    }

    /**ABOUT THIS FUNCTION - THIS IS JUST A TESTING FUNCTION FOR TABLEVIEW CONTENTS
     *  Needs to be modified in order to become flexible.
     *  TableView populating option: we can change this function so that it
     *  can be passed in values instead of hard coding values.
     *  Player's name and token need to be taken from TextField and ToggleButton**/

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


    public void testing() {

        /*** ABOUT THIS FUNCTION - TESTING LISTENER FUNCTION
         * Selection of row is implemented by this function. Right now, I am just able to determine
         * if a row is selected, which will help lead us to starting up a game
         * I want this to connect to board UI.
         */
        Stage getStage = new Stage();

        lobbyTable.getSelectionModel().selectedIndexProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

                BoardGUIPane boardObject = new BoardGUIPane();

                mainWindowController mainObject = new mainWindowController();

                //SO WITHIN HERE WE MUST TAKE VALUES OF THE COLUMNS AND PASS THEM INTO BOARD FUNCTIONALITY
                //THAT ALLOWS A GAME TO START GIVEN PLAYER INFORMATION AS WELL AS ROOM ID.

                System.out.println("Selected indices: " + lobbyTable.getSelectionModel().getSelectedItems());

                System.out.println("Selected items: " + lobbyTable.getSelectionModel().getSelectedItems());

                // boardGUI = boardObject; //CANNOT SWITCH TO BOARD UI

                //THIS IS JUST FOR TESTING: I WANT TO SWITCH OVER TO THE MAINwINDOW CONTROLLER IF I SELECT A ROW

                mainWindow = mainObject;

                mainObject.applyScene(getStage);


            }

        });

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

        // TODO If possible, it's probably more convenient to make a 1-1 coupling, having
        //      the table be able to take Messages.RoomInfo so we don't have to convert things.
        //      However, if we stick with this reflection-based approach, it's probably better
        //      to keep it as a separate UI object.

        // This code is just going to convert the Messages.RoomInfo objects into UI.RoomInfo objects.
        ObservableList<RoomInfo> information = FXCollections.observableArrayList(rooms);
        /*
        for(Messages.RoomInfo room : rooms) {
            information.add(new RoomInfo(
                    Integer.toString(room.getRoomID()),
                    "Unknown",
                    room.getPlayer1Name(),
                    room.getPlayer2Name(),
                    "X",
                    "O"
            ));
        }
         */

        lobbyTable.setItems(information);

        // TODO If we are selecting available rooms, we will need to check to make sure that the new
        //      list of rooms has not invalidated our selection. We should probably track the RoomID of the
        //      selected room, and then scan through the new list. If the RoomID is gone, we need to
        //      deselect it.
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
    public String getName() {
        return nameField.getText();
    }

    /**
     * @deprecated This should NOT be used for serious code. Instead, the
     *             user will be choosing rooms to join.
     * @return
     */
    @Deprecated
    public int getRoomID() {
        return Integer.parseInt(roomField.getText());
    }

    public TextField getNameField() {
        return nameField;
    }

    /**
     * @deprecated This is deprecated because we should NOT be using the
     *             room field. This will eventually go away and be replaced
     * by a full listing.
     * @return
     */
    @Deprecated
    public TextField getRoomField() {
        return roomField;
    }

    public Button getOpenButton() {
        return openButton;
    }

    public Button getJoinButton() {
        return joinButton;
    }


    //endregion

    //region Object overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LobbySceneController that = (LobbySceneController) o;
        return Objects.equals(getNameField(), that.getNameField()) &&
                Objects.equals(getRoomField(), that.getRoomField()) &&
                Objects.equals(getOpenButton(), that.getOpenButton()) &&
                Objects.equals(getJoinButton(), that.getJoinButton());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNameField(), getRoomField(), getOpenButton(), getJoinButton());
    }

    @Override
    public String toString() {
        return "LobbySceneController{" +
                "nameField=" + nameField +
                ", roomField=" + roomField +
                ", openButton=" + openButton +
                ", joinButton=" + joinButton +
                '}';
    }
    //endregion

}
