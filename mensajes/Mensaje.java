
package mensajes;

import java.time.ZonedDateTime;
import estructura.Usuario;
import security.Cifrable;

public abstract class Mensaje{
    protected final byte[] data;
    protected final Usuario user;
    protected final ZonedDateTime tiempo;

    public Mensaje(byte[] data, Usuario user) {
        this.data = data;
        this.user = user;
        tiempo = ZonedDateTime.now();
    }

    public byte[] getData() {
        return data;
    }

    public Usuario getUser() {
        return user;
    }

    public ZonedDateTime getTiempo() {
        return tiempo;
    }
    
    public abstract byte getTipo();
    
}
