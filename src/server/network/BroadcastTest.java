
package server.network;

import server.classes.messages.Mensaje;

public class BroadcastTest implements Runnable{
    private final Mensaje message;
    private final ChatServer listener;

    public BroadcastTest(Mensaje message, ChatServer listener){
        this.message = message;
        this.listener = listener;
    }

    @Override
    public void run() {
        //listener.getClientes().broadcast(message);
    }
    
}
