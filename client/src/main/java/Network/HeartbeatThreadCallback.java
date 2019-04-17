package Network;

import Messages.Heartbeat;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;

import java.util.Objects;

/**
 * While the HeartbeatThreadCallback is running, it will periodically send Heartbeats
 * on the outgoing channel.
 */
public class HeartbeatThreadCallback implements Runnable {

    private PubNub pn;
    private String outgoingChannel;
    private boolean alive;
    private long beatPeriod;

    public HeartbeatThreadCallback() {
        this(null, null);
    }

    public HeartbeatThreadCallback(PubNub pn, String outgoingChannel) {
        this.pn = pn;
        alive = false;
        beatPeriod = Heartbeat.DEFAULT_BEAT_PERIOD;
        this.outgoingChannel = outgoingChannel;
    }

    @Override
    public void run() {
        if(pn == null || outgoingChannel == null) {
            throw new IllegalStateException("Cannot run while Pubnub object or outgoing channel is null");
        }

        setAlive(true);

        while (isAlive()) {
            try {
                Messages.Heartbeat beat = new Heartbeat();
                pn.publish().message(beat).channel(getOutgoingChannel()).sync();
                System.out.println("Thump");
            } catch (PubNubException ex) {
                System.out.println("FAILED TO SEND HEARTBEAT: ex");
            }

            try {
                Thread.sleep(getBeatPeriod());
            }
            catch(InterruptedException ex) {
                System.out.println("Heartbeat thread interrupted; dying");
                setAlive(false);
            }
        } // End while(isAlive())
    }

    //region Adjusted getters and setters
    public PubNub getPubNub() {
        return pn;
    }

    public void setPubNub(PubNub pn) {
        this.pn = pn;
    }

    /**
     * Gets the beat period in milliseconds.
     * @return Milliseconds.
     */
    public synchronized long getBeatPeriod() {
        return beatPeriod;
    }

    /**
     * Sets the beatPeriod in milliseconds. This amount of time will be taken between beats.
     * @param beatPeriod Period in milliseconds.
     */
    public synchronized void setBeatPeriod(long beatPeriod) {
        this.beatPeriod = beatPeriod;
    }

    public synchronized String getOutgoingChannel() {
        return outgoingChannel;
    }

    public synchronized void setOutgoingChannel(String outgoingChannel) {
        this.outgoingChannel = outgoingChannel;
    }

    public synchronized boolean isAlive() {
        return alive;
    }

    /**
     * Toggles the heartbeat thread's "aliveness." If false, then the
     * thread will gracefully close itself next time it wakes up.
     * Setting this false and then true quickly might have no effect,
     * since this is only checked when the thread is awake.
     * @param alive Whether the thread should be alive or not.
     */
    public synchronized void setAlive(boolean alive) {
        this.alive = alive;
    }
    //endregion

    //region Object overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeartbeatThreadCallback that = (HeartbeatThreadCallback) o;
        return isAlive() == that.isAlive() &&
                Float.compare(that.getBeatPeriod(), getBeatPeriod()) == 0 &&
                Objects.equals(getPubNub(), that.getPubNub()) &&
                Objects.equals(getOutgoingChannel(), that.getOutgoingChannel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPubNub(), getOutgoingChannel(), isAlive(), getBeatPeriod());
    }

    @Override
    public String toString() {
        return "HeartbeatThreadCallback{" +
                "pn=" + pn +
                ", outgoingChannel='" + outgoingChannel + '\'' +
                ", alive=" + alive +
                ", beatPeriod=" + beatPeriod +
                '}';
    }

    //endregion
}
