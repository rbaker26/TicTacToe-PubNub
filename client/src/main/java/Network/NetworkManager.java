package Network;

import Messages.Channels;
import Messages.MoveInfo;
import Messages.PlayerInfo;
import Messages.RoomInfo;
import TaskThreads.TaskThread;
import TaskThreads.TaskThreadFactory;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

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
            byte[] mac = NetworkInterface.getNetworkInterfaces().nextElement().getHardwareAddress();

            if(mac == null) {
                System.out.println("Couldn't get MAC address using the Linux approach, trying Windows approach.");
                mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            }

            /*
            Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
            while(list.hasMoreElements()) {
                System.out.println(list.nextElement());
            }
            */
            //System.out.println(NetworkInterface.getNetworkInterfaces().);
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

        if(currentCallback != null) {
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
     * Asks the engine for a new room.
     * @param userID Our ID.
     * @param room Info on room we're requesting.
     * @param successResponse Is called upon successfully getting a room. If null, the listeners will be cleared.
     * @param failureResponse Is called upon unsuccessfully getting a room. If null, the listeners will be cleared.
     */
    public void requestNewRoom(String userID, RoomInfo room,
                               Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {

        //clearCurrentListener();

        String incomingChannel = Channels.privateChannelSet + pn.getConfiguration().getUuid();
        //String outgoingChannel = Channels.privateChannelSet + userID;
        String outgoingChannel = Channels.roomRequestChannel;

        //RoomInfo room = new RoomInfo();
        //roomInfo.setPlayer(userID, incomingChannel, goingFirst);
        PlayerInfo player = getPlayerInfo(userID, incomingChannel);
        room.setPlayer1(player);    // Set the creator

        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                userID, outgoingChannel, incomingChannel, room, player
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
     * @param ourUserID Our user ID.
     * @param roomInfo Info on the room. (Probably should have come as a result of listenForRooms.
     * @param successResponse Called upon successfully joining the room. If null, the listeners will be cleared.
     * @param failureResponse Called upon unsuccessfully joining the room. If null, the listeners will be cleared.
     */
    public void requestJoinRoom(String ourUserID, RoomInfo roomInfo,
                                Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {

        String incomingChannel = Channels.privateChannelSet + pn.getConfiguration().getUuid();
        String outgoingChannel = Channels.roomRequestChannel;

        // If player2 is already around, then we'll be going first.
        // Otherwise, we'll go second.
        PlayerInfo player = getPlayerInfo(ourUserID, incomingChannel);
        roomInfo.addPlayer(player); // TODO We should just do setPlayer2

        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                ourUserID, outgoingChannel, incomingChannel, roomInfo, player
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

    public void joinRoom(String ourUserID, RoomInfo room) {
        String outgoingChannel = Channels.roomMoveChannel;
        String incomingChannel = room.getRoomChannel();

        PlayerCallback callback = new PlayerCallback(ourUserID, outgoingChannel, room);

        changeListener(callback, Arrays.asList(incomingChannel));
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
