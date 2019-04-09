package Messages;

public final class Channels {
    private Channels() {}

    public static final String roomChannelSet = "Rooms::";
    public static final String privateChannelSet = "Private::";

    public static final String roomRequestChannel = roomChannelSet + "Requests";
    public static final String roomListChannel = roomChannelSet + "List";
}
