
package server;

import java.io.IOException;
import java.net.Socket;

public class ChatListener extends Listener{

    protected final ClientConnections CLIENTES = new ClientConnections(); 

    public ChatListener(Integer port) throws IOException {
        super(port);
    }
    
    @Override
    public void handlePetition(Socket guest) {
        new Thread(new ChatClientConnection(guest,this)).start();
    }

    @Override
    protected void handleAcceptError(Listener listener, Exception e) {
        System.out.println("Accpet Error");
    }

    @Override
    protected void handleServerError(Listener listener, Exception e) {
        System.out.println("Server Error");
    }
    
}
