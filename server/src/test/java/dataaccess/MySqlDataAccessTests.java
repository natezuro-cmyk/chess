package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlDataAccessTests {
   MySqlDataAccess dataAccess;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
    }

    @Test
    public void okClear() throws Exception {
        dataAccess.createUser(new UserData("billy", "1234", "billy@gmail.com"));
        dataAccess.createGame("mygame");

        dataAccess.clear();

        assertNull(dataAccess.getUser("billy"));
        assertNull(dataAccess.getGame(1));
    }

    @Test
    public void okCreateUser() throws Exception {
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        dataAccess.createUser(user);

        assertEquals(dataAccess.getUser("billy"), user);
    }

    @Test
    public void badCreateUser() throws Exception {
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        dataAccess.createUser(user);
       // create user twice
        assertThrows(DataAccessException.class, () -> dataAccess.createUser(user));
    }

    @Test
    public void okGetUser() throws Exception {
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        dataAccess.createUser(user);
        UserData data = dataAccess.getUser("billy");

        assertEquals(user, data);
    }

    //user tries to logout before logged in
    @Test
    public void badGetUser() throws Exception {
        UserData data = dataAccess.getUser("billy");
        assertNull(data);
    }

    @Test
    public void okListGames() throws Exception {
        dataAccess.createGame("1");
        dataAccess.createGame("2");
        dataAccess.createGame("3");

        List<GameData> games = dataAccess.listGames();

        assertTrue(games.size()== 3);
    }

    @Test
    public void badListGames() throws Exception {
        List<GameData> games = dataAccess.listGames();
        // no games
        assertEquals(0,games.size());
    }

    @Test
    public void okCreateGame() throws Exception {
        int id = dataAccess.createGame("mygame");
        assertTrue(id > 0);
    }

    @Test
    public void badCreateGame() throws Exception {
        assertThrows(DataAccessException.class, () -> dataAccess.createGame(null));
    }

    @Test
    public void okGetGame() throws Exception {
        int id = dataAccess.createGame("mygame");
        GameData game = dataAccess.getGame(id);
        assertEquals("mygame", game.gameName());
    }

    @Test
    public void badGetGame() throws Exception {
        GameData game = dataAccess.getGame(-1);
        assertNull(game);
    }

    @Test
    public void okUpdateGame() throws Exception {
        int id = dataAccess.createGame("mygame");
        GameData game = dataAccess.getGame(id);
        GameData updated = new GameData(id, "billy", null, "mygame", game.game());
        dataAccess.updateGame(updated);
        assertEquals("billy", dataAccess.getGame(id).whiteUsername());
    }

    @Test
    public void badUpdateGame() throws Exception {
        GameData game = new GameData(-1, null, null, "mygame", new ChessGame());
        assertThrows(DataAccessException.class, () -> dataAccess.updateGame(game));
    }

    @Test
    public void okCreateAuth() throws Exception {
        AuthData auth = new AuthData("token123", "billy");
        dataAccess.createAuth(auth);
        assertEquals(auth, dataAccess.getAuth("token123"));
    }

    @Test
    public void badCreateAuth() throws Exception {
        assertThrows(DataAccessException.class, () -> dataAccess.createAuth(null));
    }

    @Test
    public void okGetAuth() throws Exception {
        AuthData auth = new AuthData("token123", "billy");
        dataAccess.createAuth(auth);
        AuthData data = dataAccess.getAuth("token123");
        assertEquals(auth, data);
    }

    @Test
    public void badGetAuth() throws Exception {
        AuthData data = dataAccess.getAuth("faketoken");
        assertNull(data);
    }

    @Test
    public void okDeleteAuth() throws Exception {
        AuthData auth = new AuthData("token123", "billy");
        dataAccess.createAuth(auth);
        dataAccess.deleteAuth("token123");
        assertNull(dataAccess.getAuth("token123"));
    }

    @Test
    public void badDeleteAuth() throws Exception {
        assertThrows(DataAccessException.class, () -> dataAccess.deleteAuth(null));
    }




}
