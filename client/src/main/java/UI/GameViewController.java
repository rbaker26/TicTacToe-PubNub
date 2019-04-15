package UI;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class GameViewController extends AbstractSceneController {

    BoardGUIPane boardPane;

    public GameViewController() {
        Label leftLabel = new Label("I'm on\nthe left");
        Label rightLabel = new Label("I'm on\nthe right");

        boardPane = new BoardGUIPane();

        HBox box = new HBox(leftLabel, boardPane, rightLabel);

        setRoot(box);
    }
}
