package vanichat.network.server.connections.handlers;

import vanichat.classes.main.Handler;
import vanichat.network.server.connections.ChatClientConnection;

public abstract class ConnectionHandler extends Handler{
    
    protected final ChatClientConnection connection;

    public ConnectionHandler(Handler succesor, ChatClientConnection connection) {
        super(succesor);
        this.connection = connection;
    }

    public ChatClientConnection getConnection() {
        return connection;
    }
    
}
