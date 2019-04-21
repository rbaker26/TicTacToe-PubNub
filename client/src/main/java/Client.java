import Messages.RoomInfo;
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
                        responseRoom -> {
                            System.out.println("Connected (creating): " + responseRoom.toString());
                            connectToGame(primaryStage, lobbyController.getName(), responseRoom);
                        },
                        responseRoom -> {
                            System.out.println("Got rejected");
                        }
                );
            });

            lobbyController.setJoinHandler(room -> {
                System.out.println("Selected room: " + room.toString());

                waitingController.applyScene(primaryStage);

                // TODO Remove the sleep. This is a test to see what happens if the room vanishes before we can join
                try {
                    Thread.sleep(10000);
                }
                catch(InterruptedException ex) {

                }

                NetworkManager.getInstance().requestJoinRoom(
                        lobbyController.getName(),
                        room,
                        responseRoom -> {
                            System.out.println("Connected (joining): " + responseRoom.toString());
                            connectToGame(primaryStage, lobbyController.getName(), responseRoom);
                        },
                        responseRoom -> {
                            System.out.println("Got rejected");
                        }
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

    private void connectToGame(Stage primaryStage, String ourUserID, RoomInfo room) {
        gameViewController.applySceneAsync(primaryStage);
        NetworkManager.getInstance().joinRoom(ourUserID, room);
    }
}

