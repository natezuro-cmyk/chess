package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public KingMovesCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves() {
        StrictMovesCalculator kingMoves = new StrictMovesCalculator(board,pos);
        return kingMoves.getMoves("KING");
    }

}
