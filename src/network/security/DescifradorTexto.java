package network.security;

import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import structure.messages.Mensaje;
import structure.messages.MensajeDescifrado;
import structure.messages.Texto;

public class DescifradorTexto extends HandlerDescifrado {

    @Override
    public HandlerDescifrado next() {
        return new DescifradorArchivo();
    }

    @Override
    public MensajeDescifrado descifrar(Mensaje mensaje) {
        if(mensaje.getTipo() == Texto.TYPE_ID){
                try{
                    Cipher cifrador = Cipher.getInstance("AES", new BouncyCastleProvider());
                    //Falta la llave para cifrar
                    
                    //return new MensajeCifrado();
                    return null;
                }catch(Exception e){
                    return null;
                }
            }else{
                HandlerDescifrado n = next();
                if(n!=null){
                    return n.descifrar(mensaje);
                }
                return null;
            }
    }

}
