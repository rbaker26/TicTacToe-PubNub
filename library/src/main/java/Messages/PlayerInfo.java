package Messages;

import java.util.Objects;

public class PlayerInfo implements Cloneable {
    private String uuid = null;
    private String id = null;
    private String channel = null;

    public PlayerInfo() {

    }

    public PlayerInfo(String uuid, String id, String channel) {
        this.uuid = uuid;
        this.id = id;
        this.channel = channel;
    }

    public PlayerInfo(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "uuid='" + uuid + '\'' +
                ", id='" + id + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInfo that = (PlayerInfo) o;
        return Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getChannel(), that.getChannel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getId(), getChannel());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
