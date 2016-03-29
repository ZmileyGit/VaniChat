
package client.network;

import client.service.handlers.LoginServiceHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginClient extends Client{

    public static final byte MODE = 0;
    
    private final LoginServiceHandler handler;
    private final String username;
    
    public LoginClient(String username,LoginServiceHandler handler) throws IOException {
        super(ChatClient.SERVER_IP_ADDRESS, ChatClient.SERVER_PORT);
        this.handler = handler;
        this.username = username;
    }

    public LoginServiceHandler getHandler() {
        return handler;
    }
    
    @Override
    public void handleConnectionError(IOException e) {
        System.out.println("VaniChat: Login Request Error");
    }

    @Override
    public void handleConnectionToServer() throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(MODE);
        byte[] bname = username.getBytes(ChatClient.CHARSET);
        out.writeShort(bname.length);
        out.write(bname);
        
        DataInputStream in = new DataInputStream(socket.getInputStream());

        int response = in.readInt();
        handler.getService().startChat(username,response);
    }
    
}
