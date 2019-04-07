import Messages.Channels;
import Messages.RoomInfo;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

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

    public void requestNewRoom(String userID, boolean goingFirst) {

        //clearCurrentListener();

        String incomingChannel = Channels.privateChannelSet + userID;
        String outgoingChannel = Channels.privateChannelSet + userID;
        //String outgoingChannel = Channels.roomRequestChannel;

        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setPlayer(userID, incomingChannel, goingFirst);

        RoomRequesterCallback callback = new RoomRequesterCallback(
                userID, outgoingChannel, incomingChannel, roomInfo
        );

        callback.setSuccessResponse(responseRoomInfo -> {
            changeListener(null, null);
        });
        callback.setFailureResponse(responseRoomInfo -> {
            changeListener(null, null);
        });

        changeListener(callback, Arrays.asList(incomingChannel));
    }

    /**
     * Joins a room which has been created via request requestNewRoom.
     * @param userID
     * @param roomInfo
     */
    public void joinRoom(String userID, RoomInfo roomInfo) {
        String incomingChannel = Channels.privateChannelSet + userID;
        String outgoingChannel = Channels.privateChannelSet + userID;
        //String outgoingChannel = Channels.roomRequestChannel;

        // If player2 is already around, then we'll be going first.
        // Otherwise, we'll go second.
        boolean goingFirst = roomInfo.hasPlayer2();
        roomInfo.setPlayer(userID, incomingChannel, goingFirst);

        RoomRequesterCallback callback = new RoomRequesterCallback(
                userID, outgoingChannel, incomingChannel, roomInfo
        );

        callback.setSuccessResponse(responseRoomInfo -> {
            changeListener(null, null);
        });
        callback.setFailureResponse(responseRoomInfo -> {
            changeListener(null, null);
        });

        changeListener(callback, Arrays.asList(incomingChannel));
    }

    public void dieHorribly() {
        System.out.println("Going down");
        pn.unsubscribeAll();
        pn.disconnect();
        pn.destroy();
        pn = null;
        System.out.println("The pain won't stop");
    }
}
