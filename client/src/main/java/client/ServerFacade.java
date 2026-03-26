package client;

import java.net.HttpURLConnection;

public class ServerFacade {
    public void makeRequest(method, path, requestBody, authToken, responseClass){

    }

    public clear(){
        URL url = new URL("http://localhost:8080/db");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

    }
    public createGame(){
        URL url = new URL("http://localhost:8080/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
    }

    public joinGame(){
        URL url = new URL("http://localhost:8080/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
    }

    public listGames(){URL url = new URL("http://localhost:8080/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
    }

    public login(){
        URL url = new URL("http://localhost:8080/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
    }

    public logout(){
        URL url = new URL("http://localhost:8080/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
    }

    public register(){
        URL url = new URL("http://localhost:8080/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
    }




}
