
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.pubnub.api.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;

public class Client extends Application {

    public static PubNub pubNub;
    public static String roomRequestChannel = "Rooms::Request";
    public static String roomUpdateChannel = "Rooms::Update";

    public static void main(String[] args) {

        if(args.length > 0) {
            //System.out.println(args[0]);

            NetworkManager.setUuidModifier(args[0]);
            NetworkManager.getPubNub();
        }

        launch(args);

        /*
        Scanner consoleInput = new Scanner(System.in);
        System.out.println("Are you A or B?");
        String name = "";

        do {
            name = consoleInput.next();

            if(name.length() > 0) {
                name = name.substring(0, 1);
            }

        } while(!name.equals("A") && !name.equals("B"));
        System.out.println("Alright, you are " + name + "! Connecting...");

        //pnConfiguration.setUuid("User-A::Java");


        pubnub.addListener(new RoomRequesterCallback(name, roomRequestChannel, roomRequestChannel));
        pubnub.subscribe().channels(Arrays.asList(roomUpdateChannel)).execute();
        */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("HBox Experiment 1");


        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();

        Label roomLabel = new Label("Room");
        TextField roomField = new TextField();

        Button openButton = new Button("Open");
        Button joinButton = new Button("Join");

        // TODO We need a text field. Since I'm running multiple instances
        //      on a single computer, we cannot decide UUID until we have
        //      the name.

        openButton.setOnAction(value ->  {
            System.out.println("Opening");

            //NetworkManager.forceUUID(nameField.getText());
        });

        VBox vbox = new VBox(nameLabel, nameField, roomLabel, roomField, openButton, joinButton);

        Scene scene = new Scene(vbox, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
