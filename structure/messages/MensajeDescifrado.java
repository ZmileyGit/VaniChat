
package structure.messages;

import structure.main.Usuario;
import network.security.Cifrable;

public abstract class MensajeDescifrado extends Mensaje implements Cifrable {
    
    public MensajeDescifrado(byte[] data, Usuario user) {
        super(data, user);
    }
    
    @Override
    public MensajeCifrado cifrar(){
        return null;
    }
    
}
