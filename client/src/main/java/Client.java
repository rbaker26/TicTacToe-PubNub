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

        try {

            primaryStage.setTitle("SABRCATST TicTacToe");

            lobbyController = new LobbySceneController();
            playAgainController = new PlayAgainController();
            waitingController = new WaitingForOpponentScene();
            gameViewController = new GameViewController();


            //mainWindowController mainObject = new mainWindowController();


            lobbyController.setOpenHandler(() -> {
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

            lobbyController.applyScene(primaryStage);
            primaryStage.setWidth(initWidth);
            primaryStage.setHeight(initHeight);
            primaryStage.show();


        }
        catch(Exception ex) {
            // This is so that we get better information on exceptions. By default,
            // JavaFX swallows the exception and closes without saying anything useful.
            System.out.println(ex);
            throw ex;
        }

    }

    @Override
    public void stop() throws Exception {
        NetworkManager.dieIfNeeded();

        // TODO We shouldn't be forcing an exit, but... we have no other choice
        System.exit(0);
    }
}

