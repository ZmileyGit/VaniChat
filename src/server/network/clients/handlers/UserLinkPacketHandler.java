
package server.network.clients.handlers;

import java.io.DataInputStream;
import java.io.IOException;
import server.classes.user.Usuario;
import server.network.clients.ChatClientConnection;
import server.network.clients.ClientConnections;

class UserLinkPacketHandler extends PacketHandler {

    public final static byte HANDLED = 10;
    
    protected UserLinkPacketHandler(DataInputStream in, ChatClientConnection connection) {
        super(in, connection);
        this.handled = HANDLED;
        this.successor = new SessionPacketHandler(in,connection);
    }

    @Override
    protected void handleBehavior() throws IOException {
        int user_id = in.readInt();
        
        new Thread(new AsyncTask(user_id,connection)).start();
    }

    private static class AsyncTask implements Runnable{
        
        private final int user_id;
        private final ChatClientConnection connection;

        public AsyncTask(int user_id, ChatClientConnection connection) {
            this.user_id = user_id;
            this.connection = connection;
        }

        @Override
        public void run() {
            ClientConnections clientes = connection.getListener().getClientes();
            Usuario user = clientes.getUser(user_id);
            clientes.setClientConnection(user, connection);
        }
        
    }
    
}
