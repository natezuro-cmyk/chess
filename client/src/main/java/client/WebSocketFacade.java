package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;


import jakarta.websocket.*;
import model.AuthData;
import model.GameData;

import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessage serverMessage;
    private ServerFacade facade;
    private GameData gameData;
    private AuthData authData;
    private int gameID;

    public WebSocketFacade(String url){
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage messageType = new Gson().fromJson(message, ServerMessage.class);
                    switch (messageType.getServerMessageType()) {
                        case ServerMessage.ServerMessageType.LOAD_GAME:
                                gameData = new Gson().fromJson(message, LoadGameMessage.class).getGame();
                                gameID = gameData.gameID();
                            loadGame(new Gson().fromJson(message, LoadGameMessage.class), new Gson().fromJson(message, LoadGameMessage.class).getUserData());
                            break;

                        case ServerMessage.ServerMessageType.ERROR: error(new Gson().fromJson(message, ErrorMessage.class));
                        break;

                        case ServerMessage.ServerMessageType.NOTIFICATION: notification(new Gson().fromJson(message, NotificationMessage.class));
                        break;
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println("WebSocket error: " + ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void loadGame(LoadGameMessage message, AuthData data) {
    //needs to know who is calling it, white player, black player or observer
        //draws either white or black board depending on who's playing
        GameData gameData = message.getGame();
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        this.authData = data;

        if(authData.username().equals(whiteUsername)) {
            DrawBoard.drawBoard(gameData.game().getBoard(), ChessGame.TeamColor.WHITE);
        }

        else if(authData.username().equals(blackUsername)){
            DrawBoard.drawBoard(gameData.game().getBoard(), ChessGame.TeamColor.BLACK);
        }

        else{
            DrawBoard.drawBoard(gameData.game().getBoard(), ChessGame.TeamColor.WHITE);
        }

    }

    public void error(ErrorMessage message) {
        System.out.println(message.getMessage());
    }

    public void notification(NotificationMessage message) {
        System.out.println(message.getMessage());
    }

    public void makeMove(ChessMove move, String authToken) throws IOException {
        UserGameCommand makeMove = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameData.gameID(), move);
        session.getBasicRemote().sendText(new Gson().toJson(makeMove));
    }

    public void leave(String authToken) throws IOException {
        UserGameCommand leaveGame = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameData.gameID());
        session.getBasicRemote().sendText(new Gson().toJson(leaveGame));
    }

    public void resign(String authToken) throws IOException {
        UserGameCommand resign = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameData.gameID());
        session.getBasicRemote().sendText(new Gson().toJson(resign));
    }

    public void redraw(String authToken) throws IOException {
        loadGame(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData, authData), authData);
    }


}