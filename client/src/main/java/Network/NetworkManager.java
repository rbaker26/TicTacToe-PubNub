package Network;

import EngineLib.Board;
import Messages.*;
import TaskThreads.TaskThread;
import TaskThreads.TaskThreadFactory;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import javax.swing.event.ChangeListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

public final class NetworkManager {

    //region Instance setup
    private static String uuidModifier = "";

    private static NetworkManager _instance;

    /**
     * Gets the instance of the network manager. Creates it if necessary.
     * @return The network manager.
     */
    public synchronized static NetworkManager getInstance() {
        if(_instance == null) {
            _instance = new NetworkManager();
        }

        return _instance;
    }

    /**
     * Sets the uuid modifier. This is mainly intended for debug purposes,
     * such as when we want to run multiple instances of the client on
     * a single computer.
     * @param uuidModifier The string to append to the UUID.
     */
    public static void setUuidModifier(String uuidModifier) {
        NetworkManager.uuidModifier = uuidModifier;
    }

    /**
     * Cleans up the network connection if it exists.
     */
    public static void dieIfNeeded() {
        if(_instance != null) {
            _instance.dieHorribly();
            _instance = null;
        }
    }

    private static String getDefaultUUID() {
        //String hostname = "Unknown";
        String myUuid = "";

        try {
            Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            if(network == null) {
                network = list.nextElement();
            }
            byte[] mac = network.getHardwareAddress();
            StringBuilder uuid = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                uuid.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            uuid.append("Client");
            myUuid = uuid.toString();
        }
        catch(Exception ex) {
            System.out.println("Cannot get local host... Creating default UUID");
            System.out.println(ex);
        }


        return myUuid;
    }

    //endregion

    /**
     * This is our instance of the PubNub object... of course.
     */
    private PubNub pn;

    /**
     * This is the current callback.
     */
    private SubscribeCallback currentCallback = null;

    /**
     * This is the list of incoming channels we are subscribed to.
     */
    private List<String> currentChannels = null;

    /**
     * This callback is used for repeatedly sending out heartbeats.
     */
    /*
    private HeartbeatThreadCallback heartbeatCallback;
    private Thread heartbeatThread;
     */
    private TaskThread heartbeatThread;

    /**
     * When you're using this, call getBasePlayer, which makes a clone of this.
     * Modifying this would also change the result of getBasePlayer. Thus, don't
     * change this unless you know what you're doing.
     */
    private PlayerInfo basePlayer;

    private NetworkManager() {
        this(getDefaultUUID());
    }
    private NetworkManager(String uuid) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Messages.Keys.subKey);
        pnConfiguration.setPublishKey(Messages.Keys.pubKey);
        pnConfiguration.setUuid(uuid + uuidModifier);

        pn = new PubNub(pnConfiguration);

        // Now to set up the heartbeat thread.
        /*
        heartbeatCallback = new HeartbeatThreadCallback(pn, Channels.clientHeartbeatChannel);
        heartbeatThread = new Thread(heartbeatCallback);
        heartbeatThread.start();
         */
        heartbeatThread = TaskThreadFactory.makeHeartbeatThread(pn, Channels.clientHeartbeatChannel);
        heartbeatThread.start();

        basePlayer = new PlayerInfo(pnConfiguration.getUuid());
    }

    private PlayerInfo getPlayerInfo(String privateChannel) {
        try {
            PlayerInfo result = (PlayerInfo) basePlayer.clone();
            result.setChannel(privateChannel);
            return result;
        }
        catch(CloneNotSupportedException ex) {
            System.out.println("NetworkManager.getPlayerInfo failed to clone somehow");
            return null;
        }
    }

    /**
     * Changes to another listener. This will override whatever current listener we have.
     * @param newListener The callback which will listen on the channels.
     * @param newChannels The channels to subscribe to.
     * @throws IllegalArgumentException Thrown if newListener is null and newChannels is not, or if newChannels is null
     *                                  and newListener is not.
     */
    private void changeListener(SubscribeCallback newListener, List<String> newChannels) {

        if(newListener == null ^ newChannels == null) {
            throw new IllegalArgumentException("Either both newListener and newChannels must not be null, or they must both be null.");
        }

        // To reduce slowdown, we'll swap in our new listener,
        // subscribe to the new channel, and then unsubscribe
        // from the old channel.

        // Do listeners
        if(newListener != null) {
            pn.addListener(newListener);
        }

        if(currentCallback != null) {
            // Before we remove the old listener, we need to check if they have anything they want to do when
            // they get interrupted.
            if(currentCallback instanceof InterruptibleListener) {
                ((InterruptibleListener) currentCallback).interrupt(pn);
            }

            pn.removeListener(currentCallback);
        }

        // Now do channels
        if(newChannels != null) {
            pn.subscribe().channels(newChannels).execute();
        }

        if(currentChannels != null) {
            pn.unsubscribe().channels(currentChannels).execute();
        }

        // Aaaaand we can finally save stuff.
        currentCallback = newListener;
        currentChannels = newChannels;
    }

    /**
     * Makes the NetworkManager stop whatever it was doing.
     */
    public void clear() {
        changeListener(null, null);
    }


    //region Account management
    public void userLogin(LoginInfo login, Consumer<String> successResponse, Consumer<String> failureResponse) {
        String incomingChannel = Channels.privateChannelSet + pn.getConfiguration().getUuid();
        String outgoingChannel = Channels.authCheckChannel;

        // TODO Fix channels

        LoginRequestCallback lrc = new LoginRequestCallback(
                login,
                outgoingChannel,
                incomingChannel,
                successResponse, failureResponse
        );
        clear();
        changeListener(lrc, Arrays.asList(incomingChannel));
    }

    //endregion


    //region Create User Account
    public void createLogin(LoginInfo login, Consumer<String> successResponse, Consumer<String> failureResponse) {
        String incomingChannel = Channels.privateChannelSet + pn.getConfiguration().getUuid();
        String outgoingChannel = Channels.authCreateChannel;


        LoginRequestCallback lrc = new LoginRequestCallback(
                    login,
                    outgoingChannel,
                    incomingChannel,
                    successResponse, failureResponse

        );
        clear();
        changeListener(lrc, Arrays.asList(incomingChannel));

    }

    //endregion

    public void setName(String userId, String screenName) {
        basePlayer.setId(userId);
        basePlayer.setName(screenName);
    }

    //region Room requests and connections
    /**
     * Starts listening for rooms.
     * @param onUpdateCallback Gets fired whenever there is a new room list.
     */
    public void listenForRooms(Consumer<List<RoomInfo>> onUpdateCallback) {
        changeListener(new RoomListCallback(onUpdateCallback), Arrays.asList(Channels.roomListChannel));
    }

    public boolean isListeningForRooms() {
        return (currentCallback instanceof RoomListCallback);
    }

    /**
     * Ask for an easy AI room.
     * @param room
     * @param successResponse
     * @param failureResponse
     */
    public void requestEasyAIRoom(RoomInfo room, Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {
        room.setPlayer2(PlayerInfo.easyAI());

        requestNewRoom(room, successResponse, failureResponse);
    }

    public void requestHardAIRoom(RoomInfo room, Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {
        room.setPlayer2(PlayerInfo.hardAI());

        requestNewRoom(room, successResponse, failureResponse);
    }

    /**
     * Asks the engine for a new room.
     * @param room Info on room we're requesting.
     * @param successResponse Is called upon successfully getting a room. If null, the listeners will be cleared.
     * @param failureResponse Is called upon unsuccessfully getting a room. If null, the listeners will be cleared.
     */
    public void requestNewRoom(RoomInfo room, Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {

        //clearCurrentListener();


        String incomingChannel = Channels.privateChannelSet + pn.getConfiguration().getUuid();
        //String outgoingChannel = Channels.privateChannelSet + userID;
        String outgoingChannel = Channels.roomRequestChannel;

        //RoomInfo room = new RoomInfo();
        //roomInfo.setPlayer(userID, incomingChannel, goingFirst);
        PlayerInfo player = getPlayerInfo(incomingChannel);
        room.setPlayer1(player);    // Set the creator

        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                outgoingChannel, incomingChannel, room, player
        );


        if(successResponse == null) {
            successResponse = (info) -> {
                clear();
            };
        }
        if(failureResponse == null) {
            failureResponse = (info) -> {
                clear();
            };
        }

        callback.setSuccessCallback(successResponse);
        callback.setFailureCallback(failureResponse);


        changeListener(callback, Arrays.asList(incomingChannel));
    }

    /**
     * Joins a room.
     * @param roomInfo Info on the room. (Probably should have come as a result of listenForRooms.
     * @param successResponse Called upon successfully joining the room. If null, the listeners will be cleared.
     * @param failureResponse Called upon unsuccessfully joining the room. If null, the listeners will be cleared.
     */
    public void requestJoinRoom(RoomInfo roomInfo, Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {

        String incomingChannel = Channels.privateChannelSet + pn.getConfiguration().getUuid();
        String outgoingChannel = Channels.roomRequestChannel;

        // If player2 is already around, then we'll be going first.
        // Otherwise, we'll go second.
        PlayerInfo player = getPlayerInfo(incomingChannel);
        roomInfo.addPlayer(player); // TODO We should just do setPlayer2

        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                outgoingChannel, incomingChannel, roomInfo, player
        );


        if(successResponse == null) {
            successResponse = (info) -> {
                clear();
            };
        }
        if(failureResponse == null) {
            failureResponse = (info) -> {
                clear();
            };
        }

        callback.setSuccessCallback(successResponse);
        callback.setFailureCallback(failureResponse);

        changeListener(callback, Arrays.asList(incomingChannel));
    }

   // public static final String authCreateChannel = userAuthChannelSet + "Create";

    public void joinRoom(String ourUserID, RoomInfo room, Consumer<Board> response) {
        String outgoingChannel = Channels.roomMoveChannel;
        String incomingChannel = room.getRoomChannel();

        PlayerCallback callback = new PlayerCallback(ourUserID, outgoingChannel, room, response);

        changeListener(callback, Arrays.asList(incomingChannel));
    }
    //endregion

    //region Gameplay
    /**
     * Sends move info out
     */
    public void sendMove(int row, int col, int roomID, String playerID) {
        System.out.println("Publishing on " + Channels.roomMoveChannel);
        System.out.printf("Row: %d%n Col: %d%n RoomID: %d%n, Name: %s%n", row, col, roomID, playerID);
        pn.publish()
                .message(new MoveInfo(roomID, playerID, row, col))
                .channel(Channels.roomMoveChannel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle publish result, status always present, result if successful
                        // status.isError() to see if error happened
                        if (!status.isError()) {
                        }
                    }
                });
    }
    //endregion

    /**
     * Do anything possible to end this instance at all costs instantaneously without regrets.
     */
    private void dieHorribly() {
        System.out.println("Going down");

        clear();

        //heartbeatCallback.setAlive(false);
        heartbeatThread.stop();
        try {
            System.out.print("My heart...");
            heartbeatThread.join();
            System.out.println("has stopped...");
        }
        catch(InterruptedException ex) {
            System.out.println("Wow, so impatient! I'll die faster, geez.");
        }

        pn.unsubscribeAll();
        pn.disconnect();
        pn.destroy();
        pn = null;

        System.out.println("The pain won't stop...");
    }


}
