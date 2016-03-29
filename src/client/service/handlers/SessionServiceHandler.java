
package client.service.handlers;

import client.network.ChatClient;
import client.network.LoginClient;
import client.network.SessionSender;
import client.service.ChatService;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class SessionServiceHandler extends ServiceHandler {

    public final static byte HANDLED = 2;
    
    public SessionServiceHandler(ChatService service, Socket socket, DataInputStream in) {
        super(service, socket,in);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior() throws IOException {
        int length = in.readShort();
        
        ByteBuffer buffer =  ByteBuffer.allocate(length);
        for(int cont=0 ; cont<buffer.capacity(); cont+=1){
            buffer.put(in.readByte());
        }

        new Thread(new AsyncTask(buffer,this)).start();
    }

    private static class AsyncTask implements Runnable{
        
        private final ByteBuffer buffer;
        private final SessionServiceHandler handler;

        public AsyncTask(ByteBuffer buffer, SessionServiceHandler handler) {
            buffer.position(0);
            this.buffer = buffer;
            this.handler = handler;
        }
        
        @Override
        public void run() {
            try {
                CharBuffer usrbuff = ChatClient.CHARSET.newDecoder().decode(buffer);
                String userlist = usrbuff.toString().trim();
                String[] users = userlist.split(":");
                System.out.println("Trying to Establish Chat Session with...");
                for(int cont=0;cont < users.length;cont+=1){
                   System.out.println("\t- "+users[cont]);
                }
                new SessionSender(handler.getService().getChat().getSocket(),userlist,handler).start();
            } catch (IOException ex) {
                System.out.println("VaniChat: Session Error");
            }
        }
    }
    
}
