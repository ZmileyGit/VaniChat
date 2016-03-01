package client;

import estructura.Usuario;
import java.util.Scanner;

public class Main {
    
    
    public static void main(String... args) throws InterruptedException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Username");
        Usuario user = new Usuario(scan.nextLine());
        Client cliente = new Client(user,new ChatConnector("127.0.0.1",8080,user));
        cliente.connect();
    }
    
}
