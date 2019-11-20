
import java.util.Random;

/* @author Isaac  */
public class Node 
{
    
    private int indxNodo;
    private Server servidor;
    private Client cliente;
    private TablaHash<String[]> RecursoCompartido;
    
    public Node(int indxNodo, TablaHash<String[]> recursoCompartido)
    {
        this.RecursoCompartido = recursoCompartido;
        this.indxNodo = indxNodo;   
          
        iniciadorCliente ic = new iniciadorCliente(indxNodo, recursoCompartido);
        ic.start();
        
        iniciadorServidor is = new iniciadorServidor(indxNodo, recursoCompartido);
        is.start();      
        
    }    
}
