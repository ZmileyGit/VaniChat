
import java.io.IOException;
import server.ChatListener;
import server.Server;

public class Main {
    public static void main(String... args){
        try {
            Server server = new Server(new ChatListener(8080));
            server.start();
        } catch (IOException ex) {
            System.out.println("No se pudo iniciar el Servidor");
        }
    }
}
