
package vanichat.classes.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import vanichat.classes.main.Usuario;

public class ServerChats {
    private final HashMap<Integer,ServerChat> chats = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    
    public Integer addChatGroup(Collection<Usuario> users){
        lock.lock();
        try{
            Integer token = chats.size() +1;
            ServerChat chat = new ServerChat(token);
            chats.put(token,chat); 
            if(users != null){
                chat.getUsers().addUserList(users);
            }
            return token;          
        }finally{
            lock.unlock();
        }
    }
    
    public ServerChat searchChatByID(Integer ID){
        lock.lock();
        try{
            return chats.get(ID);
        }finally{
            lock.unlock();
        }
    }
}
