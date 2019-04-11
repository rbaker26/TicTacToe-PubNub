import Messages.Channels;
import Messages.RoomInfo;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.List;

/**
 * This listener will handle all requests for a server list and will publish out on the Rooms::List channel.  This
 * will also publish to the Rooms::List channel anytime the roomlist is updated
 */
public class RoomsListListener extends SubscribeCallback {
    private List<RoomInfo> roomList;
    private String myUuid;
    private String myChannel;

    /**
     * Sets the PubNub object as well as the RoomList
     * @param roomList
     */
    public RoomsListListener(List<RoomInfo> roomList, String uuid) {
        this.roomList = roomList;
        this.myUuid = uuid;
        myChannel = Channels.roomListChannel;
    }

    @Override
    public void status(PubNub pb, PNStatus status) {
        if(status.getCategory().equals(PNStatusCategory.PNConnectedCategory)) {
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
    }

    @Override
    public void message(PubNub pb, PNMessageResult message) {
        if(message.getChannel().equals(myChannel)) {
            if (!message.getPublisher().equals(myUuid)) {
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
        }
    }

    @Override
    public void presence(PubNub pb, PNPresenceEventResult presence) {
    }
}
