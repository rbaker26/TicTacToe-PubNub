import Heartbeat.HeartbeatCallback;
import Messages.Channels;
import Messages.Keys;
import Messages.RoomInfo;
import EngineLib.Lobby;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;


public class Engine {
    private PNConfiguration pnConfiguration = new PNConfiguration();
    private PubNub pb;
    private String myUuid;
    private List<RoomInfo> RoomList = Collections.synchronizedList(new ArrayList<>());     // List of all rooms
    private Map<Integer, Lobby> Lobbies = Collections.synchronizedMap(new HashMap<>());
    private static int newRoomID = 100000;
    private RoomsListListener rml;
    private GameRequestListener grl;
    private MoveListener ml;
    private HeartbeatCallback heartbeatCallback;

    public Engine() {
        pnConfiguration.setSubscribeKey(Keys.subKey);
        pnConfiguration.setPublishKey(Keys.pubKey);

        try {
            InetAddress local = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(local);
            if(network == null) {
                network = NetworkInterface.getNetworkInterfaces().nextElement();
            }
            byte[] mac = network.getHardwareAddress();
            StringBuilder uuid = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                uuid.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            uuid.append("engine");
            myUuid = uuid.toString();
            pnConfiguration.setUuid(myUuid);

        }
        catch(Exception ex) {
            System.out.println(ex);
            System.out.println("Cannot get local host... Creating default UUID");
            pnConfiguration.setUuid(pnConfiguration.getUuid() + "engine");
            myUuid = pnConfiguration.getUuid() + "engine";
        }

        heartbeatCallback = new HeartbeatCallback(Channels.clientHeartbeatChannel, true);
        pb = new PubNub(pnConfiguration);
        rml = new RoomsListListener(RoomList, myUuid);
        grl = new GameRequestListener(RoomList, Lobbies, myUuid, heartbeatCallback);
        ml = new MoveListener(Lobbies);
        Subscribe();
    }

    public void Subscribe() {
        pb.subscribe().
                channels(Arrays.asList(
                        Channels.roomListChannel,
                        Channels.roomRequestChannel,
                        Channels.roomMoveChannel,
                        Channels.clientHeartbeatChannel)).execute();
        pb.addListener(rml);
        pb.addListener(grl);
        pb.addListener(ml);
        pb.addListener(heartbeatCallback);

    }

    public static int getNewRoomID() {
        int id = newRoomID;
        newRoomID++;
        return id;
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
    }

}
