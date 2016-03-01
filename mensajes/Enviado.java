package mensajes;

public class Enviado extends Estado {

    private static Enviado instance = new Enviado();
    
    private Enviado(){
        
    }
    
    public static Enviado getInstance(){
        return instance;
    }
    
    @Override
    public Estado next(){
        return Recibido.getInstance();
    }
}
