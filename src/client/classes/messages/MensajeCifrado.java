package client.classes.messages;

import client.classes.messages.Mensaje;
import client.classes.user.Usuario;
import client.classes.messages.security.Descifrable;

public class MensajeCifrado extends Mensaje implements Descifrable{
    
    private final byte tipo;  
    
    public MensajeCifrado(byte[] data, Usuario user,byte tipo) {
        super(data, user);
        this.tipo = tipo;
    }

    @Override
    public MensajeDescifrado descifrar() {
        return null;
    }

    @Override
    public byte getTipo() {
        return this.tipo;
    }
    
}
