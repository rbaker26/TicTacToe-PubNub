import Messages.RoomInfo;
import Network.NetworkManager;
import UI.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
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
                            Platform.runLater(() -> {
                                respondToFailedConnection(
                                        primaryStage,
                                        "Failed to create room"
                                );
                            });
                        }
                );
            });

            lobbyController.setJoinHandler(room -> {
                System.out.println("Selected room: " + room.toString());
                waitingController.applyScene(primaryStage);

                //try { Thread.sleep(5000); } catch(InterruptedException ex) {}

                NetworkManager.getInstance().requestJoinRoom(
                        lobbyController.getName(),
                        room,
                        responseRoom -> {
                            System.out.println("Connected (joining): " + responseRoom.toString());
                            connectToGame(primaryStage, lobbyController.getName(), responseRoom);
                        },
                        responseRoom -> {
                            Platform.runLater(() -> {
                                respondToFailedConnection(
                                        primaryStage,
                                        "The room either no longer exists or is now full."
                                );
                            });
                        }
                );
            });


            waitingController.setOnCancel(value -> {
                NetworkManager.getInstance().clear();
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

        System.exit(0);
    }

    /**
     * Connects to the given game. This can be called whether we created the room or
     * are joining someone else's room.
     * @param primaryStage The main JavaFX stage.
     * @param ourUserID The user's ID.
     * @param room The room to join.
     */
    private void connectToGame(Stage primaryStage, String ourUserID, RoomInfo room) {
        gameViewController.applySceneAsync(primaryStage);
        NetworkManager.getInstance().joinRoom(ourUserID, room);
    }

    /**
     * Does various cleanup if failed to connect to a game. Note that this MUST be
     * run asynchronously.
     * @param primaryStage Stage to draw to.
     * @param message Message to show in the warning box.
     */
    private void respondToFailedConnection(Stage primaryStage, String message) {
        NetworkManager.getInstance().clear();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to join the room");
        alert.setContentText(message);
        alert.showAndWait();

        lobbyController.applyScene(primaryStage);
    }
}

