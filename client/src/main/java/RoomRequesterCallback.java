import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class RoomRequesterCallback extends SubscribeCallback {

    private String name;
    String roomRequestChannel;
    String roomUpdateChannel;

    public RoomRequesterCallback(String name, String roomRequestChannel, String roomUpdateChannel) {
        this.name = name;
        this.roomRequestChannel = roomRequestChannel;
        this.roomUpdateChannel = roomUpdateChannel;
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {


        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
            // This event happens when radio / connectivity is lost
        }

        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

            // Connect event. You can do stuff like publish, and know you'll get it.
            // Or just use the connected event to confirm you are subscribed for
            // UI / internal notifications, etc

            JsonObject data = new JsonObject();
            data.addProperty("name", name);

            pubnub.publish().channel(roomRequestChannel).message(data).async(new PNCallback<PNPublishResult>() {
                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {
                    // Check whether request successfully completed or not.
                    if (!status.isError()) {

                        // Message successfully published to specified channel.
                    }
                    // Request processing failed.
                    else {

                        // Handle message publish error. Check 'category' property to find out possible issue
                        // because of which request did fail.
                        //
                        // Request can be resent using: [status retry];
                    }
                }
            });
        }
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        // Handle new message stored in message.message
        if (message.getChannel() != null) {
            // Message has been received on channel group stored in
            // message.getChannel()
        }
        else {
            // Message has been received on channel stored in
            // message.getSubscription()
        }


        System.out.println();
        System.out.println("msg content: " + message.getMessage());
        System.out.println("msg publisher: " + message.getPublisher());

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
