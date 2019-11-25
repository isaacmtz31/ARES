
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;


/* @author Isaac */
public class Client extends Thread implements ActionListener{
    
    int flagg = 0;
    
    /*Client attributes*/
    private int client_indx;
    private int client_port;    
    private String fileJtxt;
    private final int CENTRAL_PORT = 8000;
    private final int MAX_PACKET_SIZE = 65000;      
    private final String CENTRAL_HOST = "127.0.0.1";        
    
    
    /*Window attributes*/    
    private JLabel lblTit = new JLabel();
    private JLabel lblSubt = new JLabel();
    private JLabel lblFile = new JLabel();
    private JLabel NoFile = new JLabel();
    private JButton search = new JButton();
    private JButton download = new JButton();
    private JFrame window = new JFrame("ARES");
    private JTextArea txtArea = new JTextArea();
    private JTextField fileTS = new JTextField();    
    private JTextField resourceN = new JTextField();    
    private JScrollPane listScrollPane = new JScrollPane(txtArea);
        
    private ArrayList<String[]> serversF = new ArrayList<>();
    
    public Client(int client_indx, int client_port)            
    {       
        this.client_indx = client_indx;
        this.client_port = client_port;        
        run();
    }
    
    public void initWindow(){

        //Propiedades de la ventana
        window.setLayout(null);
        window.setBounds(0, 0, 650, 800);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setResizable(false);   
        window.getContentPane().setBackground(new Color(218, 218, 218));
                
        //Title label
        lblTit.setText("Welcome to ARES");
        lblTit.setBounds(130, 20, 450, 50);
        lblTit.setFont(new java.awt.Font("Tw Cen MT", 0,50));
        lblTit.setForeground(Color.DARK_GRAY);    
        
        //Subtitle Label
        lblSubt.setText("P2P File Searcher");
        lblSubt.setBounds(205, 70, 450, 50);
        lblSubt.setFont(new java.awt.Font("Tw Cen MT", 0,30));
        lblSubt.setForeground(Color.DARK_GRAY); 
        
        //WRULF
        lblFile.setText("What are you searching for?");
        lblFile.setBounds(130, 210, 600, 50);
        lblFile.setFont(new java.awt.Font("Tw Cen MT", 0,35));
        lblFile.setForeground(Color.DARK_GRAY); 
        
        
        //Text box         
        fileTS.setBounds(175, 275, 300, 50);
        fileTS.setFont(new java.awt.Font("Tw Cen MT", 0,30));
        fileTS.setForeground(Color.DARK_GRAY); 
        
        //NO RESOURCE
        NoFile.setText("Resource #");
        NoFile.setBounds(175, 480, 170, 50);        
        NoFile.setFont(new java.awt.Font("Tw Cen MT", 0,25));
        NoFile.setForeground(Color.DARK_GRAY); 
        
        resourceN.setBounds(300, 485, 40, 40); 
        resourceN.setFont(new java.awt.Font("Tw Cen MT", 0,25));
        resourceN.setForeground(Color.DARK_GRAY); 
        
        //BOTON search
        ImageIcon icon4 = new ImageIcon("..\\ARES\\search.png");
        Image img4 = icon4.getImage();
        Image newImg4 = img4.getScaledInstance(50, 50, java.awt.Image.SCALE_AREA_AVERAGING);
        ImageIcon newIcon4 = new ImageIcon(newImg4);
        
        search.setBounds(480, 275, 50, 50);
        search.addActionListener(this);
        search.setIcon(newIcon4);
        search.setBorderPainted(false);
        search.setContentAreaFilled(false); 
        
        //Download
        ImageIcon icon5 = new ImageIcon("..\\ARES\\download.png");
        Image img5 = icon5.getImage();
        Image newImg5 = img5.getScaledInstance(50, 50, java.awt.Image.SCALE_AREA_AVERAGING);
        ImageIcon newIcon5 = new ImageIcon(newImg5);
        
        download.setBounds(480, 480, 50, 50);
        download.addActionListener(this);
        download.setIcon(newIcon5);
        download.setBorderPainted(false);
        download.setContentAreaFilled(false); 
        
        
        //TextArea & Scroll                        
        listScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        listScrollPane.setPreferredSize(new Dimension(250, 250));
        listScrollPane.setBounds(175, 350, 300, 100); 
        
        
        
        String ax ="";
        for(int i = 0; i < serversF.size(); i++)
        {
            System.out.println(serversF.get(i)[0] + "*****" + serversF.get(i)[1]);
            if(i==0){
                ax += "----------------------------------------------------------------------\n" + "Resource: #" + i + "\n" +"Server: " + serversF.get(i)[0] +"\nFile name: " +serversF.get(i)[2] + "\nMD5: " + serversF.get(i)[3] + "\n----------------------------------------------------------------------" +"\n";
            }
            else
            {
                ax += "Resource: #" + i + "\n" + "Server: " + serversF.get(i)[0] +"\nFile name: " +serversF.get(i)[2] + "\nMD5: " + serversF.get(i)[3] + "\n----------------------------------------------------------------------" +"\n";
            }
            txtArea.setText(ax);            
        }        
      
        //Agregar elementos a la ventana
        window.add(lblTit);
        window.add(lblSubt);    
        window.add(lblFile);
        window.add(fileTS);
        window.add(search);
        window.add(download);
        window.add(resourceN);
        window.add(NoFile);
        window.add(listScrollPane);
        window.setVisible(true);  
    }
    
    public void run()
    {                     
        initWindow();
        Scanner sc = new Scanner(System.in);        
        SocketAddress host;
        try {            
            /* We open a datagram channel */
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                        
            DatagramSocket socket = channel.socket();
            SocketAddress address = new InetSocketAddress(CENTRAL_HOST, client_port);
            socket.bind(address);
          
            Selector selector = Selector.open();
            channel.register(selector,SelectionKey.OP_WRITE|SelectionKey.OP_READ);
            
            while(true)
            {
                selector.select(1);
                Set readyKeys = selector.selectedKeys();
                Iterator iterator = readyKeys.iterator();
                
                while(iterator.hasNext() )
                {
                    SelectionKey key = (SelectionKey)iterator.next();
                    iterator.remove();
                    
                    if(key.isWritable() && (flagg == 1 ||  flagg==5))
                    {                     
                        /*Building the message*/
                        if(flagg == 1)
                        {
                            String message = "1" + "&&" + client_indx + "&&" + client_port + "&&" + fileJtxt;  /* 1 for search */                        
                            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());                     
                            int nB = channel.send(buffer,new InetSocketAddress(CENTRAL_HOST, CENTRAL_PORT));

                            System.out.println("\n---------- LOOKING FOR THE FILE IN CENTRAL ----------\n"); 
                            System.out.println("Byte enviados: " + nB);                        
                            channel.register(selector, SelectionKey.OP_READ);     
                            fileJtxt = "";
                            
                        }else if(flagg == 5){
                            
                            String message = "";
                            
                            for(int j = 0; j < serversF.size(); j++){
                                
                                message = "7" + "&&" + client_port + "&&" + serversF.size() + "&&" + j + "&&" + serversF.get(j)[2];
                                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());                                   
                                int nB = channel.send(buffer,new InetSocketAddress(CENTRAL_HOST, Integer.parseInt(serversF.get(j)[1])));
                                System.out.println("\n---------- PETITIONS HAVE BEEN SENT ----------\n"); 
                                System.out.println("Byte enviados: " + nB);                        
                                channel.register(selector, SelectionKey.OP_READ);     
                            }
                        }
                   }
                   else if(key.isReadable())
                   {       
                       ArrayList<String[]> nodes = null;
                       /* Every peer who has the file we are looking for */
                       ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
                       buffer.clear();
                       SocketAddress client = channel.receive(buffer);
                       buffer.flip();
                       ByteArrayInputStream in = new ByteArrayInputStream(buffer.array());
                       ObjectInputStream is = new ObjectInputStream(in);
                       try {
                            nodes = (ArrayList<String[]>)is.readObject();
                       } catch (Exception e) {
                       }
                       
                       for(int i = 0; i < nodes.size(); i++)
                       {
                           String[] ax = {nodes.get(i)[0], nodes.get(i)[1], nodes.get(i)[2] , nodes.get(i)[3]};                           
                           serversF.add(ax);                                                      
                           flagg = 2;  
                       }                                              
                       if(flagg==2)  
                       {
                           key.interestOps(SelectionKey.OP_WRITE);
                           break;
                       }
                   }

               }
                if(flagg==2)
                {
                    window.dispose();    
                    initWindow();
                    serversF.clear();
                    flagg=0;
                }
            }
            
      }catch(IOException e){
	System.err.println(e);
      }//catch
        
            
            
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == search){
            String file = fileTS.getText();
            if(file.equalsIgnoreCase("")){                
                JOptionPane.showMessageDialog(null, "Please, search something", "ERROR", JOptionPane.ERROR_MESSAGE);  
            }else
            {
                fileJtxt = fileTS.getText();   
                flagg = 1;   
            }                
        }  
        if(e.getSource()== download){
            
            if(resourceN.getText().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(null, "Error, type a numer file");
                
            }else{
                JOptionPane.showMessageDialog(null, "Downloading", "Ok", JOptionPane.INFORMATION_MESSAGE);
                try {
                    int x = Integer.parseInt(resourceN.getText());
                    flagg=5;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "ONLY NUMBERS", "ERROR", JOptionPane.ERROR_MESSAGE);
                }                
            }
        }
    }
    
    private void download(String name){        
        int threads = 0;
        String[] ports = new String[threads];
        
    }
}
