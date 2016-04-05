
package client.network;

import client.network.receiver.Session;
import client.service.handlers.MessageServiceHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MessageSender extends Sender{

    public static final byte MODE = 1;
    
    private final MessageServiceHandler handler;
    private final String message;
    private final Session session;
    
    public MessageSender(Socket socket, Session session,String message,MessageServiceHandler handler) throws IOException {
        super(socket);
        this.handler = handler;
        this.session = session;
        this.message = message;
    }

    public MessageServiceHandler getHandler() {
        return handler;
    }
    
    @Override
    public void handleConnectionError(IOException e) {
        System.out.println("VaniChat: Message Request Error");
    }

    @Override
    public void handleConnectionToServer() throws IOException {
        OutputStream ou = socket.getOutputStream();
        synchronized(ou){
            DataOutputStream out = new DataOutputStream(ou);
            out.writeByte(MODE);
            out.writeInt(session.getSession_id());
            byte[] bname = message.getBytes(ChatClient.CHARSET);
            out.writeShort(bname.length);
            out.write(bname);
        }
    }
    
}
