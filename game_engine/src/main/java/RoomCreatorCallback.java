import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class RoomCreatorCallback extends SubscribeCallback {

    // TODO Make this not a single instance of GameInfo; should
    //      an ArrayList or something.
    GameInfo game;
    String roomRequestChannel;
    String roomUpdateChannel;

    public RoomCreatorCallback(GameInfo game, String roomRequestChannel, String roomUpdateChannel) {
        this.game = game;
        this.roomRequestChannel = roomRequestChannel;
        this.roomUpdateChannel = roomUpdateChannel;
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {


        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
            // This event happens when radio / connectivity is lost
        }

        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

        }
        else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

            // Happens as part of our regular operation. This event happens when
            // radio / connectivity is lost, then regained.
        }
        else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

            // Handle messsage decryption error. Probably client configured to
            // encrypt messages and on live data feed it received plain text.
        }
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        // Handle new message stored in message.message
        if (message.getChannel() != null) {
            // Message has been received on channel group stored in
            // message.getChannel()

            if(message.getChannel().equals(roomRequestChannel)) {
                JsonObject json = message.getMessage().getAsJsonObject();
                String name = json.get("name").getAsString();

                game.setXPlayer(name);

                System.out.println(name);

                JsonObject data = new JsonObject();
                data.addProperty("ID", game.getRoomID());
                data.addProperty("Channel", "Rooms::" + game.getRoomID());
                data.addProperty("Creator", name);

                pubnub.publish().channel(roomUpdateChannel).message(data).async(new PNCallback<PNPublishResult>() {
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
