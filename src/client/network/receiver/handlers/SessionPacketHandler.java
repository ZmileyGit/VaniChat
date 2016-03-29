
package client.network.receiver.handlers;

import client.network.ChatClient;
import client.network.LoginClient;
import client.network.receiver.ChatReceiver;
import client.service.handlers.LoginServiceHandler;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.LinkedList;
import client.classes.user.Usuario;
import client.network.receiver.Session;

class SessionPacketHandler extends PacketHandler {

    public final static byte HANDLED = 2;
    
    protected SessionPacketHandler(DataInputStream in, ChatReceiver receiver) {
        super(in, receiver);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior() throws IOException {
        int session_id = in.readInt();
        
        short length = in.readShort();
        
        ByteBuffer buffer =  ByteBuffer.allocate(length);
        for(int cont=0 ; cont<buffer.capacity(); cont+=1){
            buffer.put(in.readByte());
        }
        
        new Thread(new AsyncTask(session_id,buffer,this)).start();
    }

    private static class AsyncTask implements Runnable{
        
        private final int session_id;
        private final ByteBuffer buffer;
        private final SessionPacketHandler handler;

        public AsyncTask(int session_id, ByteBuffer buffer, SessionPacketHandler handler) {
            buffer.position(0);
            this.session_id = session_id;
            this.buffer = buffer;
            this.handler = handler;
        }
        
        @Override
        public void run() {
            try {
                CharBuffer usrbuff = ChatClient.CHARSET.newDecoder().decode(buffer);
                String userlist = usrbuff.toString().trim();
                String[] users = userlist.split(":");
                LinkedList<Usuario> usuarios = new LinkedList<>();
                for(int cont=0; cont<users.length; cont+=1){
                    String[] userinfo = users[cont].split(",");
                    usuarios.push(new Usuario(userinfo[0],Integer.parseInt(userinfo[1])));
                }
                ((ChatClient)handler.getReceiver().getClient()).addSession(new Session(session_id,usuarios));
            } catch (IOException ex) {
                System.out.println("VaniChat: LogIn Error");
            }
        }
    }
    
}
