
package test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

public class Main {


    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1",1234);
        DataOutputStream data = new DataOutputStream(socket.getOutputStream());
        data.writeByte(0);
        String user =  "Lety";
        byte[] buser = user.getBytes(Charset.forName("UTF-16"));
        data.writeShort(buser.length);
        data.write(buser);
        socket.close();
        
        socket = new Socket("127.0.0.1",1234+5);
        data = new DataOutputStream(socket.getOutputStream());
        data.writeByte(0);
        user =  "Erick";
        buser = user.getBytes(Charset.forName("UTF-16"));
        data.writeShort(buser.length);
        data.write(buser);
        socket.close();
        
        socket = new Socket("127.0.0.1",1234+10);
        data = new DataOutputStream(socket.getOutputStream());
        data.writeByte(0);
        user =  "Ruben";
        buser = user.getBytes(Charset.forName("UTF-16"));
        data.writeShort(buser.length);
        data.write(buser);
        socket.close();
        
        
    }
    
}
