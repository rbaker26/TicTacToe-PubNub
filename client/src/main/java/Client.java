

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.pubnub.api.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        primaryStage.setTitle("SABRCATST TicTacToe");


        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();

        Label roomLabel = new Label("Room");
        TextField roomField = new TextField();

        Button openButton = new Button("Open");
        Button joinButton = new Button("Join");

        openButton.setOnAction(value ->  {
            System.out.println("Opening");

            //NetworkManager.forceUUID(nameField.getText());
            NetworkManager.getInstance().requestNewRoom(
                    nameField.getText(),
                    true,
                    responseRoomInfo -> {
                        System.out.println("Hi");

                        Platform.runLater(() ->
                            primaryStage.setScene(new Scene(new HBox(), 100, 100))
                        );
                    },
                    null
            );
        });

        joinButton.setOnAction(value -> {
            System.out.println("Joining " + roomField.getText());

            // TODO This should we where we plug the room from the other player.
            Messages.RoomInfo roomInfo = new Messages.RoomInfo();
            roomInfo.setRoomID(Integer.parseInt(roomField.getText()));
            NetworkManager.getInstance().joinRoom(nameField.getText(), roomInfo);
        });

        VBox vbox = new VBox(nameLabel, nameField, roomLabel, roomField, openButton, joinButton);

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
