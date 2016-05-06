package vanichat.network.server.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.UUID;
import org.hibernate.criterion.Restrictions;


public class Conversacion {
    
    private int id;
    private String hashID;
    
    public Conversacion(){
        
    }
    
    public Conversacion(int id){
        this.id = id;
        this.hashID = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public Conversacion(int id, String hashID) {
        this.id = id;
        this.hashID = hashID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHashID() {
        return hashID;
    }

    public void setHashID(String hashID) {
        this.hashID = hashID;
    }
    
    public void generateHashID(){
        this.hashID = UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static void guardarConversacion(Conversacion conversacion){
        try{
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            Session sesion = factory.openSession();
            Transaction t = null;
            try{
                t = sesion.beginTransaction();
                sesion.save(conversacion);
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
    
    public static Conversacion get(int id){
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            Session sesion = factory.openSession();
            List<Conversacion> conversaciones = sesion.createCriteria(Conversacion.class).list();
            sesion.close();
            for(Conversacion conversacion : conversaciones){
                return conversacion;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getConversacion(String hashID){
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            Session sesion = factory.openSession();
            List<Conversacion> conversaciones = sesion.createCriteria(Conversacion.class).add(Restrictions.eq("hashID", hashID)).list();

            for(Conversacion conversacion : conversaciones){
                String json = "{\n\"conversacion\":{\n\"mensajes\":[\n%s\n]\n}\n}";
                List<Mensaje> mensajes = sesion.createCriteria(Mensaje.class).add(Restrictions.eq("id_conversacion", conversacion.id)).list();
                sesion.close();
                String m = "";
                for(int i=0;i<mensajes.size();i++){
                    if(i==mensajes.size()-1){
                        m+=String.format("{\"fecha\":\"%s\",\"mensaje\":\"%s\"}", mensajes.get(i).getReceivedDateTime().toString(),new String(mensajes.get(i).getContenido(),vanichat.config.Configuration.DEFAULT_CHARSET));
                    }else{
                        m+=String.format("{\"fecha\":\"%s\",\"mensaje\":\"%s\"},", mensajes.get(i).getReceivedDateTime().toString(),new String(mensajes.get(i).getContenido(), vanichat.config.Configuration.DEFAULT_CHARSET));
                    }
                }
                json = String.format(json,m);
                return json;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    
}
