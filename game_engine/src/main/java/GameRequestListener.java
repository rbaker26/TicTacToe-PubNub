import Heartbeat.HeartbeatCallback;
import Messages.Channels;
import Messages.Converter;
import Messages.MoveRequest;
import Messages.RoomInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import EngineLib.Lobby;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

/**
 * This listener will handle all Creation/Join requests on the Rooms::Requests channel.
 */
public class GameRequestListener extends SubscribeCallback {
    private List<RoomInfo> roomInfoList;
    private Map<Integer, Lobby> lobbyList;
    private String myUuid;
    private String myChannel;
    private HeartbeatCallback hbCallback;
    ExecutorService lobbyThreads = Executors.newCachedThreadPool();

    public GameRequestListener(List<RoomInfo> roomList, Map<Integer, Lobby> lobbyList, String uuid) {
        this.roomInfoList = roomList;
        this.lobbyList = lobbyList;
        this.myUuid = uuid;
        this.myChannel = Channels.roomRequestChannel;
    }

    public boolean isCreateRequest(RoomInfo roomRequest) {
        return roomRequest.getRoomID() == RoomInfo.defaultRoomID;
    }

    public boolean roomIsValid(RoomInfo roomRequest) {
        return lobbyList.containsKey(roomRequest.getRoomID()) &&
                lobbyList.get(roomRequest.getRoomID()).getRoomInfo().isAvailable();
    }

    public void createRoom(PubNub pb, RoomInfo roomMsg) {
        int roomID = roomMsg.getRoomID();

        System.out.println("Create room request received: " + roomMsg);
        roomID = Engine.getNewRoomID();
        roomMsg.setRoomID(roomID);
        roomMsg.setRoomChannel(Channels.roomChannelSet + roomID);
        roomInfoList.add(roomMsg);
        lobbyList.put(roomID, new Lobby(roomMsg));
        pb.publish().message(roomInfoList.toArray())
                .channel(Channels.roomListChannel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle publish result, status always present, result if successful
                        // status.isError() to see if error happened
                        if (!status.isError()) {
                        }
                    }
                });

        //hbCallback.setExpireCallback();
    }

    public void joinRoom(PubNub pb, RoomInfo roomMsg) {

        int roomID = roomMsg.getRoomID();

        if (roomIsValid(roomMsg)) {
            System.out.println("Join room request received: " + roomMsg);
            RoomInfo room = lobbyList.get(roomID).getRoomInfo();
            room.addPlayer(roomMsg.getPlayer1());       // TODO Look into this
            for(int roomIndex = 0; roomIndex < roomInfoList.size(); roomIndex++) {
                if(roomInfoList.get(roomIndex).getRoomID() == roomID) {
                    roomInfoList.remove(roomIndex);
                }
            }
            pb.publish() // Publishing updated room list
                    .message(roomInfoList)
                    .channel(Channels.roomListChannel)
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            // handle publish result, status always present, result if successful
                            // status.isError() to see if error happened
                            if (!status.isError()) {
                            }
                        }
                    });
            pb.publish() // Notifying player 1 that game has started
                    .message(room)
                    .channel(room.getPlayer1().getChannel())
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            // handle publish result, status always present, result if successful
                            // status.isError() to see if error happened
                            if (!status.isError()) {
                            }
                        }
                    });
            pb.publish() // Notifying player 2 that game has started
                    .message(room)
                    .channel(room.getPlayer2().getChannel())
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            // handle publish result, status always present, result if successful
                            // status.isError() to see if error happened
                            if (!status.isError()) {
                            }
                        }
                    });
            // TODO This should probably not happen until both have joined the room.
            pb.publish() // Sending initial move request
                    .message(new MoveRequest(lobbyList.get(roomID).getBoard(),
                            room,
                            room.getPlayer1Name()))
                    .channel(Channels.roomChannelSet + room.getRoomID())
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            // handle publish result, status always present, result if successful
                            // status.isError() to see if error happened
                            if (!status.isError()) {
                            }
                        }
                    });
            lobbyList.get(roomID).toggleCurrentPlayer();
            System.out.println("Game started for room " + roomID);
        }
    }

    @Override
    public void status(PubNub pb, PNStatus status) {
    }

    @Override
    public void message(PubNub pb, PNMessageResult message) {
        if(message.getChannel().equals(myChannel)) {
            if (!message.getPublisher().equals(myUuid)) {
                RoomInfo roomMsg = Converter.fromJson(message.getMessage(), RoomInfo.class);
                if(isCreateRequest(roomMsg)) {
                    createRoom(pb, roomMsg);
                }
                else {
                    joinRoom(pb, roomMsg);
                }
            }
        }
    }

    @Override
    public void presence(PubNub pb, PNPresenceEventResult presence) {

    }
}
