import java.util.Objects;

public class GameInfo {

    public enum TurnControlMode {
        NONE, X_PLAYER, O_PLAYER
    }

    private String xPlayer;
    private String oPlayer;

    private int roomID;
    private TurnControlMode currentTurn;
    private Board board;

    public GameInfo() {
        this("", "", TurnControlMode.NONE);
    }

    public GameInfo(String xPlayer, String oPlayer, TurnControlMode currentTurn) {
        this.xPlayer = xPlayer;
        this.oPlayer = oPlayer;
        this.currentTurn = currentTurn;

        board = new Board();
    }

    public void setXPlayer(String xPlayer) {
        this.xPlayer = xPlayer;
    }

    public void setOPlayer(String oPlayer) {
        this.oPlayer = oPlayer;
    }

    public void setCurrentTurn(TurnControlMode currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void toggleCurrentTurn() {
        switch(currentTurn) {
            case O_PLAYER:
                currentTurn = TurnControlMode.X_PLAYER;
                break;
            case X_PLAYER:
                currentTurn = TurnControlMode.O_PLAYER;
                break;
            default:
                break;
        }
    }

    public String getXPlayer() {
        return xPlayer;
    }

    public String getOPlayer() {
        return oPlayer;
    }

    public int getRoomID() {
        return roomID;
    }


    public String getNextPlayer() {
        switch(currentTurn) {
            case O_PLAYER:
                return oPlayer;
            case X_PLAYER:
                return xPlayer;
            default:
                return "";
        }
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameInfo gameInfo = (GameInfo) o;
        return xPlayer.equals(gameInfo.xPlayer) &&
                oPlayer.equals(gameInfo.oPlayer) &&
                getBoard().equals(gameInfo.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPlayer, oPlayer, getBoard());
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "X_PLAYER='" + xPlayer + '\'' +
                ", O_PLAYER='" + oPlayer + '\'' +
                ", board=" + board +
                '}';
    }
}
