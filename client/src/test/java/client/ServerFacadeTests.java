package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() throws Exception {
        facade.clear();
    }

    //clear

    @Test
    void clearPositiveTest() throws Exception {
        facade.register("billy", "password", "billy@email.com");
        boolean exceptionThrown = false;
        try {
            facade.clear();
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    void clearNegativeTest() throws Exception {
        boolean exceptionThrown = false;
        try {
            facade.clear();
            facade.clear();
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    //register

    @Test
    void registerPositiveTest() throws Exception {
        AuthData authData = facade.register("billy", "password", "billy@email.com");
        assertNotNull(authData.authToken());
    }

    @Test
    void registerNegativeTest() throws Exception {
        boolean exceptionThrown = false;
        try {
            facade.register("billy", "password", "billy@email.com");
            facade.register("billy", "password", "billy@email.com");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    //login

    @Test
    void loginPositiveTest() throws Exception {
        facade.register("billy", "password", "billy@email.com");
        AuthData authData = facade.login("billy", "password");
        assertNotNull(authData.authToken());
    }

    @Test
    void loginNegativeTest() throws Exception {
        boolean exceptionThrown = false;
        try {
            facade.register("billy", "password", "billy@email.com");
            facade.login("billy", "wrongpassword");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    //logout

    @Test
    void logoutPositiveTest() throws Exception {
        AuthData authData = facade.register("billy", "password", "billy@email.com");
        boolean exceptionThrown = false;
        try {
            facade.logout(authData.authToken());
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    void logoutNegativeTest() throws Exception {
        boolean exceptionThrown = false;
        try {
            facade.logout("faketoken");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    //listgames

    @Test
    void listGamesPositiveTest() throws Exception {
        AuthData authData = facade.register("billy", "password", "billy@email.com");
        List<GameData> games = facade.listGames(authData.authToken());
        assertNotNull(games);
    }

    @Test
    void listGamesNegativeTest() throws Exception {
        boolean exceptionThrown = false;
        try {
            facade.listGames("faketoken");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    //creategame

    @Test
    void createGamePositiveTest() throws Exception {
        AuthData authData = facade.register("billy", "password", "billy@email.com");
        int gameID = facade.createGame(authData.authToken(), "mygame");
        assertTrue(gameID > 0);
    }

    @Test
    void createGameNegativeTest() throws Exception {
        boolean exceptionThrown = false;
        try {
            facade.createGame("faketoken", "mygame");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    //joingame

    @Test
    void joinGamePositiveTest() throws Exception {
        AuthData authData = facade.register("billy", "password", "billy@email.com");
        int gameID = facade.createGame(authData.authToken(), "mygame");
        boolean exceptionThrown = false;
        try {
            facade.joinGame(authData.authToken(), gameID, "WHITE");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    void joinGameNegativeTest() throws Exception {
        boolean exceptionThrown = false;
        try {
            facade.joinGame("faketoken", 9999, "WHITE");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

}
