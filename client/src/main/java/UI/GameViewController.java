package UI;

import EngineLib.Board;
import Messages.RoomInfo;
import Network.NetworkManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.function.BiConsumer;

public class GameViewController extends AbstractSceneController {

    private BoardGUIPane boardPane;
    private Board board = new Board();
    private RoomInfo room;
    private String myName;
    private boolean myTurn;
    private char myToken;
    private Label leftLabel = new Label();
    private Label rightLabel = new Label();
    private Label centerLabel = new Label();

    public GameViewController(RoomInfo room, String myName) {
        this.room = room;
        this.myName = myName;
        System.out.println(room);
        if(room.getPlayer1().getId().equals(myName)) {

            myToken = 'X';
            myTurn = true;
            centerLabel.setText("Your turn");
        }
        else {
            myToken = 'O';
            myTurn = false;
        }
        leftLabel.setText(room.getPlayer1().getName() + "\nPlayer X");
        rightLabel.setText(room.getPlayer2().getName() + "\nPlayer O");

        boardPane = new BoardGUIPane((row, col) -> {
            if(board.getPos(row, col) == ' ' && myTurn) {
                myTurn = false;
                centerLabel.setText("");
                board.setPos(row, col, myToken);
                NetworkManager.getInstance().sendMove(row, col, room.getRoomID(), myName);
                boardPane.drawBoard(board);
            }
        });
        BorderPane box = new BorderPane();
        leftLabel.setPadding(new Insets(5, 5, 5, 5));
        rightLabel.setPadding(new Insets(5, 5, 5, 5));
        centerLabel.setPadding(new Insets(5, 5, 5, 5));
        centerLabel.setFont(new Font("Arial", 20));
        HBox turnBox = new HBox(centerLabel);
        turnBox.setAlignment(Pos.CENTER);
        box.setLeft(leftLabel);
        box.setRight(rightLabel);
        box.setCenter(boardPane);
        box.setBottom(turnBox);
        //HBox box = new HBox(leftLabel, boardPane, rightLabel);

        setRoot(box);
    }

    public void updateBoard(Board board) {
        this.board = board;
        boardPane.drawBoard(board);
    }
    public void toggleTurn() {
        this.myTurn = true;
        //centerLabel.setText("Your turn");
        Platform.runLater(() -> {
            centerLabel.setText("Your turn");
        });
    }
}
