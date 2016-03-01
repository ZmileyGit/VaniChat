package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;
import mensajes.Texto;
import server.Server;


public class Main {
    public static void main(String... args) throws InterruptedException{
        Scanner scan = new Scanner(System.in);
        try(Socket socket = new Socket("127.0.0.1",8080);){
            
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new Thread(new Receiver(socket.getInputStream())).start();
            System.out.println("Username");
            String username = scan.nextLine();
            byte[] bymessage = username.getBytes(Charset.forName("UTF-16"));
            byte type = 0;
            out.write(type);
            out.writeShort(bymessage.length);
            out.write(bymessage);
            while(true){
                String message;
                System.out.printf("Yo: %s%n",message = scan.nextLine());
                bymessage = message.getBytes(Charset.forName("UTF-16"));
                type = Texto.TYPE_ID;
                out.write(type);
                out.writeShort(bymessage.length);
                out.write(bymessage);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static class Receiver implements Runnable{

        private final DataInputStream in;

        public Receiver(InputStream in) {
            this.in = new DataInputStream(in);
        }
        
        @Override
        public void run() {
            while(true){
                try {
                    if(in.available()!=0){
                        byte mode = (byte) in.readByte();
                        //Decidir tarea de acuerdo al modo o tipo del envío
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
                        Thread.sleep(50);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
    }
}
