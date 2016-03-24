package network.server;

import java.nio.charset.Charset;


public class Server {
    
    public static final Charset CHARSET = Charset.forName("UTF-16");
    protected Listener listener;
    
    public Server(ChatListener listener){
        this.listener = listener;
    }
    
    public void start(){
        listener.run();
    }
    
    public void restart(){
    
    }
    
    public void stop(){
        listener.stop();
    }
    
}
