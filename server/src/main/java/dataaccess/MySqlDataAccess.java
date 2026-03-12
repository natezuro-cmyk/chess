package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import exception.ResponseException;

import static dataaccess.DatabaseManager.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements DataAccess {

    private int gameID = 0;

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
        try (var conn = DatabaseManager.getConnection()){
            String[] statements = new String[]{"DROP TABLE IF EXISTS games", "DROP TABLE IF EXISTS users", "DROP TABLE IF EXISTS authTokens"};
            for(String statement: statements) {
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear database", ex);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create user database", ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statement = "SELECT username, password, email FROM users WHERE username = ?";
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                ResultSet data = preparedStatement.executeQuery();
                if(data.next()){
                    return new UserData(data.getString("username"),
                            data.getString("password"),data.getString("email"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get user from database", ex);
        }
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            ChessGame game = new ChessGame();
            String gameJson = new Gson().toJson(game);
            try(var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setNull(1, Types.NULL);
                preparedStatement.setNull(2, Types.NULL);
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(4, gameJson);
                preparedStatement.executeUpdate();
                ResultSet key = preparedStatement.getGeneratedKeys();
                if (key.next()) {
                    return key.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create user database", ex);
        }
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setInt(1, gameID);
                ResultSet data = preparedStatement.executeQuery();
                if(data.next()){
                    String json = data.getString("game");
                    ChessGame game = new Gson().fromJson(json, ChessGame.class);
                    return new GameData(data.getInt("gameID"), data.getString("whiteUsername"),
                            data.getString("blackUsername"), data.getString("gameName"),
                            game);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get user from database", ex);
        }
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()){
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try(var preparedStatement = conn.prepareStatement(statement)) {
                ResultSet data = preparedStatement.executeQuery();
                while(data.next()){
                    String json = data.getString("game");
                    GameData gameData = new GameData(data.getInt("gameID"),data.getString("whiteUsername"),
                            data.getString("blackUsername"), data.getString("gameName"),
                            new Gson().fromJson(json, ChessGame.class));
                    games.add(gameData);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get user from database", ex);
        }
        return games;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
            String gameJson = new Gson().toJson(game.game());
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                preparedStatement.setString(4, gameJson);
                preparedStatement.setInt(5, game.gameID());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to update game", ex);
        }
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statement = "INSERT INTO authTokens (authToken, username) VALUES (?, ?)";
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create authtoken", ex);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statement = "SELECT authToken, username, FROM authTokens WHERE authtoken = ?";
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                ResultSet data = preparedStatement.executeQuery();
                if(data.next()){
                    return new UserData(data.getString("username"),
                            data.getString("password"),data.getString("email"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get user from database", ex);
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
