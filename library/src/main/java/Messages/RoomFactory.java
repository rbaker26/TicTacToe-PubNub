package Messages;

public final class RoomFactory {

    private RoomFactory() {}

    /**
     * Creates a room object which tells the engine that we are no longer
     * interested in joining the room.
     * @param player The player who is not interested in joining.
     * @return An object which represents a disconnect request.
     */
    public static RoomInfo makeDisconnectRoom(PlayerInfo player) {
        RoomInfo result = new RoomInfo();
        result.setPlayer1(player);
        result.setRequestMode(RoomInfo.RequestType.DISCONNECT);

        return result;
    }

    /**
     * Creates a room which is used to say the one attempting to join cannot join.
     * @return An object which represents a deny response.
     */
    public static RoomInfo makeDeniedRoom() {
        RoomInfo result = new RoomInfo();
        result.setRequestMode(RoomInfo.RequestType.DENIED);

        return result;
    }

    /**
     * Creates a room request based on the given info.
     * @param creatorGoingFirst If true, the creator will go first. Otherwise, the joiner will.
     * @param password The password to use. Leave this parameter out if no password.
     * @return The room which can be sent to the engine.
     */
    public static RoomInfo makeCreateRequest(boolean creatorGoingFirst, String password) {
        RoomInfo result = new RoomInfo(password);

        if(!creatorGoingFirst) {
            System.out.println("WARNING: THE CREATOR CANNOT GO SECOND YET");
        }

        result.setRequestMode(RoomInfo.RequestType.NORMAL);

        return result;
    }

    /**
     * Creates a room request based on the given info.
     * @param creatorGoingFirst If true, the creator will go first. Otherwise, the joiner will.
     * @return The room which can be sent to the engine.
     */
    public static RoomInfo makeCreateRequest(boolean creatorGoingFirst) {
        return makeCreateRequest(creatorGoingFirst, "");
    }

}
