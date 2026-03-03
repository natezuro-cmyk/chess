package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {

    public DataAccess data;
    public UserData user;

    public RegisterService(DataAccess data){
        this.data = data;
    }

    public AuthData registerUser(UserData user) throws DataAccessException {
       if (user.username() == null || user.email() == null || user.password() == null){
           throw new BadRequestException("The data you entered appeared as null");
       }
       if (data.getUser(user.username()) != null){
           throw new AlreadyTakenException("That username is already taken");
       }
       else{
           data.createUser(user);
           AuthData newAuth = new AuthData(UUID.randomUUID().toString(), user.username());
           data.createAuth(newAuth);
           return newAuth;
       }
    }

}
