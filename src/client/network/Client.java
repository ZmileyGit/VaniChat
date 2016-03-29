
package client.network;

import java.io.IOException;
import java.net.Socket;

public abstract class Client implements Runnable{
    
    protected final Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }
    
    public Client(String ip,int port) throws IOException {
        this(new Socket(ip,port));
    }
    
    @Override
    public void run(){
        start();
    }
    
    public void start(){
        try(Socket server = socket){
            handleConnectionToServer();
        } catch (IOException ex) {
            handleConnectionError(ex);
        }
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public abstract void handleConnectionError(IOException e);
    public abstract void handleConnectionToServer() throws IOException;
}
