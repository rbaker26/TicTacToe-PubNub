package Messages;

public final class Channels {
    private Channels() {}

    public static final String roomChannelSet = "Rooms.";
    public static final String moveRequestChannelSet = roomChannelSet + "Moves.";
    public static final String privateChannelSet = "Private.";
    public static final String heartbeatChannelSet = "Heartbeat.";
    public static final String userAuthChannelSet = "Authorization.";

    //region Room channels
    /**
     * This channel is used to ask the engine to create a new room, or ask to join one.
     */
    public static final String roomRequestChannel = roomChannelSet + "Requests";

    /**
     * This channel is used to get updates on the currently available channels.
     */
    public static final String roomListChannel = roomChannelSet + "List";

    /**
     * This channel is used to send game moves up to the engine.
     */
    public static final String roomMoveChannel = roomChannelSet + "Move";
    //endregion


    public static final String authCreateChannel = userAuthChannelSet + "Create";
    public static final String authCheckChannel = userAuthChannelSet + "Check";

    public static final String clientHeartbeatChannel = heartbeatChannelSet + "Client";

    /**
     * Channels used by the AI system.
     */
    public final class AI {
        private AI() {}

        /**
         * These channels are used for AI things.
         */
        public static final String channelSet = "AI.";

        /**
         * This channel is used by the AI system to ensure that only one instance is online at once.
         */
        public static final String singletonChannel = channelSet + "Singleton";

        /**
         * This channel is used to ask the easy AI for a move.
         */
        public static final String easyChannel = channelSet + "Easy";

        /**
         * This channel is used to ask the hard AI for a move.
         */
        public static final String hardChannel = channelSet + "Hard";
    }
}
