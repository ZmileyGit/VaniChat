package vanichat.network.server.db;

import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Mensaje {
    
    private int id;
    private int id_conversacion;
    private Date receivedDateTime;
    private byte[] contenido;
    
    public Mensaje(){
        
    }

    public Mensaje(int id, int id_conversacion, Date receivedDateTime, byte[] contenido) {
        this.id = id;
        this.id_conversacion = id_conversacion;
        this.receivedDateTime = receivedDateTime;
        this.contenido = contenido;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_conversacion() {
        return id_conversacion;
    }

    public void setId_conversacion(int id_conversacion) {
        this.id_conversacion = id_conversacion;
    }

    public Date getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(Date receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
    public static void guardarMensaje(Mensaje mensaje){
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            Session sesion = factory.openSession();
            Transaction t = null;
            try{
                t = sesion.beginTransaction();
                sesion.save(mensaje);
                t.commit();
            }catch(Exception e){
                if(t!=null){
                    t.rollback();
                }
            }
            finally{
                sesion.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
