/* @author Isaac  */

public class Node 
{    
    private Server server;
    private Client client;
    
    public Node(int indxNode)
    {                  
        int port = 8000 + indxNode;
        server = new Server(indxNode, port);
        server.start();
        port = port + indxNode;
        client = new Client(indxNode, port);        
    }    
}
