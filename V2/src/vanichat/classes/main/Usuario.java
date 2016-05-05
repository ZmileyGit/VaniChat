
package vanichat.classes.main;

import java.util.Objects;


public class Usuario {
    private final String username;

    private Usuario(String username) {
        this.username = username;
    }
    
    public static Usuario newInstance(String username){
        return new Usuario(username);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.username.hashCode();
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
        return Objects.equals(this.username, other.username);
    }
   
}
