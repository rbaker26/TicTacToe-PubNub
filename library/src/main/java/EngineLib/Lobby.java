package EngineLib;

import Messages.RoomInfo;

/**
 * This represents a lobby in the engine, which holds the room info as well as the game board.
 * It is specifically used by the Engine to keep track of the running
 * game.
 */
public class Lobby {
    private RoomInfo roomInfo;
    private Board board;
    private String currentPlayer = "";
    private boolean gameRunning = true;

    /**
     * Sets the current Lobby's room info to the passed information.  Also initializes the game board for the room
     * @param roomInfo
     */
    public Lobby(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
        board = new Board();
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public Board getBoard() {
        return board;
    }

    public String getCurrentPlayer() { return currentPlayer; }

    public void toggleCurrentPlayer() {
        if(currentPlayer == "" || currentPlayer == roomInfo.getPlayer2().getId()) {
            currentPlayer = roomInfo.getPlayer1().getId();
        }
        else {
            currentPlayer = roomInfo.getPlayer2().getId();
        }
    }

    public boolean isRunning() {
        return gameRunning;
    }

    public void endGame() {
        gameRunning = false;
    }

    @Override
    public String toString() {
        return roomInfo.toString() + "\n " + board.toString();
    }
}
