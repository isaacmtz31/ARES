/* @author Isaac */
public class Server 
{   
    private int indxServidor;
    private int MCAST_PORT = 9999;    
        
    public Server(int indxServidor, int sumaPuerto)
    {
        this.indxServidor = indxServidor;  
        this.MCAST_PORT += sumaPuerto;        
        iniciarAnuncios();
    }
    
    public void iniciarAnuncios(){ 
        //Dos hilos, un anunciador de recursos y otro para la busqeda
        new Anuncios(MCAST_PORT, indxServidor);        
    }
}
