import Messages.Channels;
import Messages.Converter;
import Messages.Keys;
import Messages.RoomInfo;
import Engine.Lobby;
import Engine.LobbyManager;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Engine {
    private static List<RoomInfo> RoomList = new ArrayList<>();     // List of all rooms
    private static int newRoomID = 100000;
    private static HashMap<Integer, Lobby> Lobbies = new HashMap<>();
    public static void main(String[] args) {
        // Initialize pubnub stuffs
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Keys.subKey);
        pnConfiguration.setPublishKey(Keys.pubKey);
        try {
            String hostname = InetAddress.getLocalHost().getHostName() + "Engine";
            pnConfiguration.setUuid(hostname);

        }
        catch(Exception ex) {
            System.out.println("Cannot get local host...");
        }

        PubNub pb = new PubNub(pnConfiguration);
        pb.addListener(new RoomsListCallback(pb, RoomList));
        pb.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                System.out.println(message.getChannel() +": " + message.getMessage());
                switch(message.getChannel()) {
                    case Channels.roomRequestChannel:
                        // The following code handles a room create/join request
                        if (message.getPublisher() != pnConfiguration.getUuid()) {
                            RoomInfo roomMsg = Converter.fromJson(message.getMessage(), RoomInfo.class);
                            if(roomMsg.getRoomID() == RoomInfo.defaultRoomID) {
                                // This handles a create room request
                                // Only things grabbed are playerID and who goes first
                                roomMsg.setRoomID(newRoomID);
                                roomMsg.setRoomChannel(Channels.roomChannelSet + newRoomID);
                                Lobbies.put(newRoomID, new Lobby(roomMsg));
                                System.out.println("New room created: " + roomMsg);
                                newRoomID++;
                            } else {
                                if (Lobbies.containsKey(roomMsg.getRoomID())) {
                                    if (Lobbies.get(roomMsg.getRoomID()).getRoomInfo().isAvailable()) {
                                        Lobbies.get(roomMsg.getRoomID()).getRoomInfo().addPlayer(roomMsg.getPlayer1ID());
                                        pb.publish()
                                                .message(Lobbies.get(roomMsg.getRoomID()).getRoomInfo())
                                                .channel(Lobbies.get(roomMsg.getRoomID()).getRoomInfo().getPlayer1Channel())
                                                .async(new PNCallback<PNPublishResult>() {
                                                    @Override
                                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                                        // handle publish result, status always present, result if successful
                                                        // status.isError() to see if error happened
                                                        if (!status.isError()) {
                                                        }
                                                    }
                                                });
                                        pb.publish()
                                                .message(Lobbies.get(roomMsg.getRoomID()).getRoomInfo())
                                                .channel(Lobbies.get(roomMsg.getRoomID()).getRoomInfo().getPlayer2Channel())
                                                .async(new PNCallback<PNPublishResult>() {
                                                    @Override
                                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                                        // handle publish result, status always present, result if successful
                                                        // status.isError() to see if error happened
                                                        if (!status.isError()) {
                                                        }
                                                    }
                                                });
                                        pb.subscribe().channels(Arrays.asList(Lobbies.get(roomMsg.getRoomID()).getRoomInfo().getRoomChannel())).execute();
                                        System.out.println("Lobby created, listening in on " + Lobbies.get(roomMsg.getRoomID()).getRoomInfo().getRoomChannel());
                                        LobbyManager lm = new LobbyManager(pb, Lobbies.get(roomMsg.getRoomID()));
                                    }
                                }
                            }
                        }
                        break;
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
        pb.subscribe().channels(Arrays.asList(Channels.roomListChannel, Channels.roomRequestChannel)).execute();
    }
}
