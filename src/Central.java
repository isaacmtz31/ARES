import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
 

/* @author Isaac */

public class Central extends Thread 
{    
    
    public final static int CENTRAL_PORT = 8000;
    public final static int MAX_PACKET_SIZE = 2000;      
    public final static String CENTRAL_HOST = "127.0.0.2";
    private final static TablaHash<String[]> hash = new TablaHash<>();    
    
    public synchronized String[] obtenerNombreArchivo(int[] posicionProbable)
    {
        String[] auxNombre = new String[posicionProbable.length]; //Es probable que se encuentre es mas de 1
        for(int i = 0; i < posicionProbable.length; i++)
        {
            //Obtener el recurso padre.
            if(hash.get(posicionProbable[i]) != null) //Es probable encontrar algo
            {
               auxNombre[i] = hash.get(i)[0];
            }
        }        
        return auxNombre;
    }
    
    public synchronized String[] obtenerMD5Archivo(int[] posicionProbable)
    {
        String[] auxMD5 = new String[posicionProbable.length]; //Es probable que se encuentre es mas de 1
        for(int i = 0; i < posicionProbable.length; i++)
        {
            //Obtener el recurso padre.
            if(hash.get(posicionProbable[i]) != null) //Es probable encontrar algo
            {
               auxMD5[i] = hash.get(i)[0];
            }
        }        
        return auxMD5;
    }
    
    
    public synchronized String[] obtenerPuerto(int[] posicionProbable)
    {
        String[] auxPuerto = new String[posicionProbable.length]; //Es probable que se encuentre es mas de 1
        for(int i = 0; i < posicionProbable.length; i++)
        {
            //Obtener el recurso padre.
            if(hash.get(posicionProbable[i]) != null) //Es probable encontrar algo
            {
               auxPuerto[i] = hash.get(i)[2];
            }
        }        
        return auxPuerto;
    }    

    
    public synchronized boolean agregarRecurso(String[] estructura, int[] posiciones)
    {
        
        boolean flag = false;
        for(int i = 0; i < posiciones.length; i++)
        {
            if(hash.get(i) != null)
            {
                //Se puede insertar
                hash.add(estructura);
                flag = true;
            }
            else
            {
                continue;
            }
        }
        return flag;
    }
    
     
    
    public static void main(String[] args)
    {
        //String outputFile = "C:\\Users\\Isaac\\Pictures\\obrero2.png";
        try {                
                ServerSocketChannel server = ServerSocketChannel.open();                
                server.configureBlocking(false); 
                server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                server.socket().bind(new InetSocketAddress(CENTRAL_HOST, CENTRAL_PORT));                
                Selector selector = Selector.open();
                server.register(selector, SelectionKey.OP_ACCEPT);                
                System.out.println("---------- SERVIDOR INICIADO ----------"); 
                while(true) 
                {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) 
                    {
                        SelectionKey key = (SelectionKey) iterator.next();
                        iterator.remove();
                        if ( key.isAcceptable() )
                        {						
                            SocketChannel client = server.accept();
                            System.out.println("\n\n---------- NUEVA CONEXIÓN ACEPTADA ----------"); 
                            System.out.println( "\t\t> PUERTO: " + client.socket().getPort() + " Y DIRECCIÓN: " + client.socket().getInetAddress().getHostAddress() );                                                
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);						
                            continue;
                        }					                                                
                        if ( key.isReadable() )                             
                        {
                            SocketChannel ch =( SocketChannel) key.channel();
                            ByteBuffer b = ByteBuffer.allocate(MAX_PACKET_SIZE);
                            b.clear();
                            int n = ch.read(b);
                            b.flip();
                            String recursos = new String( b.array(), 0, n );                              
                            String respuesta = " CORROBORAR RECURSOS RECIBIDOS ";
                            if( hash.formatearMsj(recursos) ){
                                respuesta = "RECURSOS BIEN RECIBIDOS";
                                
                            }                                
                            ByteBuffer b2=ByteBuffer.wrap(respuesta.getBytes());
                            ch.write(b2);
                            continue;                            
                        }
                    }			
                }
        } catch (Exception e) {
                e.printStackTrace();
        }	
    }
}
