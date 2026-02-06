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
    ChessBoard board;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.board = new ChessBoard();
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

            if (isInCheck(color)) removeMoves.add(move);
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

            if(board.getPiece(move.getStartPosition()) == null) throw new InvalidMoveException("Invalid move");

            ChessPiece piece = board.getPiece(move.getStartPosition());
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

            // Check if the move is valid first
            if (!validMoves.contains(move) || getTeamTurn() != piece.getTeamColor()) {
                throw new InvalidMoveException("Invalid move");
            }

            if(move.getPromotionPiece() != null){
                ChessPiece promoPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                board.removePiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), promoPiece);
            }
            else {
                board.removePiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), piece);
            }

            if (turn == TeamColor.WHITE) {
                this.turn = TeamColor.BLACK;
            } else {
                this.turn = TeamColor.WHITE;
            }
        }

        /**
         * finds the King and returns it for easy access
         *
         * @param color
         * @return king piece
         */
        public ChessPosition getKing (TeamColor color){
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);

                    if (piece != null && piece.getTeamColor() == color
                            && piece.getPieceType() == ChessPiece.PieceType.KING) {
                        return pos;
                    }

                }
            }
            return null;
        }
        /**
         * Determines if the given team is in check
         *
         * @param teamColor which team to check for check
         * @return True if the specified team is in check
         */
        public boolean isInCheck (TeamColor teamColor){
            ChessPosition kingPos = getKing(teamColor);
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition startPos = new ChessPosition(row, col);
                    if(board.getPiece(startPos) != null){
                        ChessPiece piece = board.getPiece(startPos);
                        Collection<ChessMove> moves = piece.pieceMoves(board, startPos);
                        if (moves != null) {
                            for (ChessMove move : moves) {
                                if (move.getEndPosition().equals(kingPos)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }


        /**
         * Determines if the given team is in checkmate
         *
         * @param teamColor which team to check for checkmate
         * @return True if the specified team is in checkmate
         */
        public boolean isInCheckmate (TeamColor teamColor){
            Collection<ChessMove> validMoves = new ArrayList<>();

            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition startPos = new ChessPosition(row, col);

                    if(board.hasPiece(startPos)){
                        ChessPiece piece = board.getPiece(startPos);
                        if (piece.getTeamColor() == teamColor) {
                            validMoves.addAll(validMoves(startPos));
                        }
                    }
                }
            }
            return(isInCheck(teamColor) && validMoves.isEmpty());
        }

        /**
         * Determines if the given team is in stalemate, which here is defined as having
         * no valid moves while not in check.
         *
         * @param teamColor which team to check for stalemate
         * @return True if the specified team is in stalemate, otherwise false
         */
        public boolean isInStalemate (TeamColor teamColor){
            Collection<ChessMove> validMoves = new ArrayList<>();

            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition startPos = new ChessPosition(row, col);
            if (validMoves(getKing(teamColor)) == null && !isInCheck(teamColor)) return true;
            return false;
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

        @Override
        public boolean equals (Object other){
            ChessGame newGame = (ChessGame) other;
            return this.board.equals(newGame.board) && this.turn == newGame.getTeamTurn();
        }

        @Override
        public int hashCode () {
            return hash(this.turn, this.board);
        }

}
