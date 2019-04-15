package Network;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class AICallback extends SubscribeCallback {

    private String name;
    private String incomingChannel;
    private String outgoingChannel;

    private PlayerBehaviour behaviour;
    private Runnable onSetupCallback;

    public AICallback(String name, String incomingChannel, String outgoingChannel) {
        this.name = name;
        this.incomingChannel = incomingChannel;
        this.outgoingChannel = outgoingChannel;
    }

    /**
     * When this is called, it will
     * @param onSetupCallback
     */
    public void setOnSetupCallback(Runnable onSetupCallback) {
        this.onSetupCallback = onSetupCallback;
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
            if(onSetupCallback != null) {
                onSetupCallback.run();
            }
        }
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        String sourceChannel = ( message.getChannel() != null
                ? message.getChannel()
                : message.getSubscription()
        );

        if(sourceChannel.equals(incomingChannel)) {
            // TODO This is where we receive a request for a move.
            //      After making the move request, we will then shoot back a move.

        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
