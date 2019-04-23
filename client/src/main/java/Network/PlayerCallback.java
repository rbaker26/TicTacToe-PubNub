package Network;

import EngineLib.Board;
import Messages.Converter;
import Messages.MoveRequest;
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
import javafx.application.Platform;

import java.util.function.Consumer;

public class PlayerCallback extends SubscribeCallback {

    private String name;
    private String outgoingChannel;
    private RoomInfo room;
    private Consumer<Board> requestResponse;

    public PlayerCallback(String name, String outgoingChannel, RoomInfo room, Consumer<Board> response) {
        this.name = name;
        this.outgoingChannel = outgoingChannel;
        this.room = room;
        requestResponse = response;

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
        System.out.println("PlayerCallback message received on channel: " + message.getChannel());
        System.out.println("Request is for: " + Converter.fromJson(message.getMessage(), MoveRequest.class).getCurrentPlayer());
        System.out.println("My room channel is: " + room.getRoomChannel());
        if(message.getChannel().equals(room.getRoomChannel())) {
            System.out.println("Move request received" + message.getMessage());
            MoveRequest requestMessage = Converter.fromJson(message.getMessage(), MoveRequest.class);
            if(requestMessage.getCurrentPlayer() == null) {
                System.out.println("Game Over!");
                if(requestMessage.getBoard().isWinner('X')) {
                    System.out.println("X Player: " + requestMessage.getRoomInfo().getPlayer1().getId() + " won!");
                }
                else if(requestMessage.getBoard().isWinner('O')) {
                    System.out.println("O Player: " + requestMessage.getRoomInfo().getPlayer2().getId() + " won!");
                }
                else {
                    System.out.println("Tie game!");
                }
            }
            else if(requestMessage.getCurrentPlayer().equals(name)) {
                requestResponse.accept(requestMessage.getBoard());
                System.out.println("Your turn");
            }
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
