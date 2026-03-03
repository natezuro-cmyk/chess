package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    public DataAccess data;

    public JoinGameService(DataAccess data){
        this.data = data;
    }

    public void joinGame(AuthData authData, int gameID, String color) throws DataAccessException {
        if(data.getGame(gameID)== null || color == null){
            throw new BadRequestException("bad request");
        }
        GameData game = data.getGame(gameID);
        if(authData.authToken() == null || data.getAuth(authData.authToken()) == null){
            throw new UnauthorizedException("unauthorized");
        }
        if(game.blackUsername() != null && color.equals("BLACK")){
            throw new AlreadyTakenException("already taken");
        }
        if(game.whiteUsername() != null && color.equals("WHITE")){
            throw new AlreadyTakenException("already taken");
        }

        if(color.equals("BLACK")){
            game = new GameData(game.gameID(), game.whiteUsername(), data.getAuth(authData.authToken()).username(), game.gameName(), game.game());
        }
        if(color.equals("WHITE"))
            game = new GameData(game.gameID(), data.getAuth(authData.authToken()).username(), game.blackUsername(), game.gameName(), game.game());
        if(!color.equals("WHITE") && !color.equals("BLACK")){
            throw new BadRequestException("bad request");
        }

        data.updateGame(game);


    }

}
