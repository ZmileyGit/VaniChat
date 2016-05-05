
package vanichat.classes.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import vanichat.classes.main.Usuario;
import vanichat.network.server.connections.ChatClientConnection;

public class ServerSessions {
    private final LinkedList<ServerSession> sessions = new LinkedList<>();
    private final HashMap<Integer,Usuario> tokens = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    
    public Integer addUser(Usuario user){
        lock.lock();
        try{
            if(tokens.containsValue(user)){
                System.out.printf("Repeated Username %s%n",user.getUsername());
                return -1;
            }
            Integer token = tokens.size() + 1;
            tokens.put(token, user);
            System.out.printf("Added %s to Usernames with Token %d%n",user.getUsername(),token);
            return token;
        }finally{
            lock.unlock();
        }
    }
    
    public boolean bindSocketToToken(Integer token,ChatClientConnection connection){
        lock.lock();
        try{
            Usuario user = tokens.get(token);
        
            if(user != null){
                    ServerSession session = new ServerSession(token,user,connection);
                    if(sessions.contains(session)){
                        System.out.printf("Already Linked with User %s%n",user.getUsername());
                        return false;
                    }
                    sessions.push(session);
                    System.out.printf("Linked with User %s via Token %d%n",user.getUsername(),session.getSession_id());
                    return true;
            }
            System.out.printf("Unknown Token %d%n",token);
            return false;
        }finally{
            lock.unlock();
        }
    }
    
    public Usuario searchUsername(final String username){
        lock.lock();
        try{
            Optional<Usuario> usr = tokens.values().stream().filter((Usuario user)->{
                return user.getUsername().equals(username);
            }).findFirst();
            return usr.orElse(null);
        }finally{
            lock.unlock();
        }
    }
    
   public ServerSession searchSessionByUsername(String username){
        lock.lock();
        try{
            Optional<ServerSession> usr = sessions.stream().filter((ServerSession session)->{
                return session.getUser().getUsername().equals(username);
            }).findFirst();
            return usr.orElse(null);
        }finally{
            lock.unlock();
        }
   }
    
    public ServerSession searchSessionByConnection(ChatClientConnection session){
        lock.lock();
        try{
            Optional<ServerSession> se = sessions.stream().filter((ServerSession sess)->{
                return sess.getConnection().equals(session);
            }).findFirst();
            return se.orElse(null);
        }finally{
            lock.unlock();
        }
    }
    
    public Usuario searchUserByToken(Integer token){
        lock.lock();
        try{
            return tokens.get(token);
        }finally{
            lock.unlock();
        }
    }
}
