
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
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
import java.util.Random;
import java.util.Set;

/* @author Isaac */
public class Server extends Thread
{          
    /*Server attributes*/
    private int server_indx;
    private int server_port;    
    private final int CENTRAL_PORT = 8000;
    private final int MAX_PACKET_SIZE = 65000;      
    private final String CENTRAL_HOST = "127.0.0.1";        
    
    /*Attributes for sending to CENTRAL*/    
    private String mesage = "";    
    private ArrayList<String> available_resources;    
    
    public Server(int server_indx, int server_port){        
        this.server_indx = server_indx;
        this.server_port = server_port;        
        
    }   
    
    public void run() {
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
                        System.out.println("\n---------- SHARING FILES WITH CENTRAL ----------\n"); 
                        System.out.println("Byte enviados: " + n);
                        try {
                            Thread.sleep(15000);
                        } catch (Exception e) {
                        }
                        channel.register(selector, SelectionKey.OP_READ);
                        continue;
                        
                   }else if(key.isReadable()){                       
                       /* We have an answer from CENTRAL | Some user are searching for a file */                       
                       ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
                       buffer.clear();
                       channel.receive(buffer);
                       buffer.flip();
                       String response = new String( buffer.array(), 0, buffer.limit() );       
                       String type = formatearMsj(response, 0);
                       
                       if(type.equals("7")){
                           System.out.println("\n---------- PETITION FOR DOWNLOADING ----------\n"); 
                       }else
                       {
                           System.out.println("CENTRAL SENT: " + response);
                           channel.register(selector, SelectionKey.OP_WRITE);                                                                                                                                              
                       }
                       
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
           
           File f = new File("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+(server_indx) );             
           //File f = new File("C:\\Users\\lenovo\\Desktop\\ARES\\server-"+(server_indx) );                      
           available_resources = new ArrayList(Arrays.asList(f.list()));
           Collections.sort(available_resources);
           total_res = available_resources.size();
           
           for( int i = 0; i < total_res; i++ ) {          
               aux = available_resources.get(i);
               try{                   
                   md5S = md5O.getMD5Checksum("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+ (server_indx) +"\\" +  aux);
                   //md5S = md5O.getMD5Checksum("C:\\Users\\lenovo\\Desktop\\ARES\\server-" + (server_indx) + "\\" + aux);
               } catch (Exception e) {
                   System.err.println("ALGO SUCEDIO AL INTENTAR OBTENER EL MD5 DEL ARCHIVO A COMPARTIR");
                   e.printStackTrace();
               }               
               mesage += "2" + "&&" + server_indx + "&&" + server_port + "&&" + aux  + "&&" + md5S + "¬";    //2 for sharing           
           }    
           available_resources.clear();           
       } catch (Exception e) {
           System.err.println("ALGO SUCEDIO CON LA CREACIÓN DEL ARCHIVO A COMPARTIR");
       }       
   }
   
   private String formatearMsj(String mensaje, int idx)
    {                                          
        String[] respuestas = mensaje.split("&&");           
        return respuestas[idx];
    }           
   
   private String[] formatMessage(String message){
       return message.split("&&");
   }
   
   private byte[] sendFile(String message){
       
       String[] aux = formatMessage(message);       
       File f = new File("C:\\Users\\Isaac\\Desktop\\ARES\\server-" + (server_indx) + "\\" + aux[4]);       
       long file_size = f.length();
       long slices = file_size / Long.parseLong(aux[2]);             
       byte[] buffer = new byte[(int)slices];
       int offset = Integer.parseInt(aux[3]) * (int)slices;
       try {
           ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));                     
           int n = ois.read(buffer, offset, (int)slices);
       } catch (Exception e) {
       }
       
       
       try {
           FileInputStream fis = new FileInputStream(f);
           
       } catch (Exception e) {
       }       
       return buffer;       
   }
}
