import Heartbeat.HeartbeatListener;
import EngineLib.Lobby;
import Messages.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private List<RoomInfo> roomInfoList;        // TODO This feels a bit like a weird hack. Could a callback work?
    private Map<Integer, Lobby> lobbyList;
    private String myUuid;
    private String myChannel;
    private HeartbeatListener hbCallback;
    ExecutorService lobbyThreads = Executors.newCachedThreadPool();

    public GameRequestListener(List<RoomInfo> roomList, Map<Integer, Lobby> lobbyList, String uuid, HeartbeatListener hbCallback) {
        this.roomInfoList = roomList;
        this.lobbyList = lobbyList;
        this.myUuid = uuid;
        this.myChannel = Channels.roomRequestChannel;
        this.hbCallback = hbCallback;
    }

    public boolean isDeleteRequest(RoomInfo roomRequest) {
        return RoomInfo.RequestType.DISCONNECT.equals(roomRequest.getRequestMode());
    }

    public boolean isCreateRequest(RoomInfo roomRequest) {
        return roomRequest.getRoomID() == RoomInfo.defaultRoomID;
    }

    public boolean roomIsValid(RoomInfo roomRequest) {
        return lobbyList.containsKey(roomRequest.getRoomID()) &&
                lobbyList.get(roomRequest.getRoomID()).getRoomInfo().isOpen();
    }

    public void createRoom(PubNub pb, RoomInfo roomMsg) {
        System.out.println("Create room request received: " + roomMsg);

        final int roomID = Engine.getNewRoomID();
        roomMsg.setRoomID(roomID);
        roomMsg.setRoomChannel(Channels.roomChannelSet + roomID);

        // Put the room into the set of rooms.
        // Note that, even if the room is full and we can start up immediately,
        // we still need to add the room to both lists and then send this off.
        lobbyList.put(roomID, new Lobby(roomMsg));
        roomInfoList.add(roomMsg);

        // Check if the room they sent us is full. If it isn't, they probably are trying to play
        // against the AI.
        if(roomMsg.isOpen()) {

            publishUpdatedRoomList(pb);

            // We'll need to track if the player goes offline. If we do, then we kick them.
            PlayerInfo player = (roomMsg.hasPlayer1() ? roomMsg.getPlayer1() : roomMsg.getPlayer2());
            hbCallback.setExpireCallback(
                    player.getUuid(),
                    value -> deleteRoom(pb, player)
            );
        }
        else {
            // The room is somehow full? This must mean that they want to play with AI or something.
            startGame(pb, roomMsg);
        }
    }

    private void joinRoom(PubNub pb, RoomInfo roomMsg) {

        System.out.println("Join room request received: " + roomMsg);

        if (roomIsValid(roomMsg)) {
            startGame(pb, roomMsg);
        }
        else {
            System.out.println("Rejecting; room full or unavailable");

            // TODO We are currently assuming that Player2 is the joiner. This is NOT NECESSARILY THE CASE RIGHT NOW.
            PlayerInfo requester = roomMsg.getPlayer2();

            pb.publish()
                    .message(RoomFactory.makeDeniedRoom())
                    .channel(requester.getChannel())
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

    private void startGame(PubNub pb, RoomInfo roomMsg) {
        int roomID = roomMsg.getRoomID();

        RoomInfo room = lobbyList.get(roomID).getRoomInfo();
        System.out.println(room);

        // TODO This should probably get a refactor
        if(!room.hasPlayer1()) {
            room.setPlayer1(roomMsg.getPlayer1());
        }
        else {
            room.setPlayer2(roomMsg.getPlayer2());
        }

        hideRoom(pb, roomID);
        System.out.println("Sending out this room: " + room.toString());
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
                .channel(room.getRoomChannel())
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

    /**
     * Hides the given room, causing it to no longer show up when the room list is published.
     * @param pb Used to send out the message with the updated room list.
     * @param roomID ID of room to hide.
     */
    private void hideRoom(PubNub pb, int roomID) {
        System.out.println("Hiding room ID " + roomID);
        if(roomInfoList.removeIf(room -> room.getRoomID() == roomID)) {
            publishUpdatedRoomList(pb);
        }
    }

    /**
     * Deletes the given room entirely, making it as if the room never even existed. Use this if the creator
     * ditches the match.
     * @param pb Used to send out the message with the updated room list.
     * @param creator Creator of the room.
     */
    private void deleteRoom(PubNub pb, PlayerInfo creator) {
        System.out.println("Deleting room by " + creator.getId());
        if(roomInfoList.removeIf(room -> room.hasPlayer(creator))) {
            lobbyList.values().removeIf(lobby -> lobby.getRoomInfo().hasPlayer(creator));
            publishUpdatedRoomList(pb);
        }
    }

    private void publishUpdatedRoomList(PubNub pb) {

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
    }

    @Override
    public void status(PubNub pb, PNStatus status) {
    }

    @Override
    public void message(PubNub pb, PNMessageResult message) {
        if(message.getChannel().equals(myChannel) && !message.getPublisher().equals(myUuid)) {
            RoomInfo roomMsg = Converter.fromJson(message.getMessage(), RoomInfo.class);
            if(isDeleteRequest(roomMsg)) {
                //PlayerInfo creator = (roomMsg.hasPlayer1() ? roomMsg.getPlayer1() : roomMsg.getPlayer2());
                // TODO We're assuming player 1 is the creator.
                PlayerInfo creator = roomMsg.getPlayer1();
                deleteRoom(pb, creator);
            }
            else if(isCreateRequest(roomMsg)) {
                createRoom(pb, roomMsg);
            }
            else {
                joinRoom(pb, roomMsg);
            }
        }
    }

    @Override
    public void presence(PubNub pb, PNPresenceEventResult presence) {

    }
}
