import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.channels.*;
import java.nio.*;
import java.util.Collections;
import java.util.Iterator;

/*@author Isaac*/
public class Anuncios extends Thread
{       
    /*Server attributes*/
    private int server_indx;     
    private int server_port;    
    private final int CENTRAL_PORT = 8000;
    private final int MAX_PACKET_SIZE = 2000;      
    private final String CENTRAL_HOST = "127.0.0.2";        
    /*Attributes for sending to CENTRAL*/
    
    private String mesage = "";    
    private ArrayList<String> available_resources;    
    
    public Anuncios(int server_indx, int server_port){
        this.server_indx = server_indx;
        this.server_port = server_port;
    }
    
    @Override
    public void run()
    {
        
        
        /*
       try {           
           
           SocketChannel cliente = SocketChannel.open();
           cliente.configureBlocking(false);           
           cliente.connect( new InetSocketAddress( CENTRAL_HOST, CENTRAL_PORT ) );   
           Selector sel = Selector.open();
           cliente.register(sel,SelectionKey.OP_CONNECT);                                 
           while ( true ) {
               sel.select();
               Iterator<SelectionKey>it = sel.selectedKeys().iterator();
               while(it.hasNext()){
                   SelectionKey k = (SelectionKey)it.next();
                   it.remove();
                   if( k.isConnectable() ){
                       
                       SocketChannel ch = (SocketChannel)k.channel(); 
                       if( ch.isConnectionPending() ){
                           try{                                                              
                               ch.finishConnect();
                               server_port = ch.socket().getLocalPort();
                               System.out.println("CONEXION CON CENTRAL ESTABLECIDA, LISTO PARA COMPARTIR RECURSOS");                               
                           }catch(Exception e){
                               e.printStackTrace();
                           }
                       }                       
                       ch.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                       continue;
                       
                   }
                   if( k.isWritable() ){
                       
                       recuperaRecursos();
                       SocketChannel ch = (SocketChannel)k.channel();                       
                       ByteBuffer buffer = ByteBuffer.wrap(mesage.getBytes());
                       ch.write(buffer);                           
                       mesage = "";
                       k.interestOps(SelectionKey.OP_READ);
                       Thread.sleep(10000);
                       continue;    
                       
                   } else if( k.isReadable() ){
                       
                       SocketChannel ch = (SocketChannel)k.channel();
                       ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
                       buffer.clear();
                       int n = ch.read(buffer);
                       buffer.flip();
                       String respuestaServidor = new String(buffer.array());                       
                       System.out.println("RESPUESTA DEL SERVIDOR > \t" + respuestaServidor);
                       k.interestOps(SelectionKey.OP_WRITE);
                       continue;
                       
                   }
               }
           }
       }catch(Exception e){
           e.printStackTrace();
       }*/ 
    }
    
}
