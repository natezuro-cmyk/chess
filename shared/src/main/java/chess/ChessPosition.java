package chess;

import static java.util.Objects.hash;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public boolean equals(Object other){
        ChessPosition newPos = (ChessPosition) other;
        return (this.row == newPos.getRow() && this.col == newPos.getColumn());
    }

    @Override
    public int hashCode(){
        return hash(row,col);
    }

    public String toString(){
        return("Row: " + getRow() + " Column: " + getColumn());
    }
}
