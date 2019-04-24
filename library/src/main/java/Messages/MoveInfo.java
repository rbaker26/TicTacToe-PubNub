package Messages;


public class MoveInfo {
    private int roomID;
    private String playerID;
    private int row;
    private int col;

    public MoveInfo() {

    }

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

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "roomID: " + roomID +
                " playerID: " + playerID +
                " row: " + row +
                " col: " + col;
    }
}
