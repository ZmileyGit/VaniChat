
package vanichat.network.server.connections.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.LinkedList;
import vanichat.classes.main.Usuario;
import vanichat.classes.server.ServerChat;
import vanichat.classes.server.ServerSession;
import vanichat.config.Configuration;
import vanichat.network.server.connections.ChatClientConnection;

public class GroupConnectionHandler extends ConnectionHandler{

    public final static byte HANDLED = Configuration.GROUP;
    
    protected GroupConnectionHandler(ChatClientConnection connection) {
        super(new ChatLinkConnectionHandler(connection),connection);
        this.handled = HANDLED;
    }

    public LinkedList<Usuario> getUsersFromString(String usernames){
        String[] usrsep = usernames.split(Configuration.USERNAME_SEPARATOR);
        LinkedList<String> cleanusrs = cleanUsernames(usrsep);
        LinkedList<Usuario> users = new LinkedList<>();
        cleanusrs.stream().forEach((String usr)->{
            Usuario user = connection.getListener().getSessions().searchUsername(usr);
            if(user != null){
                users.push(user);
            }
        });
        return users;
    }
    
    public LinkedList<String> cleanUsernames(String[] usernames){
        LinkedList<String> users = new LinkedList<>();
        for(int cont=0; cont < usernames.length; cont+=1){
            if(usernames[cont] != null){
                usernames[cont] = usernames[cont].trim();
                if(!usernames[cont].isEmpty()){
                    users.push(usernames[cont]);
                }
            }
        }
        return users;
    }
    
    @Override
    protected void handleBehavior(ByteBuffer buffer) throws IOException {
        CharBuffer usrbuffer = Configuration.DEFAULT_CHARSET.newDecoder().decode(buffer);
        String usrstr = usrbuffer.toString().trim();
        
        LinkedList<Usuario> users = getUsersFromString(usrstr);
        ServerSession root = connection.getListener().getSessions().searchSessionByConnection(connection);
        users.push(root.getUser());
        
        String ustr = users.stream().reduce("", (String s,Usuario user)->{
            return s + String.format("\t- User %s%n",user.getUsername());
        }, (String a,String r)->{
            return a + r;
        });
        System.out.printf("User %s requested a Group with:%n%s",root.getUser().getUsername(),ustr);
        
        Integer cid = connection.getListener().getChats().addChatGroup(users);
        System.out.printf("Chat Group created with ID %d%n",cid);
        
        ServerChat chat = connection.getListener().getChats().searchChatByID(cid);
        usrstr = chat.getUsers().toString();
        byte[] info = usrstr.getBytes(Configuration.DEFAULT_CHARSET);

        chat.getUsers().getIterableUserList().stream().forEach((Usuario user)->{
            ServerSession session = connection.getListener().getSessions().searchSessionByUsername(user.getUsername());
            ByteBuffer response = ByteBuffer.allocate(4 + 1 + 4 + info.length);
            response.putInt(1 + 4 + info.length);
            response.put(HANDLED);
            response.putInt(cid);
            response.put(info);
            response.position(0);
            try {
                session.getConnection().getWriter().write(response);
            } catch (IOException ex) {
                System.out.printf("Couldn't reach User %s for Chat %d%n", user.getUsername(),chat.getID());
                ex.printStackTrace();
            }
        });
        
    }
    
}
