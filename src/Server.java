
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/* @author Isaac */
public class Server 
{          
     /*Server attributes*/
    private int server_indx;
    private int server_port;    
    private final int CENTRAL_PORT = 8000;
    private final int MAX_PACKET_SIZE = 2000;      
    private final String CENTRAL_HOST = "127.0.0.1";        
    
    /*Attributes for sending to CENTRAL*/    
    private String mesage = "";    
    private ArrayList<String> available_resources;    
    
    public Server(int server_indx, int server_port){        
        this.server_indx = server_indx;
        this.server_port = server_port;
        init();
        
    }   
    
    private void init() {
        SocketAddress host;
        try {            
            /* We open a datagram channel */
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            
            DatagramSocket socket = channel.socket();
            SocketAddress address = new InetSocketAddress(CENTRAL_HOST, server_port);
            socket.bind(address);
          
            Selector selector = Selector.open();
            channel.register(selector,SelectionKey.OP_WRITE);
            
            while(true)
            {
                selector.select(5000);
                Set readyKeys = selector.selectedKeys();
                Iterator iterator = readyKeys.iterator();
                while(iterator.hasNext())
                {
                    SelectionKey key = (SelectionKey)iterator.next();
                    iterator.remove();
                    if(key.isWritable())
                    {
                        recuperaRecursos();
                        ByteBuffer buffer = ByteBuffer.wrap(mesage.getBytes());                     
                        int n = channel.send(buffer, new InetSocketAddress(CENTRAL_HOST, CENTRAL_PORT));
                        System.out.println("\n---------- SHARING FILES WITH CENTTRAL ----------\n"); 
                        System.out.println("Byte enviados: " + n);
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {
                        }
                        continue;
                        
                   }else if(key.isReadable()){                       
                       /* We have an answer from CENTRAL */
                       ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
                       buffer.clear();
                       SocketAddress client = channel.receive(buffer);
                       buffer.flip();
                       String respuesta = new String( buffer.array(), 0, buffer.limit() );                              
                        System.out.println("CENTRAL SENT: " + respuesta);
                       channel.register(selector, SelectionKey.OP_WRITE);
                       continue;
                   }

               }
            }
      }catch(IOException e){
	System.err.println(e);
      }//catch
        
    }
    
            
   private void recuperaRecursos(){       
       mesage = "";
       String aux = "";
       String md5S = "";
       int total_res = 0;
       MD5 md5O = new MD5();

       try{                     
           /*Files from each server*/
           
           //File f = new File("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+(server_indx) );           
           File f = new File("C:\\Users\\lenovo\\Desktop\\ARES\\server-"+(server_indx) );                      
           available_resources = new ArrayList(Arrays.asList(f.list()));
           Collections.sort(available_resources);
           total_res = available_resources.size();
           
           for( int i = 0; i < total_res; i++ ) {          
               aux = available_resources.get(i);
               try{                   
                   //md5S = md5O.getMD5Checksum("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+ (server_indx) +"\\" +  aux);
                   md5S = md5O.getMD5Checksum("C:\\Users\\lenovo\\Desktop\\ARES\\server-" + (server_indx) + "\\" + aux);
               } catch (Exception e) {
                   System.err.println("ALGO SUCEDIO AL INTENTAR OBTENER EL MD5 DEL ARCHIVO A COMPARTIR");
                   e.printStackTrace();
               }               
               mesage += server_indx + "&&" + server_port + "&&" + aux  + "&&" + md5S + "¬";               
           }    
           available_resources.clear();           
       } catch (Exception e) {
           System.err.println("ALGO SUCEDIO CON LA CREACIÓN DEL ARCHIVO A COMPARTIR");
       }
       
   }
}
