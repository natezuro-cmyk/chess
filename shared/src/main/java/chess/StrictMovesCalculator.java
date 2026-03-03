package chess;

import java.util.ArrayList;
import java.util.Collection;

public class StrictMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public StrictMovesCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves(String type){
        int[][] directions = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};

        if (type.equals("KING")){
            directions = new int[][]{{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
        }

        for (int[] dir: directions){
            ChessPosition nextPos = new ChessPosition(this.pos.getRow()+dir[0], this.pos.getColumn()+dir[1]);

            if(!board.isOOB(nextPos)){
                if(!board.hasPiece(nextPos)){
                    moves.add(new ChessMove(pos, nextPos, null));
                }
                else if(board.getPiece(nextPos).getTeamColor() != board.getPiece(pos).getTeamColor()){
                    moves.add(new ChessMove(pos, nextPos, null));
                }
            }
        }
        return moves;
    }

}
