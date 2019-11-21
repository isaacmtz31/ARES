import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*@author Isaac*/
public class Anuncios extends Thread
{   
    
    InetAddress gpo = null;
    private int indxServidor; 
    private String mensaje = "";    
    private int MCAST_PORT;
    private int DGRAM_BUF_LEN = 1024;
    private String MCAST_ADDR  = "228.1.1.1";  	    
    private MulticastSocket servidorAnuncios;
    ArrayList<String> recursosDisponibles;
    //
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    public Anuncios() {
    }
        
    public Anuncios(int puerto, int indxServidor){        
        this.indxServidor = indxServidor;
        this.MCAST_PORT = puerto;
        try{
            servidorAnuncios = new MulticastSocket(MCAST_PORT);
            servidorAnuncios.setReuseAddress(true);
            servidorAnuncios.setTimeToLive(128);
            recuperaRecursos();
            try{
                gpo = InetAddress.getByName(MCAST_ADDR);
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }
            servidorAnuncios.joinGroup(gpo);    
            run();                        
        }catch(Exception e){
            
        }//catch
    }
    
   private void recuperaRecursos()
   {    
       String x = "";
       MD5 md5 = new MD5();
       try {           
           //File f = new File("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+(indxServidor) );           
           File f = new File("C:\\Users\\lenovo\\Desktop\\ARES\\server-"+(indxServidor) );           
           recursosDisponibles = new ArrayList(Arrays.asList(f.list()));
           for(int i = 0; i < recursosDisponibles.size(); i++)
           {          
               String aux = recursosDisponibles.get(i);
               try {
                  //x = md5.getMD5Checksum("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+(indxServidor)+"\\"+aux);
                  x = md5.getMD5Checksum("C:\\Users\\lenovo\\Desktop\\ARES\\server-"+(indxServidor)+"\\"+aux);
               } catch (Exception e) {
                   e.printStackTrace();
               }               
               mensaje += aux +"&&"+ x + "&&" + "From Server:"+indxServidor+"¬";               
           }           
       } catch (Exception e) {
       }
       
   }
    
    @Override
    public void run()
    {        
        byte[] b = new byte[DGRAM_BUF_LEN];
        b = mensaje.getBytes();
        int i = 0;
        for(;;)
        {                        
            System.out.println("-------------------------------------"+i+"-------------------------------------------");
            DatagramPacket paquete = new DatagramPacket(b,b.length,gpo,MCAST_PORT);
            try 
            {                   
                servidorAnuncios.send(paquete);                          
                System.out.println("Enviando mis recursos");                          
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println("Problema enviando el archivo");
            }            
            i++;
        }
    }
    
}
