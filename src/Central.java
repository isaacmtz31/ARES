import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/* @author lenovo */

public class Central extends Thread
{
    private int PORT_U = 1234;
    private String HOST_ADDRESS = "127.0.0.1";
    private TablaHash<String[]> hash;
    
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
        //C:\Users\lenovo\Documents\ARES\build\classes
    }
    
    private TablaHash<String[]> recuperaHash() throws RemoteException
    {
        return hash;
    }
    
    private String[] obtenerNombreArchivo(int[] posicionProbable)throws RemoteException
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
}
