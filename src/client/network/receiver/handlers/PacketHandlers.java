package client.network.receiver.handlers;

import client.network.receiver.ChatReceiver;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketHandlers {
    
    public void handlePacket(DataInputStream in,ChatReceiver con) throws IOException{
        byte mode = (byte) in.readByte();
        new SessionPacketHandler(in,con).handlePacket(mode);
    }
}
