package network.security;

import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import structure.messages.Mensaje;
import structure.messages.MensajeDescifrado;
//import structure.messages.Archivo;

public class DescifradorArchivo extends HandlerDescifrado {

    @Override
    public HandlerDescifrado next() {
        return null;
    }

    @Override
    public MensajeDescifrado descifrar(Mensaje mensaje) {
        if(mensaje.getTipo() == 2 /*Archivo.TYPE_ID*/){
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
