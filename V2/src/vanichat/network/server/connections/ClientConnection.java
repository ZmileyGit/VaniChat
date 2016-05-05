
package vanichat.network.server.connections;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ClientConnection implements Runnable{
    protected final Socket socket;
    protected final AtomicBoolean leaveOpen = new AtomicBoolean(false);

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public void run() {
        try{
            handleClientPackets();
        } catch (Exception ex) {
            handleClientError(ex);
        }finally{
            if(!leaveOpen.get()){
                try {
                    socket.close();
                } catch (IOException ex) {}
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.socket);
        return hash;
    }
    
    public void leaveOpen(){
        leaveOpen.set(true);
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
