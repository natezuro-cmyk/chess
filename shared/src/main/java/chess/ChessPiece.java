package chess;

import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.hash;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
            BishopMovesCalculator b = new BishopMovesCalculator(board, myPosition);
            return b.getMoves();
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK){
            RookMovesCalculator r = new RookMovesCalculator(board, myPosition);
            return r.getMoves();
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN){
            QueenMovesCalculator q = new QueenMovesCalculator(board, myPosition);
            return q.getMoves();
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN){
            PawnMovesCalculator p = new PawnMovesCalculator(board, myPosition);
            return p.getMoves();
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT){
            KnightMovesCalculator k = new KnightMovesCalculator(board, myPosition);
            return k.getMoves();
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KING){
            KingMovesCalculator K = new KingMovesCalculator(board, myPosition);
            return K.getMoves();
        }

        return Collections.emptyList();
    }
    @Override
    public boolean equals(Object other){
        ChessPiece piece = (ChessPiece) other;
        return (this.type == piece.getPieceType() && this.pieceColor == piece.getTeamColor());
    }

    @Override
    public int hashCode(){
        return hash(pieceColor,type);
    }

    public String toString(){
        return(pieceColor + " " + type);
    }
}
