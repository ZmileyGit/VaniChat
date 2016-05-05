
package vanichat.network.server.connections.handlers.groups;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import vanichat.classes.main.Usuario;
import vanichat.config.Configuration;
import vanichat.network.server.connections.ChatGroupConnection;
import vanichat.network.server.connections.handlers.ConnectionHandler;

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
        
        getConnection().getChat().getUsers().getIterableUserList().stream().forEach((Usuario user)->{
            
        });
        /*ByteBuffer response = ByteBuffer.allocate(4 + 1 + 4);
        response.putInt(1 + 4);
        response.put(HANDLED);
        response.putInt(token);
        response.position(0);
        
        connection.getWriter().write(response);
        connection.stop();*/
    }
    
    @Override
    public ChatGroupConnection getConnection() {
        return (ChatGroupConnection)connection;
    }
    
}
