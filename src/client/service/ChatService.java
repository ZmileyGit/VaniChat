
package client.service;

import client.classes.user.Usuario;
import client.network.ChatClient;
import client.network.SessionSender;
import client.service.handlers.ServiceHandlers;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class ChatService extends Service{

    public static final int DEFAULT_PORT = 1234;
    public static final boolean DEBUG = true;
    private ChatClient chat = null;
    private final LinkedList<SessionSender> sesiones = new LinkedList<>();

    public ChatService(Integer port) throws IOException {
        super(port);
    }    
    
    @Override
    protected void handlePetition(Socket guest) throws IOException {
        new ServiceHandlers().handlePacket(this, guest);
    }

    @Override
    protected void handleAcceptError(Exception e) {
        System.out.println("Vanichat: Accept error");
        e.printStackTrace();
    }

    @Override
    protected void handleServerError(Exception e) {
        throw new UnsupportedOperationException("VaniChat: Server Error");
    }

    public synchronized void startChat(String username,int serverID) {
        if(chat == null){
            try {
                System.out.printf("Signed in as %s with ID: %d%n",username,serverID);
                this.chat = new ChatClient(new Usuario(username,serverID));
                this.chat.start();
            } catch (IOException ex) {
                System.out.println("VaniChat: Chat Error");
            }
        }else{
            System.out.printf("VaniChat: Active User %s%n",chat.getUser().getUsername());
        }
    }

    public ChatClient getChat() {
        return chat;
    }
    
}
