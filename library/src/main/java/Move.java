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
<<<<<<< HEAD
        return "RoomID:\t" + String.valueOf(roomID) + "\tRow:\t" + String.valueOf(row)+
                "\tCol:\t" + String.valueOf(col)    + "\tplayerID:\t" + String.valueOf(playerID);
=======
         return "RoomID:\t" + String.valueOf(roomID) + "\tRow:\t" + String.valueOf(row)+
                 "\tCol:\t" + String.valueOf(col)    + "\tplayerID:\t" + String.valueOf(playerID);
>>>>>>> cbf7d4fd102e62c78034be54c2830da9898fd769
    }
}
