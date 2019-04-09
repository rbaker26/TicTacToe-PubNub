package Messages;


public class MoveInfo {
    private int roomID;
    private String playerID;
    private int row;
    private int col;

    public MoveInfo(int roomID, String playerID, int row, int col) {
        this.roomID = roomID;
        this.playerID = playerID;
        this.row = row;
        this.col = col;
    }

    public int getRoomID() {
        return roomID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
