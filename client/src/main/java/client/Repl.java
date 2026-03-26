package client;

import java.util.Scanner;

public class Repl {
    private ServerFacade facade;
    private PreLogin preLogin;
    private PostLogin postLogin;
    private Scanner scanner;
    private boolean loggedIn = false;
    private String authToken = null;

    public Repl(int port){
        facade = new ServerFacade(port);
        preLogin = new PreLogin(facade);
        postLogin = new PostLogin(facade);
    }

    public void run(){
        System.out.println("Welcome to Chess.");
        System.out.print(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            if(authToken == null){
                result = preLogin.eval(line);
                System.out.print(result);
                authToken = preLogin.getAuthToken();

            }
            else{
                result = postLogin.eval(line, authToken);
                System.out.print(result);
                if(result.equals("You have been logged out.")){
                    loggedIn = false;
                    authToken = null;
                }
            }
        }
        System.out.println();
    }

}
