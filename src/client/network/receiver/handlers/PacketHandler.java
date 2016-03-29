package client.network.receiver.handlers;

import java.io.DataInputStream;
import java.io.IOException;
import client.network.receiver.ChatReceiver;

public abstract class PacketHandler{
    
    protected final DataInputStream in;
    protected final ChatReceiver receiver;
    protected PacketHandler successor = null;
    protected byte handled = -1;

    public PacketHandler(DataInputStream in, ChatReceiver receiver) {
        this.in = in;
        this.receiver = receiver;
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

    public ChatReceiver getReceiver() {
        return receiver;
    }
    
    protected abstract void handleBehavior() throws IOException;
}
