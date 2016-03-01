
package structure.messages;

import structure.main.Usuario;
import network.server.Server;

public class Texto extends MensajeDescifrado{
    
    public static final byte TYPE_ID = 1;
    
    public Texto(byte[] data, Usuario user) {
        super(data, user);
    }

    public String contentToString(){
        return new String(data,Server.CHARSET);
    }

    @Override
    public byte getTipo() {
        return TYPE_ID;
    }

}
