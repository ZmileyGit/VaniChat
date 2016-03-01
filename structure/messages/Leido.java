package structure.messages;

public class Leido extends Estado {

    private static Leido instance = new Leido();
    
    private Leido(){
        
    }
    
    public static Leido getInstance(){
        return instance;
    }
    
    @Override
    public Estado next(){
        return this;
    }
}
