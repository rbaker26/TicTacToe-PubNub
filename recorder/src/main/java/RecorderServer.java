import Messages.Channels;
import Messages.Keys;
import Messages.RoomInfo;
import EngineLib.Lobby;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class RecorderServer {
    private PNConfiguration pnConfiguration = new PNConfiguration();
    private PubNub pb;
    private String myUuid;
    private List<RoomInfo> RoomList = Collections.synchronizedList(new ArrayList<>());     // List of all rooms
    private Map<Integer, Lobby> Lobbies = Collections.synchronizedMap(new HashMap<>());
    private static int newRoomID = 100000;

    private DataListener dataListener;

    public RecorderServer() {
        pnConfiguration.setSubscribeKey(Keys.subKey);
        pnConfiguration.setPublishKey(Keys.pubKey);

        try {
            byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            StringBuilder uuid = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                uuid.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            uuid.append("recorder");
            myUuid = uuid.toString();
            pnConfiguration.setUuid(myUuid);

        }
        catch(Exception ex) {
            System.out.println("Cannot get local host... Creating default UUID");
            myUuid = pnConfiguration.getUuid();
        }

        pb = new PubNub(pnConfiguration);
        dataListener = new DataListener(RoomList, Lobbies, myUuid);
        Subscribe();
    }


    public void Subscribe() {
        pb.subscribe().
                channels(Arrays.asList(
                        Channels.roomListChannel,
                        Channels.roomRequestChannel,
                        Channels.roomMoveChannel)).execute();
        pb.addListener(dataListener);


    }

    public static void main(String[] args) {
        RecorderServer rs = new RecorderServer();
    }

}

        //*********************************************
        // Get Database Connection
        //*********************************************
       // Db_Manager db = Db_Manager.GetInstance();
        //*********************************************


        //*********************************************
        // Connect to PubNub
        //*********************************************


        //*********************************************


//       ArrayDeque<JsonObject> bad_transmit = new ArrayDeque<JsonObject>();
//       int toggle = 0;
//        try {
//            while (true) {
//                //*********************************************
//                // Get Pubnub Message
//                //*********************************************
//                JsonObject json = new JsonObject();
//
//                //*********************************************
//
//
//                //*********************************************
//                // Call Appropriate DB Function
//                //*********************************************
//                try {
//                    // Send to data base
//                    throw new SQLException();
//                }
//                catch(SQLException sqle) {
//                    bad_transmit.add(json);
//                }
//                finally {
//                    toggle = (toggle + 1) %4;
//                    if(toggle == 1) {
//                        try{
//                            // Resend message
//                            throw new SQLException();
//                        }
//                        catch(SQLException sqle) {
//
//                        }
//
//                    }
//                }
//                //*********************************************
//
//            }
//        }
//        catch(Exception e) {
//            // Exit server
//        }
//        // Test code
////        Move move = new Move(1,2,3,4);
////        System.out.println(move);
////
////        try {
////            //db.WriteMove(99553, 1, 1, 6587);
////            //db.WritePlayer("Homi", 37159);
////
////            db.WriteMove(10000, 99, 99, 4653);
////
//////            for(int i = 0; i < 3; i++) {
//////                for(int j = 0; j < 3; j++) {
//////                    db.WriteMove(10000, i, j, 4653);
//////                    Thread.sleep(1000);
//////                }
//////            }
////        }
////        catch(Exception e){
////            System.out.println(e.toString());
////        }
//    }
//
//
//
//    public Move GetMoveFromPubNub() {
//
//
//
//
//        return new Move(0,0,0,0);
//    }

