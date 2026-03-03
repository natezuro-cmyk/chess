import dataaccess.BadRequestException;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    public void okClear() throws Exception {
        DataAccess dataAccess = new MemoryDataAccess();
        ClearService clearService = new ClearService(dataAccess);
        dataAccess.createUser(new UserData("billy", "1234", "billy@gmail.com"));
        dataAccess.createGame("mygame");

        clearService.clear();

        assertNull(dataAccess.getUser("billy"));
        assertNull(dataAccess.getGame(1));
    }

    @Test
    public void OKlogin() throws Exception {
        DataAccess dataAccess = new MemoryDataAccess();
        LoginService loginService = new LoginService(dataAccess);
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        dataAccess.createUser(user);

        loginService.login(user);

        assertTrue(dataAccess.getUser("billy") == user);
    }

    @Test
    public void badLogin() throws Exception {
        DataAccess dataAccess = new MemoryDataAccess();
        LoginService loginService = new LoginService(dataAccess);
        UserData user = new UserData("billy", "1234", "billy@gmail.com");
        dataAccess.createUser(user);
        UserData secondUser = new UserData("johnny", "12", "johnny@gmail.com");

        loginService.login(user);

        assertThrows(UnauthorizedException.class, () -> loginService.login(secondUser));
    }

    @Test
    public void OKlogout() throws Exception {
        DataAccess dataAccess = new MemoryDataAccess();
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
        DataAccess dataAccess = new MemoryDataAccess();
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
        DataAccess dataAccess = new MemoryDataAccess();
        ListGamesService listGamesService = new ListGamesService(dataAccess);
        dataAccess.createGame("1");
        dataAccess.createGame("2");
        dataAccess.createGame("3");

        list

        assertThrows(UnauthorizedException.class, () -> logoutService.logout(data));
    }




}
