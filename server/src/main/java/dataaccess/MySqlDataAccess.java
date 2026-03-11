package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.sql.*;
import java.util.List;
import exception.ResponseException;

import static dataaccess.DatabaseManager.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements DataAccess {

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            String[] statements = new String[]{"CREATE TABLE IF NOT EXISTS games(id INT NOT NULL AUTO_INCREMENT, whiteUsername VARCHAR(255) NOT NULL, blackUsername VARCHAR(255) NOT NULL, gameName VARCHAR(255) NOT NULL, chessGame TEXT NOT NULL);",
                    "CREATE TABLE IF NOT EXISTS users (username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL);",
                    "CREATE TABLE IF NOT EXISTS authTokens (authToken VARCHAR(255) NOT NULL, username VARCHAR(255) NOT NULL);"};
            for(String statement: statements) {
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

        @Override
    public void clear() throws DataAccessException {

        var statement = "TRUNCATE TABLE user";

        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
        DROP DATABASE pet_store;
        CREATE DATABASE pet_store;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
