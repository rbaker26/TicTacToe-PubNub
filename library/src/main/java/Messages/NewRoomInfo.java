package Messages;

import java.util.Objects;

public class NewRoomInfo {
    public static final int defaultRoomID = -1;

    private String player1ID = null;
    private String player1Channel = null;

    private String player2ID = null;
    private String player2Channel = null;

    private int roomID = defaultRoomID;
    private String roomChannel = null;


    public NewRoomInfo() {

    }

    //<editor-fold desc="Getters and setters">

    /**
     * Sets the player 1 ID and channel. Neither string can be null.
     * This also cannot be called if player 1 is already present.
     * @param player1ID Player 1's ID.
     * @param player1Channel Player 1's channel.
     * @throws NullPointerException If a parameter is null.
     * @throws IllegalStateException If player 1 is already present.
     */
    public void setPlayer1(String player1ID, String player1Channel) {
        if(player1ID == null || player1Channel == null) {
            throw new NullPointerException("Null ID or channel");
        }
        else if(hasPlayer1()) {
            throw new IllegalStateException("Already have player 1");
        }

        this.player1ID = player1ID;
        this.player1Channel = player1Channel;
    }

    /**
     * Sets the player 2 ID and channel. Neither string can be null.
     * This also cannot be called if player 2 is already present.
     * @param player2ID Player 2's ID.
     * @param player2Channel Player 2's channel.
     * @throws NullPointerException If a parameter is null.
     * @throws IllegalStateException If player 2 is already present.
     */
    public void setPlayer2(String player2ID, String player2Channel) {
        if(player2ID == null || player2Channel == null) {
            throw new NullPointerException("Null ID or channel");
        }
        else if(hasPlayer2()) {
            throw new IllegalStateException("Already have player 2");
        }

        this.player2ID = player2ID;
        this.player2Channel = player2Channel;
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
        NewRoomInfo that = (NewRoomInfo) o;
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
        return "NewRoomInfo{" +
                "player1ID='" + player1ID + '\'' +
                ", player1Channel='" + player1Channel + '\'' +
                ", player2ID='" + player2ID + '\'' +
                ", player2Channel='" + player2Channel + '\'' +
                ", roomID=" + roomID +
                ", roomChannel='" + roomChannel + '\'' +
                '}';
    }
}
