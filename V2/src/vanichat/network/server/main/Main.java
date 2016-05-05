package vanichat.network.server.main;

import java.io.IOException;

public class Main {
    public static void main(String... args) throws IOException{
        Server server = ChatServer.newInstance(8080);
        server.start();
    }
}
