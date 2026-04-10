package client;

import chess.*;
import ui.EscapeSequences;

public class DrawBoard {

    private static final String LIGHT_SQUARE = EscapeSequences.SET_BG_COLOR_GREEN;
    private static final String DARK_SQUARE  = EscapeSequences.SET_BG_COLOR_WHITE;
    private static final String BORDER       = EscapeSequences.SET_BG_COLOR_DARK_GREY;

    private static final String[] WHITE_COLS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] BLACK_COLS = {"h", "g", "f", "e", "d", "c", "b", "a"};

    private static final int[] WHITE_COL_NUMS = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] BLACK_COL_NUMS = {8, 7, 6, 5, 4, 3, 2, 1};

    private static final int[] WHITE_ROW_ORDER = {8, 7, 6, 5, 4, 3, 2, 1}; // row 8 at top, row 1 at bottom
    private static final int[] BLACK_ROW_ORDER = {1, 2, 3, 4, 5, 6, 7, 8}; // row 1 at top, row 8 at bottom

    public static void drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            drawWhiteBoard(board);
        } else {
            drawBlackBoard(board);
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }

    private static void drawWhiteBoard(ChessBoard board) {
        printLetterBorder(WHITE_COLS);
        for (int row : WHITE_ROW_ORDER) {
            printRow(board, row, WHITE_COL_NUMS);
        }
        printLetterBorder(WHITE_COLS);
    }

    private static void drawBlackBoard(ChessBoard board) {
        printLetterBorder(BLACK_COLS);
        for (int row : BLACK_ROW_ORDER) {
            printRow(board, row, BLACK_COL_NUMS);
        }
        printLetterBorder(BLACK_COLS);
    }

    private static void printLetterBorder(String[] cols) {
        System.out.print(BORDER + "   ");
        for (String col : cols) {
            System.out.print(" " + col + " ");
        }
        System.out.print("   " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    private static void printRow(ChessBoard board, int row, int[] colNums) {
        // Left row number
        System.out.print(BORDER + " " + row + " " + EscapeSequences.RESET_BG_COLOR);

        // Print each square in the row
        for (int col : colNums) {
            String squareColor = getSquareColor(row, col);
            String pieceSymbol = getPieceSymbol(board.getPiece(new ChessPosition(row, col)));
            System.out.print(squareColor + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
        }

        // Right row number
        System.out.print(BORDER + " " + row + " " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    private static String getSquareColor(int row, int col) {
        boolean isLightSquare = (row + col) % 2 == 0;
        return isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        return switch (piece.getPieceType()) {
            case KING   -> isWhite ? EscapeSequences.WHITE_KING   : EscapeSequences.BLACK_KING;
            case QUEEN  -> isWhite ? EscapeSequences.WHITE_QUEEN  : EscapeSequences.BLACK_QUEEN;
            case BISHOP -> isWhite ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> isWhite ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case ROOK   -> isWhite ? EscapeSequences.WHITE_ROOK   : EscapeSequences.BLACK_ROOK;
            case PAWN   -> isWhite ? EscapeSequences.WHITE_PAWN   : EscapeSequences.BLACK_PAWN;
        };
    }
}
