package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public BishopMovesCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves(){
        ContinuousMovesCalculator bishopMoves = new ContinuousMovesCalculator(board,pos);
        return bishopMoves.getMoves("BISHOP");
    }


}
