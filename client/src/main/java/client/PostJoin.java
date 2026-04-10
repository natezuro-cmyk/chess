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
                - move <from> <to>   (e.g. move e2 e4)
                - highlight <pos>    (e.g. highlight e2)
                - leave
                - resign
                - redraw
                """;
    }

    private ChessPosition getPosition(String boardPos){
        char[] parse = boardPos.toCharArray();
        int col = parse[0]-'a' +1;
        int row = parse[1]-'1' +1;
        return new ChessPosition(row,col);
    }


    private String highlight(String[] params) {
        if (params.length < 1) { return "Usage: highlight <position>  (e.g. highlight e2)"; }
        ChessPosition pos = getPosition(params[0]);
        facade.highlight(pos);
        return "";
    }

    private String makeMove(String[] params, String authToken) throws Exception{
        ChessPosition startPos = getPosition(params[0]);
        ChessPosition endPos = getPosition(params[1]);
        ChessMove move = new ChessMove (startPos, endPos, null);
        facade.makeMove(move, authToken);

        return "Making move.";
    }

    private String leave(String authToken) throws Exception {
        facade.leave(authToken);
        return "Leaving game.";
    }

    private String resign(String authToken) throws Exception {
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
