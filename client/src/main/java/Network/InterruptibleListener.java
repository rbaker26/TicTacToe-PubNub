package Network;

import com.pubnub.api.PubNub;

/**
 * A network callback which may be interrupted. Note that, if a callback implements this, it should *probably*
 * be called before just removing it from the listener list. Also, it would be extremely poor style to implement
 * this without also extending com.pubnub.api.callbacks.SubscribeCallback.
 */
public interface InterruptibleListener {
    void interrupt(PubNub pubnub);
}
