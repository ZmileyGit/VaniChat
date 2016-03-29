
package client.network.receiver;

import client.network.Client;
import java.io.IOException;
import java.net.Socket;

public abstract class Receiver implements Runnable{
    
    protected final Client client;
    
    public Receiver(Client client) {
        this.client = client;
    }
    
    @Override
    public void run(){
        try(Socket server = client.getSocket()){
            handleReceivedPackets();
        } catch (IOException ex) {
            handleReceivedException(ex);
        }
    }

    public Client getClient() {
        return client;
    }
    
    public abstract void handleReceivedPackets() throws IOException;
    
    public abstract void handleReceivedException(Exception e);
}
