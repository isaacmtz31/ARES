
import java.rmi.Remote;
import java.rmi.RemoteException;


/* @author Isaac */
public interface RecursosCompartidoInterface extends Remote {
    TablaHash<String[]> recuperaHash() throws RemoteException;
    String[] obtenerMD5Archivo(int[] posicionProbable)throws RemoteException;
    String[] obtenerPuerto(int[] posicionProbable)throws RemoteException;
    String[] obtenerNombreArchivo(int[] posicionProbable)throws RemoteException;   
    boolean agregarRecurso(String[] estructura, int[] posiciones) throws RemoteException;
}
