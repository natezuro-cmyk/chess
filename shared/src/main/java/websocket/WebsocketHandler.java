package websocket;

import websocket.commands.UserGameCommand;

import java.sql.Connection;

public class WebsocketHandler {


    public WebsocketHandler(UserGameCommand command){
        if(command.getCommandType() == UserGameCommand.CommandType.CONNECT){
            connectHandler();
        }
        if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            makemoveHandler();
        }
        if(command.getCommandType() == UserGameCommand.CommandType.LEAVE){
            leaveHandler();
        }
        if(command.getCommandType() == UserGameCommand.CommandType.RESIGN){
            resignHandler();
        }

    }


    public connectHandler(){
        JoinGameService jgs = new JoinGameService()
    }

    public makemoveHandler(){

    }

    public leaveHanlder(){

    }

    public resignHanlder(){

    }


}
