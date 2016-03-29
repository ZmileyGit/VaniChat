
package server.network.clients.handlers;

import client.classes.messages.Texto;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import server.network.BroadcastTest;
import server.network.clients.ChatClientConnection;
import server.network.ChatServer;

class MessageHandler extends PacketHandler {

    public final static byte HANDLED = 1;
    
    protected MessageHandler(DataInputStream in, ChatClientConnection connection) {
        super(in, connection);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior() throws IOException {
        short length = (short) in.readShort();
            
        byte[] bytext = new byte[length];
        for(int cont = 0;cont < length;cont+=1){
            bytext[cont] = (byte) in.readByte();
        }

    }

    
}
