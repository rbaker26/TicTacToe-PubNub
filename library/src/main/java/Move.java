public class Move {

    public int roomID = 0;
    public int row = 0;
    public int col = 0;
    public int playerID = 0;

    public Move(int roomID, int row, int col, int playerID) {
        this.roomID   = roomID;
        this.row      = row;
        this.col      = col;
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return "RoomID:\t" + String.valueOf(roomID) + "\tRow:\t" + String.valueOf(row)+
                 "\tCol:\t" + String.valueOf(col)    + "\tplayerID:\t" + String.valueOf(playerID);
    }
}
