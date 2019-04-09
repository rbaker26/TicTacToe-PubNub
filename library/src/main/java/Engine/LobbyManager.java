package Engine;

import Messages.Channels;
import Messages.Converter;
import Messages.MoveInfo;
import Messages.MoveRequest;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class LobbyManager {
    private PubNub pb;
    private Lobby lobby;
    private String currentPlayerID;
    private String roomChannel;

    public LobbyManager(PubNub pb, Lobby lobby) {
        this.pb = pb;
        this.lobby = lobby;
        currentPlayerID = lobby.getRoomInfo().getPlayer1ID();
        roomChannel = lobby.getRoomInfo().getRoomChannel();

        pb.unsubscribe().channels(Arrays.asList(lobby.getRoomInfo().getPlayer1Channel(), lobby.getRoomInfo().getPlayer2Channel())).execute();
        pb.addListener(new SubscribeCallback() {
            /**
             * This listener handles the back and forth between the two players checking, validating, and requesting
             * moves from the players
             */
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                System.out.println(message.getMessage());
                if(message.getChannel() == roomChannel) {
                    if(message.getPublisher() != pb.getConfiguration().getUuid()) {
                        MoveInfo moveMsg = Converter.fromJson(message.getMessage(), MoveInfo.class);
                        if(moveMsg.getPlayerID() == currentPlayerID && lobby.getBoard().getPos(moveMsg.getRow(), moveMsg.getCol()) == ' ') {
                            currentPlayerID = null;
                            char token;
                            if(moveMsg.getPlayerID() == lobby.getRoomInfo().getPlayer1ID()) {
                                token = 'X';
                            }
                            else {
                                token = 'O';
                            }
                            lobby.getBoard().setPos(moveMsg.getRow(), moveMsg.getCol(), token);
                            System.out.println(lobby.getBoard());
                            if(lobby.getRoomInfo().getPlayer1ID() == moveMsg.getPlayerID()) {
                                currentPlayerID = lobby.getRoomInfo().getPlayer2ID();
                            }
                            else {
                                currentPlayerID = lobby.getRoomInfo().getPlayer1ID();
                            }
                        }
                    }
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        while(lobby.isRunning()) {
            pb.publish()
                    .message(new MoveRequest(lobby.getBoard(), lobby.getRoomInfo(), currentPlayerID))
                    .channel(roomChannel)
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

}
