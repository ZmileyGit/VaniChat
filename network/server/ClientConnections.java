
package network.server;

import structure.main.Usuario;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import structure.messages.Mensaje;

public class ClientConnections {
    private final HashMap<Usuario,ClientConnection> CONNECTIONS = new HashMap<>();
    
    public synchronized Usuario addClient(String username, ClientConnection client){
        Usuario nuevo = new Usuario(username);
        CONNECTIONS.put(nuevo,client);
        return nuevo;
    }
    
    protected synchronized void broadcast(Mensaje message){
        CONNECTIONS.keySet().stream().filter((Usuario user)->{
            return !user.equals(message.getUser());
        }).forEach((Usuario user)->{
            Socket socket =  CONNECTIONS.get(user).getSocket();
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.write(message.getTipo());
                out.writeShort(message.getData().length);
                out.write(message.getData());
                byte[] usr = message.getUser().getUsername().getBytes(Server.CHARSET);
                byte[] usrfield = new byte[64];
                int cont = 0;
                for(cont = 0 ; cont < usrfield.length; cont+=2){
                    usrfield[cont] = cont < usr.length ? usr[cont] : -2;
                    usrfield[cont+1] = cont < usr.length ? usr[cont+1] : -1;
                }
                out.write(usrfield);
            } catch (IOException ex) {
                System.out.println("Error de envío a :" + user.getUsername());
            }            
        });
    }
    
}
