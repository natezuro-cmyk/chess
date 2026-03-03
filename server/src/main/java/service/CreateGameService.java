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
        int gameID = data.createGame(gameName);
        return gameID;
    }

}
