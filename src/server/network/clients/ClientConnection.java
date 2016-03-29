
package server.network.clients;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public abstract class ClientConnection implements Runnable{
    protected final Socket socket;

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try(Socket guest = socket){
            handleClientPackets();
        } catch (IOException ex) {
            handleClientError(ex);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.socket);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientConnection other = (ClientConnection) obj;
        if (!Objects.equals(this.socket, other.socket)) {
            return false;
        }
        return true;
    }
    
    
    
    public abstract void handleClientPackets() throws IOException;
    public abstract void handleClientError(Exception e);
}
