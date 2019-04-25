package TaskThreads;

import Messages.Heartbeat;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;

import java.util.Objects;

/**
 * While the this is running, it will periodically send Heartbeats on the outgoing channel.
 */
class HeartbeatCallback extends AbstractInterruptibleCallback {

    private PubNub pn;
    private String outgoingChannel;
    private Heartbeat beat;

    HeartbeatCallback(PubNub pn, String outgoingChannel, Heartbeat beat) {
        super(Heartbeat.DEFAULT_BEAT_PERIOD);

        if(pn == null || outgoingChannel == null) {
            throw new IllegalStateException("Cannot run while Pubnub object or outgoing channel is null");
        }

        if(beat == null) {
            throw new NullPointerException("The heartbeat object passed to HeartbeatCallback must NOT be null!");
        }

        this.pn = pn;
        this.beat = beat;
        this.outgoingChannel = outgoingChannel;
    }

    @Override
    protected void update() {

        try {
            pn.publish().message(beat).channel(getOutgoingChannel()).sync();
            //System.out.println("Thump");
        } catch (PubNubException ex) {
            System.out.println("SKIPPED A BEAT: " + ex.toString());
        }

    }

    //region Getters and setters
    public PubNub getPubNub() {
        return pn;
    }

    public void setPubNub(PubNub pn) {
        this.pn = pn;
    }

    public synchronized String getOutgoingChannel() {
        return outgoingChannel;
    }

    public synchronized void setOutgoingChannel(String outgoingChannel) {
        this.outgoingChannel = outgoingChannel;
    }
    //endregion

    //region Object overrides


    @Override
    public String toString() {
        return "HeartbeatCallback{" +
                "pn=" + pn +
                ", outgoingChannel='" + outgoingChannel + '\'' +
                ", beat=" + beat +
                "} which extends " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HeartbeatCallback that = (HeartbeatCallback) o;
        return Objects.equals(pn, that.pn) &&
                Objects.equals(getOutgoingChannel(), that.getOutgoingChannel()) &&
                Objects.equals(beat, that.beat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pn, getOutgoingChannel(), beat);
    }
    //endregion
}
