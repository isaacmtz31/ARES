
/* @author Isaac */
public class iniciadorCliente extends Thread{
    
    private int suma;
    private  int MCAST_PORT = 9999;
    private TablaHash<String[]> recursoCompartido;
    
    public iniciadorCliente(int sumaPuerto, TablaHash<String[]> recursoCompartido)
    {
        this.MCAST_PORT = MCAST_PORT;
        this.recursoCompartido = recursoCompartido;
        suma = sumaPuerto;
    }
    
    @Override
    public void run() {
        //Iniciamos servicio del cliente
        Client cliente = new Client(MCAST_PORT, suma, recursoCompartido);
        cliente.start();
    }        
}
