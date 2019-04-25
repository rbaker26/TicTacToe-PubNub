package UI;

import EngineLib.Board;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/*
 * This board is a simple 3 x 3 grid set up to play tic-tac-toe using a javafx gridpane
 * The constructor iterates through each cell of the gridpane placing a pane with an ImageView.
 * Each pane is set up with an on click event to handle the toggling the tokens in the space
 */
public class BoardGUIPane extends GridPane {
    private final int MAX_SIZE = 3;
    private Image xImg;
    private Image oImg;
    private Image emptyImg;

    public BoardGUIPane(BiConsumer<Integer, Integer> consumer) {

        try {
            String path = "src/main/resources/img/";

            xImg = new Image(new FileInputStream(path + "X.png"));
            oImg = new Image(new FileInputStream(path + "O.png"));
            emptyImg = new Image(new FileInputStream(path + "Empty.png"));
        }
        catch(Exception ex) {
            System.out.println("File not found");
        }

        for (int row = 0; row < MAX_SIZE; row++) {
            for (int col = 0; col < MAX_SIZE; col++) {
                Pane space = new Pane();
                ImageView token = new ImageView();
                token.setImage(emptyImg);
                token.setPreserveRatio(true);
                token.setFitHeight(100);
                space.getChildren().add(token);
                space.setOnMouseClicked(e -> {
                    System.out.println("Clicked");
//                    int rowClicked = GridPane.getRowIndex((Node)e.getSource());
//                    int colClicked = GridPane.getColumnIndex((Node)e.getSource());
//                    if(myTurn && moveIsValid(rowClicked, colClicked)) {
//                        System.out.println("Move is valid");
//                        myTurn = false;
//                        consumer.accept(rowClicked, colClicked);
//                        board.setPos(rowClicked, colClicked, myToken);
//                    }
                    consumer.accept(GridPane.getRowIndex((Node)e.getSource()), GridPane.getColumnIndex((Node)e.getSource()));
                });
                this.add(space, col, row);
            }
        }
        this.setGridLinesVisible(true);
        this.setMinHeight(100 * 3);
        this.setMinWidth(100 * 3);
    }

    public void resetBoard() {
        ObservableList<Node> nodes = this.getChildren();
        for(Node node : nodes) {
            if(node instanceof Pane) {
                ((ImageView)((Pane) node).getChildren().get(0)).setImage(emptyImg);
            }
        }
    }

//    public void setClickEvent(BiConsumer<Integer, Integer> consumer) {
//        ObservableList<Node> nodes = this.getChildren();
//        for(Node node : nodes) {
//            if(node instanceof Pane) {
//                ((Pane)node).getChildren().get(0).setOnMouseClicked(e -> {
//                    System.out.println("Clicked");
//                    int row = GridPane.getRowIndex((Node)e.getSource());
//                    int col = GridPane.getColumnIndex((Node)e.getSource());
//                    if(myTurn && moveIsValid(row, col)) {
//                        System.out.println("Move is valid");
//                        myTurn = false;
//                        consumer.accept(row, col);
//                        board.setPos(row, col, myToken);
//                    }
//                });
//            }
//        }
//    }
//
//    public void setToken(char token) {
//        myToken = token;
//        System.out.println("my turn");
//    }
//
//    public void toggleTurn() {
//        myTurn = true;
//    }
//    public boolean moveIsValid(int row, int col) {
//        return board.getPos(row, col) == ' ';
//    }

    public void drawBoard(Board currentBoard) {
        //System.out.println("Drawing board");

        ObservableList<Node> nodes = this.getChildren();
        ImageView image;
        for(Node node : nodes) {

            if(node instanceof Pane) {
                image = (ImageView) ((Pane) node).getChildren().get(0);
                int row = GridPane.getRowIndex(node);
                int col = GridPane.getColumnIndex(node);


                // TODO This is terrible. Characters are garbage.
                switch(currentBoard.getPos(row, col)) {
                    case 'X':
                    case 'x':
                        image.setImage(xImg);
                        break;
                    case 'O':
                    case 'o':
                        image.setImage(oImg);
                        break;
                    case ' ':
                        image.setImage(emptyImg);
                        break;
                    default:
                        throw new RuntimeException("Invalid character in board");
                }
            }
        }
    }
}
