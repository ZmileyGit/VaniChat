
package vanichat.classes.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OutputWriter {
    private final DataOutputStream stream;
    private final Lock write = new ReentrantLock();
    
    public OutputWriter(DataOutputStream stream){
        this.stream = stream;
    }
    
    public OutputWriter(OutputStream stream){
        this(new DataOutputStream(stream));
    }
    
    public void write(ByteBuffer buffer) throws IOException{
        write.lock();
        buffer.position(0);
        try{
            for(int cont = 0; cont < buffer.capacity(); cont+=1){
                byte b = buffer.get();
                stream.write(b);
            }
            stream.flush();
        }finally{
            write.unlock();
        }
    }
}
