
package structure.main;

import java.net.Socket;
import java.util.Objects;

public class Usuario {
    private final String username;

    public Usuario(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }
    
    
}
