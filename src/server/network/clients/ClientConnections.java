
package server.network.clients;

import server.classes.user.Usuario;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientConnections {
    private final HashMap<Usuario,Integer> ids = new HashMap<>();
    private final HashMap<Integer,Usuario> usuarios = new HashMap<>();
    private final HashMap<Usuario,ChatClientConnection> connections = new HashMap<>();
    private final HashMap<ChatClientConnection,Usuario> links = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger(1);
    
    public synchronized Usuario addClient(String username){
        Usuario nuevo = new Usuario(username,counter.getAndAdd(1));
        Integer connection = getUserID(username);
        if(connection == null){
            ids.put(nuevo,nuevo.getServerID());
            usuarios.put(nuevo.getServerID(), nuevo);
        }else{
            nuevo = null;
        }
        return nuevo;
    }
    
    public synchronized Usuario getUser(Integer id){
        return usuarios.get(id);
    }
    
    public synchronized Integer getUserID(String username){
        return ids.get(new Usuario(username,-1));
    }
    
    public synchronized void setClientConnection(Usuario usuario,ChatClientConnection connection){
        System.out.println("Connection with " + usuario.getUsername() + " has been established");
        connections.put(usuario, connection);
        links.put(connection, usuario);
    }
    
    public synchronized ChatClientConnection getClientConnection(Usuario usuario){
        return connections.get(usuario);
    }
    
    public synchronized Usuario getUser(ChatClientConnection connection){
        return links.get(connection);
    }
    
}
