
package vanichat.classes.server;

import vanichat.classes.main.Session;
import vanichat.classes.main.Usuario;
import vanichat.network.server.connections.ChatClientConnection;

public class ServerSession extends Session{

    private final ChatClientConnection connection;
    
    public ServerSession(Integer session_id, Usuario user, ChatClientConnection connection) {
        super(session_id, user);
        this.connection = connection;
    }

    public ChatClientConnection getConnection() {
        return connection;
    }
}