package client;

import model.AuthData;

import java.util.Arrays;

public class PreLogin {
    private ServerFacade facade;
    private String authToken = null;

    public PreLogin(ServerFacade facade){
        this.facade = facade;
    }

    public String eval(String input){
        try {
            String[] tokens = input.toLowerCase().trim().split("\\s+");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> "Unknown command. Type 'help' to see available commands.";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String help(){
        return """
                - help
                - login
                - register
                - quit
                """;
    }

    private String login(String[] params) throws Exception {
        if (params.length >= 2) {
            AuthData result = facade.login(params[0], params[1]);
            this.authToken = result.authToken();
            return String.format("""
                    You logged in as %s.You're options are:
                    - help
                    - logout
                    - list
                    - create
                    - play
                    - observe
                    - quit
                    """, params[0]);
        }
        throw new Exception("Please type a username and password.");
    }

    private String register(String[] params) throws Exception {
        if (params.length >= 3) {
            AuthData result = facade.register(params[0], params[1], params[2]);
            this.authToken = result.authToken();
            return String.format("You registered as %s.", params[0]);
        }
            throw new Exception("Please provide a username, password and email");
    }

    public String getAuthToken(){
        return authToken;
    }

}

