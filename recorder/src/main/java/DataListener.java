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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class DataListener extends SubscribeCallback{
    private List<RoomInfo> roomInfoList;
    private Map<Integer, Lobby> lobbyList;
    private String myUuid;
    private String myChannel;

    public DataListener(List<RoomInfo> roomList, Map<Integer, Lobby> lobbyList, String uuid) {
        this.roomInfoList = roomList;
        this.lobbyList = lobbyList;
        this.myUuid = uuid;
        this.myChannel = Channels.roomRequestChannel;
    }



    @Override
    public void status(PubNub pubnub, PNStatus status) {
        // not used
        System.out.println("Here in status");
        if(status.getCategory() == PNStatusCategory.PNConnectedCategory) {
            System.out.println("Connected");
            pubnub.publish().message("").channel(Channels.roomListChannel).async(new PNCallback<PNPublishResult>() {


                @Override
                public void onResponse(PNPublishResult result, PNStatus statut) {

                }
            });
        }
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        System.out.println("Message received: " + message);
        System.out.println("From: " + message.getPublisher());
        System.out.println("On Channel: " + message.getChannel());
        if(message.getChannel().equals(Channels.roomMoveChannel)) {
            MoveInfo move = Converter.fromJson(message.getMessage(), MoveInfo.class);
            System.out.println("Received move: " + move);

            try {
                Db_Manager.GetInstance().WriteMove(move);
            }
            catch(SQLException sqle) {
                // TODO Push on backup-queue
                System.out.println(sqle);
            }
        }
        else if(message.getChannel().equals(Channels.roomChannelSet)) {

        }
        else if(message.getChannel().equals((Channels.roomRequestChannel))){
            MoveRequest mr = Converter.fromJson(message.getMessage(), MoveRequest.class);
            if(mr.getCurrentPlayer() == null ) {// check for null)
                try {
                    String username1 = mr.getRoomInfo().getPlayer1ID();
                    String username2 = mr.getRoomInfo().getPlayer2ID();
                    String winner;
                    if(mr.getBoard().isWinner('X')) {
                        winner = username1;
                        Db_Manager.GetInstance().UpdateScore(winner, true);
                        Db_Manager.GetInstance().UpdateScore(username2, false);

                    }
                    else if(mr.getBoard().isWinner('O')) {
                        winner = username2;
                        Db_Manager.GetInstance().UpdateScore(winner, true);
                        Db_Manager.GetInstance().UpdateScore(username1, false);
                    }
                    else {
                        Db_Manager.GetInstance().UpdateScore(username1, false);
                        Db_Manager.GetInstance().UpdateScore(username2, false);
                    }


                }
                catch(Exception sqle) {
                    // TODO Push on backup-queue
                    System.out.println(sqle);
                }
            }

        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        // not used
    }
}
