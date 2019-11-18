
import java.net.*;
import java.net.MulticastSocket;
/* @author Isaac */
public class Client extends Thread
{
    private int MCAST_PORT;
    private int DGRAM_BUF_LEN = 1024;
    private String MCAST_ADDR  = "228.1.1.1";  
    private volatile int indxClient;

    public Client(int puerto, int indxClient)
    {

        this.MCAST_PORT = puerto;
        this.indxClient = indxClient;
        
    }
    
    public void receptorAnuncios() {
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
                System.out.println("Datagrama recibido..");
                String msj = new String(p.getData());                
                System.out.println("--------------------------------------------------------------------------------");
                System.out.println("Cliente: "+ indxClient + "\n\t MSJ" + msj);
                System.out.println("--------------------------------------------------------------------------------");
               
            }//for
            
        }catch(Exception e){
            
        }//catch  
    }

    @Override
    public void run() {
        receptorAnuncios();
    }
}
