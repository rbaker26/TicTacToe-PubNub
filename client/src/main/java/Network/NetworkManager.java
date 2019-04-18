package Network;

import Messages.Channels;
import Messages.MoveInfo;
import Messages.PlayerInfo;
import Messages.RoomInfo;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
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
    public static NetworkManager getInstance() {
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
        String hostname = "Unknown";

        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();

        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }

        return hostname;
    }

    //endregion


    private PubNub pn;
    private SubscribeCallback currentListener = null;
    private List<String> currentChannels = null;

    private HeartbeatThreadCallback heartbeatCallback;
    private Thread heartbeatThread;

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
        pnConfiguration.setUuid(uuid + "Client" + uuidModifier);

        pn = new PubNub(pnConfiguration);

        // Now to set up the heartbeat thread.
        heartbeatCallback = new HeartbeatThreadCallback(pn, Channels.clientHeartbeatChannel);
        heartbeatThread = new Thread(heartbeatCallback);
        heartbeatThread.start();

        basePlayer = new PlayerInfo(pnConfiguration.getUuid());
    }

    private PlayerInfo getPlayerInfo(String userID, String privateChannel) {
        try {
            PlayerInfo result = (PlayerInfo) basePlayer.clone();
            result.setId(userID);
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

        if(currentListener != null) {
            pn.removeListener(currentListener);
        }

        // Now do channels
        if(newChannels != null) {
            pn.subscribe().channels(newChannels).execute();
        }

        if(currentChannels != null) {
            pn.unsubscribe().channels(currentChannels).execute();
        }

        // Aaaaand we can finally save stuff.
        currentListener = newListener;
        currentChannels = newChannels;
    }

    /**
     * Removes current listener and unsubscribes from all channels.
     */
    private void removeListener() {
        changeListener(null, null);
    }

    /**
     * Starts listening for rooms.
     * @param onUpdateCallback Gets fired whenever there is a new room list.
     */
    public void listenForRooms(Consumer<List<RoomInfo>> onUpdateCallback) {

        changeListener(new RoomListCallback(onUpdateCallback), Arrays.asList(Channels.roomListChannel));
    }

    /**
     * Asks the engine for a new room.
     * @param userID Our ID.
     * @param goingFirst If true, we'll be going first.
     * @param successResponse Is called upon successfully getting a room. If null, the listeners will be cleared.
     * @param failureResponse Is called upon unsuccessfully getting a room. If null, the listeners will be cleared.
     */
    public void requestNewRoom(String userID, boolean goingFirst,
                               Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {

        //clearCurrentListener();

        String incomingChannel = Channels.privateChannelSet + userID;
        //String outgoingChannel = Channels.privateChannelSet + userID;
        String outgoingChannel = Channels.roomRequestChannel;

        RoomInfo room = new RoomInfo();
        //roomInfo.setPlayer(userID, incomingChannel, goingFirst);
        PlayerInfo player = getPlayerInfo(userID, incomingChannel);

        if(goingFirst) {
            room.setPlayer1(player);
        }
        else {
            room.setPlayer2(player);
        }


        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                userID, outgoingChannel, incomingChannel, room
        );


        if(successResponse == null) {
            successResponse = (info) -> {
                removeListener();
            };
        }
        if(failureResponse == null) {
            failureResponse = (info) -> {
                removeListener();
            };
        }

        callback.setSuccessResponse(successResponse);
        callback.setFailureResponse(failureResponse);


        changeListener(callback, Arrays.asList(incomingChannel));
    }

    /**
     * Call the other joinRoom function instead.
     * @param ourUserID
     * @param roomInfo
     */
    @Deprecated
    public void joinRoom(String ourUserID, RoomInfo roomInfo) {
        joinRoom(ourUserID, roomInfo, null, null);
    }

    /**
     * Joins a room.
     * @param ourUserID Our user ID.
     * @param roomInfo Info on the room. (Probably should have come as a result of listenForRooms.
     * @param successResponse Called upon successfully joining the room. If null, the listeners will be cleared.
     * @param failureResponse Called upon unsuccessfully joining the room. If null, the listeners will be cleared.
     */
    public void joinRoom(String ourUserID, RoomInfo roomInfo,
                         Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {
        String incomingChannel = Channels.privateChannelSet + roomInfo.getPlayer1Name();
        String outgoingChannel = Channels.roomRequestChannel;

        // If player2 is already around, then we'll be going first.
        // Otherwise, we'll go second.
        PlayerInfo player = getPlayerInfo(ourUserID, incomingChannel);
        roomInfo.addPlayer(player);

        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                ourUserID, outgoingChannel, incomingChannel, roomInfo
        );


        if(successResponse == null) {
            successResponse = (info) -> {
                removeListener();
            };
        }
        if(failureResponse == null) {
            failureResponse = (info) -> {
                removeListener();
            };
        }

        callback.setSuccessResponse(successResponse);
        callback.setFailureResponse(failureResponse);


        changeListener(callback, Arrays.asList(incomingChannel));
    }

    /**
     * Do anything possible to end this instance at all costs instantaneously without regrets.
     */
    private void dieHorribly() {
        System.out.println("Going down");

        heartbeatCallback.setAlive(false);
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

    /**
     * Sends move info out
     */
    public void sendMove(int row, int col, int roomID, String playerID) {
        System.out.println("Publishing on " + Channels.roomMoveChannel);
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
}
