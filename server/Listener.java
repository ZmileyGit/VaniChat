
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Listener implements Runnable,Stoppable {
    
    protected final ServerSocket socket;
    protected final AtomicBoolean run = new AtomicBoolean(true);

    protected Listener(Integer port) throws IOException {
        this.socket = new ServerSocket(port);
    }
    
    @Override
    public void run(){
        try(ServerSocket server = socket;){
            while(run.get()){
                Socket guest;
                try {
                    System.out.println("Escuchando...");
                    guest = server.accept();
                    handlePetition(guest);
                } catch (IOException ex) {
                    handleAcceptError(this,ex);
                }
            }
        } catch (IOException ex) {
            handleServerError(this,ex);
        }
    }
    
    @Override
    public void stop(){
         run.set(false);
    }
    
    protected abstract void handlePetition(Socket guest);
    protected abstract void handleAcceptError(Listener listener,Exception e);
    protected abstract void handleServerError(Listener listener,Exception e);
    
}
