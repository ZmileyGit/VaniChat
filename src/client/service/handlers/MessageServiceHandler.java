
package client.service.handlers;

import client.network.ChatClient;
import client.network.LoginClient;
import client.service.ChatService;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class MessageServiceHandler extends ServiceHandler{
    
    public final static byte HANDLED = 1;
    
    public MessageServiceHandler(ChatService service, Socket socket, DataInputStream in) {
        super(service, socket,in);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior() throws IOException {
        int session_id = in.readInt();
        
        int length = in.readShort();
        
        ByteBuffer buffer =  ByteBuffer.allocate(length);
        for(int cont=0 ; cont<buffer.capacity(); cont+=1){
            buffer.put(in.readByte());
        }

        new Thread(new AsyncTask(buffer,session_id,this)).start();
    }

    private static class AsyncTask implements Runnable{
        
        private final ByteBuffer buffer;
        private final int session_id;
        private final MessageServiceHandler handler;

        public AsyncTask(ByteBuffer buffer, int session_id, MessageServiceHandler handler) {
            buffer.position(0);
            this.session_id = session_id;
            this.buffer = buffer;
            this.handler = handler;
        }
        
        @Override
        public void run() {
            try {
                CharBuffer usrbuff = ChatClient.CHARSET.newDecoder().decode(buffer);
                String message = usrbuff.toString().trim();
                System.out.printf("Enviando mensaje %s%n",message);
            } catch (IOException ex) {
                System.out.println("VaniChat: LogIn Error");
            }
        }
    }
}
