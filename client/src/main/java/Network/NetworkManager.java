package Network;

import Messages.Channels;
import Messages.MoveInfo;
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
    public static NetworkManager getInstance() {
        if(_instance == null) {
            _instance = new NetworkManager();
        }

        return _instance;
    }

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

    private NetworkManager() {
        this(getDefaultUUID());
    }
    private NetworkManager(String uuid) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Messages.Keys.subKey);
        pnConfiguration.setPublishKey(Messages.Keys.pubKey);
        pnConfiguration.setUuid(uuid + "Client" + uuidModifier);

        pn = new PubNub(pnConfiguration);

    }
    //endregion

    private PubNub pn;
    private SubscribeCallback currentListener = null;
    private List<String> currentChannels = null;

    private void changeListener(SubscribeCallback newListener, List<String> newChannels) {
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

    public void requestNewRoom(String userID, boolean goingFirst, Consumer<RoomInfo> successResponse, Consumer<RoomInfo> failureResponse) {

        //clearCurrentListener();

        String incomingChannel = Channels.privateChannelSet + userID;
        //String outgoingChannel = Channels.privateChannelSet + userID;
        String outgoingChannel = Channels.roomRequestChannel;

        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setPlayer(userID, incomingChannel, goingFirst);

        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                userID, outgoingChannel, incomingChannel, roomInfo
        );

        callback.setSuccessResponse(responseRoomInfo -> {
            if(successResponse != null) {
                successResponse.accept(responseRoomInfo);
            }

            changeListener(null, null);
        });
        callback.setFailureResponse(responseRoomInfo -> {
            if(failureResponse != null) {
                failureResponse.accept(responseRoomInfo);
            }

            changeListener(null, null);
        });

        changeListener(callback, Arrays.asList(incomingChannel));
    }

    /**
     * Joins a room which has been created via request requestNewRoom.
     * @param roomInfo
     */
    /*
    public void joinRoom(String userID, RoomInfo roomInfo) {
        String incomingChannel = Channels.privateChannelSet + userID;
        String outgoingChannel = Channels.privateChannelSet + userID;
        //String outgoingChannel = Channels.roomRequestChannel;

        // If player2 is already around, then we'll be going first.
        // Otherwise, we'll go second.
        boolean goingFirst = roomInfo.hasPlayer2();
        roomInfo.setPlayer(userID, incomingChannel, goingFirst);

        Network.RoomRequesterCallback callback = new Network.RoomRequesterCallback(
                userID, outgoingChannel, incomingChannel, roomInfo
        );

        callback.setSuccessResponse(responseRoomInfo -> {
            changeListener(null, null);
        });
        callback.setFailureResponse(responseRoomInfo -> {
            changeListener(null, null);
        });
=======
*/
    public void joinRoom(String ourUserID, RoomInfo roomInfo) {
        String incomingChannel = Channels.privateChannelSet + roomInfo.getPlayer1ID();
        String outgoingChannel = Channels.roomRequestChannel;

        // If player2 is already around, then we'll be going first.
        // Otherwise, we'll go second.
        boolean goingFirst = roomInfo.hasPlayer2();
        roomInfo.setPlayer(ourUserID, incomingChannel, goingFirst);

        RoomRequesterCallback callback = new RoomRequesterCallback(
                //userID, incomingChannel, incomingChannel, roomInfo
                ourUserID, outgoingChannel, incomingChannel, roomInfo
        );

        callback.setSuccessResponse(responseRoomInfo -> {
            /*
            pn.removeListener(callback);
            pn.unsubscribe().channels(Arrays.asList(incomingChannel)).execute();
             */
            changeListener(null, null);
        });

        /*
        pn.addListener(callback);
        pn.subscribe().channels(Arrays.asList(incomingChannel)).execute();
         */

        changeListener(callback, Arrays.asList(incomingChannel));
    }

    /**
     * Requests a room list from the engine
     */
    public void getRoomList() {
        changeListener(new RoomsListCallback(), Arrays.asList(Channels.roomListChannel));
    }

    /**
     * Do anything possible to end this instance at all costs instantaneously without regrets.
     */
    private void dieHorribly() {
        System.out.println("Going down");
        pn.unsubscribeAll();
        pn.disconnect();
        pn.destroy();
        pn = null;
        System.out.println("The pain won't stop");
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
