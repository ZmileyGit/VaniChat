
package client.network;


import client.network.receiver.ChatReceiver;
import client.classes.user.Usuario;
import java.io.DataOutputStream;
import java.io.IOException;

import java.nio.charset.Charset;
import java.util.LinkedList;
import client.network.receiver.Session;

public class ChatClient extends Client{
    
    public static final byte USER_LINK = 10;
    
    public static final String CHARSET_STRING = "UTF-16";
    public static final Charset CHARSET = Charset.forName(CHARSET_STRING);
    public static final String SERVER_IP_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 8080;
    
    private final Usuario user;
    private final ChatReceiver receiver;
    private final LinkedList<Session> sessions = new LinkedList<>();

    public ChatClient(Usuario user) throws IOException {
        super(ChatClient.SERVER_IP_ADDRESS, ChatClient.SERVER_PORT);
        this.user = user;
        this.receiver = new ChatReceiver(this);
    }
    
    @Override
    public void handleConnectionError(IOException e) {
        System.out.println("Error de conexión");
    }

    @Override
    public void handleConnectionToServer() throws IOException {
        
        Thread receive = new Thread(receiver);
        receive.start();
        
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(USER_LINK);
        out.writeInt(user.getServerID());
        
        System.out.println(user.getUsername() + " connected to Server");
        try {receive.join();} catch (InterruptedException ex) {}
    }

    
    
    public Usuario getUser() {
        return user;
    }

    public synchronized LinkedList<Session> getSessions() {
        return sessions;
    }
    
    public synchronized void addSession(Session session){
        if(!sessions.contains(session)){
            sessions.add(session);
        }else{
            System.out.println("Session Already Active" + session);
        }
        System.out.printf("%s%nSesiones de Chat Activas%s%n",user.getUsername(),sessions);
    }

}
