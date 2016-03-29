
package server.network.clients.handlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import server.classes.user.Usuario;
import server.network.ChatServer;
import server.network.clients.ChatClientConnection;
import server.network.clients.ClientConnections;
import server.network.clients.Session;

class SessionPacketHandler extends PacketHandler {

    public final static byte HANDLED = 2;
    
    protected SessionPacketHandler(DataInputStream in, ChatClientConnection connection) {
        super(in, connection);
        this.handled = HANDLED;
        this.successor = new MessageHandler(in,connection);
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
                String[] users = usrbuffer.toString().trim().split(":");
                LinkedList<Usuario> usuarios = new LinkedList<>();
                LinkedList<ChatClientConnection> cons= new LinkedList<>();
                ClientConnections connections = connection.getListener().getClientes();
                for(int cont=0; cont<users.length; cont+=1){
                    Integer usrid = connections.getUserID(users[cont]);
                    System.out.println(users[cont]);
                    if(usrid != null){
                        Usuario user = connections.getUser(usrid);
                        usuarios.push(user);
                        cons.push(connections.getClientConnection(user));
                    }
                }
                Usuario user = connections.getUser(connection);
                String clientes = String.format("%s,%d", user.getUsername(),user.getServerID());
                Iterator<Usuario> itusr = usuarios.iterator();
                Iterator<ChatClientConnection> itcon = cons.iterator();
                HashMap<Usuario,ChatClientConnection> map = new HashMap<>();
                while(itusr.hasNext()){
                    Usuario usr = itusr.next();
                    ChatClientConnection con = itcon.next();
                    map.put(usr, con);
                    clientes += String.format(":%s,%d", usr.getUsername(),usr.getServerID());
                }
                usuarios.push(user);
                cons.push(connections.getClientConnection(user));
                Session session = new Session(connection.getSessions().size()+1,map);
                connection.addSession(session);
                while(cons.size() > 0){
                    ChatClientConnection con = cons.pop();
                    OutputStream ou = con.getSocket().getOutputStream();
                    synchronized(ou){
                        DataOutputStream out = new DataOutputStream(ou);
                        out.writeByte(HANDLED);
                        out.writeInt(session.getSession_id());
                        byte[] bname = clientes.getBytes(ChatServer.CHARSET);
                        out.writeShort(bname.length);
                        out.write(bname);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Server: Client Login Error");
            }
        }
        
    }
    
}
