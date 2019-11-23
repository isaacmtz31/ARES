
import java.util.Random;

/* @author Isaac  */
public class Node 
{    
    private int indxNode;
    private Server server;
    private Client client;
    
    public Node(int indxNode)
    {        
        this.indxNode = indxNode;    
        Server server = new Server(indxNode, 8001+indxNode);
    }    
}
