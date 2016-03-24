package network.security;

import structure.messages.Mensaje;
import structure.messages.MensajeDescifrado;

public abstract class HandlerDescifrado {

    public static HandlerDescifrado getInstance(){
        return new DescifradorTexto();
    }
    
    public abstract HandlerDescifrado next();
    public abstract MensajeDescifrado descifrar(Mensaje mensaje);
}
