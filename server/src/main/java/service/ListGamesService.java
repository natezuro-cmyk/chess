package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.GameData;

import java.util.List;

public class ListGamesService {
    public DataAccess data;

    public ListGamesService(DataAccess data){
        this.data = data;
    }

    public List<GameData> listGames(AuthData authData) throws DataAccessException {
        if(authData.authToken() == null || data.getAuth(authData.authToken()) == null){
            throw new UnauthorizedException("unauthorized");
        }
        return this.data.listGames();
    }

}
