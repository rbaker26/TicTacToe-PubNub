package Messages;

public final class Channels {
    private Channels() {}

    public static final String roomChannelSet = "Rooms::";
    public static final String privateChannelSet = "Private::";
    public static final String heartbeatChannelSet = "Heartbeat::";

    public static final String roomRequestChannel = roomChannelSet + "Requests";
    public static final String roomListChannel = roomChannelSet + "List";
    public static final String roomMoveChannel = roomChannelSet + "Move";

    public static final String clientHeartbeatChannel = heartbeatChannelSet + "Client";
}
