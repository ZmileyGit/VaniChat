
package network.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ClientConnection implements Runnable,Stoppable{
    protected final Socket socket;
    protected final AtomicBoolean run = new AtomicBoolean(true);
    protected final Listener source;

    public ClientConnection(Socket socket, Listener source) {
        this.socket = socket;
        this.source = source;
    }
    
    @Override
    public void run() {
        try(Socket guest = socket; DataInputStream in = new DataInputStream(socket.getInputStream());){
            while(run.get()){
                handleClientPackets(guest,in);
            }
        } catch (IOException ex) {
            handleClientError(socket,ex);
        }
    }
    
    @Override
    public void stop(){
        run.set(false);
    }

    public Socket getSocket() {
        return socket;
    }
    
    public abstract void handleClientPackets(Socket socket, DataInputStream in) throws IOException;
    public abstract void handleClientError(Socket socket,Exception e);
}
