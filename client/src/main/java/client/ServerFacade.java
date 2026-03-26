package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ServerFacade {
    int port;

    public ServerFacade(int port){
        this.port = port;
    }

    public void clear()throws Exception{
        URL url = new URL("http://localhost:"+port+"/db");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("DELETE");

        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        var body = Map.of();
        var json = new Gson().toJson(body);
        writer.write(json);
        writer.close();

        if (connection.getResponseCode() != 200) {
            var errorStream = connection.getErrorStream();
            var reader = new InputStreamReader(errorStream);
            var error = new Gson().fromJson(reader, Map.class);
            throw new Exception((String) error.get("message"));
        }

    }
    public int createGame(String authToken, String gameName)throws Exception{
        URL url = new URL("http://localhost:"+port+"/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("authorization", authToken);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        var body = Map.of("gameName", gameName);
        var json = new Gson().toJson(body);
        writer.write(json);
        writer.close();

        if (connection.getResponseCode() != 200) {
            var errorStream = connection.getErrorStream();
            var reader = new InputStreamReader(errorStream);
            var error = new Gson().fromJson(reader, Map.class);
            throw new Exception((String) error.get("message"));
        }

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);

        var response = new Gson().fromJson(reader, Map.class);
        return ((Double) response.get("gameID")).intValue();
    }

    public void joinGame(String authToken, int gameID, String playerColor)throws Exception{
        URL url = new URL("http://localhost:"+port+"/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("authorization", authToken);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        var body = Map.of("gameID",gameID,"playerColor", playerColor);
        var json = new Gson().toJson(body);
        writer.write(json);
        writer.close();

        if (connection.getResponseCode() != 200) {
            var errorStream = connection.getErrorStream();
            var reader = new InputStreamReader(errorStream);
            var error = new Gson().fromJson(reader, Map.class);
            throw new Exception((String) error.get("message"));
        }

    }

    public List<GameData> listGames(String authToken)throws Exception{
        URL url = new URL("http://localhost:"+port+"/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("authorization", authToken);
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            var errorStream = connection.getErrorStream();
            var reader = new InputStreamReader(errorStream);
            var error = new Gson().fromJson(reader, Map.class);
            throw new Exception((String) error.get("message"));
        }

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);

        var map = new Gson().fromJson(reader, Map.class);
        var gamesJson = new Gson().toJson(map.get("games"));
        List<GameData> games = new Gson().fromJson(gamesJson, new TypeToken<List<GameData>>(){}.getType());
        return games;
    }

    public AuthData login(String username, String password)throws Exception{
        URL url = new URL("http://localhost:"+port+"/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        var outputStream = connection.getOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        var body = Map.of("username", username, "password", password);
        var json = new Gson().toJson(body);
        writer.write(json);
        writer.close();

        if (connection.getResponseCode() != 200) {
            var errorStream = connection.getErrorStream();
            var reader = new InputStreamReader(errorStream);
            var error = new Gson().fromJson(reader, Map.class);
            throw new Exception((String) error.get("message"));
        }

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);

        return new Gson().fromJson(reader, AuthData.class);
    }

    public void logout(String authToken)throws Exception{
        URL url = new URL("http://localhost:"+port+"/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("authorization", authToken);
        connection.setRequestMethod("DELETE");

        if (connection.getResponseCode() != 200) {
            var errorStream = connection.getErrorStream();
            var reader = new InputStreamReader(errorStream);
            var error = new Gson().fromJson(reader, Map.class);
            throw new Exception((String) error.get("message"));
        }

        var inputStream = connection.getInputStream();
    }

    public AuthData register(String username, String password, String email) throws Exception{
        URL url = new URL("http://localhost:"+port+"/user");
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

        if (connection.getResponseCode() != 200) {
            var errorStream = connection.getErrorStream();
            var reader = new InputStreamReader(errorStream);
            var error = new Gson().fromJson(reader, Map.class);
            throw new Exception((String) error.get("message"));
        }

        var inputStream = connection.getInputStream();
        var reader = new InputStreamReader(inputStream);
        var authData = new Gson().fromJson(reader, AuthData.class);

        return authData;

    }

}
