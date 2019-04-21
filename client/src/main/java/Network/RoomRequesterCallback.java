package Network;

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

import java.util.function.Consumer;

/**
 * Attempts to ask for a room from the engine. This can be used either
 * for joining or for creating a room. (If the roomInfo object has an ID,
 * it's assumed we're joining. Otherwise, it's assumed we're creating.)
 */
public class RoomRequesterCallback extends SubscribeCallback {

    private String ourUserID;
    private String outgoingChannel;
    private String incomingChannel;
    private RoomInfo roomInfo;
    private Consumer<RoomInfo> successResponse;
    private Consumer<RoomInfo> failureResponse;

    public RoomRequesterCallback(String ourUserID, String outgoingChannel, String incomingChannel, RoomInfo roomInfo) {
        this.ourUserID = ourUserID;
        this.outgoingChannel = outgoingChannel;
        this.incomingChannel = incomingChannel;

        this.roomInfo = roomInfo;
    }

    /**
     * Sets the callback to use when we've successfully gotten a room which we
     * may join.
     * @param successResponse Response to use. May be null, and overrides old callbacks.
     */
    public void setSuccessResponse(Consumer<RoomInfo> successResponse) {
        this.successResponse = successResponse;
    }

    /**
     * Sets the callback to use when we've failed to get a room which we
     * may join. This can occur for several reasons, and might be able
     * to be deduced based on the RoomInfo object.
     * @param failureResponse Response to use. May be null, and overrides old callbacks.
     */
    public void setFailureResponse(Consumer<RoomInfo> failureResponse) {
        this.failureResponse = failureResponse;
    }

    /**
     * Cancels the request to join/create a room. This does NOT clean up this callback;
     * the caller must detach the callback and everything else.
     * @param pubnub Used for sending the message.
     */
    public void cancelRequest(PubNub pubnub) {
        roomInfo.setRequestMode(RoomInfo.RequestType.DISCONNECT);
        pubnub.publish().channel(outgoingChannel).message(roomInfo).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) { }
        });
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

            pubnub.publish().channel(outgoingChannel).message(roomInfo).async(new PNCallback<PNPublishResult>() {
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
            RoomInfo responseRoomInfo = Converter.fromJson(message.getMessage(), RoomInfo.class);

            System.out.println(responseRoomInfo.toString());

            // TODO Check for success/failure
            if(successResponse != null) {
                successResponse.accept(responseRoomInfo);
            }

        }
    } // End message(PubNube pubnub, PNMessageResult message)

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
