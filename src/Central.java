
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;

 

/* @author Isaac */

public class Central extends Thread 
{    
    public final static int CENTRAL_PORT = 8000;
    public final static int MAX_PACKET_SIZE = 65000;      
    public final static String CENTRAL_HOST = "127.0.0.1";
    private final static TablaHash<String[]> hash = new TablaHash<>();    
       
    public static void main(String[] args){
        SocketAddress host;
        int client_port = 0;
        try{
            /*We open a datagram channel*/
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                        
            /*We bind our server*/
            DatagramSocket socket = channel.socket();
            SocketAddress address = new InetSocketAddress(CENTRAL_HOST, CENTRAL_PORT);
            socket.bind(address);
            System.out.println("\n---------- THE CENTRAL HAS BEEN INITIALIZED ----------\n"); 

            
            Selector selector = Selector.open();
            channel.register(selector,SelectionKey.OP_READ);
            ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
            selector.select();
            
            while(true)
            {
                selector.select(5000);               
                Iterator<SelectionKey>it = selector.selectedKeys().iterator();            
                while( it.hasNext() ){
                    SelectionKey key = (SelectionKey)it.next();
                    it.remove();
                    if( key.isWritable() ) {
                        /*
                        buffer.clear();
                        buffer.putInt(++n);
                        buffer.flip();
                        channel.send(buffer, remote);
                        System.out.println("Escribiendo el dato: "+n);
                        continue;*/
                    }else if( key.isReadable() ){
                       
                       buffer.clear();
                       SocketAddress client = channel.receive(buffer);
                       
                       if(buffer.hasArray()){
                           
                           buffer.flip();
                           String recursos = new String( buffer.array(), 0, buffer.limit());                                
                           String petition = formatearMsj(recursos,0);
                           
                           if(petition.equals("2"))
                           {                               
                               String nR = newResource(recursos);
                               client_port = hash.formatearMsj(nR);                               
                               hash.imprimirTablaHash();
                               String mesage = "RESOURCES RECIEVED FROM: " + client_port; //2 for server response
                               ByteBuffer bufferT = ByteBuffer.wrap(mesage.getBytes());
                               channel.send(bufferT, new InetSocketAddress(CENTRAL_HOST, client_port));
                           }else
                           {
                               ByteArrayOutputStream bos = new ByteArrayOutputStream();
                               ObjectOutputStream oos = new ObjectOutputStream(bos);
                               ArrayList<String[]> nodes = hash.searchFile(recursos);
                               oos.writeObject(nodes);
                               ByteBuffer bufferT = ByteBuffer.wrap(bos.toByteArray());
                               channel.send(bufferT, new InetSocketAddress(CENTRAL_HOST, Integer.parseInt(formatearMsj(recursos, 2))));
                           }
                       }
                       else{
                           System.out.println("IT HASN'T ARRAY");
                       }                       
                       continue; 
                    }
               }                
            }
      }catch(Exception e){
        System.out.println("GENERAL EXCEPTION");
	e.printStackTrace();
      }

    }
    
    private static String formatearMsj(String mensaje, int idx)
    {                                          
        String[] respuestas = mensaje.split("&&");           
        return respuestas[idx];
    }
    
    private static String newResource(String resource){
        
        String recurso = "";
        String[] recursos = resource.split("¬");
        
        for(int i = 0; i< recursos.length; i++)        
            recurso += recursos[i].substring(recursos[i].indexOf("&&")+2, recursos[i].length()) + "¬";
                
        return recurso;        
    }
}
