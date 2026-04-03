package websocket.service;

import chess.ChessMove;
import dataaccess.DataAccess;
import model.GameData;

public class ResignService {
    private DataAccess data;

    public ResignService(DataAccess data){
        this.data = data;
    }

    public void resign(ChessMove move, GameData gamedata){
        GameData game = data.getGame(gamedata.gameID());
        makeMove(move, game);
        data.updateGame(game);
    }

}
