/* @author Isaac */
import java.util.*;
 
public class Reader extends Thread {
    private TablaHash<String> sharedList;
 
    public Reader(TablaHash<String> sharedList) {
        this.sharedList = sharedList;
    }
 
    public void run() 
    {
        Random random = new Random();
        int index = random.nextInt(sharedList.size());
        String[] number = sharedList.get(index);
 
        System.out.println(getName() + " -> get: " + number);
 
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie ) { ie.printStackTrace(); }
 
    }
}