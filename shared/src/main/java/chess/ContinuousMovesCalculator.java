package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ContinuousMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public ContinuousMovesCalculator(ChessBoard board, ChessPosition pos) {
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves (String type) {
        int[][] directions = {{0,1},{0,-1},{1,0},{-1,0},{1, 1},{1, -1},{-1, 1},{-1, -1}};

        if(type.equals("ROOK")){
            directions = new int[][]{{0,1},{0,-1},{1,0},{-1,0}};
        }
        if(type.equals("BISHOP")){
            directions = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        }

        for (int[] dir : directions) {
            ChessPosition nextPos = new ChessPosition(this.pos.getRow() + dir[0], this.pos.getColumn() + dir[1]);
            while (!board.isOOB(nextPos)) {
                if (!board.hasPiece(nextPos)) {
                    moves.add(new ChessMove(this.pos, nextPos, null));
                    nextPos = new ChessPosition(nextPos.getRow() + dir[0], nextPos.getColumn() + dir[1]);
                } else if (board.hasPiece(nextPos) && board.getPiece(nextPos).getTeamColor() != board.getPiece(pos).getTeamColor()) {
                    moves.add(new ChessMove(this.pos, nextPos, null));
                    break;
                } else {
                    break;
                }
            }
        }
        return moves;
    }
}

