
package vanichat.classes.server;

import java.util.LinkedList;
import vanichat.classes.main.Chat;
import vanichat.classes.main.Usuario;
import vanichat.network.server.connections.ChatGroupConnection;

public class ServerChat extends Chat{
    
   private final LinkedList<ChatSession> sessions = new LinkedList<>();
    
    public ServerChat(Integer ID) {
        super(ID);
    }
    
    public boolean userIsAllowedInChat(Usuario user){
        slock.lock();
        try{
            if(users.searchByUsername(user.getUsername()) == null){
                System.out.printf("User %s is not allowed in Chat %d%n",user.getUsername(),ID);
                return false;
            }
            return true;
        }finally{
            slock.unlock();
        }
    }
    
    public boolean registerUserConnection(Integer token,Usuario user,ChatGroupConnection connection){
        slock.lock();
        userIsAllowedInChat(user);
        try{
            ChatSession session = new ChatSession(token,user,connection);
            if(sessions.contains(session)){
                System.out.printf("User %s is already linked to Chat %d%n",user.getUsername(),ID);
                return false;
            }
            sessions.push(session);
            return true;
        }finally{
            slock.unlock();
        }
    }
    
    public LinkedList<ChatSession> getIterableSessionList(){
        slock.lock();
        try{
            LinkedList<ChatSession> sessions =  new LinkedList<>();
            this.sessions.stream().forEach(sessions::push);
            return sessions;
        }finally{
            slock.unlock();
        }
    }
    
}
