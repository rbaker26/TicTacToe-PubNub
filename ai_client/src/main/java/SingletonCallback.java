import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

/**
 * This callback simply listens for messages on a channel. If it
 * hears any, it will automatically send out a response which contains
 * the time this callback was created.
 *
 * This is intended to be used to ensure only one instance of this
 * program is running at a time.
 */
public class SingletonCallback extends SubscribeCallback {

    /**
     * This is the time (in milliseconds) to wait before declaring ourselves the master.
     */
    private static long waitTime = 5000;
    //private static long waitTime = 20000;

    private static class StatusMessage {
        public long spawnTime;

        public StatusMessage(long spawnTime) {
            this.spawnTime = spawnTime;
        }
    }

    /**
     * This marks our state related to other singleton objects.
     */
    private static enum UniquenessState {
        /**
         * We haven't decided whether there's something else out there.
         */
        UNKNOWN,
        /**
         * We waited long enough and decided we're the only ones around.
         */
        UNIQUE,
        /**
         * Someone else responded to our message; we are not alone.
         */
        DUPLICATE
    }

    /**
     * This is always the time which we are spawned at. It is used to
     * track which object is the oldest.
     */
    private long spawnTime;

    // These are the channels to publish are subscribed to, respectively.
    // Though these should probably always be the same, they are tracked
    // separately just in case the distinction ever comes up.

    /**
     * Channel to publish to.
     */
    private String outgoingChannel;

    /**
     * Channel to listen on.
     */
    private String incomingChannel;

    /**
     * Is run if no collision (i.e. someone else has claimed our singleton position) is detected.
     */
    private Runnable isMasterCallback;

    /**
     * Is run if a collision (i.e. someone else has claimed our singleton position) is detected.
     */
    private Runnable notMasterCallback;

    /**
     * Is run if the check is interrupted somehow. This should never happen but it's possible.
     * Probably best to leave this empty.
     */
    private Runnable interruptedCallback;

    /**
     * Do NOT access directly; use makeMaster and checkIsMaster instead.
     * Since we have no idea if we might be running in many thread, we
     * need to make sure everything is synchronized.
     */
    private UniquenessState state;


    //region Getters and setters
    public Runnable getIsMasterCallback() {
        return isMasterCallback;
    }

    public void setIsMasterCallback(Runnable isMasterCallback) {
        this.isMasterCallback = isMasterCallback;
    }

    public Runnable getNotMasterCallback() {
        return notMasterCallback;
    }

    public void setNotMasterCallback(Runnable notMasterCallback) {
        this.notMasterCallback = notMasterCallback;
    }

    public Runnable getInterruptedCallback() {
        return interruptedCallback;
    }

    public void setInterruptedCallback(Runnable interruptedCallback) {
        this.interruptedCallback = interruptedCallback;
    }
    //endregion

    public SingletonCallback(String channel) {
        spawnTime = System.currentTimeMillis();
        outgoingChannel = channel;
        incomingChannel = channel;
        state = UniquenessState.UNKNOWN;
    }

    /**
     * Creates a status message for this instance of the singleton.
     * @return
     */
    public StatusMessage createStatusMessage() {
        return new StatusMessage(spawnTime);
    }

    /**
     * Attempts to mark ourselves as the master. Fails if we are marked as a duplicate.
     * @return True if successful (or already master), false otherwise.
     */
    synchronized boolean makeMaster() {
        if(state != UniquenessState.DUPLICATE) {
            state = UniquenessState.UNIQUE;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Checks if we are a duplicate object based on the message we receive.
     * This will cause cleanup if necessary.
     * @param pubnub Used for publishing a message in the case that we win.
     * @return
     */
    synchronized UniquenessState checkForDuplicate(PubNub pubnub, StatusMessage msg) {

        // Don't bother doing anything if we've already deemed ourself a duplicate
        if(state != UniquenessState.DUPLICATE) {
            // If the other message was spawned before we were...
            if (msg.spawnTime < spawnTime) {
                // ...then they win! We are the duplicate.
                state = UniquenessState.DUPLICATE;
                notMasterCallback.run();
            }
            else {
                // We're older than them, so we win! We need to let them
                // know so that they can clean themselves up.
                pubnub.publish().channel(outgoingChannel).message(createStatusMessage()).async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                    }
                });
            }
        }

        return state;
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

            pubnub.publish().channel(outgoingChannel).message(createStatusMessage()).async(new PNCallback<PNPublishResult>() {
                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {
                    if (!status.isError()) {
                        // We've just asked if we're the master. If no one responds to our request within
                        // the time limit, we'll assume we are the only ones. This can potentially get interrupted
                        // by another message coming to us.
                        try {
                            Thread.sleep(waitTime);

                            if(makeMaster()) {
                                // If we are successful in becoming master, then... we've done it!
                                // We can continue to do... whatever it was we were trying to do.
                                isMasterCallback.run();
                            }
                        }
                        catch(java.lang.InterruptedException ex) {
                            /**
                             * If we get interrupted, we're probably killing the thread. Thus, we failed?
                             */
                            interruptedCallback.run();
                        }
                    }
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

        if(sourceChannel.equals(incomingChannel)) {
            StatusMessage msg = Messages.Converter.fromJson(message.getMessage(), StatusMessage.class);
            checkForDuplicate(pubnub, msg);
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
