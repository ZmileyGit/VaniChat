
package client.network.receiver;

import client.classes.user.Usuario;
import java.util.LinkedList;
import java.util.Objects;

public class Session {
    private final Integer session_id;
    private final LinkedList<Usuario> usuarios;

    public Session(Integer session_id, LinkedList<Usuario> usuarios) {
        this.session_id = session_id;
        this.usuarios = usuarios;
    }

    public Integer getSession_id() {
        return session_id;
    }

    public LinkedList<Usuario> getUsuarios() {
        return usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.session_id);
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

    @Override
    public String toString() {
        return "Session{" + "session_id=" + session_id + ", usuarios=" + usuarios + '}';
    }
    
    
    
}
