import Messages.Converter;
import Messages.Heartbeat;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatCallback extends SubscribeCallback {

    private static final long EXPIRATION_TIME = Heartbeat.DEFAULT_BEAT_PERIOD * 3;

    private Map<String, HeartbeatSourceInfo> livingSources;
    private String incomingChannel;

    public HeartbeatCallback(String incomingChannel) {
        this.incomingChannel = incomingChannel;
        livingSources = Collections.synchronizedMap(new HashMap<>());
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

            HeartbeatSourceInfo sourceInfo = livingSources.get(sourceUUID);
            if(sourceInfo == null) {
                sourceInfo = new HeartbeatSourceInfo(sourceUUID);
                livingSources.put(sourceUUID, sourceInfo);
            }

            sourceInfo.setTimeOfLastHeartbeat(System.currentTimeMillis());
            sourceInfo.setPreviousBeat(beat);

            System.out.println(sourceInfo);
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
