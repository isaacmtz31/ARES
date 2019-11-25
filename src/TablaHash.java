import java.util.*;
import java.util.concurrent.locks.*;

/* @author Isaac */

public class TablaHash<E> 
{
    private ArrayList<String[]> list;
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final int MAX_LENGTH_LIST = 500;
        
    public TablaHash(){
        String[] aux = {"","","",""};
        list = new ArrayList(MAX_LENGTH_LIST);        
        for(int i = 0; i < MAX_LENGTH_LIST; i++)        
            list.add(aux);                
    }
 
    public void add(String[] element, int position ) 
    {        
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            list.set(position, element);
        } finally {
            writeLock.unlock();
        }
    }
 
    public String[] get(int position) {
        
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            return list.get(position);
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
    
    public void imprimirTablaHash()
    {        
        
        boolean flag = true;
        System.out.println("\n\n---------- RECURSOS DISPONIBLES ----------\n"); 
        for(int i = 0; i < size(); i++)
        {
            if( !list.get(i)[0].equals("") && !list.get(i)[1].equals("") && !list.get(i)[2].equals("") && !list.get(i)[3].equals("") )
            {
                if(flag)
                {
                    System.out.println("\t----------------------");
                    flag = false;
                }
                for(int j = 0; j < 4; j++){
                    System.out.println( "\t" + list.get(i)[j] );    
                }
                System.out.println("\t----------------------");
            }
        }
    }
    
    public boolean existeTablaHash(String[] subrecurso, int position)
    {
        boolean flag = true;       
        
        if( (list.get(position)[0]).equals(subrecurso[0]) && (list.get(position)[1]).equals(subrecurso[1]) && (list.get(position)[2]).equals(subrecurso[2]) && (list.get(position)[3]).equals(subrecurso[3]) ) 
        {
            System.err.println("THAT FILE ALREADY EXISTS");
            flag = false;                    
        }                                                                      
        return flag;
    }
    
    public int formatearMsj(String mensaje)
    {   
        long posLIS = 0;
        long posFNV = 0;
        long posMUR = 0;        
        int server_port = 0;
        boolean flag = false;                
        String[] subrecurso = null;        
        
        FNVHash fnv = new FNVHash();
        MurmurHash mur = new MurmurHash();                        
        String[] recurso = mensaje.split("¬");    
        
        for ( int i = 0; i < recurso.length ;i++ ) 
        {                                                
            subrecurso = recurso[i].split("&&");            
            server_port = Integer.parseInt(subrecurso[1]);
            
            //HASH FUNCTIONS
            posFNV = fnv.hash64( subrecurso[0] + "-" + subrecurso[2] ) % MAX_LENGTH_LIST;
            posMUR = mur.hash64( subrecurso[0] + "-" + subrecurso[2] ) % MAX_LENGTH_LIST;
            
            //IF WE GET A NEGATIVE NUMBER POSITION FOR THE HAST TABLE
            if( posFNV < 0 )
                posFNV = ( posFNV * -1 );
            if( posMUR < 0 ) 
                posMUR = ( posMUR * -1 );
            if( posFNV > posMUR )
                posLIS = posMUR;
            else
                posLIS = posFNV;
                                   
            if( existeTablaHash( subrecurso, (int)posLIS ) ){
                add(subrecurso, (int)posLIS);                
            }
        }  
        return server_port;
    }   
    
    public ArrayList<String[]> searchFile(String petition){
        
        long posLIS = 0;
        long posFNV = 0;
        long posMUR = 0;        
        
        FNVHash fnv = new FNVHash();
        MurmurHash mur = new MurmurHash();                        

        String[] aux = petition.split("&&");
        String file = aux[3];
        ArrayList<String[]> nodes = new ArrayList();
        for(int i = 1; i < 11 ; i++) //Check in 10 nodes
        {
            //HASH FUNCTIONS
            posFNV = fnv.hash64( i + "-" + file ) % MAX_LENGTH_LIST;
            posMUR = mur.hash64( i + "-" + file ) % MAX_LENGTH_LIST;
            
            //IF WE GET A NEGATIVE NUMBER POSITION FOR THE HAST TABLE
            if( posFNV < 0 )
                posFNV = ( posFNV * -1 );
            if( posMUR < 0 ) 
                posMUR = ( posMUR * -1 );            
            if( posFNV > posMUR )
                posLIS = posMUR;
            else
                posLIS = posFNV;
            
            if( list.get((int)posLIS)[2].equalsIgnoreCase(file) || list.get((int)posLIS)[2].contains(file))
            {
                nodes.add(list.get((int)posLIS));
            }                
        }
        
        return nodes;
    }
}
