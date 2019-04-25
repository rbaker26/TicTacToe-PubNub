import EngineLib.Board;
import Messages.Channels;
import Messages.LoginInfo;
import Messages.MoveRequest;
import Messages.RoomFactory;
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


            lobbyController.setOpenHandler(requestInfo -> {
                System.out.println("Opening " + requestInfo);

                waitingController.applyScene(primaryStage);

                NetworkManager.getInstance().requestNewRoom(
                        RoomFactory.makeCreateRequest(requestInfo.isGoingFirst(), requestInfo.getPassword()),
                        responseRoom -> {
                            System.out.println("Connected (creating): " + responseRoom.toString());
                            connectToGame(primaryStage, userName, responseRoom, lobbyController);
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
                        room,
                        responseRoom -> {
                            System.out.println("Connected (joining): " + responseRoom.toString());
                            connectToGame(primaryStage, userName, responseRoom, lobbyController);
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


            mainWindowController.getEasyAIButton().setOnAction(value -> {
                System.out.println("Easy AI");
                //waitingController.applyScene(primaryStage);

                // TODO This is a bit of a hack to make stuff work. Need to figure out why this is needed
                NetworkManager.getInstance().listenForRooms(null);

                NetworkManager.getInstance().requestEasyAIRoom(
                        RoomFactory.makeCreateRequest(true, ""),
                        responseRoom -> {
                            System.out.println("Connected (creating): " + responseRoom.toString());
                            connectToGame(primaryStage, userName, responseRoom, mainWindowController);
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

            mainWindowController.getHardAIButton().setOnAction(value -> {
                System.out.println("Hard AI");
                //waitingController.applyScene(primaryStage);

                // TODO This is a bit of a hack to make stuff work. Need to figure out why this is needed
                NetworkManager.getInstance().listenForRooms(null);

                NetworkManager.getInstance().requestHardAIRoom(
                        RoomFactory.makeCreateRequest(true, ""),
                        responseRoom -> {
                            System.out.println("Connected (creating): " + responseRoom.toString());
                            connectToGame(primaryStage, userName, responseRoom, mainWindowController);
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


            lobbyController.getBackButton().setOnAction(value -> {

                mainWindowController.applyScene(primaryStage);

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
                                NetworkManager.getInstance().setName(userName, playerName);
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
                            NetworkManager.getInstance().setName(userName, playerName);
                            Platform.runLater(() -> mainWindowController.applyScene(primaryStage));
                        },
                        (reason) -> {
                            // TODO FAIL CODE GOES HERE
                            Platform.runLater(() -> respondToFailedAuthorization(reason));
                        });
                System.out.println("Welcome player");

            });

            //Logging out of game is done here
            mainWindowController.getLogoutButton().setOnAction(value -> {

                userName = null;
                playerName = null;
                loginController.clearFields();
                loginController.applyScene(primaryStage);

            });

            loginController.applyScene(primaryStage);
            primaryStage.setWidth(initWidth);
            primaryStage.setHeight(initHeight);
            primaryStage.show();


        }
        catch(Exception ex) {
            // This is so that we get better information on exceptions. By default,
            // JavaFX swallows the exception and closes without saying anything useful.
            ex.printStackTrace();
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
    private void connectToGame(Stage primaryStage, String ourUserID, RoomInfo room, ISceneController conclusionScene) {
        gameViewController = new GameViewController(room, ourUserID);
        NetworkManager.getInstance().joinRoom(ourUserID, room, (board) -> {
            gameViewController.updateBoard(board);
            if(!board.isWinner('X') && !board.isWinner('O') && board.numEmptySpaces() != 0) {
                gameViewController.toggleTurn();
            }
            checkWin(primaryStage, board, room, conclusionScene);
        });
        gameViewController.applySceneAsync(primaryStage);
        primaryStage.setWidth(425);
        primaryStage.setHeight(425);
    }

    private void checkWin(Stage primaryStage, Board board, RoomInfo room, ISceneController conclusionScene) {
        String endResult;
        if(board.isWinner('X') || board.isWinner('O') || board.numEmptySpaces() == 0) {
            if (board.isWinner('X')) {
                endResult = "X Player: " + room.getPlayer1().getName() + " won!";
            } else if (board.isWinner('O')) {
                endResult = "O Player: " + room.getPlayer2().getName() + " won!";
            } else {
                endResult = "Tie game!";
            }
            Platform.runLater(() -> {
                endGameAlert(primaryStage, endResult, conclusionScene);
            });
        }
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

    private void endGameAlert(Stage primaryStage, String message, ISceneController conclusionScene) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText("Game Winning Results");
        alert.setContentText(message);
        alert.showAndWait();

        conclusionScene.applyScene(primaryStage);
        primaryStage.setWidth(initWidth);
        primaryStage.setHeight(initHeight);
    }
}

