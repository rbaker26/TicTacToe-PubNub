import Messages.Channels;
import Messages.RoomInfo;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.List;

/**
 * This listener will handle all requests for a server list and will publish out on the Rooms::List channel
 */
public class RoomsListCallback extends SubscribeCallback {
    PubNub pb;
    private List<RoomInfo> roomList = new ArrayList<>();

    /**
     * Sets the PubNub object as well as the RoomList
     * @param pb
     * @param roomList
     */
    public RoomsListCallback(PubNub pb, List<RoomInfo> roomList) {
        this.pb = pb;
        this.roomList = roomList;
    }

    /**
     * Updates the RoomList as well as publishes it
     * @param roomList
     */
    public void updateRoomList(List<RoomInfo> roomList) {
        this.roomList = roomList;
        publish();
    }

    public void publish() {
        pb.publish().message(roomList.toArray())
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
    public void status(PubNub pubnub, PNStatus status) {

    }

    @Override
    public void message(PubNub pb, PNMessageResult message) {
        if(message.getChannel() == Channels.roomListChannel) {
            if (message.getPublisher() != pb.getConfiguration().getUuid()) {
                publish();
            }
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
