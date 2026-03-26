package client;

import model.AuthData;
import model.GameData;

import java.util.Arrays;
import java.util.List;

public class PostLogin {
    List<GameData> games;
    private String authToken = null;
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
        this.authToken = null;
        return String.format("You are not logged in");
    }

    private String listGames(String authToken) throws Exception {
        games = facade.listGames(authToken);
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= games.size(); i++){
            GameData game = games.get(i-1);
            sb.append(i).append(".").append(game.gameName());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String createGame(String[] params, String authToken) throws Exception {
        if (params.length >= 1) {
            int gameID =  facade.createGame(authToken, params[0]);
            return "Game created successfully.";
        }
        throw new Exception("Please provide game name.");
    }

    private String playGame(String[] params, String authToken) throws Exception{
        if (params.length >= 2) {
            int i = Integer.valueOf(params[0]);
            facade.joinGame(authToken, i, params[1]);
            PostJoin.drawBoard(params)
        }
        throw new Exception("enter a game number and color.");
    }

    private String observeGame(String[] params){
        if (params.length >= 1) {
            int gameID =  facade.joinGame(authToken, params[0], params[1]);
        }
        throw new Exception("Please type a username and password.");
    }

}
