package client;

import chess.*;
import model.GameData;
import websocket.commands.UserGameCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PostJoin {

    private List<GameData> games;
    private WebSocketFacade facade;

    public PostJoin(WebSocketFacade facade){
        this.facade = facade;
    }

    public String eval(String input, String authToken){
        try {
            String[] tokens = input.toLowerCase().trim().split("\\s+");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "move" -> makeMove(params, authToken);
                case "highlight" -> highlight(params);
                case "leave" -> leave(authToken);
                case "resign" -> resign(authToken);
                case "redraw" -> redraw(authToken);
                default -> "Unknown command. Type 'help' to see available commands.";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String help(){
        return """
                - help
                - move <from> <to> [promotion]   (e.g. move e2 e4, move e7 e8 queen)
                - highlight <pos>    (e.g. highlight e2)
                - leave
                - resign
                - redraw
                """;
    }

    private ChessPosition getPosition(String boardPos) throws Exception {
        if (boardPos.length() != 2) {
            throw new Exception("Invalid position '" + boardPos + "'. Use a letter followed by number..");
        }
        char colChar = boardPos.charAt(0);
        char rowChar = boardPos.charAt(1);
        if (colChar < 'a' || colChar > 'h' || rowChar < '1' || rowChar > '8') {
            throw new Exception("Invalid position '" + boardPos + "'. Letter must be a-h and rumber must be 1-8.");
        }
        int col = colChar - 'a' + 1;
        int row = rowChar - '1' + 1;
        return new ChessPosition(row, col);
    }


    private String highlight(String[] params) throws Exception {
        if (params.length < 1) { return "Usage: highlight <position>  (e.g. highlight e2)"; }
        ChessPosition pos = getPosition(params[0]);
        facade.highlight(pos);
        return "";
    }

    private String makeMove(String[] params, String authToken) throws Exception{
        if (facade.isObserver()) { return "Observers cannot make moves."; }
        ChessPosition startPos = getPosition(params[0]);
        ChessPosition endPos = getPosition(params[1]);
        ChessPiece.PieceType promotion = null;
        if (params.length >= 3) {
            promotion = parsePromotion(params[2]);
        } else if (facade.isPromotionMove(startPos, endPos)) {
            System.out.print("Pawn promotion! Choose a piece! queen, rook, bishop, knight): ");
            Scanner scanner = new Scanner(System.in);
            promotion = parsePromotion(scanner.nextLine().trim().toLowerCase());
        }
        ChessMove move = new ChessMove(startPos, endPos, promotion);
        facade.makeMove(move, authToken);
        return "";
    }

    private ChessPiece.PieceType parsePromotion(String input) throws Exception {
        return switch (input) {
            case "queen"  -> ChessPiece.PieceType.QUEEN;
            case "rook"   -> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new Exception("Invalid promotion piece. Use queen, rook, bishop, or knight.");
        };
    }

    private String leave(String authToken) throws Exception {
        facade.leave(authToken);
        return "Leaving game.";
    }

    private String resign(String authToken) throws Exception {
        if (facade.isObserver()) { return "Observers cannot resign."; }
        System.out.print("Are you sure you want to resign? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("yes") || confirmation.equals("y")) {
            facade.resign(authToken);
            return "Resigning.";
        }
        return "Resign cancelled.";
    }

    private String redraw(String authToken) throws Exception {
        facade.redraw(authToken);
        return "Redrawing";
    }
}
