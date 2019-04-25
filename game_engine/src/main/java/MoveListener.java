import EngineLib.Board;
import EngineLib.Lobby;
import Messages.*;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This listener will handle all incoming moves on the Rooms::Move channel.  All the move validation and
 * execution will be handled here.
 */
public class MoveListener extends SubscribeCallback {
    private String myChannel;
    private Map<Integer, Lobby> lobbyList;

    public MoveListener(Map<Integer, Lobby> lobbyList) {
        this.myChannel = Channels.roomMoveChannel;
        this.lobbyList = lobbyList;
    }

    public boolean moveIsValid(MoveInfo move) {
        int roomID = move.getRoomID();
        Lobby lobby;
        if(lobbyList.containsKey(roomID)) {
            System.out.println("Lobby found");
            lobby = lobbyList.get(roomID);
            if(lobby.isRunning())
                System.out.println("Lobby running");
            if(lobby.getCurrentPlayer() == move.getPlayerID())
                System.out.println("Player matched");
            System.out.println("Current player: " + lobby.getCurrentPlayer());
            System.out.println("Move player: " + move.getPlayerID());
            return lobby.isRunning() && lobby.getCurrentPlayer().equals(move.getPlayerID());
        }
        return false;
    }

    public boolean playerWon(Board board, char token){
        return board.isWinner(token);
    }

    @Override
    public void status(PubNub pb, PNStatus status) {
        if(status.getCategory().equals(PNStatusCategory.PNConnectedCategory)) {
        }
    }

    @Override
    public void message(PubNub pb, PNMessageResult message) {
        if (message.getChannel().equals(myChannel)) {
            MoveInfo move = Converter.fromJson(message.getMessage(), MoveInfo.class);
            System.out.println("Received move: " + move);
            int roomID = move.getRoomID();
            if (moveIsValid(move)) {
                // Code to make moves on the board and check fun stuff
                Lobby lobby = lobbyList.get(roomID);
                char token;
                System.out.println("Valid move received");
                if (move.getPlayerID().equals(lobby.getRoomInfo().getPlayer1Name()))
                    token = 'X';
                else
                    token = 'O';
                lobby.getBoard().setPos(move.getRow(), move.getCol(), token);
                System.out.println("Room " + roomID + "'s board:\n " + lobby.getBoard());

                if (playerWon(lobby.getBoard(), token) || lobby.getBoard().numEmptySpaces() == 0) {
                    System.out.println("Game over");
                    pb.publish() // Sending move request to next player
                            .message(new MoveRequest(lobby.getBoard(), lobby.getRoomInfo(), null))
                            .channel(lobby.getRoomInfo().getRoomChannel())
                            .async(new PNCallback<PNPublishResult>() {
                                @Override
                                public void onResponse(PNPublishResult result, PNStatus status) {
                                    // handle publish result, status always present, result if successful
                                    // status.isError() to see if error happened
                                    if (!status.isError()) {
                                    }
                                }
                            });
                    lobby.endGame();
                } else {
                    lobby.toggleCurrentPlayer();
                    // will need to publish the next move request at the end of this
                    System.out.printf("Sending move request to: %s%n Current Board: %s%n", lobby.getCurrentPlayer(), lobby.getBoard());

                    List<String> outgoingChannels = new LinkedList();
                    outgoingChannels.add(lobby.getRoomInfo().getRoomChannel());
                    if(lobby.getRoomInfo().getPlayer2().isAI()) {
                        outgoingChannels.add(lobby.getRoomInfo().getPlayer2().getChannel());
                    }

                    for(String channelName : outgoingChannels) {
                        pb.publish() // Sending move request to next player
                                .message(new MoveRequest(lobby.getBoard(), lobby.getRoomInfo(), lobby.getCurrentPlayer()))
                                .channel(channelName)
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
        }
    }

    @Override
    public void presence(PubNub pb, PNPresenceEventResult presence) {
    }
}
