package service;

import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public class MakeMoveService {
    private DataAccess data;

    public MakeMoveService(DataAccess data){
        this.data = data;
    }

    public void makeMove(ChessMove move, GameData gamedata) throws DataAccessException, InvalidMoveException {
        Collection<ChessMove> moves = gamedata.game().validMoves(move.getStartPosition());
        if(!moves.contains(move)){
            throw new InvalidMoveException("Invalid move");
        }

        GameData game = data.getGame(gamedata.gameID());
        game.game().makeMove(move);
        data.updateGame(game);
    }
}
