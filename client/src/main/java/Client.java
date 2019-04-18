

import Network.NetworkManager;
import UI.*;
import javafx.application.Application;
import javafx.stage.Stage;


// NOTE: To change the uuid, go to the launch configurations and add this to the "Arguments" field
//   -Pargs=Something
// This will tack on "Something" to the UUID. Make several launch configs if you want.

public class Client extends Application {

    private static final double initWidth = 800;
    private static final double initHeight = 600;

    private LobbySceneController lobbyController;
    private WaitingForOpponentScene waitingController;
    private PlayAgainController playAgainController;
    //private ISceneController mainWindowController;

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

        lobbyController = new LobbySceneController();
        //Network.NetworkManager.getInstance();

        playAgainController = new PlayAgainController();

        //mainWindowController mainObject = new mainWindowController();

        waitingController = new WaitingForOpponentScene();


        lobbyController.getOpenButton().setOnAction(value ->  {
            System.out.println("Opening");

            waitingController.applyScene(primaryStage);

            //Network.NetworkManager.forceUUID(nameField.getText());
            NetworkManager.getInstance().requestNewRoom(
                    lobbyController.getName(),
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

        lobbyController.getJoinButton().setOnAction(value -> {
            System.out.println("Joining " + lobbyController.getRoomID());

            // TODO This should we where we plug the room from the other player.
            Messages.RoomInfo roomInfo = new Messages.RoomInfo();
            roomInfo.setRoomID(lobbyController.getRoomID());
            NetworkManager.getInstance().joinRoom(lobbyController.getName(), roomInfo);
        });


        waitingController.setOnCancel(value -> {
            NetworkManager.getInstance().stopWaitingForRoom();
            lobbyController.applySceneAsync(primaryStage);
        });

	/*
        Label turnOrder = new Label("Go first?");
        CheckBox goFirst = new CheckBox();

        Button openButton = new Button("Open");
        Button joinButton = new Button("Join");
        Button refreshButton = new Button("Get Room List");

        openButton.setOnAction(value ->  {
            System.out.println("Opening");


            Network.NetworkManager.getInstance().requestNewRoom(nameField.getText(), goFirst.isSelected());
        });

        joinButton.setOnAction(e -> {
            System.out.println("Joining");
            RoomInfo room = new RoomInfo(Integer.parseInt(roomField.getText()), nameField.getText());
            System.out.println(room);
            Network.NetworkManager.getInstance().joinLobby(room);
        });

        refreshButton.setOnAction(e -> {
            System.out.println("Getting rooms");
            Network.NetworkManager.getInstance().getRoomList();
        });

        // FOLLOWING FOR TESTING
        Label rowLabel = new Label("Row: ");
        TextField rowField = new TextField();
        Label colLabel = new Label("Col: ");
        TextField colField = new TextField();
        Button moveButton = new Button("Send Move");

        moveButton.setOnAction(e -> {
            Network.NetworkManager.getInstance().sendMove(Integer.parseInt(rowField.getText()), Integer.parseInt(colField.getText()), 100000, nameField.getText());
        });

        HBox hbox = new HBox(rowLabel, rowField, colLabel, colField);
        VBox vbox = new VBox(nameLabel, nameField, roomLabel, roomField, turnOrder, goFirst, openButton, joinButton, refreshButton, hbox, moveButton);
>>>>>>> Engine
	*/



        lobbyController.applyScene(primaryStage);
        primaryStage.setWidth(initWidth);
        primaryStage.setHeight(initHeight);
        primaryStage.show();


    }

    @Override
    public void stop() throws Exception {
        NetworkManager.dieIfNeeded();

        // TODO We shouldn't be forcing an exit, but... we have no other choice
        System.exit(0);
    }
}

