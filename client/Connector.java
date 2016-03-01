
package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Connector implements Runnable{
    
    protected final String ip;
    protected final Integer port;

    public Connector(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
    
    @Override
    public void run(){
        try(Socket socket = new Socket(ip,port);DataOutputStream out = new DataOutputStream(socket.getOutputStream());){
            handleConnectionToServer(socket,out);
        } catch (IOException ex) {
            handleConnectionError(ex);
        }
    }
    
    public abstract void handleConnectionError(IOException e);
    public abstract void handleConnectionToServer(Socket socket,DataOutputStream out) throws IOException;
}
