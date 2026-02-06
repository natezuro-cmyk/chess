package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static java.util.Objects.hash;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor turn;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
        this.turn = TeamColor.WHITE;
        ChessBoard board = new ChessBoard();
    }

    public ChessGame(ChessBoard board, TeamColor color) {
        this.board = board;
        this.turn = color;
        this.turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> removeInvalidMoves(Collection<ChessMove> moves, TeamColor color) {
        Collection<ChessMove> removeMoves = new ArrayList<>();

        for (ChessMove move : moves) {
            ChessBoard copiedBoard = new ChessBoard(this.board);
            ChessGame copiedGame = new ChessGame(copiedBoard, color);

            ChessPiece piece = copiedBoard.getPiece(move.getStartPosition());
            copiedBoard.removePiece(move.getStartPosition());
            copiedBoard.addPiece(move.getEndPosition(), piece);

            if (copiedGame.isInCheck(color)) removeMoves.add(move);
        }
        moves.removeAll(removeMoves);
        return moves;

    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * removes moves that put the king in check
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves (ChessPosition startPosition){
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> validMoves = piece.pieceMoves(this.board, startPosition);
        return removeInvalidMoves(validMoves, piece.getTeamColor());
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck (TeamColor teamColor){
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate (TeamColor teamColor){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate (TeamColor teamColor) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard (ChessBoard board){
        this.board = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard () {
        return this.board;
    }

}
