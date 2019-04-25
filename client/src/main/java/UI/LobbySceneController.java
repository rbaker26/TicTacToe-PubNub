package UI;

import Messages.RoomInfo;
import Network.NetworkManager;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class LobbySceneController extends AbstractSceneController {
    public static class OpenRequest {

        private String password;
        private boolean goingFirst;

        public OpenRequest(String password, boolean goingFirst) {
            this.password = password;
            this.goingFirst = goingFirst;
        }

        public String getPassword() {
            return password;
        }

        public boolean isGoingFirst() {
            return goingFirst;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OpenRequest that = (OpenRequest) o;
            return isGoingFirst() == that.isGoingFirst() &&
                    Objects.equals(getPassword(), that.getPassword());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getPassword(), isGoingFirst());
        }

        @Override
        public String toString() {
            return "OpenRequest{" +
                    "password='" + password + '\'' +
                    ", goingFirst=" + goingFirst +
                    '}';
        }
    }

    private Button openButton;
    private Button backButton;


    private Consumer<RoomInfo> joinHandler;
    private Consumer<OpenRequest> openHandler;
    private Runnable sceneApplyHandler;


    TableView<RoomInfo> lobbyTable;
    List<RoomInfo> latestRoomList;

    public LobbySceneController() {

        openButton = new Button("Open");
        openButton.setOnAction(event -> { CreateRoomDialog(); });

        backButton = new Button("Back");

        //region Table config
        /*
        TableColumn<RoomInfo, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(150);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
         */

        TableColumn<RoomInfo, String> lobbyStatColumn = new TableColumn<>("Room Status");
        lobbyStatColumn.setMinWidth(150);
        lobbyStatColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<RoomInfo, String> player1Column = new TableColumn<>("Player1");
        player1Column.setMinWidth(150);
        player1Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RoomInfo, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RoomInfo, String> r) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleObjectProperty<>(r.getValue().getPlayer1().getName());
            }
        });

        /*
        TableColumn<RoomInfo, String> player2Column = new TableColumn<>("Player2");
        player2Column.setMinWidth(150);
        player2Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RoomInfo, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RoomInfo, String> r) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleObjectProperty<>(r.getValue().getPlayer2().getName());
            }
        });
         */

        TableColumn<RoomInfo, String> player1TokenColumn = new TableColumn<>("P1 Token");
        player1TokenColumn.setMinWidth(50);
        player1TokenColumn.setCellValueFactory(new PropertyValueFactory<>("player1Token"));

        TableColumn<RoomInfo, String> player2TokenColumn = new TableColumn<>("P2 Token");
        player2TokenColumn.setMinWidth(50);
        player2TokenColumn.setCellValueFactory(new PropertyValueFactory<>("player2Token"));

        TableColumn<RoomInfo, Boolean> hasPasswordColumn = new TableColumn<>("Password?");
        hasPasswordColumn.setMinWidth(50);
        hasPasswordColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().hasPassword()));
        hasPasswordColumn.setCellFactory(col -> new TableCell<RoomInfo, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : (item ? "Yes" : "No") );
            }
        });


        lobbyTable = new TableView<>();
        lobbyTable.getColumns().addAll(lobbyStatColumn, player1Column,
                player1TokenColumn, player2TokenColumn, hasPasswordColumn);

        /*
        lobbyTable.getSelectionModel().selectedIndexProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

                ObservableList<RoomInfo> selection = lobbyTable.getSelectionModel().getSelectedItems();

                if(selection.size() > 0) {
                    //joinHandler.accept( selection.get(0) );
                    OnSelectedRoom(selection.get(0));
                }
            }
        });
         */

        lobbyTable.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY) && !lobbyTable.getSelectionModel().isEmpty()) {
                //System.out.println(lobbyTable.getSelectionModel().getSelectedItem());
                OnSelectedRoom(lobbyTable.getSelectionModel().getSelectedItem());
            }
        });
        //endregion

        VBox vbox = new VBox(lobbyTable, openButton, backButton);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #66ccff, #ff9966)");


        //setMasterScene(new Scene(vbox, 200, 200));
        setRoot(vbox);
    }

    private void OnSelectedRoom(RoomInfo room) {
        System.out.println("Picked this room: " + room);
        System.out.println(room.hasPassword());

        if(room.hasPassword()) {
            PasswordDialog( room );
        }
        else {
            joinHandler.accept(room);
        }
    }

    private void CreateRoomDialog() {
        // See "Custom Login Dialog" in https://code.makery.ch/blog/javafx-dialogs-official/

        // Create the custom dialog.
        Dialog<OpenRequest> dialog = new Dialog<>();
        dialog.setTitle("Room Configuration");
        //dialog.setHeaderText("");
        dialog.initStyle(StageStyle.UTILITY);

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        final ToggleGroup turnOrderGroup = new ToggleGroup();
        RadioButton firstButton = new RadioButton("First");
        RadioButton secondButton = new RadioButton("Second");
        firstButton.setSelected(true);
        firstButton.setToggleGroup(turnOrderGroup);
        secondButton.setToggleGroup(turnOrderGroup);

        grid.add(new Label("Password:"), 0, 0);
        grid.add(password, 1, 0);
        grid.add(firstButton, 0, 1);
        grid.add(secondButton, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new OpenRequest(password.getText(), firstButton.isSelected());
            }
            return null;
        });

        Optional<OpenRequest> result = dialog.showAndWait();

        result.ifPresent(requestInfo -> {
            //System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
            //System.out.println(result);
            if(openHandler != null) {
                openHandler.accept(requestInfo);
            }
        });
    }

    private void PasswordDialog(RoomInfo room) {
        // See "Custom Login Dialog" in https://code.makery.ch/blog/javafx-dialogs-official/

        // Create the custom dialog.
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Enter password");
        dialog.initStyle(StageStyle.UTILITY);

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the password field
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Password:"), 0, 0);
        grid.add(password, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == ButtonType.OK) {
                return room.passwordMatches(password.getText());
            }
            else {
                return null;
            }
        });

        Optional<Boolean> result = dialog.showAndWait();

        result.ifPresent(isCorrect -> {
            if(isCorrect) {
                if (joinHandler != null) {
                    joinHandler.accept(room);
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incorrect password");
                alert.setHeaderText("That password was not correct.");
                alert.setContentText("Do you have caps-lock on?");

                alert.showAndWait();
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

        // This code is just going to convert the Messages.RoomInfo objects into UI.RoomInfo objects.
        ObservableList<RoomInfo> information = FXCollections.observableArrayList(rooms);
        lobbyTable.setItems(information);

    }

    @Override
    public void applyScene(Stage targetStage) {
        super.applyScene(targetStage);


        // TODO This needs to be passed in as a lambda
        // This causes us to automatically launch into the listening state when opening this scene.
        // It passes the setRoomInfoAsync function, which will get called whenever we get an
        // updated list of rooms.
        if(!NetworkManager.getInstance().isListeningForRooms()) {
            NetworkManager.getInstance().listenForRooms(this::setRoomInfoAsync);
        }
    }

    public Button getBackButton() {
        return backButton;
    }
  
    public void setOpenHandler(Consumer<OpenRequest> handler) {
        openHandler = handler;
    }
    public void setJoinHandler(Consumer<RoomInfo> handler) {
        joinHandler = handler;
    }
}
