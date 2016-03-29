
package client.network;

import java.io.IOException;
import java.net.Socket;

public abstract class Sender extends Client{

    public Sender(Socket socket) {
        super(socket);
    }
    
    public void start(){
        try{
            handleConnectionToServer();
        } catch (IOException ex) {
            handleConnectionError(ex);
        }
    }
}
