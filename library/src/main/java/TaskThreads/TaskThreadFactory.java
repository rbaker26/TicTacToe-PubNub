package TaskThreads;

import Messages.Heartbeat;
import com.pubnub.api.PubNub;

/**
 * Used for making task threads.
 */
public final class TaskThreadFactory {
    private TaskThreadFactory() {}

    /**
     * Creates a task thread which is used for heartbeats on a given channel.
     * @param pubnub Object used to send messages from.
     * @param outgoingChannel The channel to send the heartbeats on.
     * @return The new task thread.
     */
    public static TaskThread makeHeartbeatThread(PubNub pubnub, String outgoingChannel) {
        return makeHeartbeatThread(pubnub, outgoingChannel, new Heartbeat());
    }

    /**
     * Creates a task thread which is used for heartbeats on a given channel.
     * @param pubnub Object used to send messages from.
     * @param outgoingChannel The channel to send the heartbeats on.
     * @param beat The heartbeat object to send out. May be null.
     * @return The new task thread.
     */
    public static TaskThread makeHeartbeatThread(PubNub pubnub, String outgoingChannel, Heartbeat beat) {

        return new DefaultTaskThread(
                new HeartbeatCallback(pubnub, outgoingChannel, beat)
        );
    }

    /**
     * Creates a custom thread which runs the given code.
     * @param period The time between updates.
     * @param callback The code to run.
     * @return Returns the thread which runs the callback.
     */
    public static TaskThread makeCustomThread(long period, Runnable callback) {

        return new DefaultTaskThread(
                new AbstractInterruptibleCallback(period) {
                    @Override
                    protected void update() {
                        callback.run();
                    }
                }
        );
    }
}
