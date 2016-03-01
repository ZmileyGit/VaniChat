
package network.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import network.server.Stoppable;

public abstract class Receiver implements Runnable,Stoppable{
    
    protected final Socket socket;
    private AtomicBoolean run = new AtomicBoolean(true);
    
    public Receiver(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run(){
        try(Socket server = socket;DataInputStream in = new DataInputStream(socket.getInputStream())){
            while(run.get()){
                handleReceivedPackets(server,in);
            }
        } catch (IOException ex) {
            handleReceivedException(ex);
        }
    }
    
    @Override
    public void stop(){
        run.set(false);
    }
    
    public abstract void handleReceivedPackets(Socket socket,DataInputStream in) throws IOException;
    
    public abstract void handleReceivedException(Exception e);
}
