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
        SidewaysMovesCalculator moves = new SidewaysMovesCalculator(board,pos);
        return moves.getMoves();
    }


}
