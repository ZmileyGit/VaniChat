
package vanichat.classes.main;

import java.util.Objects;

public class Session {
    protected final Integer session_id;
    protected final Usuario user;

    public Session(Integer session_id, Usuario user) {
        this.session_id = session_id;
        this.user = user;
    }

    public Usuario getUser() {
        return user;
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
