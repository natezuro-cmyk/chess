package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {
    Collection<ChessMove> moves = new ArrayList<>();
    ChessBoard board;
    ChessPosition pos;

    public PawnMovesCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> getMoves(){
        int direction = (board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE) ? 1:-1;
        boolean startPos = (pos.getRow() == 2 || pos.getRow() == 7) ? true: false;
        ChessPosition inFront = new ChessPosition (pos.getRow()+direction, pos.getColumn());

        //get move in front and second move in front
        if(!board.hasPiece(inFront)){
            addMove(new ChessMove (pos,inFront, null));
            ChessPosition twoInFront = new ChessPosition (pos.getRow()+(direction*2), pos.getColumn());
            if(!board.isOOB(twoInFront) && !board.hasPiece(twoInFront) && startPos){
                addMove(new ChessMove (pos,twoInFront, null));
            }
        }
        //right diagonal
        ChessPosition rightDiag = new ChessPosition(pos.getRow()+direction, pos.getColumn()+1);
        if(!board.isOOB(rightDiag) && board.hasPiece(rightDiag)
                && board.getPiece(rightDiag).getTeamColor() != board.getPiece(pos).getTeamColor()){
            addMove(new ChessMove(pos, rightDiag, null));
        }
        //left diagonal
        ChessPosition leftDiag = new ChessPosition(pos.getRow()+direction, pos.getColumn()-1);
        if(!board.isOOB(leftDiag) && board.hasPiece(leftDiag)
                && board.getPiece(leftDiag).getTeamColor() != board.getPiece(pos).getTeamColor()){
            addMove(new ChessMove(pos, leftDiag, null));
        }
        //left diagonal
        return moves;
    }

    public void addMove(ChessMove move){
        if (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1){
            moves.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
        }
        else moves.add(new ChessMove(move.getStartPosition(),move.getEndPosition(), null));
    }

}
