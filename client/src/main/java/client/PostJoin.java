package client;

import chess.*;
import ui.EscapeSequences;

public class PostJoin {

    public static void drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            drawWhiteBoard(board);
        } else {
            drawBlackBoard(board);
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }

    private static void drawWhiteBoard(ChessBoard board) {
        String[] cols = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int[] colNums = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] rowNums = {8, 7, 6, 5, 4, 3, 2, 1};

        printLetterBorder(cols);
        for (int row : rowNums) {
            printRow(board, row, colNums);
        }
        printLetterBorder(cols);
    }

    private static void drawBlackBoard(ChessBoard board) {
        String[] cols = {"h", "g", "f", "e", "d", "c", "b", "a"};
        int[] colNums = {8, 7, 6, 5, 4, 3, 2, 1};
        int[] rowNums = {1, 2, 3, 4, 5, 6, 7, 8};

        printLetterBorder(cols);
        for (int row : rowNums) {
            printRow(board, row, colNums);
        }
        printLetterBorder(cols);
    }

    private static void printLetterBorder(String[] cols) {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + "   ");
        for (String col : cols) {
            System.out.print(" " + col + " ");
        }
        System.out.print("   " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    private static void printRow(ChessBoard board, int row, int[] colNums) {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + row + " " + EscapeSequences.RESET_BG_COLOR);
        for (int col : colNums) {
            boolean isLight = (row + col) % 2 == 0;
            String bgColor = isLight ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_GREEN;
            System.out.print(bgColor + getPieceSymbol(board.getPiece(new ChessPosition(row, col))) + EscapeSequences.RESET_BG_COLOR);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + row + " " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) return EscapeSequences.EMPTY;
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
