
package test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

public class Session {
    
    public static void main(String... args) throws IOException{
        Socket socket = new Socket("127.0.0.1",1234+5);
        DataOutputStream data = new DataOutputStream(socket.getOutputStream());
        data.writeByte(2);
        String user =  "Ruben:Lety";
        byte[] buser = user.getBytes(Charset.forName("UTF-16"));
        data.writeShort(buser.length);
        data.write(buser);
        socket.close();
    }
    
}
