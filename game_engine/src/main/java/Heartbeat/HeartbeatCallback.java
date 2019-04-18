package Heartbeat;

import Messages.Converter;
import Messages.Heartbeat;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HeartbeatCallback extends SubscribeCallback {

    private static final long EXPIRATION_TIME = Heartbeat.DEFAULT_BEAT_PERIOD * 2;
    private static final long CLEAN_PERIOD = 5000;

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
                HeartbeatSourceInfo info = null;
                Iterator<HeartbeatSourceInfo> iterator = livingSources.values().iterator();
                while (iterator.hasNext()) {
                    info = iterator.next();

                    if (System.currentTimeMillis() - info.getTimeOfLastHeartbeat() > EXPIRATION_TIME) {
                        System.out.println(info.getUuid() + " died! q.p");
                        // TODO We need to call the callback
                        iterator.remove();
                    }
                    else {
                        System.out.println(info.getUuid() + " is still alive");
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

    private Map<String, HeartbeatSourceInfo> livingSources;
    private String incomingChannel;

    private SourceGarbageCollector cleanerCallback;
    private Thread cleanerThread;

    public HeartbeatCallback(String incomingChannel) {
        this.incomingChannel = incomingChannel;
        livingSources = Collections.synchronizedMap(new HashMap<>());

        cleanerCallback = new SourceGarbageCollector();
        cleanerThread = new Thread(cleanerCallback);
        cleanerThread.start();
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

            long currentTime = System.currentTimeMillis();

            HeartbeatSourceInfo sourceInfo = livingSources.get(sourceUUID);
            if(sourceInfo == null) {
                sourceInfo = new HeartbeatSourceInfo(sourceUUID, currentTime);
                livingSources.put(sourceUUID, sourceInfo);
            }
            else {
                //System.out.println(currentTime - sourceInfo.getTimeOfLastHeartbeat());
                sourceInfo.setTimeOfLastHeartbeat(currentTime);
            }

            System.out.println(sourceInfo);


            sourceInfo.setPreviousBeat(beat);

        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
