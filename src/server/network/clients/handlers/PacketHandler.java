package server.network.clients.handlers;

import java.io.DataInputStream;
import java.io.IOException;
import server.network.clients.ChatClientConnection;

public abstract class PacketHandler{
    
    protected final DataInputStream in;
    protected final ChatClientConnection connection;
    protected PacketHandler successor = null;
    protected byte handled = -1;

    public PacketHandler(DataInputStream in, ChatClientConnection connection) {
        this.in = in;
        this.connection = connection;
    }
    
    protected final void defaultBehavior(){
        System.out.println("Final de la Cadena");
    }
    
    public final void handlePacket(byte mode) throws IOException{
        if(mode == handled && mode > -1){
            handleBehavior();
        }else{
            if(successor == null){
                defaultBehavior();
            }else{
                successor.handlePacket(mode);
            }
        }
    }
    
    protected abstract void handleBehavior() throws IOException;
}
