package EngineLib;

import java.util.Arrays;
import java.util.Objects;

public class Board {
    //***************************************************************************
    // Data
    //***************************************************************************
    final int ROW_COUNT = 3;
    final int COL_COUNT = 3;
    final char DEFAULT_VALUE = ' ';
    private char[][] boardArray = new char[ROW_COUNT][COL_COUNT];
    //***************************************************************************


    //***************************************************************************
    // Constructors
    //***************************************************************************
    /**
     * Default Board Constructor
     * Makes a Board Object with all positions set to ' '.
     * @author Bob Baker
     */
    public Board() {
        // Set Default-Board to all spaces
        for(int i = 0; i < ROW_COUNT; ++i) {
            for(int j = 0; j < COL_COUNT; ++j) {
                boardArray[i][j] = DEFAULT_VALUE;
            }
        }
    }

    //***************************************************************************
    /**
     * Makes a Board Object from a 2D Array of a Board.
     * @param boardArray
     * @author Bob Baker
     */
    public Board(char[][] boardArray) {
        this.boardArray = new char[this.ROW_COUNT][this.COL_COUNT];
        for(int i = 0; i < this.ROW_COUNT; i++) {
            for(int j = 0; j < this.COL_COUNT; j++) {
                this.boardArray[i][j] = boardArray[i][j];
            }
        }
        //this.boardArray = boardArray;
    }
    //***************************************************************************


    //***************************************************************************
    /**
     * Returns a 2D array of the board data.
     * @return boardArray
     * @author Bob Baker
     */
    public char[][] getBoardArray() {
        return boardArray;
    }
    //***************************************************************************


    //***************************************************************************
    /**
     * Sets the value of one position on the Board.
     * @param row
     * @param col
     * @param c
     * @throws IllegalArgumentException
     * @author Bob Baker
     */
    public void setPos(int row, int col, char c) {
        // Check Bounds - Throw RT-Expt if not in bounds
        if((row > ROW_COUNT || col > COL_COUNT) || (row < 0 || col < 0))  {
            String error = String.format("INVALID XY CHORD (%d,%d): Must be bounded by " +
                    "(ROW_COUNT,COL_COUNT)(%d,%d)",row,col, ROW_COUNT, COL_COUNT);
            throw new IllegalArgumentException(error);
        }
        else {
            boardArray[row][col] = c;
        }
    }
    //***************************************************************************


    //***************************************************************************
    /**
     * Returns the char value at a given position on the Board.
     * @param row
     * @param col
     * @return (char) value
     * @throws IllegalArgumentException
     * @author Bob Baker
     */
    public char getPos(int row, int col) {
        // Check Bounds - Throw RT-Expt if not in bounds
        if ((row > ROW_COUNT || col > COL_COUNT) || (row < 0 || col < 0)) {
            String error = String.format("INVALID XY CHORD (%d,%d): Must be bounded by " +
                    "(ROW_COUNT,COL_COUNT)(%d,%d)", row, col, ROW_COUNT, COL_COUNT);
            throw new IllegalArgumentException(error);
        } else {
            return boardArray[row][col];
        }
    }
    //***************************************************************************


    //***************************************************************************
    /**
     * Returns the hashCode reference of a Board Object
     * @return hashCode
     * @author Bob Baker
     */
    @Override
    public int hashCode() {
        return Objects.hash(boardArray,DEFAULT_VALUE, ROW_COUNT, COL_COUNT);
    }
    //***************************************************************************


    //***************************************************************************
    /**
     * Compares two Board objects
     * @param obj
     * @return (bool) isEqual
     * @author Bob Baker
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Board) {
            Board board= (Board) obj;
            return (Arrays.deepEquals(this.boardArray, board.boardArray)     &&
                    Objects.equals(this.DEFAULT_VALUE, board.DEFAULT_VALUE) &&
                    Objects.equals(this.ROW_COUNT, board.ROW_COUNT)   &&
                    Objects.equals(this.COL_COUNT, board.COL_COUNT)     );
        }
        else {
            return false;
        }
    }
    //***************************************************************************


    //***************************************************************************
    @Override
    /**
     * Returns a formatted string representing the Board View.
     * @return (String) toString
     * @author Bob Baker
     */
    // not for dev, only for testing.
    // prints with formatting
    public String toString() {
        char delim = ' ';
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < ROW_COUNT; ++i) {
            for(int j = 0; j < COL_COUNT; ++j) {
                sb.append(boardArray[i][j]);
                //sb.append(delim);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    //***************************************************************************



    //***************************************************************************
    /**
     * Returns the amount of empty spaces on the board
     * @return (int) emptySpaces
     * @author Bob Baker
     */
    public int numEmptySpaces(){
        int count = 0;

        for(int i = 0; i < ROW_COUNT; i++) {
            for(int j = 0; j < COL_COUNT; j++){
                if(boardArray[i][j] == DEFAULT_VALUE) {
                    count++;
                }
            }
        }
        return count;
    }
    //***************************************************************************
}