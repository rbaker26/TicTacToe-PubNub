import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class Engine {



    public static void main(String[] args) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-89b1d65a-4f40-11e9-bc3e-aabf89950afa");
        pnConfiguration.setPublishKey("pub-c-79140f4c-8b9e-49ed-992f-cf6322c68d04");

        PubNub pubnub = new PubNub(pnConfiguration);

        String updateChannelName = "Game_Update::A_B";
        String connectedChannelName = "System";
        String moveChannelName = "Move";

        // create message payload using Gson
        /*
        MoveData data = new MoveData();
        data.column = 1;
        data.row = 30;
        data.sender = "Rad dude";
        data.eVal = MoveData.TestEnum.Value2;
        */
        JsonObject data = new JsonObject();
        data.addProperty("type", "move");
        data.addProperty("row", 2);
        data.addProperty("column", 0);

        System.out.println("Message to send: " + data.toString());
        //System.out.println("Message to send: " + data.toString());

        pubnub.addListener(new SubscribeCallback() {
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
        });

        pubnub.subscribe().channels(Arrays.asList(connectedChannelName, moveChannelName)).execute();
    }
}
