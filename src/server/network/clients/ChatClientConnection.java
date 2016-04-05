
package server.network.clients;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import server.network.clients.handlers.PacketHandlers;
import server.network.ChatServer;
import server.network.Stoppable;

public class ChatClientConnection extends ClientConnection implements Stoppable{
    
    private final ChatServer server;
    protected final AtomicBoolean run = new AtomicBoolean(true);
    private final LinkedList<Session> sessions = new LinkedList<>();

    public ChatClientConnection(Socket socket, ChatServer listener)  {
        super(socket);
        this.server = listener;
    }

    public ChatServer getListener() {
        return server;
    }
    
    @Override
    public void handleClientPackets() throws IOException{
        DataInputStream in = new DataInputStream(socket.getInputStream());
        while(run.get()){
            if(in.available()!=0){
                new PacketHandlers().handlePacket(in,this);
                System.out.println(sessions);
            }else{
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {}
            }
        }
    }
    
    @Override
    public void handleClientError(Exception e) {
        System.out.println("Client Error");
    }
    
    @Override
    public void stop(){
        run.set(false);
    }
    
    public synchronized void addSession(Session session){
        sessions.add(session);
    }

    public synchronized LinkedList<Session> getSessions() {
        return sessions;
    }
    
    public synchronized Session getSession(Integer session_id){
        System.out.println("Tamanio " + sessions.size());
        Optional<Session> session = sessions.stream().filter(
                (Session s)->{
                    return s.getSession_id() == session_id;
                }
        ).findFirst();
        return session.orElse(null);
    }
    
}
