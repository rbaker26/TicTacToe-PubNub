package Messages;

import java.util.Objects;

public class PlayerInfo implements Cloneable {

    public static enum PlayerType {
        HUMAN, AI_EASY, AI_HARD
    }

    private String uuid = null;
    private String id = null;
    private String channel = null;
    private String name = null;
    private PlayerType type = PlayerType.HUMAN;

    public static PlayerInfo easyAI() {
        PlayerInfo result = new PlayerInfo();
        result.setId("Easy AI");
        result.type = PlayerType.AI_EASY;
        result.channel = Channels.AI.easyChannel;
        result.name = "Easy AI";

        return result;
    }

    public static PlayerInfo hardAI() {
        PlayerInfo result = new PlayerInfo();
        result.setId("Hard AI");
        result.type = PlayerType.AI_HARD;
        result.channel = Channels.AI.hardChannel;
        result.name = "Hard AI";

        return result;
    }

    public PlayerInfo() {
    }

    public PlayerInfo(String uuid, String id, String channel) {
        this.uuid = uuid;
        this.id = id;
        this.channel = channel;
    }

    //region Getters and setters
    public boolean isAI() {
        return type.equals(PlayerType.AI_HARD) || type.equals(PlayerType.AI_EASY);
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

    public PlayerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion

    //region Object overrides

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "uuid='" + uuid + '\'' +
                ", id='" + id + '\'' +
                ", channel='" + channel + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInfo that = (PlayerInfo) o;
        return Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getChannel(), that.getChannel()) &&
                Objects.equals(getName(), that.getName()) &&
                getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getId(), getChannel(), getName(), getType());
    }


    //endregion


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

