
import java.util.Random;

/* @author Isaac  */
public class Node 
{
    
    private int indxNodo;
    private Server servidor;
    private Client cliente;
    
    public Node(int indxNodo){
        this.indxNodo = indxNodo;   
        iniciadorServidor is = new iniciadorServidor(indxNodo);
        is.start();
        
        iniciadorCliente ic = new iniciadorCliente(indxNodo);
        ic.start();
        
    }    
}
