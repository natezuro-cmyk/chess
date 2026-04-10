package websocket.messages;

import model.AuthData;
import model.GameData;

public class LoadGameMessage extends ServerMessage{
    GameData game;
    AuthData userData;

    public LoadGameMessage(ServerMessageType type, GameData data, AuthData userData) {
        super(type);
        this.game = data;
        this.userData = userData;
    }

    public GameData getGame(){
        return this.game;
    }

    public AuthData getUserData(){
        return this.userData;
    }



}
