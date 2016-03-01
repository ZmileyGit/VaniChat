
package network.client;

import structure.main.Usuario;

public class Client {
    private final Usuario usuario;
    protected final ChatConnector connector;

    public Client(Usuario usuario, ChatConnector connector) {
        this.usuario = usuario;
        this.connector = connector;
    }
    
    public void connect(){
        connector.run();
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    public void stop(){
        connector.stop();
    }
}
