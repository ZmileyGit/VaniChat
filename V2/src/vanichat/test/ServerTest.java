
package vanichat.test;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import vanichat.classes.main.InputReader;
import vanichat.classes.main.OutputWriter;
import vanichat.config.Configuration;
import vanichat.network.server.main.ChatServer;
import vanichat.network.server.main.Server;

public class ServerTest {

    
    public static Integer registerUsername(String username) throws IOException{
        Socket socket = new Socket(Configuration.SERVER_IP_ADDRESS,Configuration.SERVER_PORT);
        OutputWriter data = new OutputWriter(socket.getOutputStream());
        String user =  username;
        byte[] buser = user.getBytes(Configuration.DEFAULT_CHARSET);
        ByteBuffer buff = ByteBuffer.allocate(4 + 1 + buser.length);
        buff.putInt(buser.length + 1);
        buff.put(Configuration.LOGIN);
        buff.put(buser);
        data.write(buff);
        InputReader in = new InputReader(socket.getInputStream());
        ByteBuffer buffin = in.read();
        buffin.position(1);
        Integer token = buffin.getInt();
        System.out.printf("Username: %s Token: %d%n",user,token);
        return token;
    }
    
    public static Socket connectToServer(Integer token) throws IOException{
        Socket socket = new Socket(Configuration.SERVER_IP_ADDRESS,Configuration.SERVER_PORT);
        OutputWriter data = new OutputWriter(socket.getOutputStream());
        ByteBuffer buff = ByteBuffer.allocate(4 + 1 + 4);
        buff.putInt(4 + 1);
        buff.put(Configuration.USERLINK);
        buff.putInt(token);
        data.write(buff);

        InputReader in = new InputReader(socket.getInputStream());
        ByteBuffer buffin = in.read();
        buffin.position(1);
        Byte response = buffin.get();
        if(response > 0){
            System.out.printf("Couldn't Connect to Server%n");
        }else{
            System.out.printf("Succesfully Connected to Server%n");
        }
        return socket;
    }
    
    public static Integer requestGroup(OutputWriter data,InputReader in,String usernames) throws IOException{
        String user =  usernames;
        byte[] buser = user.getBytes(Configuration.DEFAULT_CHARSET);
        
        ByteBuffer buff = ByteBuffer.allocate(4 + 1 + buser.length);
        buff.putInt(buser.length + 1);
        buff.put(Configuration.GROUP);
        buff.put(buser);
        data.write(buff);
        
        ByteBuffer buffin = in.read();
        buffin.position(1);
        Integer cid = buffin.getInt();
        System.out.printf("Created Chat %d succesfully with Users: %s%n",cid,Configuration.DEFAULT_CHARSET.newDecoder().decode(buffin));
        return cid;
    }
    
    public static Socket connectToChat(Integer token,Integer cid) throws IOException{
        Socket socket = new Socket(Configuration.SERVER_IP_ADDRESS,Configuration.SERVER_PORT);
        OutputWriter data = new OutputWriter(socket.getOutputStream());
        ByteBuffer buff = ByteBuffer.allocate(4 + 1 + 4 + 4);
        buff.putInt(1 + 4 + 4);
        buff.put(Configuration.CHATLINK);
        buff.putInt(cid);
        buff.putInt(token);
        data.write(buff);

        InputReader in = new InputReader(socket.getInputStream());            
        ByteBuffer buffin = in.read();
        buffin.position(1);
        Byte response = buffin.get();
        Integer chat = buffin.getInt();           
        if(response > 0){
            System.out.printf("Couldn't Connect to Chat %d Code: %d%n",chat,response);
        }else{
            System.out.printf("%d - Succesfully Connected to Chat %d%n",token,chat);
        }
        return socket;
    }
    
    public static Integer sendMessage(Socket socket, String message) throws IOException{
        InputReader in = new InputReader(socket.getInputStream());
        OutputWriter data = new OutputWriter(socket.getOutputStream());
        byte[] buser = message.getBytes(Configuration.DEFAULT_CHARSET);
        ByteBuffer buff = ByteBuffer.allocate(4 + 1 + buser.length);
        buff.putInt(buser.length + 1);
        buff.put(Configuration.MESSAGE);
        buff.put(buser);
        data.write(buff);
        
        ByteBuffer buffer = in.read();
        buffer.position(1);
        Integer cid = buffer.getInt();
        Integer largo =  buffer.getInt();
        ByteBuffer root = ByteBuffer.allocate(largo);
        for(int cont=0; cont < root.capacity(); cont+=1){
            root.put(buffer.get());
        }
        root.position(0);
        CharBuffer usrchar = Configuration.DEFAULT_CHARSET.newDecoder().decode(root);
        String user = usrchar.toString().trim();
        
        CharBuffer messchar = Configuration.DEFAULT_CHARSET.newDecoder().decode(buffer);
        String mess = messchar.toString().trim();
        
        System.out.printf(" %s -> %s%n", user, mess);
        return 0;
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread s = new Thread(()->{
            Server server;
            try {
                server = ChatServer.newInstance(Configuration.SERVER_PORT);
                server.start();
            } catch (IOException ex) {
                System.out.println("Failed to Initialize Server");
            }
        });
                
        s.start();
        
        Thread.sleep(3000);
        
        Integer tokenL = registerUsername("Lety");
        Socket socketL = connectToServer(tokenL);
        
        Integer tokenE = registerUsername("Ruby");
        Socket socketE = connectToServer(tokenE);
        OutputWriter outE = new OutputWriter(socketE.getOutputStream());
        InputReader inE = new InputReader(socketE.getInputStream());
        Integer cidLE = requestGroup(outE,inE,"Lety");
        
        Socket SR = connectToChat(tokenE,cidLE);
        sendMessage(SR,"Hola Lety");
        
        Socket SL = connectToChat(tokenL,cidLE);
        
        Integer token = registerUsername("Erick");
        Socket socket = connectToServer(token);
        OutputWriter out = new OutputWriter(socket.getOutputStream());
        InputReader in = new InputReader(socket.getInputStream());
        Integer cid = requestGroup(out,in,"Ruby");

        s.join();
        
    }
    
}
