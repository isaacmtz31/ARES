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
 
    public int size() {
        Lock readLock = rwLock.readLock();
        readLock.lock();
 
        try {
            return list.size();
        } finally {
            readLock.unlock();
        }
    }
}
