package vanichat.classes.server;

import vanichat.network.server.connections.handlers.*;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ServerHandlers implements Runnable{
    
    private final ByteBuffer buffer;
    private final ConnectionHandler first;
    
    public ServerHandlers(ConnectionHandler first) throws IOException{
        this.buffer = first.getConnection().getReader().read();
        this.first = first;
    }

    @Override
    public void run() {
        try {
            first.handlePacket(buffer);
        } catch (IOException ex) {
            System.out.println("Error Processing Connection");
        }
    }
}
