package Heartbeat;

import Messages.Converter;
import Messages.Heartbeat;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.*;
import java.util.function.Consumer;

/**
 * A callback which tracks heartbeats. Its main purpose is to allow us to react
 * to clients going offline. Note that it may take some time before we decide
 * that a client has gone offline.
 */
public class HeartbeatCallback extends SubscribeCallback {

    private static final long EXPIRATION_TIME = Heartbeat.DEFAULT_BEAT_PERIOD * 2;
    private static final long CLEAN_PERIOD = 500;

    private class SourceGarbageCollector implements Runnable {

        boolean alive = false;

        public synchronized boolean isAlive() {
            return alive;
        }

        public synchronized void setAlive(boolean alive) {
            this.alive = alive;
        }

        @Override
        public void run() {
            alive = true;

            // We're using the iterator rather than foreach because we need to
            // be able to call iterator.delete() to remove elements.
            while(alive) {
                HeartbeatSource info = null;
                Iterator<HeartbeatSource> iterator = livingSources.values().iterator();
                while (iterator.hasNext()) {
                    info = iterator.next();

                    if (System.currentTimeMillis() - info.getTimeOfLastHeartbeat() > EXPIRATION_TIME) {

                        if(debugMode) {
                            System.out.println(info.getUuid() + " died! q.p");
                        }

                        info.executeExpiredCallback();
                        iterator.remove();
                    }
                }

                try {
                    Thread.sleep(CLEAN_PERIOD);
                }
                catch(InterruptedException ex) {
                    System.out.println("SourceGarbageCollector (in HeartbeatCallback) has been interrupted and will die");
                    alive = false;
                }
            } // End while(alive)
        }
    }

    private Map<String, HeartbeatSource> livingSources;
    private String incomingChannel;

    private SourceGarbageCollector cleanerCallback;
    private Thread cleanerThread;

    private boolean debugMode;

    /**
     * Constructs an instance.
     * @param incomingChannel Channel to listen on.
     * @param debugMode If true, stuff will be printed to the console.
     */
    public HeartbeatCallback(String incomingChannel, boolean debugMode) {
        this(incomingChannel);
        this.debugMode = debugMode;
    }

    public HeartbeatCallback(String incomingChannel) {
        this.incomingChannel = incomingChannel;
        livingSources = Collections.synchronizedMap(new HashMap<>());

        cleanerCallback = new SourceGarbageCollector();
        cleanerThread = new Thread(cleanerCallback);
        cleanerThread.start();
    }

    /**
     * Sets the callback to be triggered when a device with the given UUID
     * has its heart stop. Be warned; if the source has never sent a heartbeat,
     * their UUID will be registered as if they just sent a heartbeat. This CAN
     * still timeout, so it's possible to get something timeout without it ever
     * existing.
     *
     * @param uuid UUID to listen on.
     * @param callback Callback to call. This can be null.
     */
    public void setExpireCallback(String uuid, Consumer<HeartbeatSource> callback) {
        getHeartbeatSource(uuid).setExpiredCallback(callback);
    }

    /**
     * Gets the info on the heartbeat source associated with sourceUUID.
     * Be careful when calling this; if the uuid isn't included in
     * livingSources, it will create the source for you on the
     * livingSources map. This will get garbage collected automatically
     * if no one sends a heartbeat from this uuid.
     * @param sourceUUID The UUID to get info from.
     * @return The info associated with sourceUUID.
     */
    private HeartbeatSource getHeartbeatSource(String sourceUUID) {
        long currentTime = System.currentTimeMillis();

        HeartbeatSource sourceInfo = livingSources.get(sourceUUID);
        if(sourceInfo == null) {
            sourceInfo = new HeartbeatSource(sourceUUID, currentTime);
            livingSources.put(sourceUUID, sourceInfo);
        }

        return sourceInfo;
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {

    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        String sourceChannel = ( message.getChannel() != null
                ? message.getChannel()
                : message.getSubscription()
        );

        if(sourceChannel.equals(incomingChannel)) {
            Heartbeat beat = Converter.fromJson(message.getMessage(), Heartbeat.class);
            String sourceUUID = message.getPublisher();

            HeartbeatSource sourceInfo = getHeartbeatSource(sourceUUID);
            sourceInfo.setTimeOfLastHeartbeat(System.currentTimeMillis());

            if(debugMode) {
                System.out.println("Got a heartbeat from: " + sourceUUID);
            }

            sourceInfo.setPreviousBeat(beat);

        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
