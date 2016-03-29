
package client.service;

import client.network.Stoppable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Service implements Stoppable,Runnable {
    
    protected final ServerSocket socket;
    protected final AtomicBoolean run = new AtomicBoolean(true);

    protected Service(Integer port) throws IOException {
        this.socket = new ServerSocket(port);
    }
    
    public void start(){
        try(ServerSocket server = socket;){
            System.out.println("Bienvenido a VaniChat");
            while(run.get()){
                Socket guest;
                try { 
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
    
    protected abstract void handlePetition(Socket guest) throws IOException;
    protected abstract void handleAcceptError(Exception e);
    protected abstract void handleServerError(Exception e);
    
}
