/* @author Isaac */
public class Server 
{   
    private int indxServidor;
    private int MCAST_PORT = 9999;    
    private TablaHash<String[]> recursoCompartido;
        
    public Server(int indxServidor, TablaHash<String[]> recursoCompartido)
    {
        this.indxServidor = indxServidor;     
        this.recursoCompartido = recursoCompartido;
        iniciarAnuncios();
    }
    
    public synchronized void iniciarAnuncios(){ 
        //Dos hilos, un anunciador de recursos y otro para la busqeda
        new Anuncios(MCAST_PORT, indxServidor);        
    }
}
