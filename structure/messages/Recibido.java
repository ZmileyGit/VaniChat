package structure.messages;

public class Recibido extends Estado {

    private static Recibido instance = new Recibido();
    
    private Recibido(){
        
    }
    
    public static Recibido getInstance(){
        return instance;
    }
    
    @Override
    public Estado next(){
        return Leido.getInstance();
    }
}
