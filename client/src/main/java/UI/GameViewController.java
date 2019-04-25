package UI;

import EngineLib.Board;
import Messages.RoomInfo;
import Network.NetworkManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.function.BiConsumer;

public class GameViewController extends AbstractSceneController {

    private BoardGUIPane boardPane;
    private Board board = new Board();
    private RoomInfo room;
    private String myName;
    private boolean myTurn;
    private char myToken;

    public GameViewController(RoomInfo room, String myName) {
        this.room = room;
        this.myName = myName;
        System.out.println(room);
        if(room.getPlayer1().getId().equals(myName)) {

            myToken = 'X';
            myTurn = true;
        }
        else {
            myToken = 'O';
            myTurn = false;
        }
        Label leftLabel = new Label(room.getPlayer1Name() + "\nPlayer X");
        Label rightLabel = new Label(room.getPlayer2Name() + "\nPlayer O");

        boardPane = new BoardGUIPane((row, col) -> {
            if(board.getPos(row, col) == ' ' && myTurn) {
                myTurn = false;
                board.setPos(row, col, myToken);
                NetworkManager.getInstance().sendMove(row, col, room.getRoomID(), myName);
                boardPane.drawBoard(board);
            }
        });
        BorderPane box = new BorderPane();
        box.setLeft(leftLabel);
        box.setRight(rightLabel);
        box.setCenter(boardPane);
        leftLabel.setPadding(new Insets(5, 5, 5, 5));
        rightLabel.setPadding(new Insets(5, 5, 5, 5));
        //HBox box = new HBox(leftLabel, boardPane, rightLabel);

        setRoot(box);
    }

    public void updateBoard(Board board) {
        this.board = board;
        boardPane.drawBoard(board);
    }
    public void toggleTurn() {
        this.myTurn = true;
    }
}
