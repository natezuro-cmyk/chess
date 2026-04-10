package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        //case1: no one in the game yet
        if (connections.get(gameID) == null) {
            List<Session> list = new ArrayList<>();
            list.add(session);
            connections.put(gameID, list);
        }
        //case2: has people in the game already
        else {
            List<Session> list = connections.get(gameID);
            list.add(session);
            connections.put(gameID, list);
        }
    }

    public void remove(int gameID, Session session) {
        List<Session> newSessions = connections.get(gameID);
        newSessions.remove(session);
        connections.put(gameID, newSessions);
    }

    public void broadcast(int gameID, Session session, ServerMessage message) throws IOException {
        List<Session> sessions = connections.get(gameID);
        if (sessions == null) {
            return;
        }
        List<Session> deadSessions = new ArrayList<>();
        for (Session player : sessions) {
            if (session == null || !session.equals(player)) {
                if (!player.isOpen()) {
                    deadSessions.add(player);
                    continue;
                }
                try {
                    player.getRemote().sendString(new Gson().toJson(message));
                } catch (IOException e) {
                    deadSessions.add(player);
                }
            }
        }
        sessions.removeAll(deadSessions);
    }
}
