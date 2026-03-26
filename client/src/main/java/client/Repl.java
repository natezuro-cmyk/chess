package client;

import java.util.Scanner;

public class Repl {
    private ServerFacade facade;
    private PreLogin preLogin;
    private PostLogin postLogin;
    private Scanner scanner;
    private boolean loggedIn = false;
    String authToken = null;

    public void Repl(int port){
        facade = new ServerFacade(port);
        preLogin = new PreLogin(facade);
        postLogin = new PostLogin(facade);
    }

    public void run(){

    }

}
