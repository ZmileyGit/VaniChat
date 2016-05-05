
package vanichat.network.server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import vanichat.network.interfaces.Stoppable;

public abstract class Server implements Stoppable,Runnable {
    
    protected final ServerSocket socket;
    protected final AtomicBoolean run = new AtomicBoolean(true);

    protected Server(ServerSocket socket){
        this.socket = socket;
    }
    
    public void start(){
        try(ServerSocket server = socket;){
            while(run.get()){
                Socket guest;
                try {
                    System.out.println("Escuchando...");
                    guest = server.accept();
                    handlePetition(guest);
                } catch (Exception ex) {
                    handleAcceptError(ex);
                }
            }
        } catch (Exception ex) {
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
    
    protected abstract void handlePetition(Socket guest) throws IOException;
    protected abstract void handleAcceptError(Exception e);
    protected abstract void handleServerError(Exception e);
    
}