
package vanichat.network.server.connections.handlers.groups;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Calendar;
import vanichat.classes.server.ChatSession;
import vanichat.config.Configuration;
import vanichat.network.server.connections.ChatGroupConnection;
import vanichat.network.server.connections.handlers.ConnectionHandler;
import vanichat.network.server.db.*;

public class MessageConnectionHandler extends ConnectionHandler {

    public final static byte HANDLED = Configuration.MESSAGE;
    
    public MessageConnectionHandler(ChatGroupConnection connection) {
        super(null,connection);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior(ByteBuffer buffer) throws IOException {
        CharBuffer usrbuffer = Configuration.DEFAULT_CHARSET.newDecoder().decode(buffer);
        
        String message = usrbuffer.toString().trim();
        byte[] bmess = message.getBytes(Configuration.DEFAULT_CHARSET);
        byte[] root = getConnection().getUser().getUsername().getBytes(Configuration.DEFAULT_CHARSET);
        
        ByteBuffer response = ByteBuffer.allocate(4 + 1 + 4 + 4 + root.length + bmess.length);
        response.putInt(1 + 4 + 4 + root.length + bmess.length);
        response.put(HANDLED);
        response.putInt(getConnection().getChat().getID());
        response.putInt(root.length);
        response.put(root);
        response.put(bmess);
        
        getConnection().getChat().getIterableSessionList().stream().forEach((ChatSession session)->{
            response.position(0);
            try {
                session.getConnection().getWriter().write(response);
            } catch (IOException ex) {
                System.out.printf("Couldn't send Message [%s] to User %s%n",message,session.getUser().getUsername());
            }
        });
        
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
        Conversacion c = Conversacion.get(getConnection().getChat().getID());
                    if(c==null){
                        c = new Conversacion(getConnection().getChat().getID());
                        Conversacion.guardarConversacion(c);
                    }
                    Mensaje m = new Mensaje();
                    m.setContenido(message.getBytes(Configuration.DEFAULT_CHARSET));
                    m.setId_conversacion(c.getId());
                    Calendar calendar = Calendar.getInstance();
                    m.setReceivedDateTime(calendar.getTime());
                    Mensaje.guardarMensaje(m);
    }
    
    @Override
    public ChatGroupConnection getConnection() {
        return (ChatGroupConnection)connection;
    }
    
}
