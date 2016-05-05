
package vanichat.network.server.connections.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import vanichat.classes.main.Usuario;
import vanichat.classes.server.ServerChat;
import vanichat.config.Configuration;
import vanichat.network.server.connections.ChatClientConnection;
import vanichat.network.server.connections.ChatGroupConnection;

public class ChatLinkConnectionHandler extends ConnectionHandler{
    
    public final static byte HANDLED = Configuration.CHATLINK;
    
    protected ChatLinkConnectionHandler(ChatClientConnection connection) {
        super(null,connection);
        this.handled = HANDLED;
    }

    @Override
    protected void handleBehavior(ByteBuffer buffer) throws IOException {
        Integer cid = buffer.getInt();
        Integer token = buffer.getInt();

        Usuario user = connection.getListener().getSessions().searchUserByToken(token);
        byte code = 1;
        
        if(user != null){
            ServerChat chat = connection.getListener().getChats().searchChatByID(cid);
            if(chat.userIsAllowedInChat(user)){
                
                code = 0;
                ChatGroupConnection group = new ChatGroupConnection(
                        connection.getSocket(),
                        connection.getListener(),
                        connection.getReader(),
                        connection.getWriter(),
                        user,
                        chat
                );
                chat.registerUserConnection(token, user, group);
                connection.leaveOpen();
                connection.stop();
                
                ByteBuffer response = ByteBuffer.allocate(4 + 1 + 1 + 4);
                response.putInt(1 + 1 + 4);
                response.put(HANDLED);
                response.put(code);
                response.putInt(cid);
                connection.getWriter().write(response);
                
                group.run();
                
            }else{
                code = 1;
            }
        }else{
            code = 2;
        }
        
        ByteBuffer response = ByteBuffer.allocate(4 + 1 + 1 + 4);
        response.putInt(1 + 1 + 4);
        response.put(HANDLED);
        response.put(code);
        response.putInt(cid);
        
        connection.getWriter().write(response);
    }
}
