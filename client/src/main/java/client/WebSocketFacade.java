package client;

import com.google.gson.Gson;
import exception.ResponseException;

import jakarta.websocket.*;
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

    public WebSocketFacade(String url) throws ResponseException {
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
                        case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame(new Gson().fromJson(message, LoadGameMessage.class));
                        case ServerMessage.ServerMessageType.ERROR -> error(new Gson().fromJson(message, ErrorMessage.class));
                        case ServerMessage.ServerMessageType.NOTIFICATION -> notification(new Gson().fromJson(message, NotificationMessage.class));
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void loadGame(LoadGameMessage message) {
    //needs to know who is calling it, white player, black player or observer
        //draws either white or black board depending on whos playing
        GameData gamedata = message.getGame();

    }

    public void error(ErrorMessage message) {

    }

    public void notification(NotificationMessage message) {

    }

}