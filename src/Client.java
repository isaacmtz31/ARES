
import java.net.*;
import java.net.MulticastSocket;
/* @author Isaac */
public class Client extends Thread
{
    /*Datos de la conexion*/
    private int MCAST_PORT;
    private int DGRAM_BUF_LEN = 1024;
    private String MCAST_ADDR  = "228.1.1.1";  
    
    /*Recursos compartido y de id*/
    private volatile int indxClient;    
    private TablaHash<String[]> recursoCompartido;
    
    public Client(int puerto, int indxClient, TablaHash<String[]> recursoCompartido)
    {       
        this.MCAST_PORT = puerto;
        this.indxClient = indxClient;        
        this.recursoCompartido = recursoCompartido;
    }
    
    public synchronized void receptorAnuncios() {
        InetAddress gpo = null;       
        try
        {
            MulticastSocket cl= new MulticastSocket(MCAST_PORT);
            System.out.println("Cliente escuchando puerto #: "+ cl.getLocalPort());
            cl.setReuseAddress(true);
            try{
                gpo = InetAddress.getByName(MCAST_ADDR);
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }//catch
            cl.joinGroup(gpo);
            System.out.println("Unido al grupo");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[DGRAM_BUF_LEN],DGRAM_BUF_LEN);
                cl.receive(p);
                //System.out.println("Datagrama recibido..");
                String msj = new String(p.getData());                                
                System.out.println("Cliente: "+ indxClient + "\n MSJ:\t" + msj);                
                formatearMsj(msj);
               
            }//for
            
        }catch(Exception e){
            
        }//catch  
    }
    public void formatearMsj(String mensaje)
    {
        long length = mensaje.length();
        String[] recurso = mensaje.split("¬");    
        String[] subrecurso = null;
        for (int i = 0; i < recurso.length ;i++) {
            //System.out.println("Recursos-->" + recurso[i]);
            subrecurso = recurso[i].split("&&");                       
            for(int j = 0; j < subrecurso.length; j++) {
                System.out.println("Subrecurso: " + subrecurso[j]);
            }
        }
        

        
    }
    @Override
    public void run() {
        receptorAnuncios();
    }
}
