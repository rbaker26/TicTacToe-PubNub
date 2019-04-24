package Network;

import AI.NPCBehaviour;
import Messages.Converter;
import Messages.MoveRequest;
import Messages.PlayerInfo;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class AICallback extends SubscribeCallback {

    private PlayerInfo player;
    private String incomingChannelSet;
    private String outgoingChannel;

    private NPCBehaviour behaviour;
    private Runnable onSetupCallback;

    /**
     * Creates a new AI callback.
     * @param player This is the player which we consider ourselves to be. We do not respond unless this matches our player.
     * @param behaviour The behaviour which decides the moves.
     * @param incomingChannelSet The set of channels which we listen on. Should NOT include the '*'.
     * @param outgoingChannel The channel which we publish our moves to.
     */
    public AICallback(PlayerInfo player, NPCBehaviour behaviour, String incomingChannelSet, String outgoingChannel) {
        this.player = player;
        this.behaviour = behaviour;
        this.incomingChannelSet = incomingChannelSet;
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

        System.out.println(message.getMessage());
        System.out.println(sourceChannel);

        if(sourceChannel.startsWith(incomingChannelSet)) {
            // TODO This is where we receive a request for a move.
            //      After making the move request, we will then shoot back a move.

            MoveRequest request = Converter.fromJson(message.getMessage(), MoveRequest.class);

            System.out.println(request);
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
