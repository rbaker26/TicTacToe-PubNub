
import java.io.IOException;
import java.util.Scanner;
import com.google.gson.*;
import com.pubnub.api.*;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.concurrent.TimeUnit;

import java.util.Arrays;

public class Client {


    public static void main(String[] args) {

        Scanner consoleInput = new Scanner(System.in);
        System.out.println("Are you A or B?");
        String name = "";

        do {
            name = consoleInput.next();

            if(name.length() > 0) {
                name = name.substring(0, 1);
            }

        } while(!name.equals("A") && !name.equals("B"));
        System.out.println("Alright, you are " + name + "! Connecting...");
        final String finalName = name;   // @#$!@#@ JAVA Y U WANT FINALS????

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-89b1d65a-4f40-11e9-bc3e-aabf89950afa");
        pnConfiguration.setPublishKey("pub-c-79140f4c-8b9e-49ed-992f-cf6322c68d04");
        //pnConfiguration.setUuid("User-A::Java");

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
        /*
        JsonObject data = new JsonObject();
        data.addProperty("type", "move");
        data.addProperty("row", 2);
        data.addProperty("column", 0);
        */

        //System.out.println("Message to send: " + data.toString());

        pubnub.addListener(new SubscribeCallback() {
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
                    data.addProperty("name", finalName);

                    pubnub.publish().channel(connectedChannelName).message(data).async(new PNCallback<PNPublishResult>() {
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

        pubnub.subscribe().channels(Arrays.asList(updateChannelName)).execute();
    }
}
