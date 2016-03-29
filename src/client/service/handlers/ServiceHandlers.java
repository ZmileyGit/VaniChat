package client.service.handlers;


import client.service.ChatService;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServiceHandlers{
    
    public void handlePacket(ChatService service,Socket con) throws IOException{
        DataInputStream in = new DataInputStream(con.getInputStream());
        byte mode = (byte) in.readByte();
        new LoginServiceHandler(service,con,in).handlePacket(mode);
    }
    
}
