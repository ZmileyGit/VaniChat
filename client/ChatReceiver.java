package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import server.Server;

public class ChatReceiver extends Receiver{
    
    public ChatReceiver(Socket socket) {
        super(socket);
    }

    @Override
    public void handleReceivedPackets(Socket socket, DataInputStream in) throws IOException {
        if(in.available()!=0){
            byte mode = (byte) in.readByte();
            short length = (short) in.readShort();
            byte[] bytext = new byte[length];
            for(int cont = 0;cont < length;cont+=1){
                bytext[cont] = (byte) in.readByte();
            }
            byte[] user = new byte[64];
            for(int cont = 0; cont < user.length; cont+=1){
                user[cont] = in.readByte();
            }
            if(mode == 1){
                System.out.printf("%s {%s} %n",new String(user,Server.CHARSET).trim(),new String(bytext,Server.CHARSET));
            }
        }else{
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {}
        }
    }

    @Override
    public void handleReceivedException(Exception e) {
        System.out.println("Error receiving packets");
    }
    
}
