
package client.network;

import client.service.handlers.SessionServiceHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SessionSender extends Sender{

    public static final byte MODE = 2;
    
    private final SessionServiceHandler handler;
    private final String users;
    
    public SessionSender(Socket socket, String users,SessionServiceHandler handler) throws IOException {
        super(socket);
        this.handler = handler;
        this.users = users;
    }

    public SessionServiceHandler getHandler() {
        return handler;
    }
    
    @Override
    public void handleConnectionError(IOException e) {
        System.out.println("VaniChat: Session Request Error");
    }

    @Override
    public void handleConnectionToServer() throws IOException {
        OutputStream ou = socket.getOutputStream();
        synchronized(ou){
            DataOutputStream out = new DataOutputStream(ou);
            out.writeByte(MODE);
            byte[] bname = users.getBytes(ChatClient.CHARSET);
            out.writeShort(bname.length);
            out.write(bname);
        }
    }
    
}
