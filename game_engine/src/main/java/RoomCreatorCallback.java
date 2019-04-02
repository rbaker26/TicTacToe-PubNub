import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class RoomCreatorCallback extends SubscribeCallback {

    // TODO Make this not a single instance
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
        }
        else {
            // Message has been received on channel stored in
            // message.getSubscription()
            JsonObject json = message.getMessage().getAsJsonObject();
            if(message.getSubscription().equals(roomRequestChannel)) {
                String name = json.get("name").getAsString();

                game.setXPlayer(name);

                System.out.println(name);
            }
        }


        System.out.println();
        System.out.println("msg content: " + message.getMessage());
        System.out.println("msg publisher: " + message.getPublisher());

                /*
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                MoveData origMove = gson.fromJson(message.getMessage(), MoveData.class);
                System.out.println(MoveData.class.getSimpleName());

                System.out.println( "Move class: " + origMove.toString() );
                */

                /*
                // We want to wait a bit after getting a message.
                // Otherwise, we could burn up all of our messages and make pubnub unhappy.
                try {
                    TimeUnit.SECONDS.sleep(3);
                }
                catch(InterruptedException ex) {

                }

                MoveData responseMove = new MoveData();
                responseMove.column = origMove.column + 1;
                responseMove.row = origMove.row - 1;

                pubnub.publish().channel(channelName).message(responseMove).async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                    }
                });
                */


            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }

}
