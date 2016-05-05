
package vanichat.network.server.connections.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import vanichat.config.Configuration;
import vanichat.network.server.connections.ChatClientConnection;

class UserLinkConnectionHandler extends ConnectionHandler {

    public final static byte HANDLED = Configuration.USERLINK;
    
    public UserLinkConnectionHandler(ChatClientConnection connection) {
        super(new GroupConnectionHandler(connection),connection);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior(ByteBuffer buffer) throws IOException {
        Integer token = buffer.getInt();
        
        ByteBuffer response = ByteBuffer.allocate(4 + 1 + 1);
        response.putInt(1 + 1);
        response.put(HANDLED);        
        
        if(connection.getListener().getSessions().bindSocketToToken(token, connection)){
            response.put((byte)0);
            response.position(0);
            connection.getWriter().write(response);
        }else{
            response.put((byte)1);
            response.position(0);
            connection.getWriter().write(response);
            connection.stop();
        }
    }
    
}
