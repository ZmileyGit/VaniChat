
package server.network.clients;

import java.util.HashMap;
import java.util.Objects;
import server.classes.user.Usuario;

public class Session {
    private final Integer session_id;
    private final HashMap<Usuario,ChatClientConnection> connections;

    public Session(Integer session_id, HashMap<Usuario, ChatClientConnection> connections) {
        this.session_id = session_id;
        this.connections = connections;
    }

    public synchronized HashMap<Usuario, ChatClientConnection> getConnections() {
        return connections;
    }
 
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.session_id);
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
        final Session other = (Session) obj;
        if (!Objects.equals(this.session_id, other.session_id)) {
            return false;
        }
        return true;
    }

    public Integer getSession_id() {
        return session_id;
    }
    
}
