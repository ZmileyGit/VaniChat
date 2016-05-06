
package vanichat.network.server.connections;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import vanichat.classes.main.InputReader;
import vanichat.classes.main.OutputWriter;
import vanichat.classes.main.Usuario;
import vanichat.classes.server.ServerChat;
import vanichat.classes.server.ServerHandlers;
import vanichat.network.server.connections.handlers.groups.MessageConnectionHandler;
import vanichat.network.server.main.ChatServer;

public class ChatGroupConnection extends ChatClientConnection{
    
    private final Usuario user;
    private final ServerChat chat;

    public ChatGroupConnection(Socket socket, ChatServer listener, InputReader reader, OutputWriter writer, Usuario user, ServerChat chat) throws IOException {
        super(socket, listener, reader, writer);
        this.chat = chat;
        this.user = user;
        System.out.printf("User %s Connection Delegated for Chat %d%n",user.getUsername(),chat.getID());
    }

    @Override
    public void handleReceivedBytes() throws IOException{
        ServerHandlers handler = new ServerHandlers(new MessageConnectionHandler(this));
        new Thread(handler).start();
    }

    public Usuario getUser() {
        return user;
    }

    public ServerChat getChat() {
        return chat;
    }
    
}
