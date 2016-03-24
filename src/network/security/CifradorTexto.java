package network.security;

import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import structure.messages.Mensaje;
import structure.messages.MensajeCifrado;
import structure.messages.Texto;

public class CifradorTexto extends HandlerCifrado {

        @Override
        protected HandlerCifrado next() {
            return new CifradorArchivo();
        }

        @Override
        public MensajeCifrado cifrar(Mensaje mensaje) {
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
                HandlerCifrado n = next();
                if(n!=null){
                    return n.cifrar(mensaje);
                }
                return null;
            }
        }
}
