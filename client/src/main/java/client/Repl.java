package client;

import java.util.Scanner;

public class Repl {
    private ServerFacade facade;
    private WebSocketFacade webFacade;
    private PreLogin preLogin;
    private PostLogin postLogin;
    private PostJoin postJoin;
    private boolean loggedIn;
    private boolean postJoinBool = false;
    private String authToken = null;
    int port;

    public Repl(int port){
        this.port = port;
        facade = new ServerFacade(port);
        preLogin = new PreLogin(facade);
        postLogin = new PostLogin(facade);
    }

    public void run() {
        System.out.println("Welcome to Chess.");
        System.out.print(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            if(authToken == null){
                result = preLogin.eval(line);
                System.out.println(result);
                authToken = preLogin.getAuthToken();

            }
            else if(authToken != null && !postJoinBool){
                result = postLogin.eval(line, authToken);
                System.out.println(result);
                if(result.equals("You have been logged out.")){
                    loggedIn = false;
                    authToken = null;
                }
                if(result.equals("Joined game as " + postLogin.getName() + ".")){
                    webFacade = postLogin.getWebFacade();
                    postJoin = new PostJoin(webFacade);
                    postJoinBool = true;
                }
            }
            else if(postJoinBool){
                result = postJoin.eval(line, authToken);
                System.out.println(result);
                if(result.equals("Leaving game.")){
                    postJoinBool = false;
                }
            }
        }
        System.out.println();
    }

}
