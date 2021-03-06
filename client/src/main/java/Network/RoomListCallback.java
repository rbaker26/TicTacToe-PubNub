package Network;

import Messages.Channels;
import Messages.Converter;
import Messages.RoomInfo;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RoomListCallback extends SubscribeCallback {

    Consumer<List<RoomInfo>> onUpdateCallback;

    public RoomListCallback(Consumer<List<RoomInfo>> onUpdateCallback) {
        this.onUpdateCallback = onUpdateCallback;
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {
        // ToDo: add checks here on connection status
        if(status.getCategory() == PNStatusCategory.PNConnectedCategory) {
            pubnub.publish().message("").channel(Channels.roomListChannel).async(new PNCallback<PNPublishResult>() {
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
    public void message(PubNub pubnub, PNMessageResult message) {
        if(message.getPublisher().contains("engine")) {
            System.out.println("RoomList received:\n");
            List<RoomInfo> rooms = Arrays.asList(Converter.fromJson(message.getMessage(), RoomInfo[].class));
            System.out.println("Received msg:\n");
            for (int room = 0; room < rooms.size(); room++) {
                System.out.println("Room " + room);
                System.out.println(rooms.get(room));
            }

            if(onUpdateCallback != null) {
                onUpdateCallback.accept(rooms);
            }
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
