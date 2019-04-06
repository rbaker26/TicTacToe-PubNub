package Messages;

import java.util.Objects;

public class RoomInfo {
    public static final int defaultRoomID = -1;

    private String player1ID = null;
    private String player1Channel = null;

    private String player2ID = null;
    private String player2Channel = null;

    private int roomID = defaultRoomID;
    private String roomChannel = null;


    public RoomInfo() {

    }

    //<editor-fold desc="Getters and setters">

    /**
     * Sets the player ID and channel. Neither string can be null.
     * This also cannot be called if player is already present.
     * @param playerID Player's ID.
     * @param playerChannel Player's channel.
     * @throws NullPointerException If a parameter is null.
     * @throws IllegalStateException If player is already present.
     */
    public void setPlayer(String playerID, String playerChannel, boolean isFirstPlayer) {

        if(playerID == null || playerChannel == null) {
            throw new NullPointerException("Null ID or channel");
        }
        else if((isFirstPlayer && hasPlayer1()) || hasPlayer2()) {
            throw new IllegalStateException("Already have player 1");
        }

        if(isFirstPlayer) {
            this.player1ID = playerID;
            this.player1Channel = playerChannel;
        }
        else {
            this.player2ID = playerID;
            this.player2Channel = playerChannel;
        }
    }


    public String getPlayer1ID() {
        return player1ID;
    }

    public String getPlayer1Channel() {
        return player1Channel;
    }

    public String getPlayer2ID() {
        return player2ID;
    }

    public String getPlayer2Channel() {
        return player2Channel;
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
        return player1ID != null;
    }

    public boolean hasPlayer2() {
        return player2ID != null;
    }
    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInfo that = (RoomInfo) o;
        return getRoomID() == that.getRoomID() &&
                isOpen() == that.isOpen() &&
                Objects.equals(getPlayer1ID(), that.getPlayer1ID()) &&
                Objects.equals(getPlayer1Channel(), that.getPlayer1Channel()) &&
                Objects.equals(getPlayer2ID(), that.getPlayer2ID()) &&
                Objects.equals(getPlayer2Channel(), that.getPlayer2Channel()) &&
                Objects.equals(getRoomChannel(), that.getRoomChannel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer1ID(), getPlayer1Channel(), getPlayer2ID(), getPlayer2Channel(), getRoomID(), getRoomChannel(), isOpen());
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "player1ID='" + player1ID + '\'' +
                ", player1Channel='" + player1Channel + '\'' +
                ", player2ID='" + player2ID + '\'' +
                ", player2Channel='" + player2Channel + '\'' +
                ", roomID=" + roomID +
                ", roomChannel='" + roomChannel + '\'' +
                '}';
    }
}
