
package mensajes;

import estructura.Usuario;
import security.Cifrable;

public abstract class MensajeDescifrado extends Mensaje implements Cifrable {
    
    public MensajeDescifrado(byte[] data, Usuario user) {
        super(data, user);
    }
    
    @Override
    public MensajeCifrado cifrar(){
        return null;
    }
    
}
