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
    /*Atributos para el servidor*/
    private int indxServidor;     
    private int cliente_puerto;
    private final int CENTRAL_PORT = 8000;
    private final int MAX_PACKET_SIZE = 2000;      
    private final String CENTRAL_HOST = "127.0.0.2";
        
    /*Atributos para enviar recursos a CENTRAL*/    
    private String mensaje = "";    
    private ArrayList<String> recursosDisponibles;    
    
    public Anuncios(int indxServidor){
        this.indxServidor = indxServidor;
    }
        
   private void recuperaRecursos(){       
       
       String aux = "";
       String md5S = "";
       MD5 md5O = new MD5();
       int totalRecursos = 0;
       
       try{                     
           
           /*Recursos de cada servidor*/
           File f = new File("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+(indxServidor) );           
           //File f = new File("C:\\Users\\lenovo\\Desktop\\ARES\\server-"+(indxServidor) );                      
           recursosDisponibles = new ArrayList(Arrays.asList(f.list()));
           Collections.sort(recursosDisponibles);
           totalRecursos = recursosDisponibles.size();
           
           for( int i = 0; i < totalRecursos; i++ ) {          
               aux = recursosDisponibles.get(i);
               try{
                   
                   md5S = md5O.getMD5Checksum("C:\\Users\\Isaac\\Desktop\\ARES\\server-"+(indxServidor)+"\\"+aux);
                   //md5S = md5O.getMD5Checksum("C:\\Users\\lenovo\\Desktop\\ARES\\server-"+(indxServidor)+"\\"+aux);
               } catch (Exception e) {
                   System.err.println("ALGO SUCEDIO AL INTENTAR OBTENER EL MD5 DEL ARCHIVO A COMPARTIR");
                   e.printStackTrace();
               }               
               mensaje += indxServidor + "&&" + aux  + "&&" + md5S + "¬";               
           }    
           recursosDisponibles.clear();           
       } catch (Exception e) {
           System.err.println("ALGO SUCEDIO CON LA CREACIÓN DEL ARCHIVO A COMPARTIR");
       }
       
   }
    
    @Override
    public void run()
    {                
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
                       
                       SocketChannel ch = (SocketChannel)k.channel(); /**/
                       if( ch.isConnectionPending() ){
                           try{                                                              
                               ch.finishConnect();
                               cliente_puerto = ch.socket().getLocalPort();
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
                       ByteBuffer buffer = ByteBuffer.wrap(mensaje.getBytes());
                       ch.write(buffer);                           
                       mensaje = "";
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
       } 
    }
    
}
