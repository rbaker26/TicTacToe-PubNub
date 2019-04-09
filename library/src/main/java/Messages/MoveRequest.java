package Messages;

import Engine.Board;

public class MoveRequest {
    private Board board;
    private RoomInfo roomInfo;
    private String currentPlayer;

    public MoveRequest(Board board, RoomInfo roomInfo, String currentPlayer) {
        this.board = board;
        this.roomInfo = roomInfo;
        this.currentPlayer = currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
