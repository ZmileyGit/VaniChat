package client.main;

import client.service.ChatService;
import client.service.Service;
import java.io.IOException;

public class Main {
    
    
    public static void main(String... args) throws InterruptedException{
        try {
            new Thread(new ChatService(ChatService.DEFAULT_PORT)).start();
            new Thread(new ChatService(ChatService.DEFAULT_PORT+5)).start();
            Service chat = new ChatService(ChatService.DEFAULT_PORT+10);
            chat.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
