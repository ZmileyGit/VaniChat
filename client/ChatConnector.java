
package client;

import estructura.Usuario;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import mensajes.Texto;
import server.Stoppable;

public class ChatConnector extends Connector implements Stoppable{
    
    private final Usuario user;
    private final AtomicBoolean run = new AtomicBoolean(true);
    private Receiver receiver;

    public ChatConnector(String ip, Integer port, Usuario usuario) {
        super(ip, port);
        this.user = usuario;
        
    }
    
    @Override
    public void stop(){
        run.set(false);
    }
    
    @Override
    public void handleConnectionError(IOException e) {
        System.out.println("Error de conexión");
    }

    @Override
    public void handleConnectionToServer(Socket socket,DataOutputStream out) throws IOException {
            loginToServer(out);
            this.receiver = new ChatReceiver(socket);
            new Thread(receiver).start();
            Scanner scan = new Scanner(System.in);
            while(run.get()){
                String message;
                System.out.printf("Yo: %s%n",message = scan.nextLine());
                byte[] bymessage = message.getBytes(Charset.forName("UTF-16"));
                byte type = Texto.TYPE_ID;
                out.write(type);
                out.writeShort(bymessage.length);
                out.write(bymessage);
            }
    }
    
    public void loginToServer(DataOutputStream out) throws IOException{
        byte[] bymessage = user.getUsername().getBytes(Charset.forName("UTF-16"));
        byte type = 0;
        out.write(type);
        out.writeShort(bymessage.length);
        out.write(bymessage);
    }
    
}
