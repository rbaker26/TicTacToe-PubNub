import EngineLib.Lobby;
import Messages.Channels;
import Messages.Converter;
import Messages.MoveInfo;
import Messages.RoomInfo;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
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
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

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

        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        // not used
    }
}
