package server.network.clients.handlers;

import java.io.DataInputStream;
import java.io.IOException;
import server.network.clients.ChatClientConnection;

public class PacketHandlers {
    
    public void handlePacket(DataInputStream in,ChatClientConnection con) throws IOException{
        byte mode = (byte) in.readByte();
        new LogInPacketHandler(in,con).handlePacket(mode);
    }
}
