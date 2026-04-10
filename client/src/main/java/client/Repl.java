package client;

import java.util.Scanner;

public class Repl {
    private ServerFacade facade;
    private WebSocketFacade webFacade;
    private PreLogin preLogin;
    private PostLogin postLogin;
    private PostJoin postJoin;
    private boolean loggedIn;
    private boolean postJoinBool;
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
                if(result.equals("Joined game as " + postLogin.getName() + ".")){
                    webFacade = new WebSocketFacade("http://localhost:" + port);
                    postJoin = new PostJoin(webFacade);
                    result = postJoin.eval(line, authToken);
                    System.out.print(result);
                    postJoinBool = true;
                }
                if(result.equals("Leaving game.")){
                    postJoinBool = false;
                    result = postLogin.eval(line, authToken);
                    System.out.print(result);
                }

            }
            if(postJoinBool){
                result = postJoin.eval(line, authToken);
                System.out.print(result);
            }
        }
        System.out.println();
    }

}
