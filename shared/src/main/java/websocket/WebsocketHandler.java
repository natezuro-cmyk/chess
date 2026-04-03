package websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;


public class WebsocketHandler{

    public void onMessage(WsMessageContext ctx){
        UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        switch(command.getCommandType()){
            case UserGameCommand.CommandType.CONNECT -> connectHandler();
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMoveHandler();
            case UserGameCommand.CommandType.LEAVE -> leaveHanlder();
            case UserGameCommand.CommandType.RESIGN -> resignHandlder();
        }


    public connectHandler(){

        }

    public makemoveHandler(){
            MakeMoveService makeMoveService = new MakeMoveService()
        }

    public leaveHanlder(){

        }

    public resignHanlder(){

        }


    }
}


