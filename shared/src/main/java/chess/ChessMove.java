package chess;

import chess.ChessPiece;
import chess.ChessPosition;

import static java.util.Objects.hash;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public void setStartPosition(ChessPosition startPosition) {this.startPosition = startPosition;}
    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }

    @Override
    public String toString(){
        return("[" + startPosition.getRow() + "," + startPosition.getColumn() + "]"
                + "[" + endPosition.getRow() + "," + endPosition.getColumn() + "]");
    }

    @Override
    public boolean equals(Object other){
        ChessMove newMove = (ChessMove) other;
        return this.startPosition.equals(newMove.getStartPosition())
                && this.endPosition.equals(newMove.getEndPosition()) &&this.getPromotionPiece() == newMove.getPromotionPiece();
    }

    @Override
    public int hashCode(){
        return hash(startPosition,endPosition,promotionPiece);
    }
}