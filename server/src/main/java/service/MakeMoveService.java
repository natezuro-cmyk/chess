package websocket.service;

import chess.ChessMove;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

public class MakeMoveService {
    private DataAccess data;

    public MakeMoveService(DataAccess data){
        this.data = data;
    }

    public void makeMove(ChessMove move, GameData gamedata) throws DataAccessException {
        GameData game = data.getGame(gamedata.gameID());
        makeMove(move, game);
        data.updateGame(game);
    }
}
