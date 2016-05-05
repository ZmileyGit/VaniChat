
package vanichat.network.server.connections;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import vanichat.classes.main.InputReader;
import vanichat.classes.main.OutputWriter;
import vanichat.classes.server.ServerHandlers;
import vanichat.network.interfaces.Stoppable;
import vanichat.network.server.connections.handlers.LogInConnectionHandler;
import vanichat.network.server.main.ChatServer;

public class ChatClientConnection extends ClientConnection implements Stoppable{
    
    private final ChatServer server;
    protected final AtomicBoolean run = new AtomicBoolean(true);
    private final InputReader reader;
    private final OutputWriter writer;

    public ChatClientConnection(Socket socket, ChatServer listener) throws IOException  {
        this(socket,listener,new InputReader(socket.getInputStream()),new OutputWriter(socket.getOutputStream()));
    }
    
    public ChatClientConnection(Socket socket, ChatServer listener, InputReader reader, OutputWriter writer) throws IOException  {
        super(socket);
        this.server = listener;
        this.reader = reader;
        this.writer = writer;
    }

    public ChatServer getListener() {
        return server;
    }
    
    @Override
    public void handleClientPackets() throws IOException{
        while(run.get()){
            if(reader.bytesAvailable()!=0){
                handleReceivedBytes();
            }else{
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {}
            }
        }
    }
    
    public void handleReceivedBytes() throws IOException{
        ServerHandlers handler = new ServerHandlers(new LogInConnectionHandler(this));
        new Thread(handler).start();
    }
    
    @Override
    public void handleClientError(Exception e) {
        System.out.println("Client Error");
    }
    
    @Override
    public void stop(){
        run.set(false);
    }
    
    public InputReader getReader(){
        return reader;
    }
    
    public OutputWriter getWriter(){
        return writer;
    }
    
}
