
package client.network.receiver.handlers;

import client.network.ChatClient;
import client.network.receiver.ChatReceiver;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.LinkedList;
import client.classes.user.Usuario;
import client.network.receiver.Session;

class MessagePacketHandler extends PacketHandler {

    public final static byte HANDLED = 2;
    
    protected MessagePacketHandler(DataInputStream in, ChatReceiver receiver) {
        super(in, receiver);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior() throws IOException {
        int user_id = in.readInt();
        int session_id = in.readInt();
        
        short length = in.readShort();
        
        ByteBuffer buffer =  ByteBuffer.allocate(length);
        for(int cont=0 ; cont<buffer.capacity(); cont+=1){
            buffer.put(in.readByte());
        }
        buffer.position(0);
        CharBuffer usrbuff = ChatClient.CHARSET.newDecoder().decode(buffer);
        String message = usrbuff.toString().trim();
        System.out.println("Mensaje nuevo " + user_id + " " + session_id + " : " + message);
        //new Thread(new AsyncTask(session_id,buffer,this)).start();
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
