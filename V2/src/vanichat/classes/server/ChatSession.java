
package vanichat.classes.server;

import vanichat.classes.main.Session;
import vanichat.classes.main.Usuario;
import vanichat.network.server.connections.ChatGroupConnection;

public class ChatSession extends Session{
    
    private final ChatGroupConnection connection;
    
    public ChatSession(Integer session_id, Usuario user, ChatGroupConnection connection) {
        super(session_id, user);
        this.connection = connection;
    }

    public ChatGroupConnection getConnection() {
        return connection;
    }
    
}
