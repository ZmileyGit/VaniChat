
package client.classes.messages;

import client.classes.user.Usuario;
import client.network.ChatClient;

public class Texto extends MensajeDescifrado{
    
    public static final byte TYPE_ID = 1;
    
    public Texto(byte[] data, Usuario user) {
        super(data, user);
    }

    public String contentToString(){
        return new String(data,ChatClient.CHARSET);
    }

    @Override
    public byte getTipo() {
        return TYPE_ID;
    }

}
