package Network;

import AI.NPCBehaviour;
import Messages.*;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class AICallback extends SubscribeCallback {

    private PlayerInfo player;
    private String incomingChannel;
    private String outgoingChannel;

    private NPCBehaviour behaviour;
    private Runnable onSetupCallback;

    /**
     * Creates a new AI callback. We will be listening on the player's channel.
     * @param player This is the player which we consider ourselves to be. We do not respond unless this matches our player.
     * @param behaviour The behaviour which decides the moves.
     * @param outgoingChannel The channel which we publish our moves to.
     */
    public AICallback(PlayerInfo player, NPCBehaviour behaviour, String outgoingChannel) {
        this.player = player;
        this.behaviour = behaviour;
        this.incomingChannel = player.getChannel();
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
            MoveRequest request = Converter.fromJson(message.getMessage(), MoveRequest.class);

            //System.out.println(request);

            String currentPlayerID = request.getCurrentPlayer();

            if(currentPlayerID != null && currentPlayerID.equals(player.getId()))
            {
                //System.out.println("It's for us!");
                MoveInfo move = behaviour.getMove(request.getBoard(), request.getRoomInfo().getPlayer2Token().charAt(0));
                move.setRoomID(request.getRoomInfo().getRoomID());
                move.setPlayerID(player.getId());

                pubnub.publish()
                        .message(move)
                        .channel(outgoingChannel)
                        .async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // handle publish result, status always present, result if successful
                                // status.isError() to see if error happened
                                if (!status.isError()) {
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
