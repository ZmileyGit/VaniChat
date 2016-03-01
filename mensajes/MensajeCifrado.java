package mensajes;

import estructura.Usuario;
import security.Descifrable;

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
