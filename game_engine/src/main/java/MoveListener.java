import EngineLib.Lobby;
import Messages.Channels;
import Messages.Converter;
import Messages.MoveInfo;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
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
    @Override
    public void status(PubNub pb, PNStatus status) {
        if(status.getCategory().equals(PNStatusCategory.PNConnectedCategory)) {
        }
    }

    @Override
    public void message(PubNub pb, PNMessageResult message) {
        if(message.getChannel().equals(myChannel)) {
            MoveInfo move = Converter.fromJson(message.getMessage(), MoveInfo.class);
            System.out.println("Received move: " + move);
            int roomID = move.getRoomID();
            if(moveIsValid(move)) {
                // Code to make moves on the board and check fun stuff
                Lobby lobby = lobbyList.get(move.getRoomID());
                char token;
                System.out.println("Valid move received");
                if(move.getPlayerID().equals(lobby.getRoomInfo().getPlayer1Name()))
                    token = 'X';
                else
                    token = 'O';
                lobby.getBoard().setPos(move.getRow(), move.getCol(), token);
                System.out.println("Room " + move.getRoomID() + "'s board:\n " + lobby.getBoard());
                lobby.toggleCurrentPlayer();
                // will need to publish the next move request at the end of this
            }
        }
    }

    @Override
    public void presence(PubNub pb, PNPresenceEventResult presence) {
    }
}
