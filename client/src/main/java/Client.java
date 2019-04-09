

import java.net.InetAddress;
import java.net.UnknownHostException;

import Messages.RoomInfo;
import com.pubnub.api.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;

// NOTE: To change the uuid, go to the launch configurations and add this to the "Arguments" field
//   -Pargs=Something
// This will tack on "Something" to the UUID. Make several launch configs if you want.

public class Client extends Application {



    public static void main(String[] args) {

        if(args.length > 0) {
            //System.out.println(args[0]);

            NetworkManager.setUuidModifier(args[0]);
        }

        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("HBox Experiment 1");
        NetworkManager.getInstance();

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();

        Label roomLabel = new Label("Room");
        TextField roomField = new TextField();

        Label turnOrder = new Label("Go first?");
        CheckBox goFirst = new CheckBox();

        Button openButton = new Button("Open");
        Button joinButton = new Button("Join");
        Button refreshButton = new Button("Get Room List");

        openButton.setOnAction(value ->  {
            System.out.println("Opening");


            NetworkManager.getInstance().requestNewRoom(nameField.getText(), goFirst.isSelected());
        });

        joinButton.setOnAction(e -> {
            System.out.println("Joining");
            RoomInfo room = new RoomInfo(Integer.parseInt(roomField.getText()), nameField.getText());
            System.out.println(room);
            NetworkManager.getInstance().joinLobby(room);
        });

        refreshButton.setOnAction(e -> {
            System.out.println("Getting rooms");
            NetworkManager.getInstance().getRoomList();
        });

        // FOLLOWING FOR TESTING
        Label rowLabel = new Label("Row: ");
        TextField rowField = new TextField();
        Label colLabel = new Label("Col: ");
        TextField colField = new TextField();
        Button moveButton = new Button("Send Move");

        moveButton.setOnAction(e -> {
            NetworkManager.getInstance().sendMove(Integer.parseInt(rowField.getText()), Integer.parseInt(colField.getText()), 100000, nameField.getText());
        });

        HBox hbox = new HBox(rowLabel, rowField, colLabel, colField);
        VBox vbox = new VBox(nameLabel, nameField, roomLabel, roomField, turnOrder, goFirst, openButton, joinButton, refreshButton, hbox, moveButton);

        Scene scene = new Scene(vbox, 200, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        NetworkManager.getInstance().dieHorribly();

        // TODO We shouldn't be forcing an exit, but... we have no other choice
        System.exit(0);
    }
}
