
package server;

import mensajes.Mensaje;

public class BroadcastTest implements Runnable{
    private final Mensaje message;
    private final ChatListener listener;

    public BroadcastTest(Mensaje message, ChatListener listener){
        this.message = message;
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.CLIENTES.broadcast(message);
    }
    
}
