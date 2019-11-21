import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/* @author Isaac */

public class Central extends Thread implements RecursosCompartidoInterface
{
    private int PORT_U = 1234;
    private String HOST_ADDRESS = "127.0.0.1";
    private TablaHash<String[]> hash;
    
    public Central(){}
    
    public Central(TablaHash<String[]> hash){
        this.hash = hash;        
    }
    
    private void init(){
        try
        {
            java.rmi.registry.LocateRegistry.createRegistry(1099); //puerto default del rmiregistr
            System.out.println("RMI REGISTRY READY");            
        }catch (Exception e){	 
            System.out.println("¡EXCEPTION STARTING RMI REGISTRY!");		            
            e.printStackTrace();	  
        }
        
        try {
            //String cb = "file:///C:\Users\lenovo\Documents\ARES\build\classes/";
            String cb = "file:/C:\\Users\\Isaac\\Documents\\classes\\NetBeansProjects\\ARES\\build\\classes/";
            System.setProperty("java.rmi.server.codebase",cb); 
	    Central central = new Central();
	    RecursosCompartidoInterface stub = (RecursosCompartidoInterface) UnicastRemoteObject.exportObject(central,0);
	    // Bind the remote object's stub in the registry
	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Recursos", stub);
	    System.err.println("SERVER READY");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}        
    }
    
    @Override
    public TablaHash<String[]> recuperaHash() throws RemoteException
    {
        return hash;
    }
    
    @Override
    public String[] obtenerNombreArchivo(int[] posicionProbable)throws RemoteException
    {
        String[] auxNombre = new String[posicionProbable.length]; //Es probable que se encuentre es mas de 1
        for(int i = 0; i < posicionProbable.length; i++)
        {
            //Obtener el recurso padre.
            if(hash.get(posicionProbable[i]) != null) //Es probable encontrar algo
            {
               auxNombre[i] = hash.get(i)[0];
            }
        }        
        return auxNombre;
    }
    
    public String[] obtenerMD5Archivo(int[] posicionProbable)throws RemoteException
    {
        String[] auxMD5 = new String[posicionProbable.length]; //Es probable que se encuentre es mas de 1
        for(int i = 0; i < posicionProbable.length; i++)
        {
            //Obtener el recurso padre.
            if(hash.get(posicionProbable[i]) != null) //Es probable encontrar algo
            {
               auxMD5[i] = hash.get(i)[0];
            }
        }        
        return auxMD5;
    }
    
    
    public String[] obtenerPuerto(int[] posicionProbable)throws RemoteException
    {
        String[] auxPuerto = new String[posicionProbable.length]; //Es probable que se encuentre es mas de 1
        for(int i = 0; i < posicionProbable.length; i++)
        {
            //Obtener el recurso padre.
            if(hash.get(posicionProbable[i]) != null) //Es probable encontrar algo
            {
               auxPuerto[i] = hash.get(i)[2];
            }
        }        
        return auxPuerto;
    }    

    @Override
    public boolean agregarRecurso(String[] estructura, int[] posiciones) throws RemoteException {
        
        boolean flag = false;
        for(int i = 0; i < posiciones.length; i++)
        {
            if(hash.get(i) != null)
            {
                //Se puede insertar
                hash.add(estructura);
                flag = true;
            }
            else
            {
                continue;
            }
        }
        return flag;
    }
}
