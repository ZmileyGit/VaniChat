
package client.service.handlers;

import client.network.ChatClient;
import client.network.LoginClient;
import client.service.ChatService;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class LoginServiceHandler extends ServiceHandler {

    public final static byte HANDLED = 0;
    
    public LoginServiceHandler(ChatService service, Socket socket, DataInputStream in) {
        super(service, socket,in);
        this.handled = HANDLED;
        this.successor = new SessionServiceHandler(service,socket,in);
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
        private final LoginServiceHandler handler;

        public AsyncTask(ByteBuffer buffer, LoginServiceHandler handler) {
            buffer.position(0);
            this.buffer = buffer;
            this.handler = handler;
        }
        
        @Override
        public void run() {
            try {
                CharBuffer usrbuff = ChatClient.CHARSET.newDecoder().decode(buffer);
                String username = usrbuff.toString().trim();
                System.out.printf("Signing in as %s...%n",username);
                new LoginClient(username,handler).start();
            } catch (IOException ex) {
                System.out.println("VaniChat: LogIn Error");
            }
        }
    }
    
}
