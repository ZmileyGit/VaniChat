
package vanichat.classes.main;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Chat {
    protected final Integer ID;
    protected final Usuarios users;
    protected final Lock slock = new ReentrantLock();
    
    public Chat(Integer ID){
        this.ID = ID;
        users = new Usuarios(slock);
    }
    
    public Usuarios getUsers() {
        return users;
    }

    public Integer getID() {
        return ID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.ID);
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
        final Chat other = (Chat) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }
}
