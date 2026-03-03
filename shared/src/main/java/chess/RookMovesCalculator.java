package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public RookMovesCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves(){
        StraightMovesCalculator rookMoves = new StraightMovesCalculator(board,pos);
        return rookMoves.getMoves();

    }

}
