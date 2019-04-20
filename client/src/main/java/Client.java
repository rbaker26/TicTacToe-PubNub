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
    private GameViewController gameViewController;
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
        playAgainController = new PlayAgainController();
        waitingController = new WaitingForOpponentScene();
        gameViewController = new GameViewController();


        //mainWindowController mainObject = new mainWindowController();



        lobbyController.setOpenHandler(() ->  {
            System.out.println("Opening");

            waitingController.applyScene(primaryStage);

            //Network.NetworkManager.forceUUID(nameField.getText());
            NetworkManager.getInstance().requestNewRoom(
                    lobbyController.getName(),
                    true,
                    responseRoomInfo -> {
                        System.out.println("Connected (creating): " + responseRoomInfo.toString());
                        gameViewController.applySceneAsync(primaryStage);
                    },
                    null
            );
        });

        lobbyController.setJoinHandler(room -> {
            System.out.println("Selected room: " + room.toString());
            NetworkManager.getInstance().joinRoom(
                    lobbyController.getName(),
                    room,
                    responseRoomInfo -> {
                        System.out.println("Connected (joining): " + responseRoomInfo.toString());
                        gameViewController.applySceneAsync(primaryStage);
                    },
                    null
            );
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

