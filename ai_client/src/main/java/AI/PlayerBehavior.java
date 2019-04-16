package AI;

import java.io.Serializable;
import java.util.Objects;

/**
 * This represents the behavior for a player. To request a move, call
 * the getMove function. However, depending on the player's behavior, this
 * may not be able to return a move immediately. To recieve the final result,
 * addSubscriber to the PlayerBehavior and listen for its MoveInfo object.
 *
 * @author Daniel Edwards
 */
public interface PlayerBehavior extends Serializable {


    /**
     * Puts in the request for a move. This will eventually result
     * in MoveInfo getting sent out to all observers.
     * @param b The current board state.
     * @param token Player's token or symbol.
     */
    void getMove(EngineLib.Board b, char token, int roomID, String playerID);
}
