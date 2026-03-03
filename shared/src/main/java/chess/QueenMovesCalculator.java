package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public QueenMovesCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves() {
        ContinuousMovesCalculator queenMoves = new ContinuousMovesCalculator(board,pos);

        return queenMoves.getMoves("QUEEN");
    }


}
