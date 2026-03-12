package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlDataAccessTests {
    DataAccess dataAccess = new MySqlDataAccess();

    @BeforeEach
    void clear() throws DataAccessException {
        dataAcces.clear();
    }

    @Test
    public void okClear() throws Exception {
        ClearService clearService = new ClearService(dataAccess);
        dataAccess.createUser(new UserData("billy", "1234", "billy@gmail.com"));
        dataAccess.createGame("mygame");

        dataAcces.clear();

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
        ListGamesService listGamesService = new ListGamesService(dataAccess);
        AuthData data = new AuthData("1234","billy");
        dataAccess.createAuth(new AuthData("1234", "billy"));
        dataAccess.createGame("1");

        int size = listGamesService.listGames(data).size();

        assertTrue(size == 1);
        assertTrue(data.username().equals("billy"));
    }

    @Test
    public void badCreateGame() throws Exception {
        CreateGameService createGameService = new CreateGameService(dataAccess);
        assertThrows(UnauthorizedException.class, () ->
                createGameService.createGame(new AuthData("1234", "billy"), "firstgame"));
    }

    @Test
    public void okJoin() throws Exception {
        dataAccess.createAuth(new AuthData("token123", "billy"));
        JoinGameService joinGameService = new JoinGameService(dataAccess);
        int gameID = dataAccess.createGame("mygame");
        assertDoesNotThrow(() ->
                joinGameService.joinGame(new AuthData("token123", "billy"), gameID, "WHITE"));
    }

    @Test
    public void badJoin() throws Exception {
        JoinGameService joinGameService = new JoinGameService(dataAccess);
        dataAccess.createAuth(new AuthData("1234", "billy"));
        dataAccess.createAuth(new AuthData("567", "johnny"));
        int gameID = dataAccess.createGame("firstGame");
        joinGameService.joinGame(new AuthData("1234", "billy"), gameID, "WHITE");
        assertThrows(AlreadyTakenException.class, () ->
                joinGameService.joinGame(new AuthData("567", "johnny"), gameID, "WHITE"));
    }

    @Test
    public void okRegister() throws Exception {
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        RegisterService registerService = new RegisterService(dataAccess);
        AuthData result = registerService.registerUser(user);
        assertEquals("billy", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void badRegister() throws Exception {
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        RegisterService registerService = new RegisterService(dataAccess);
        registerService.registerUser(user);
        assertThrows(AlreadyTakenException.class, () -> registerService.registerUser(user));
    }




}
