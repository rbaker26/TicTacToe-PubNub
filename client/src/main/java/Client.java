<<<<<<< HEAD

import com.google.gson.*;
import com.pubnub.api.*;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;


public class Client {

    public static void main(String[] args) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("demo");
        pnConfiguration.setPublishKey("demo");

        PubNub pubnub = new PubNub(pnConfiguration);

        String channelName = "awesomeChannel";

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();
        messageJsonObject.addProperty("msg", "hello");

        System.out.println("Message to send: " + messageJsonObject.toString());

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {


                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (!status.isError()) {

                                    // Message successfully published to specified channel.
                                }
                                // Request processing failed.
                                else {

                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                }
                            }
                        });
                    }
                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

                JsonElement receivedMessageObject = message.getMessage();
                System.out.println("Received message content: " + receivedMessageObject.toString());
                // extract desired parts of the payload, using Gson
                String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
                System.out.println("msg content: " + msg);


            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channels(Arrays.asList(channelName)).execute();
    }
}
=======


import Network.NetworkManager;
import UI.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


// NOTE: To change the uuid, go to the launch configurations and add this to the "Arguments" field
//   -Pargs=Something
// This will tack on "Something" to the UUID. Make several launch configs if you want.

public class Client extends Application {

    private static final double initWidth = 800;
    private static final double initHeight = 600;

    private ISceneController lobbyController;
    private ISceneController waitingController;
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
        //Network.NetworkManager.getInstance();

        playAgainController paObject = new playAgainController();
        playAgainController = paObject;

        mainWindowController mainObject = new mainWindowController();
        mainWindowController = mainObject;

        waitingController = new WaitingForOpponentScene();


        lobby.getOpenButton().setOnAction(value ->  {
            System.out.println("Opening");

            waitingController.applyScene(primaryStage);

            //Network.NetworkManager.forceUUID(nameField.getText());
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

>>>>>>> origin/lobby
