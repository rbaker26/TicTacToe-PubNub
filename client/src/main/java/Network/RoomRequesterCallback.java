package Network;

import Messages.Converter;
import Messages.PlayerInfo;
import Messages.RoomFactory;
import Messages.RoomInfo;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.function.Consumer;

/**
 * Attempts to ask for a room from the engine. This can be used either
 * for joining or for creating a room. (If the request object has an ID,
 * it's assumed we're joining. Otherwise, it's assumed we're creating.)
 */
public class RoomRequesterCallback extends SubscribeCallback implements InterruptibleListener {

    private String outgoingChannel;
    private String incomingChannel;
    private RoomInfo request;
    private PlayerInfo ourPlayer;
    private Consumer<RoomInfo> successCallback;
    private Consumer<RoomInfo> failureCallback;

    /**
     * If true, then we haven't heard back yet. If we have to stop and we haven't heard back yet, then we'll
     * call the interrupted thing.
     */
    private boolean isWaiting;

    public RoomRequesterCallback(String outgoingChannel, String incomingChannel, RoomInfo roomInfo, PlayerInfo ourPlayer) {
        this.outgoingChannel = outgoingChannel;
        this.incomingChannel = incomingChannel;
        this.ourPlayer = ourPlayer;

        this.request = roomInfo;
        isWaiting = false;
    }

    /**
     * Sets the callback to use when we've successfully gotten a room which we
     * may join.
     * @param successCallback Response to use. May be null, and overrides old callbacks.
     */
    public void setSuccessCallback(Consumer<RoomInfo> successCallback) {
        this.successCallback = successCallback;
    }

    /**
     * Sets the callback to use when we've failed to get a room which we
     * may join. This can occur for several reasons, and might be able
     * to be deduced based on the RoomInfo object.
     * @param failureCallback Response to use. May be null, and overrides old callbacks.
     */
    public void setFailureCallback(Consumer<RoomInfo> failureCallback) {
        this.failureCallback = failureCallback;
    }

    /**
     * Cancels the request to join/create a room. This does NOT clean up this callback;
     * the caller must detach the callback and everything else.
     * @param pubnub Used for sending the message.
     */
    public void interrupt(PubNub pubnub) {
        //request.setRequestMode(RoomInfo.RequestType.DISCONNECT);
        if(isWaiting) {
            RoomInfo deleteRequest = RoomFactory.makeDisconnectRoom(ourPlayer);
            pubnub.publish().channel(outgoingChannel).message(deleteRequest).async(new PNCallback<PNPublishResult>() {
                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {
                }
            });

            isWaiting = false;
        }
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {


        System.out.println("Starting " + status.getCategory());

        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
            // This event happens when radio / connectivity is lost
        }

        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

            // Connect event. You can do stuff like publish, and know you'll get it.
            // Or just use the connected event to confirm you are subscribed for
            // UI / internal notifications, etc

            pubnub.publish().channel(outgoingChannel).message(request).async(new PNCallback<PNPublishResult>() {
                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {
                    // Check whether request successfully completed or not.
                    if (!status.isError()) {
                        // Message successfully published to specified channel.
                        isWaiting = true;

                        System.out.println("Waiting on a response to " + request);
                    }
                    // Request processing failed.
                    else {

                        // Handle message publish error. Check 'category' property to find out possible issue
                        // because of which request did fail.
                        //
                        // Request can be resent using: [status retry];
                        System.out.println("Failed to send " + request);
                    }
                }
            });
        }
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

        String sourceChannel = ( message.getChannel() != null
                ? message.getChannel()
                : message.getSubscription()
        );

        System.out.println();
        System.out.println("msg content: " + message.getMessage());
        System.out.println("source channel: " + sourceChannel);

        if(sourceChannel.equals(incomingChannel)) {
            //JsonObject json = message.getMessage().getAsJsonObject();
            //String creator = json.get("Creator").getAsString();
            RoomInfo response = Converter.fromJson(message.getMessage(), RoomInfo.class);

            System.out.println(response.toString());

            if (response.getRequestMode().equals(RoomInfo.RequestType.DENIED) && failureCallback != null) {
                failureCallback.accept(response);
            }
            else if(successCallback != null) {
                successCallback.accept(response);
            }

            isWaiting = false;
        }
    } // End message(PubNub pubnub, PNMessageResult message)

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
