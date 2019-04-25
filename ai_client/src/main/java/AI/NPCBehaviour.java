package AI;

import Messages.MoveInfo;

import java.io.Serializable;
import java.util.Objects;

/**
 * This represents the behavior for an AI.
 *
 * @author Daniel Edwards
 */
public interface NPCBehaviour {

    /**
     * Asks the AI to decide upon a move.
     * @param b The current board state.
     * @param token Player's token or symbol.
     * @return The move. This only populates the row and the column.
     */
    MoveInfo getMove(EngineLib.Board b, char token);
}
