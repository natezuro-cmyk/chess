package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    DataAccess dataAccess = new MemoryDataAccess();

    @Test
    public void okClear() throws Exception {
        ClearService clearService = new ClearService(dataAccess);
        dataAccess.createUser(new UserData("billy", "1234", "billy@gmail.com"));
        dataAccess.createGame("mygame");

        clearService.clear();

        assertNull(dataAccess.getUser("billy"));
        assertNull(dataAccess.getGame(1));
    }

    @Test
    public void okLogin() throws Exception {
        LoginService loginService = new LoginService(dataAccess);
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        dataAccess.createUser(user);

        loginService.login(user);

        assertTrue(dataAccess.getUser("billy") == user);
    }

    @Test
    public void badLogin() throws Exception {
        LoginService loginService = new LoginService(dataAccess);
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        dataAccess.createUser(user);
        UserData secondUser = new UserData("johnny", "12", "johnny@gmail.com");

        loginService.login(user);

        assertThrows(UnauthorizedException.class, () -> loginService.login(secondUser));
    }

    @Test
    public void okLogout() throws Exception {
        RegisterService registerService = new RegisterService(dataAccess);
        LoginService loginService = new LoginService(dataAccess);
        LogoutService logoutService = new LogoutService(dataAccess);
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        AuthData data = registerService.registerUser(user);

        loginService.login(user);
        logoutService.logout(data);

        assertTrue(dataAccess.getAuth(data.authToken()) == null);
    }

    //user tries to logout before logged in
    @Test
    public void badLogout() throws Exception {
        RegisterService registerService = new RegisterService(dataAccess);
        LoginService loginService = new LoginService(dataAccess);
        LogoutService logoutService = new LogoutService(dataAccess);
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        AuthData data = registerService.registerUser(user);

        logoutService.logout(data);

        assertThrows(UnauthorizedException.class, () -> logoutService.logout(data));
    }

    @Test
    public void okListGames() throws Exception {
        ListGamesService listGamesService = new ListGamesService(dataAccess);
        RegisterService registerService = new RegisterService(dataAccess);
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        AuthData data = registerService.registerUser(user);
        dataAccess.createGame("1");
        dataAccess.createGame("2");
        dataAccess.createGame("3");

        int size = listGamesService.listGames(data).size();

        assertTrue(size == 3);
    }


    @Test
    public void badListGames() throws Exception {
        ListGamesService listGamesService = new ListGamesService(dataAccess);
        AuthData data = new AuthData("1234","billy");
        dataAccess.createGame("1");
        dataAccess.createGame("2");
        dataAccess.createGame("3");

        assertThrows(UnauthorizedException.class, () -> listGamesService.listGames(data));
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
