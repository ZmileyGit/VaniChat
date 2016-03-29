
package server.network.clients.handlers;

import server.classes.user.Usuario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import server.network.ChatServer;
import server.network.clients.ChatClientConnection;

public class LogInPacketHandler extends PacketHandler{

    public final static byte HANDLED = 0;
    
    protected LogInPacketHandler(DataInputStream in, ChatClientConnection connection) {
        super(in, connection);
        this.successor = new UserLinkPacketHandler(in,connection);
        this.handled = HANDLED;
    } 
    
    @Override
    protected void handleBehavior() throws IOException {
        short length = in.readShort();
            
        ByteBuffer ubuffer = ByteBuffer.allocate(length);
        for(int cont = 0;cont < ubuffer.capacity();cont+=1){
            ubuffer.put(in.readByte());
        }
        
        new Thread(new AsyncTask(ubuffer,connection)).start();
    }

    private static class AsyncTask implements Runnable{
        
        private final ByteBuffer buffer;
        private final ChatClientConnection connection;

        public AsyncTask(ByteBuffer buffer, ChatClientConnection connection) {
            buffer.position(0);
            this.buffer = buffer;
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                CharBuffer usrbuffer = ChatServer.CHARSET.newDecoder().decode(buffer);
                String username = usrbuffer.toString().trim();
                Usuario nuevo = connection.getListener().getClientes().addClient(username);
                
                
                int response = nuevo == null ? 0 : nuevo.getServerID();
                Socket cliente = connection.getSocket();
                DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
                System.out.printf(nuevo == null ? "Server: Repeated Username %s%n" : "Server: New Username %s%n",username);
                out.writeInt(response);
                connection.stop();
            } catch (IOException ex) {
                System.out.println("Server: Client Login Error");
            }
        }
        
    }
    
}
