/* @author Isaac */
public class Server 
{          
    private int indxServidor;
    
    public Server(int indxServidor){        
        this.indxServidor = indxServidor;
        iniciarAnuncios();
    }   
    
    private void iniciarAnuncios(){         
        Anuncios adv = new Anuncios(indxServidor);        
        adv.start();
    }
    
    private void iniciarIdentificacion(){
        Identificacion id = new Identificacion(indxServidor);
    }
}
