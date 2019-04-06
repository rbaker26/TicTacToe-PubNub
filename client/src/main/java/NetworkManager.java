import Messages.NewRoomInfo;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public final class NetworkManager {

    //region Channel stuff
    private static final String roomChannelSet = "Rooms::";
    private static final String privateChannelSet = "Private::";

    private static final String roomRequestChannel = roomChannelSet + "Requests";
    //endregion

    //region Instance setup
    private static final String subKey = "sub-c-89b1d65a-4f40-11e9-bc3e-aabf89950afa";
    private static final String pubKey = "pub-c-79140f4c-8b9e-49ed-992f-cf6322c68d04";
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
        pnConfiguration.setSubscribeKey(subKey);
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setUuid(uuid + "Client" + uuidModifier);

        pn = new PubNub(pnConfiguration);
    }
    //endregion

    private PubNub pn;
    private SubscribeCallback currentListener = null;

    private void clearCurrentListener() {
        // We have this mainly so that we can use it in a lambda expression.
        if(currentListener != null) {
            pn.removeListener(currentListener);
            currentListener = null;
        }
    }

    public void requestNewRoom(String userID, boolean goingFirst) {

        //clearCurrentListener();

        String incomingChannel = privateChannelSet + userID;
        System.out.println(incomingChannel);

        NewRoomInfo roomInfo = new NewRoomInfo();
        roomInfo.setPlayer(userID, incomingChannel, goingFirst);

//        RoomRequesterCallback callback = new RoomRequesterCallback(
//                userID, roomRequestChannel, incomingChannel, roomInfo
//        );
//
//        pn.addListener(callback);
//        pn.subscribe().channels(Arrays.asList(incomingChannel)).execute();

        RoomRequesterCallback callback = new RoomRequesterCallback(
                userID, incomingChannel, incomingChannel, roomInfo
        );

        callback.setSuccessResponse(responseRoomInfo -> {
            pn.removeListener(callback);
            pn.unsubscribeAll();
        });

        pn.addListener(callback);
        pn.subscribe().channels(Arrays.asList(incomingChannel)).execute();
    }

    /**
     * Joins a room which has been created via request requestNewRoom.
     * @param roomInfo
     */
    public void joinLobby(NewRoomInfo roomInfo) {
        throw new NotImplementedException();
    }
}
