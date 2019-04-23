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

import java.sql.SQLException;
import java.util.Map;


public class DataCallBack extends SubscribeCallback{


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
            }
        }
        else if(message.getChannel().equals(Channels.roomChannelSet)) {

        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        // not used
    }
}
