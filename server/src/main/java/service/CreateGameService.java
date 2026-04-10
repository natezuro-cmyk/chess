package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.GameData;

import java.util.List;

public class CreateGameService {
    public DataAccess data;
    private int gameID = 1;

    public CreateGameService(DataAccess data){
        this.data = data;
    }

    public int createGame(AuthData authData, String gameName) throws DataAccessException {
        if(gameName == null){
            throw new BadRequestException("bad request");
        }
        if(authData.authToken() == null || data.getAuth(authData.authToken()) == null){
            throw new UnauthorizedException("unauthorized");
        }
        List<GameData> games = data.listGames();
        for (GameData game : games) {
            if (game.gameName().equals(gameName)) {
                throw new BadRequestException("a game with that name already exists");
            }
        }
        int gameID = data.createGame(gameName);
        return gameID;
    }

}
