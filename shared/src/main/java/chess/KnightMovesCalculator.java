package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public KnightMovesCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves(){
        StrictMovesCalculator knightMoves = new StrictMovesCalculator(board,pos);
        return knightMoves.getMoves("KNIGHT");
    }

}
