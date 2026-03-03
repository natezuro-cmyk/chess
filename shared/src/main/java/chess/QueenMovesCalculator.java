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
        SidewaysMovesCalculator sideMoves = new SidewaysMovesCalculator(board,pos);
        StraightMovesCalculator straightMoves = new StraightMovesCalculator(board,pos);
        moves.addAll(sideMoves.getMoves());
        moves.addAll(straightMoves.getMoves());
        return moves;
    }


}
