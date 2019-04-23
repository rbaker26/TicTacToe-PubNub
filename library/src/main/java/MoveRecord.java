import java.time.LocalDateTime;

public class MoveRecord extends Move{
    public LocalDateTime dt;

    MoveRecord(Move move, LocalDateTime dt) {
        super(move.roomID,move.row,move.col,move.playerID);
        this.dt = dt;
    }

    MoveRecord(Move move) {
        super(move.roomID,move.row,move.col,move.playerID);
        this.dt = LocalDateTime.now();
    }


}
