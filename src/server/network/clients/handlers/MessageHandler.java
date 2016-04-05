
package server.network.clients.handlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.classes.user.Usuario;
import server.network.ChatServer;
import server.network.clients.ChatClientConnection;
import server.network.clients.Session;

class MessageHandler extends PacketHandler {

    public final static byte HANDLED = 1;
    
    protected MessageHandler(DataInputStream in, ChatClientConnection connection) {
        super(in, connection);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior() throws IOException {
        int session_id = in.readInt();
        short length = (short) in.readShort();
        
        ByteBuffer ubuffer = ByteBuffer.allocate(length);
        for(int cont = 0;cont < ubuffer.capacity();cont+=1){
            ubuffer.put(in.readByte());
        }
        
        new AsyncTask(ubuffer,session_id,connection).run();
    }

    private static class AsyncTask implements Runnable{
        
        private final ByteBuffer buffer;
        private final ChatClientConnection connection;
        private final int session_id;

        public AsyncTask(ByteBuffer buffer, int session_id, ChatClientConnection connection) {
            buffer.position(0);
            this.buffer = buffer;
            this.session_id = session_id;
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                CharBuffer usrbuffer = ChatServer.CHARSET.newDecoder().decode(buffer);
                String message = usrbuffer.toString().trim();
                if(connection.getSession(session_id) != null){
                    Session s = connection.getSession(session_id);
                    System.out.println(s);
                    Usuario user = connection.getListener().getClientes().getUser(connection);
                    HashMap<Usuario, ChatClientConnection> connections = s.getConnections();
                    synchronized(connections){
                        Set<Usuario> usuarios = connections.keySet();
                        usuarios.stream().filter(
                            (Usuario u)->{
                                return u.getServerID() != user.getServerID();
                            }
                        ).forEach(
                            (Usuario usr)->{
                                Socket socket = connections.get(usr).getSocket();
                                try {
                                    DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
                                    out.writeByte(HANDLED);
                                    out.writeInt(user.getServerID());
                                    out.writeInt(session_id);
                                    out.write(message.getBytes(ChatServer.CHARSET));
                                } catch (IOException ex) {
                                    System.out.println("Error Sending Error");
                                }
                            }
                        );
                    }
                }else{
                    System.out.printf("Mensaje Descartado -> %s%n", message);
                }
            } catch (IOException ex) {
                System.out.println("Server: Client Login Error");
            }
        }
        
    }
}
