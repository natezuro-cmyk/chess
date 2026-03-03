package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class LoginService {
    public DataAccess data;

    public LoginService(DataAccess data){
        this.data = data;
    }

    public AuthData login(UserData user) throws DataAccessException {
       if(user.username() ==null || user.password() == null){
           throw new BadRequestException("bad request");
       }
       if(data.getUser(user.username()) == null || !data.getUser(user.username()).password().equals(user.password())){
           throw new UnauthorizedException("unauthorized");
       }
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), user.username());
        data.createAuth(newAuth);
        return newAuth;
    }

}
