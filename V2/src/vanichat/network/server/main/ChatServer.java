
package vanichat.network.server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import vanichat.classes.server.ServerChats;
import vanichat.classes.server.ServerSessions;
import vanichat.network.server.connections.ChatClientConnection;

public class ChatServer extends Server{

    private final ServerSessions sessions = new ServerSessions();
    private final ServerChats chats = new ServerChats();
    
    private ChatServer(ServerSocket socket) throws IOException {
        super(socket);
    }
    
    public static ChatServer newInstance(int server_port) throws IOException{
        return new ChatServer(new ServerSocket(server_port));
    }
    
    public ServerSessions getSessions(){
        return sessions;
    }

    public ServerChats getChats() {
        return chats;
    }
    
    @Override
    public void handlePetition(Socket guest) throws IOException {
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
