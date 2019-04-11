package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Objects;

public class LobbySceneController extends AbstractSceneController {

    private TextField nameField;
    private TextField roomField;
    private Button openButton;
    private Button joinButton;

    TableView<RoomInfo> lobbyTable;

    public LobbySceneController() {
        Label nameLabel = new Label("Name");
        nameField = new TextField();

        Label roomLabel = new Label("Room");
        roomField = new TextField();

        openButton = new Button("Open");
        joinButton = new Button("Join");

	/*
        // TODO THIS IS JUST FOR TESTING
        Button debugButton = new Button("Show board view");
        debugButton.setOnAction(value -> {
            GameViewController newView = new GameViewController();
            newView.applyScene((Stage) getRoot().getScene().getWindow());
        });

        //VBox vbox = new VBox(nameLabel, nameField, roomLabel, roomField, openButton, joinButton, debugButton);
        VBox vbox = new VBox(nameLabel, nameField, roomLabel, roomField, openButton, joinButton);
	*/
	//
        //CREATING THE COLUMNS FOR THE TABLE
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
        information.add(new RoomInfo("34562", "Closed - Game in Play", "Bobby", "Keane", "X", "O"));
        information.add(new RoomInfo("67895", "Closed - Game in Play", "Daniel", "Naomi", "O", "X"));

        return information;
    }



    //region Getters
    /*
    public TextField getNameField() {
        return nameField;
    }

    public TextField getRoomField() {
        return roomField;
    }
     */
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
