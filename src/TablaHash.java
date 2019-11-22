/**
 *
 * @author Isaac
 */
import java.util.*;
import java.util.concurrent.locks.*;
 
public class TablaHash<E> 
{
    private List<String[]> list = new ArrayList<>();
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
 
    public TablaHash(String[]... initialElements) { //genérico:Clase, Interfaz
        list.addAll(Arrays.asList(initialElements));
    }
    
    public TablaHash(){};
 
    public void add(String[] element) 
    {
        
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            list.add(element);
        } finally {
            writeLock.unlock();
        }
    }
 
    public String[] get(int index) {
        
        Lock readLock = rwLock.readLock();
        readLock.lock();
 
        try {
            return list.get(index);
        } finally {
            readLock.unlock();
        }        
    }
    
    public synchronized boolean existeTablaHash(String[] subrecurso)
    {
        boolean flag = false;       
        int s = size();
        if(s == 0)
        {
            add(subrecurso);   
            imprimirTablaHash();
        }else
        {            
            for(int i = 0; i < s; i++)
            {
                if((list.get(i)[0]).equals(subrecurso[0]) )
                {                 
                    System.out.println(list.get(i)[0] + "=" + subrecurso[0] + "?");
                    imprimirTablaHash();
                    if( list.get(i)[1].equals(subrecurso[1]) )                                                
                    {
                        imprimirTablaHash();
                        System.out.println(list.get(i)[1] + "=" + subrecurso[1] + "?");
                        System.out.println("ESE RECURSO YA EXISTE, NO SE AGREGARÁ");
                    }
                    else{
                        
                    }
                }else
                {
                    
                    System.out.println("ESE RECURSO NO EXISTE, SE AGREGARÁ");
                            flag = true;
                            add(subrecurso);
                }
            }
        }                
        return flag;
    }
 
    public synchronized int size() {
        Lock readLock = rwLock.readLock();
        readLock.lock();
 
        try {
            return list.size();
        } finally {
            readLock.unlock();
        }
    }
    
    public synchronized void imprimirTablaHash()
    {
        System.out.println("\n\n---------- RECURSOS DISPONIBLES ----------\n"); 
        for(int i = 0; i < size(); i++)
        {
            if(i == 0)
                System.out.println("\t----------------------");
            for(int j = 0; j < 3; j++)
            {
                System.out.println( "\t" + list.get(i)[j] );    
            }
            System.out.println("\t----------------------");
        }
    }
    
    public synchronized boolean formatearMsj(String mensaje)
    {        
        boolean flag = false;        
        String[] recurso = mensaje.split("¬");    
        String[] subrecurso = null;
        String[] subHash = new String[3];
        for ( int i = 0; i < recurso.length ;i++ ) {            
            subrecurso = recurso[i].split("&&");    
            for( int j = 0; j < subrecurso.length; j++ ) {           
                subHash[j] = subrecurso[j];          
                System.out.println("--->" +subHash[j]);
            }
            /*Agregamos el recurso a la tablaHASH*/
            if(existeTablaHash(subHash))                            
                flag = true;            
            else
                continue;            
        }       
        
        return flag;
    }   
}
