package server;

import dataaccess.*;
import io.javalin.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.*;

import java.util.List;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    private DataAccess dataAccess = new DataAccess();
    private ClearService clearService = new ClearService(dataAccess);
    private RegisterService registerService = new RegisterService(dataAccess);
    private LoginService loginService = new LoginService(dataAccess);
    private LogoutService logoutService = new LogoutService(dataAccess);
    private ListGamesService listGamesService = new ListGamesService(dataAccess);
    private CreateGameService createGameService = new CreateGameService(dataAccess);
    private JoinGameService joinGameService = new JoinGameService(dataAccess);

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new io.javalin.json.JavalinGson());
        });
        clearHandler();
        registerHandler();
        loginHandler();
        logoutHandler();
        listGamesHandler();
        createGameHandler();
        joinGameHandler();
        // Register your endpoints and exception handlers here.
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void clearHandler(){
        javalin.delete("/db", ctx -> {
            try {
                clearService.clear();
                ctx.status(200);
                ctx.json(Map.of());
            }
            catch(Exception e) {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        });
    }

    public void registerHandler(){
        javalin.post("/user", ctx -> {
            try {
                AuthData data = registerService.registerUser(ctx.bodyAsClass(UserData.class));
                ctx.status(200);
                ctx.json(Map.of("username", data.username(), "authToken", data.authToken()));
            }
            catch(AlreadyTakenException e){
                ctx.status(403);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
            catch(BadRequestException e){
                ctx.status(400);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
            catch(Exception e) {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        });
    }

    public void loginHandler(){
        javalin.post("/session", ctx -> {
            try {
                AuthData data = loginService.login(ctx.bodyAsClass(UserData.class));
                ctx.status(200);
                ctx.json(Map.of("username", data.username(), "authToken", data.authToken()));
            }
            catch(BadRequestException e){
                ctx.status(400);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
            catch(UnauthorizedException e){
                ctx.status(401);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
            catch(Exception e) {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        });
    }

    public void logoutHandler() {
        javalin.delete("/session", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                AuthData authData = new AuthData(authToken, null);
                logoutService.logout(authData);
                ctx.status(200);
                ctx.json("{}");
            } catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            } catch (Exception e) {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        });
    }

    public void listGamesHandler() {
        javalin.get("/game", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                AuthData authData = new AuthData(authToken, null);
                List<GameData> games = listGamesService.listGames(authData);
                ctx.status(200);
                ctx.json(Map.of("games", games));
            } catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            } catch (Exception e) {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        });
    }

    public void createGameHandler() {
        javalin.post("/game", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                AuthData authData = new AuthData(authToken, null);
                var jsonBody = ctx.bodyAsClass(Map.class);
                String gameName = (String) jsonBody.get("gameName");
                int gameID = createGameService.createGame(authData, gameName);
                ctx.status(200);
                ctx.json(Map.of("gameID", gameID));
            }catch (BadRequestException e){
                ctx.status(400);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            } catch (Exception e) {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        });
    }

    public void joinGameHandler() {
        javalin.put("/game", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                AuthData authData = new AuthData(authToken, null);
                var jsonBody = ctx.bodyAsClass(Map.class);
                if(jsonBody.get("gameID") == null){
                    ctx.status(400);
                    ctx.json(Map.of("message", "Error: bad request"));
                    return;
                }
                int gameID = ((Double) jsonBody.get("gameID")).intValue();
                String color = (String)jsonBody.get("playerColor");
                joinGameService.joinGame(authData, gameID, color);
                ctx.status(200);
                ctx.json("{}");
            }catch (BadRequestException e){
                ctx.status(400);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }catch (AlreadyTakenException e){
                ctx.status(403);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            } catch (Exception e) {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        });
    }


}