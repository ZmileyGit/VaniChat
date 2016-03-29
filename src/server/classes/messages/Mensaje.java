
package server.classes.messages;

import java.time.ZonedDateTime;
import server.classes.user.Usuario;
import client.classes.messages.security.Cifrable;

public class Mensaje{
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
    
}
