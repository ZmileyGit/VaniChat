
package server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Server implements Stoppable,Runnable {
    
    protected final ServerSocket socket;
    protected final AtomicBoolean run = new AtomicBoolean(true);

    protected Server(Integer port) throws IOException {
        this.socket = new ServerSocket(port);
    }
    
    public void start(){
        try(ServerSocket server = socket;){
            while(run.get()){
                Socket guest;
                try {
                    System.out.println("Escuchando...");
                    guest = server.accept();
                    handlePetition(guest);
                } catch (IOException ex) {
                    handleAcceptError(ex);
                }
            }
        } catch (IOException ex) {
            handleServerError(ex);
        }
    }
    
    @Override
    public void run(){
        start();
    }
    
    @Override
    public void stop(){
         run.set(false);
    }
    
    protected abstract void handlePetition(Socket guest);
    protected abstract void handleAcceptError(Exception e);
    protected abstract void handleServerError(Exception e);
    
}
