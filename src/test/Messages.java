
package test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

public class Messages {

    public static void main(String... args) throws IOException{
        Socket socket = new Socket("127.0.0.1",1234);
        DataOutputStream data = new DataOutputStream(socket.getOutputStream());
        data.writeByte(1);
        data.writeInt(1);
        String mensaje =  "Mensaje de Prueba :P";
        byte[] buser = mensaje.getBytes(Charset.forName("UTF-16"));
        data.writeShort(buser.length);
        data.write(buser);
        socket.close();
    }
    
}
