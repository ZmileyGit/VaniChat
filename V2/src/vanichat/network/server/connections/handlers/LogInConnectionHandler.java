
package vanichat.network.server.connections.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import vanichat.classes.main.Usuario;
import vanichat.config.Configuration;
import vanichat.network.server.connections.ChatClientConnection;

public class LogInConnectionHandler extends ConnectionHandler{
    
    public final static byte HANDLED = Configuration.LOGIN;
    
    public LogInConnectionHandler(ChatClientConnection connection) {
        super(new UserLinkConnectionHandler(connection),connection);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior(ByteBuffer buffer) throws IOException {
        CharBuffer usrbuffer = Configuration.DEFAULT_CHARSET.newDecoder().decode(buffer);
        String username = usrbuffer.toString().trim();
        Integer token = connection.getListener().getSessions().addUser(Usuario.newInstance(username));
        
        ByteBuffer response = ByteBuffer.allocate(4 + 1 + 4);
        response.putInt(1 + 4);
        response.put(HANDLED);
        response.putInt(token);
        response.position(0);
        
        connection.getWriter().write(response);
        connection.stop();
    }
}
