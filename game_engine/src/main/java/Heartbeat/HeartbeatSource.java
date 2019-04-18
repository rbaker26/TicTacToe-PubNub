package Heartbeat;

import Messages.Heartbeat;

import java.util.Objects;
import java.util.function.Consumer;

public class HeartbeatSource {
    private Consumer<HeartbeatSource> expiredCallback;
    private long timeOfLastHeartbeat;
    private String uuid;
    private Heartbeat previousBeat;

    public HeartbeatSource(String uuid, long timeOfLastHeartbeat) {
        this.uuid = uuid;
        this.timeOfLastHeartbeat = timeOfLastHeartbeat;
    }

    public synchronized void executeExpiredCallback() {
        if(expiredCallback != null) {
            expiredCallback.accept(this);
        }
    }

    //region Getters and setters
    public synchronized Consumer<HeartbeatSource> getExpiredCallback() {
        return expiredCallback;
    }

    public synchronized void setExpiredCallback(Consumer<HeartbeatSource> expiredCallback) {
        this.expiredCallback = expiredCallback;
    }

    public synchronized long getTimeOfLastHeartbeat() {
        return timeOfLastHeartbeat;
    }

    public synchronized void setTimeOfLastHeartbeat(long timeOfLastHeartbeat) {
        this.timeOfLastHeartbeat = timeOfLastHeartbeat;
    }

    public String getUuid() {
        return uuid;
    }

    public synchronized Heartbeat getPreviousBeat() {
        return previousBeat;
    }

    public synchronized void setPreviousBeat(Heartbeat previousBeat) {
        this.previousBeat = previousBeat;
    }

    //endregion

    //region Object overrides

    @Override
    public String toString() {
        return "Heartbeat.HeartbeatSource{" +
                "expiredCallback=" + expiredCallback +
                ", timeOfLastHeartbeat=" + timeOfLastHeartbeat +
                ", uuid='" + uuid + '\'' +
                ", previousBeat=" + previousBeat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeartbeatSource that = (HeartbeatSource) o;
        return getTimeOfLastHeartbeat() == that.getTimeOfLastHeartbeat() &&
                Objects.equals(getExpiredCallback(), that.getExpiredCallback()) &&
                Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getPreviousBeat(), that.getPreviousBeat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExpiredCallback(), getTimeOfLastHeartbeat(), getUuid(), getPreviousBeat());
    }

    //endregion
}
