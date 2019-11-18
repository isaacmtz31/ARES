
/* @author Isaac */
public class iniciadorCliente extends Thread{
    private  int MCAST_PORT = 9999;
    private int suma;
    
    public iniciadorCliente(int sumaPuerto)
    {
        this.MCAST_PORT = MCAST_PORT ;
        suma = sumaPuerto;
    }
    
    @Override
    public void run() {
        //Iniciamos servicio del cliente
        Client cliente = new Client(MCAST_PORT, suma);
        cliente.start();
    }        
}
