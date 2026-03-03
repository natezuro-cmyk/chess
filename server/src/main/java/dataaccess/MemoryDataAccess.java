package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class MemoryDataAccess implements DataAccess{
    public Hashtable<Integer, GameData> games = new Hashtable<>();
    public Hashtable<String, AuthData> authTokens = new Hashtable<>();
    public Hashtable<String, UserData> users = new Hashtable<>();

    private int gameID = 0;

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        authTokens.clear();
        users.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        gameID++;
        GameData data = new GameData(gameID, null, null, gameName, null);
        games.put(gameID,data);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> list = new ArrayList<>(games.values());
        return list;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        authTokens.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authTokens.remove(authToken);

    }
}
