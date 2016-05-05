
package vanichat.classes.main;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InputReader {
    private final DataInputStream stream;
    private final Lock read = new ReentrantLock();
    private final AtomicBoolean reading = new AtomicBoolean(false);
    
    public InputReader(DataInputStream stream){
        this.stream = stream;
    }
    
    public InputReader(InputStream stream){
        this(new DataInputStream(stream));
    }
    
    public ByteBuffer read() throws IOException{
        read.lock();
        reading.set(true);
        int bytes = stream.readInt();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        for(int cont = 0; cont < bytes; cont+=1){
            buffer.put(stream.readByte());
        }
        buffer.position(0);
        try{
            return buffer;
        }finally{
            read.unlock();
            reading.set(false);
        }
    }
    
    public boolean isReading(){
        return reading.get();
    }
    
    public int bytesAvailable() throws IOException{
        read.lock();
        try{
            return stream.available();
        }finally{
            read.unlock();
        }
    }
}
