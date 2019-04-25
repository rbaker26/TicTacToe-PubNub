import Messages.Channels;
import Messages.LoginInfo;
import Messages.MoveRequest;
import Messages.RoomInfo;
import Network.LoginRequestCallback;
import Network.NetworkManager;
import UI.*;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import EngineLib.Lobby;

import java.net.NetworkInterface;


// NOTE: To change the uuid, go to the launch configurations and add this to the "Arguments" field
//   -Pargs=Something
// This will tack on "Something" to the UUID. Make several launch configs if you want.

public class Client extends Application {

    private static final double initWidth = 800;
    private static final double initHeight = 600;

    private PubNub pb;

    private LobbySceneController lobbyController;
    private WaitingForOpponentScene waitingController;
    private PlayAgainController playAgainController;
    private GameViewController gameViewController;
    private loginController loginController;
    private GameScoreController gameScoreController;
    private mainWindowController mainWindowController;
    private String userName;
    private String playerName;

    private LoginUserController loginUserController;
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
            //gameViewController = new GameViewController();
            gameScoreController = new GameScoreController();
            loginController = new loginController();
            mainWindowController = new mainWindowController();



            lobbyController.setOpenHandler(() -> {
                System.out.println("Opening");

                waitingController.applyScene(primaryStage);

                //Network.NetworkManager.forceUUID(nameField.getText());
                NetworkManager.getInstance().requestNewRoom(
                        userName,
                        true,
                        responseRoom -> {
                            System.out.println("Connected (creating): " + responseRoom.toString());
                            connectToGame(primaryStage, userName, responseRoom);
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
                        userName,
                        room,
                        responseRoom -> {
                            System.out.println("Connected (joining): " + responseRoom.toString());
                            connectToGame(primaryStage, userName, responseRoom);
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

            /**
             * Starting from the main window, when the user selects
             * Multiplayer button, they get transferred to the
             * lobby window.
             */
            mainWindowController.getMultiPlayerButton().setOnAction(value -> {

                lobbyController.applyScene(primaryStage);

            });

            /**
             * Both the easyAI and hardAI buttons will switch to the board ui
             */
            mainWindowController.getEasyAIButton().setOnAction(value -> {

                //TAKE TO BOARD UI
                System.out.println("Easy AI Button works!");

                gameViewController.applyScene(primaryStage);

            });

            mainWindowController.getHardAIButton().setOnAction(value -> {

                //TAKE TO BOARD UI
                System.out.println("Hard AI Button works!");

                gameViewController.applyScene(primaryStage);

            });

            lobbyController.getBackButton().setOnAction(value -> {

                mainWindowController.applyScene(primaryStage);

            });

            mainWindowController.getGameHistoryButton().setOnAction(value -> {

                gameScoreController.applyScene(primaryStage);

            });

            gameScoreController.getBackButton().setOnAction(value -> {

                mainWindowController.applyScene(primaryStage);

            });

            //User enters their credentials - creates account
            loginController.getCreateButton().setOnAction(value ->  {

                String usr; //username
                String psw; //password
                String scn; //screenName
                usr = loginController.getUsernameField();
                psw = loginController.getPasswordField();
                scn = loginController.getScreenNameField();

                LoginInfo loginObject = new LoginInfo(usr, psw, scn);
                System.out.println("Sending creation request");
                NetworkManager.getInstance().createLogin(loginObject,
                        (unused) -> {
                                System.out.println("New user created");
                                userName = usr;
                                playerName = scn;
                                Platform.runLater(() -> mainWindowController.applyScene(primaryStage));

                        },

                        (reason) -> {
                            //IF CREATING A USER FAILS
//                            Alert alert = new Alert(Alert.AlertType.ERROR);
//                            alert.setTitle("Failed to create new player");
//                            alert.setHeaderText("Username already taken!");
//                            alert.setContentText("Try a different username.");
//
//                            alert.showAndWait();
                            Platform.runLater(() -> respondToFailedAuthorization(reason));

                        });

                System.out.println("Creating new player");

            });

            //User enters their credentials - login check
            loginController.getEnterButton().setOnAction(value -> {
                String usr;
                String psw;
                String scn;
                usr = loginController.getUsernameField();
                psw = loginController.getPasswordField();
                scn = loginController.getScreenNameField();
                userName = usr;
                LoginInfo loginObject = new LoginInfo(usr, psw, scn);

                NetworkManager.getInstance().userLogin(loginObject,
                        (pnm) -> {
                            System.out.println("Successful Login");
                            // TODO SUCCESS CODE GOES HERE
                            playerName = pnm;
                            System.out.println("Username: " + userName);
                            System.out.println("Screenname: " + playerName);
                            Platform.runLater(() -> mainWindowController.applyScene(primaryStage));
                        },
                        (reason) -> {
                            // TODO FAIL CODE GOES HERE
                            Platform.runLater(() -> respondToFailedAuthorization(reason));
                        });
                System.out.println("Welcome player");

            });


            //mainWindowController.applyScene(primaryStage);
            //gameScoreController.applyScene(primaryStage);
            loginController.applyScene(primaryStage);
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
        gameViewController = new GameViewController(room, ourUserID);

        NetworkManager.getInstance().joinRoom(ourUserID, room, (board) -> {
            gameViewController.updateBoard(board);
            gameViewController.toggleTurn();
        });
        gameViewController.applySceneAsync(primaryStage);
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

    /**
     * This method will display an error message on a failed login/auth creation
     */
    private void respondToFailedAuthorization(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed Authorization");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

