package client.service.handlers;

import client.service.ChatService;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ServiceHandler{
    
    protected final ChatService service;
    protected final DataInputStream in;
    protected final Socket socket;
    protected ServiceHandler successor = null;
    protected byte handled = -1;

    public ServiceHandler(ChatService service, Socket socket, DataInputStream in) {
        this.service = service;
        this.socket = socket;
        this.in = in;
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

    public ChatService getService() {
        return service;
    }

    public Socket getSocket() {
        return socket;
    }
}
