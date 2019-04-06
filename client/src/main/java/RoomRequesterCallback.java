import Messages.Converter;
import Messages.NewRoomInfo;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Attempts to ask for a room from the engine. This can be used either
 * for joining or for creating a room. (If the roomInfo object has an ID,
 * it's assumed we're joining. Otherwise, it's assumed we're creating.)
 */
public class RoomRequesterCallback extends SubscribeCallback {

    private String userID;
    private String outgoingChannel;
    private String incomingChannel;
    private NewRoomInfo roomInfo;
    private Consumer<NewRoomInfo> successResponse;
    private Consumer<NewRoomInfo> failureResponse;

    public RoomRequesterCallback(String userID, String outgoingChannel, String incomingChannel, NewRoomInfo roomInfo) {
        this.userID = userID;
        this.outgoingChannel = outgoingChannel;
        this.incomingChannel = incomingChannel;

        this.roomInfo = roomInfo;
    }

    /**
     * Sets the callback to use when we've successfully gotten a room which we
     * may join.
     * @param successResponse Response to use. May be null, and overrides old callbacks.
     */
    public void setSuccessResponse(Consumer<NewRoomInfo> successResponse) {
        this.successResponse = successResponse;
    }

    /**
     * Sets the callback to use when we've failed to get a room which we
     * may join. This can occur for several reasons, and might be able
     * to be deduced based on the NewRoomInfo object.
     * @param failureResponse Response to use. May be null, and overrides old callbacks.
     */
    public void setFailureResponse(Consumer<NewRoomInfo> failureResponse) {
        this.failureResponse = failureResponse;
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
            NewRoomInfo responseRoomInfo = Converter.fromJson(message.getMessage(), NewRoomInfo.class);

            System.out.println(responseRoomInfo.toString());

            // TODO Check for success/failure
            if(successResponse != null) {
                successResponse.accept(roomInfo);
            }


            /*
            if (creator.equals(userID)) {

                String channel = json.get("Channel").getAsString();

                pubnub.addListener(new PlayerCallback(userID, channel));
                pubnub.removeListener(this);

                pubnub.subscribe().channels(Arrays.asList(channel)).execute();
                pubnub.unsubscribe().channels(Arrays.asList(sourceChannel)).execute();
            }
             */
        }
    } // End message(PubNube pubnub, PNMessageResult message)

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
