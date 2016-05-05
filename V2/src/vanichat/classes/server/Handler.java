package vanichat.classes.server;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class Handler{
    
    protected Handler successor;
    protected byte handled = -1;

    public Handler(Handler succesor) {
        this.successor = succesor;
    }
    
    protected final void defaultBehavior(){
        System.out.println("Final de la Cadena");
    }
    
    public final void handlePacket(ByteBuffer buffer) throws IOException{
        buffer.position(0);
        byte mode = buffer.get();
        if(mode == handled && mode > -1){
            handleBehavior(buffer);
        }else{
            if(successor == null){
                defaultBehavior();
            }else{
                successor.handlePacket(buffer);
            }
        }
    }
    
    protected abstract void handleBehavior(ByteBuffer buffer) throws IOException;
}
