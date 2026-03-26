package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServerFacade {
    public void makeRequest(method, path, requestBody, authToken, responseClass){

    }

    public clear()throws Exception{
        URL url = new URL("http://localhost:8080/db");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

    }
    public createGame()throws Exception{
        URL url = new URL("http://localhost:8080/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
    }

    public joinGame()throws Exception{
        URL url = new URL("http://localhost:8080/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        AuthData authData= new AuthData(authToken, username);
        var json = new Gson().toJson(authData);
        writer.write(json);
        writer.close();

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);

        return new Gson().fromJson(reader, AuthData.class);
    }

    public List<GameData> listGames(String authToken, String username)throws Exception{
        URL url = new URL("http://localhost:8080/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "authorization");
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        AuthData authData= new AuthData(authToken, username);
        var json = new Gson().toJson(authData);
        writer.write(json);
        writer.close();

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);

        return new Gson().fromJson(reader, AuthData.class);
    }

    public AuthData login(String authToken, String username)throws Exception{
        URL url = new URL("http://localhost:8080/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        AuthData authData= new AuthData(authToken, username);
        var json = new Gson().toJson(authData);
        writer.write(json);
        writer.close();

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);

        return new Gson().fromJson(reader, AuthData.class);
    }

    public AuthData logout(String username, String password)throws Exception{
        URL url = new URL("http://localhost:8080/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "authorization");
        connection.setRequestMethod("DELETE");

        connection.setDoOutput(true);
        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        AuthData authData= new AuthData(username, password);
        var json = new Gson().toJson(authData);
        writer.write(json);
        writer.close();

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);

        return new Gson().fromJson(reader, AuthData.class);
    }

    public AuthData register(String username, String password, String email) throws Exception{
        URL url = new URL("http://localhost:8080/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        UserData userData = new UserData(username, password, email);
        var json = new Gson().toJson(userData);
        writer.write(json);
        writer.close();

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);
        var authData = new Gson().fromJson(reader, AuthData.class);

        return authData;

    }




}
