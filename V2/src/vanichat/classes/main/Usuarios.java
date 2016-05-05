
package vanichat.classes.main;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import vanichat.config.Configuration;

public class Usuarios {
    private final LinkedList<Usuario> users = new LinkedList<>();
    private final Lock lock;
    
    public Usuarios(Lock lock){
        this.lock = lock;
    }
    
    public boolean addUser(Usuario user){
        lock.lock();
        try{
            if(users.contains(user)){
                return false;
            }
            users.push(user);
            return true;
        }finally{
            lock.unlock();
        }
    }
    
    public void addUserList(final Collection<Usuario> users){
        lock.lock();
        try{
            users.stream().forEach((Usuario user)->{
                if(!this.users.contains(user)){
                    this.users.push(user);
                }
            });
        }finally{
            lock.unlock();
        }
    }
    
    public Usuario searchByUsername(String username){
        lock.lock();
        try{
            Optional<Usuario> user = users.stream().filter((Usuario usr)->{
                return usr.getUsername().equals(username);
            }).findFirst();
            return user.orElse(null);
        }finally{
            lock.unlock();
        } 
    }
    
    @Override
    public String toString(){
        lock.lock();
        try{
            String str = users.stream().reduce("", (String s,Usuario user)->{
                return s + Configuration.USERNAME_SEPARATOR + user.getUsername();
            }, (String accum,String res)->{
                return accum + Configuration.USERNAME_SEPARATOR + res;
            });
            if(str.isEmpty()){
                return str;
            }
            return str.substring(1);
        }finally{
            lock.unlock();
        }
    }
    
    public LinkedList<Usuario> getIterableUserList(){
        lock.lock();
        try{
            LinkedList<Usuario> users = new LinkedList<>();
            this.users.stream().forEach((Usuario user)->{
                users.push(user);
            });
            return users;
        }finally{
            lock.unlock();
        }
    }
}
