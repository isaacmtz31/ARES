
import java.util.Random;

/* @author Isaac */
public class iniciadorServidor extends Thread
{
    int sumaPuerto;
    
    public iniciadorServidor(int sumaPuerto)
    {
        this.sumaPuerto = sumaPuerto;
    }
    
    @Override
    public void run() 
    {        
        Random r = new Random();
        Server servidor = new Server(sumaPuerto,r.nextInt(10));

    }   
}
