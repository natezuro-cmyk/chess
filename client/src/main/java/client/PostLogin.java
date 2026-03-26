package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.Arrays;
import java.util.List;

public class PostLogin {
    private List<GameData> games;
    private ServerFacade facade;

    public PostLogin(ServerFacade facade){
        this.facade = facade;
    }

    public String eval(String input, String authToken){
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "logout" -> logout(authToken);
                case "list" -> listGames(authToken);
                case "create" -> createGame(params, authToken);
                case "play" -> playGame(params, authToken);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> "Unknown command. Type 'help' to see available commands.";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String help(){
        return """
                - help
                - logout
                - list
                - create
                - play
                - observe
                - quit
                """;
    }

    private String logout(String authToken) throws Exception{
        facade.logout(authToken);
        return "You have been logged out.";
    }

    private String listGames(String authToken) throws Exception {
        games = facade.listGames(authToken);
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= games.size(); i++){
            GameData game = games.get(i-1);
            sb.append(i).append(". ").append(game.gameName());
            sb.append(" | White: ").append(game.whiteUsername() != null ? game.whiteUsername() : "open");
            sb.append(" | Black: ").append(game.blackUsername() != null ? game.blackUsername() : "open");
            sb.append("\n");
        }
        return sb.toString();
    }

    private String createGame(String[] params, String authToken) throws Exception {
        if (params.length >= 1) {
            facade.createGame(authToken, params[0]);
            return "Game created successfully.";
        }
        throw new Exception("Please provide a game name.");
    }

    private String playGame(String[] params, String authToken) throws Exception{
        if (params.length >= 2) {
            int i = Integer.parseInt(params[0]);
            facade.joinGame(authToken, games.get(i - 1).gameID(), params[1].toUpperCase());
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            ChessGame.TeamColor perspective = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
            PostJoin.drawBoard(board, perspective);
            return "Joined game as " + params[1].toUpperCase() + ".";
        }
        throw new Exception("Please enter a game number and color");
    }

    private String observeGame(String[] params) throws Exception{
        if (params.length >= 1) {
            int i = Integer.parseInt(params[0]);
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            PostJoin.drawBoard(board, ChessGame.TeamColor.WHITE);
            return "Observing game " + params[0];
        }
        throw new Exception("Please enter a game number.");
    }

}
