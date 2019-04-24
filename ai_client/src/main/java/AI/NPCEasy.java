package AI;

import Messages.MoveInfo;
import javafx.util.Pair;

import java.util.Random;
import java.util.ArrayList;

/**
 * This is a stupid NPC which just selects a space randomly. Guaranteed to
 * pick an empty space, though.
 *
 * @author Daniel Edwards
 */
public class NPCEasy implements NPCBehaviour {

    public NPCEasy() {
    }

    @Override
    public MoveInfo getMove(EngineLib.Board b, char token) {

        MoveInfo result = null;
        char[][] boardArray = b.getBoardArray();

        ArrayList<Pair<Integer, Integer>> emptySpaces = new ArrayList<>();

        // Get all empty spaces
        for(int row = 0; row < b.ROW_COUNT; row++) {
            for(int col = 0; col < b.COL_COUNT; col++) {
                if(b.getPos(row, col) == b.DEFAULT_VALUE) {
                    emptySpaces.add(new Pair<>(row, col));
                }
            }
        }

        // Select one of the spaces
        if(emptySpaces.size() > 0) {
            Random r = new Random();

            Pair<Integer, Integer> selectedMove = emptySpaces.get( r.nextInt(emptySpaces.size()) );

            result = new MoveInfo();
            result.setRow(selectedMove.getKey());
            result.setCol(selectedMove.getValue());
        }
        else {
            throw new IllegalStateException("If we hit this, there is a big problem!!!");
            // This state should not be possible.
        }

        return result;
    }


}
