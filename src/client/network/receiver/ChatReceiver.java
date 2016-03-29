package client.network.receiver;

import client.network.ChatClient;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import client.network.Stoppable;
import client.network.receiver.handlers.PacketHandlers;

public class ChatReceiver extends Receiver implements Stoppable{
    
    private final AtomicBoolean run = new AtomicBoolean(true);
    
    public ChatReceiver(ChatClient client) {
        super(client);
    }

    @Override
    public void handleReceivedPackets() throws IOException {
        Socket socket = client.getSocket();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        while(run.get()){
            if(in.available()!=0){
                new PacketHandlers().handlePacket(in, this);
            }else{
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {}
            }
        }
    }

    @Override
    public void handleReceivedException(Exception e) {
        System.out.println("Reception Error");
    }

    @Override
    public void stop() {
        run.set(false);
    }
    
}
