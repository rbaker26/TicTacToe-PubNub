package Messages;

import java.util.Objects;

/**
 * This is info which relates specifically with one room, or requests to the engine
 * pertaining to rooms.
 */
public class RoomInfo {

    /**
     * Marks which kind of request this is, assuming it's getting sent across PubNub.
     */
    public static enum RequestType {
        NORMAL, DISCONNECT, DENIED
    }

    public static final int defaultRoomID = -1;

    PlayerInfo player1 = null;
    PlayerInfo player2 = null;

    private int roomID = defaultRoomID;
    private String roomChannel = null;

    private String password;

    // TODO Throw this in PlayerInfo
    private String player1Token = "X";
    private String player2Token = "O";

    private RequestType requestMode = RequestType.NORMAL;

    //region Factories

    //endregion

    public RoomInfo() {
        this("");
    }

    public RoomInfo(String password) {
        if(password == null) {
            password = "";
        }

        this.password = password;
    }

    public void addPlayer(PlayerInfo newPlayer) {
        if(getPlayer1() == null) {
            setPlayer1(newPlayer);
        }
        else if(getPlayer2() == null) {
            setPlayer2(newPlayer);
        }
        else {
            throw new IllegalStateException("Cannot add the player to a full game.");
        }
    }

    public boolean hasPlayer(PlayerInfo creator) {
        if(creator == null) {
            return false;
        }
        else {
            return creator.equals(getPlayer1()) || creator.equals(getPlayer2());
        }
    }

    //region Password stuff

    /**
     * Checks if the room is password protected.
     * @return True, if the room is password protected.
     */
    public boolean hasPassword() {
        return !password.isEmpty();
    }

    /**
     * Checks if the given password matches.
     * @param attempt The password attempt.
     * @return If the password matches the stored one, returns true.
     */
    public boolean passwordMatches(String attempt) {
        return password.equals(attempt);
    }
    //endregion

    //region Getters and setters
    public RequestType getRequestMode() {
        return requestMode;
    }

    void setRequestMode(RequestType requestMode) {
        this.requestMode = requestMode;
    }

    public PlayerInfo getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerInfo player1) {
        this.player1 = player1;
    }

    public PlayerInfo getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerInfo player2) {
        this.player2 = player2;
    }

    @Deprecated
    public String getPlayer1Name() {
        if(hasPlayer1()) {
            return player1.getId();
        }
        else {
            return null;
        }
    }

    @Deprecated
    public String getPlayer2Name() {
        if(hasPlayer2()) {
            return player2.getId();
        }
        else {
            return null;
        }
    }

    public String getStatus() {
        return (isOpen() ? "Open" : "Closed");
    }

    public String getPlayer1Token() {
        return player1Token;
    }

    public void setPlayer1Token(String player1Token) {
        this.player1Token = player1Token;
    }

    public String getPlayer2Token() {
        return player2Token;
    }

    public void setPlayer2Token(String player2Token) {
        this.player2Token = player2Token;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomChannel() {
        return roomChannel;
    }

    public void setRoomChannel(String roomChannel) {
        this.roomChannel = roomChannel;
    }

    public boolean isOpen() {
        return !isClosed();
    }

    public boolean isClosed() {
        return hasPlayer1() && hasPlayer2();
    }

    public boolean hasPlayer1() {
        return player1 != null;
    }

    public boolean hasPlayer2() {
        return player2 != null;
    }

    @Deprecated
    public boolean isAvailable() {
        return player1 == null || player2 == null;
    }
    //endregion

    //region Object overrides
    @Override
    public String toString() {
        return "RoomInfo{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", roomID=" + roomID +
                ", roomChannel='" + roomChannel + '\'' +
                ", password='" + password + '\'' +
                ", player1Token='" + player1Token + '\'' +
                ", player2Token='" + player2Token + '\'' +
                ", requestMode=" + requestMode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInfo roomInfo = (RoomInfo) o;
        return getRoomID() == roomInfo.getRoomID() &&
                Objects.equals(getPlayer1(), roomInfo.getPlayer1()) &&
                Objects.equals(getPlayer2(), roomInfo.getPlayer2()) &&
                Objects.equals(getRoomChannel(), roomInfo.getRoomChannel()) &&
                Objects.equals(password, roomInfo.password) &&
                Objects.equals(getPlayer1Token(), roomInfo.getPlayer1Token()) &&
                Objects.equals(getPlayer2Token(), roomInfo.getPlayer2Token()) &&
                getRequestMode() == roomInfo.getRequestMode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer1(), getPlayer2(), getRoomID(), getRoomChannel(), password, getPlayer1Token(), getPlayer2Token(), getRequestMode());
    }

    //endregion

}
