
package server.network;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import server.network.clients.ChatClientConnection;
import server.network.clients.ClientConnections;

public class ChatServer extends Server{

    public static final Charset CHARSET = Charset.forName("UTF-16");
    public static final int SERVER_PORT = 8080;
    
    protected final ClientConnections clientes = new ClientConnections(); 

    public ChatServer() throws IOException {
        super(SERVER_PORT);
    }

    public ClientConnections getClientes() {
        return clientes;
    }
    
    @Override
    public void handlePetition(Socket guest) {
        new Thread(new ChatClientConnection(guest,this)).start();
    }

    @Override
    protected void handleAcceptError(Exception e) {
        System.out.println("Accept Error");
    }

    @Override
    protected void handleServerError(Exception e) {
        System.out.println("Server Error");
    }
    
}
