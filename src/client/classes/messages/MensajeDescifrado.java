
package client.classes.messages;

import client.classes.user.Usuario;
import client.classes.messages.security.Cifrable;

public abstract class MensajeDescifrado extends Mensaje implements Cifrable {
    
    public MensajeDescifrado(byte[] data, Usuario user) {
        super(data, user);
    }
    
    @Override
    public MensajeCifrado cifrar(){
        return null;
    }
    
}
