/*@author Isaac*/
public class ARES 
{    
    public static void main(String args[])
    {
       //Iniciallzar nodos            
        String[] r1 = new String[3];
        TablaHash<String[]> RecursoCompartido = new TablaHash<>(r1); //Vacio para su proximo majneo
        for (int i = 1; i < 5; i++) {
            new Node(i, RecursoCompartido);            
        }       
    }
}
