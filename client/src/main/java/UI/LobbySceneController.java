package UI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class LobbySceneController extends AbstractSceneController {

    private TextField nameField;
    private TextField roomField;
    private Button openButton;
    private Button joinButton;

    public LobbySceneController() {
        Label nameLabel = new Label("Name");
        nameField = new TextField();

        Label roomLabel = new Label("Room");
        roomField = new TextField();

        openButton = new Button("Open");
        joinButton = new Button("Join");

        VBox vbox = new VBox(nameLabel, nameField, roomLabel, roomField, openButton, joinButton);

        setMasterScene(new Scene(vbox, 200, 200));
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
