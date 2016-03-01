
package server;

import estructura.Usuario;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import mensajes.Texto;

public class ChatClientConnection extends ClientConnection{
    
    private Usuario user;
    
    public ChatClientConnection(Socket socket,ChatListener source){
        super(socket,source);
    }
    
    @Override
    public void handleClientPackets(Socket socket, DataInputStream in) throws IOException{
        
        if(in.available()!=0){
            byte mode = (byte) in.readByte();
            
            short length = (short) in.readShort();
            
            byte[] bytext = new byte[length];
            for(int cont = 0;cont < length;cont+=1){
                bytext[cont] = (byte) in.readByte();
            }
            
            //Decidir tarea de acuerdo al tipo del envío
            switch(mode){
                case 0:
                    user = ((ChatListener)source).CLIENTES.addClient(new String(bytext,Server.CHARSET), this);
                    System.out.println("Nuevo usuario-> " + user.getUsername());
                    break;
                case 1:
                    Texto message = new Texto(bytext,user);
                    new Thread(new BroadcastTest(message,(ChatListener)source)).start();
                    break;
            }   
        }else{
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {}
        }
        
    }
    
    @Override
    public void handleClientError(Socket socket, Exception e) {
        System.out.println("Client Error");
    }
    
}
