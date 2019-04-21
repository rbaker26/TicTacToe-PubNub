package Network;

import Messages.RoomInfo;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class PlayerCallback extends SubscribeCallback {

    private String name;
    private String outgoingChannel;
    private RoomInfo room;

    public PlayerCallback(String name, String outgoingChannel, RoomInfo room) {
        this.name = name;
        this.outgoingChannel = outgoingChannel;
        this.room = room;
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {
        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
            // This event happens when radio / connectivity is lost
        }

        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

            System.out.println("Established connection: " + room.toString());

            // Connect event. You can do stuff like publish, and know you'll get it.
            // Or just use the connected event to confirm you are subscribed for
            // UI / internal notifications, etc


            // TODO We might want to send some message to say that we're ready.
            /*
            JsonObject data = new JsonObject();
            data.addProperty("name", name);

            pubnub.publish().channel(outgoingChannel).message(data).async(new PNCallback<PNPublishResult>() {
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

             */
        }
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
