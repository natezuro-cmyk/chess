package websocket;

import chess.*;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import service.*;
import dataaccess.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import java.io.IOException;

public class WebsocketHandler{

    private final ConnectionManager connections = new ConnectionManager();

    private DataAccess dataAccess;
    private MakeMoveService makeMoveService;


    public WebsocketHandler(){
        try {
            dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        makeMoveService = new MakeMoveService(dataAccess);
    }

    public void onConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    public void onMessage(WsMessageContext ctx) {
        try{
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> connect(command, ctx.session);
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(new Gson().fromJson(ctx.message(), MakeMoveCommand.class), ctx.session);
                case UserGameCommand.CommandType.LEAVE -> leave(command, ctx.session);
                case UserGameCommand.CommandType.RESIGN -> resign(command, ctx.session);
            }
        } catch (IOException | DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void onClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connect(UserGameCommand userGameCommand, Session session) throws DataAccessException, IOException {
        //checks the authtoken to make sure the player is logged in
        if(dataAccess.getAuth(userGameCommand.getAuthToken())== null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: person either doesn't exist or isn't logged in.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        if(dataAccess.getGame(userGameCommand.getGameID()) == null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: that gameID does not exist.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        //add a new session to our map
        connections.add(userGameCommand.getGameID(), session);
        GameData game = dataAccess.getGame(userGameCommand.getGameID());
        String username = dataAccess.getAuth(userGameCommand.getAuthToken()).username();
        String color = "an observer.";
        if(username.equals(game.whiteUsername())) color = "white";
        if(username.equals(game.blackUsername())) color = "black";

        //send a message to all player about who has joined
        ServerMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username +" has joined the game as " + color);
        connections.broadcast(userGameCommand.getGameID(), session, notificationMessage);

        //if you connect it needs to load the game for you
        ServerMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, dataAccess.getAuth(userGameCommand.getAuthToken()));
        session.getRemote().sendString(new Gson().toJson(loadGame));
    }

    public void makeMove(MakeMoveCommand userGameCommand, Session session) throws DataAccessException, IOException {
        //uses the makemove service to make a move
        //sends a load_game to the clients connected
        //sends a notification to all users about who made a move and to where
        GameData game = dataAccess.getGame(userGameCommand.getGameID());

        if(game.game().getTeamTurn() == null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: this game is already over.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        AuthData authData = dataAccess.getAuth(userGameCommand.getAuthToken());
        ChessPiece piece = game.game().getBoard().getPiece(userGameCommand.getMove().getStartPosition());

        if(authData == null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: you are not logged in.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        if(!authData.username().equals(game.whiteUsername()) && !authData.username().equals(game.blackUsername())){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: you cannot make moves as an observer.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        ChessGame.TeamColor playerColor = null;
        if(game.whiteUsername().equals(authData.username())){playerColor = ChessGame.TeamColor.WHITE;}
        else if(game.blackUsername().equals(authData.username())){playerColor = ChessGame.TeamColor.BLACK;}

        if(playerColor != game.game().getTeamTurn()){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: it is " + game.game().getTeamTurn() + "'s turn.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        if(userGameCommand.getMove() == null || userGameCommand.getMove().getStartPosition() == null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        try{
            MakeMoveService move = new MakeMoveService(dataAccess);
            move.makeMove(userGameCommand.getMove(), game);
            GameData newGame = dataAccess.getGame(userGameCommand.getGameID());

            String username = authData.username();
            ChessPosition endPosition = userGameCommand.getMove().getEndPosition();
            ChessPosition startPosition = userGameCommand.getMove().getStartPosition();

            ServerMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    username +" moved their " + piece + " from " + startPosition +" to " + endPosition);
            connections.broadcast(userGameCommand.getGameID(), session, notificationMessage);

            ServerMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, newGame, dataAccess.getAuth(userGameCommand.getAuthToken()));
            connections.broadcast(userGameCommand.getGameID(), null, loadGame);

            //Check if a color is in check, checkmate or stalemate
            ChessGame.TeamColor color = newGame.game().getTeamTurn();
            if(newGame.game().isInCheckmate(color)){
                ServerMessage checkMate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        color + " was mated, game over");
                connections.broadcast(userGameCommand.getGameID(), null, checkMate);
                newGame.game().setTeamTurn(null);
                dataAccess.updateGame(newGame);
            }
            else if(newGame.game().isInCheck(color)){
                ServerMessage check = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        color + " is in check.");
                connections.broadcast(userGameCommand.getGameID(), null, check);

            }
            else if(newGame.game().isInStalemate(color)){
                ServerMessage staleMate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        "CONGRATULATIONS, STALEMATE. It's a draw.");
                connections.broadcast(userGameCommand.getGameID(), null, staleMate);
                newGame.game().setTeamTurn(null);
                dataAccess.updateGame(newGame);
            }

        } catch (InvalidMoveException e){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
    }

    public void leave(UserGameCommand userGameCommand, Session session) throws DataAccessException, IOException {
        //removes the person from the hashmap
        if(dataAccess.getAuth(userGameCommand.getAuthToken())== null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: you are not logged in.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        connections.remove(userGameCommand.getGameID(), session);
        String username = dataAccess.getAuth(userGameCommand.getAuthToken()).username();

        // Clear the leaving player's slot in the database so the spot can be rejoined
        GameData game = dataAccess.getGame(userGameCommand.getGameID());
        if (game != null) {
            if (username.equals(game.whiteUsername())) {
                dataAccess.updateGame(new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game()));
            } else if (username.equals(game.blackUsername())) {
                dataAccess.updateGame(new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game()));
            }
        }

        ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the game.");
        connections.broadcast(userGameCommand.getGameID(), session, notification);

    }

    public void resign(UserGameCommand userGameCommand, Session session) throws DataAccessException, IOException {
        //should set the turn to null to mark that the game is over
        //should send a notification to all players saying someone has resigned.
        AuthData authData = dataAccess.getAuth(userGameCommand.getAuthToken());
        GameData gamedata = dataAccess.getGame(userGameCommand.getGameID());
        ChessGame.TeamColor turn = gamedata.game().getTeamTurn();

        if(authData == null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: you are not logged in.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        if(turn == null){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: this game is already over.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        if(!authData.username().equals(gamedata.whiteUsername()) && !authData.username().equals(gamedata.blackUsername())){
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: observers cannot resign.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        ServerMessage resignMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, authData.username() + " has resigned.");
        System.out.println("After setting - turn is: " + gamedata.game().getTeamTurn());
        gamedata.game().setTeamTurn(null);
        System.out.println("After setting - turn is: " + gamedata.game().getTeamTurn());
        dataAccess.updateGame(gamedata);
        connections.broadcast(userGameCommand.getGameID(), null, resignMessage);
        GameData verifyGame = dataAccess.getGame(userGameCommand.getGameID());
        System.out.println("After database update - turn is: " + verifyGame.game().getTeamTurn());
    }


}


