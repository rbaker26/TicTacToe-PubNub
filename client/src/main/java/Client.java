

import java.net.InetAddress;
import java.net.UnknownHostException;

import UI.ISceneController;
import UI.LobbySceneController;
import UI.RoomListController;
import UI.playAgainController;
import UI.WaitingForOpponentScene;
import UI.mainWindowController;
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

    private static final double initWidth = 800;
    private static final double initHeight = 600;

    private ISceneController lobbyController;
    private ISceneController waitingController;

    //I'm testing out my tableview
    private ISceneController roomListController;

    private ISceneController playAgainController;

    private ISceneController mainWindowController;

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

        LobbySceneController lobby = new LobbySceneController();
        lobbyController = lobby;

        playAgainController paObject = new playAgainController();
        playAgainController = paObject;

        RoomListController roomlistObject = new RoomListController();
        roomListController = roomlistObject;

        mainWindowController mainObject = new mainWindowController();
        mainWindowController = mainObject;

        waitingController = new WaitingForOpponentScene();


        lobby.getOpenButton().setOnAction(value ->  {
            System.out.println("Opening");

            waitingController.applyScene(primaryStage);

            //NetworkManager.forceUUID(nameField.getText());
            NetworkManager.getInstance().requestNewRoom(
                    lobby.getName(),
                    true,
                    responseRoomInfo -> {
                        /*
                        System.out.println("Hi");

                        Platform.runLater(() ->
                            primaryStage.setScene(new Scene(new HBox(), 100, 100))
                        );
                         */

                    },
                    null
            );
        });

        lobby.getJoinButton().setOnAction(value -> {
            System.out.println("Joining " + lobby.getRoomID());

            // TODO This should we where we plug the room from the other player.
            Messages.RoomInfo roomInfo = new Messages.RoomInfo();
            roomInfo.setRoomID(lobby.getRoomID());
            NetworkManager.getInstance().joinRoom(lobby.getName(), roomInfo);
        });

        roomlistObject.applyScene(primaryStage);
        primaryStage.setWidth(initWidth);
        primaryStage.setHeight(initHeight);
        primaryStage.show();


    }

    @Override
    public void stop() throws Exception {
        NetworkManager.getInstance().dieHorribly();

        // TODO We shouldn't be forcing an exit, but... we have no other choice
        System.exit(0);
    }
}

