package server.main;


import java.io.IOException;
import server.network.ChatServer;
import server.network.Server;

public class Main {
    public static void main(String... args){
        try {
            Server server = new ChatServer();
            server.start();
        } catch (IOException ex) {
            System.out.println("No se pudo iniciar el Servidor");
        }
    }
}
