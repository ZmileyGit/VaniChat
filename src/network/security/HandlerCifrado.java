package network.security;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import structure.messages.Mensaje;
import structure.messages.MensajeCifrado;
import structure.messages.Texto;

public abstract class HandlerCifrado {

    public static HandlerCifrado getInstance(){
        return new CifradorTexto();
    }
    
    protected abstract HandlerCifrado next();
    public abstract MensajeCifrado cifrar(Mensaje mensaje);
    
}
