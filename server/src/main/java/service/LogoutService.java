package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class LogoutService {
    public DataAccess data;

    public LogoutService(DataAccess data){
        this.data = data;
    }

    public void logout(AuthData authData) throws DataAccessException {
        if(authData.authToken() == null || data.getAuth(authData.authToken()) == null){
            throw new UnauthorizedException("unauthorized");
        }
        data.deleteAuth(authData.authToken());
    }

}
