import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;



public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //declare Component
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);



    //constructor
    public Client()
    {
        try
        {
            System.out.println("Sending request to server");

            socket=new Socket("192.168.1.7",7777);

            System.out.println("connection done");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();


        }
        catch(Exception e)
        {

        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Key Released"+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    //System.out.println("you have pressed enter button");
                    String contentTosend=messageInput.getText();
                    messageArea.append("Me :"+contentTosend+"\n");
                    out.println(contentTosend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();


                }

                
            }
            
        });
    }

    public void createGUI()
    {
        //gui code

        this.setTitle("Client Message");
        this.setSize(500,650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("icon.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);



        
        //set layout of frame
        this.setLayout(new BorderLayout());

        //adding the component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);




        this.setVisible(true);
    }


    //start reading method
    public void startReading()
    {
        //thread-read karke data rakhega
        Runnable r1=()->{
            System.out.println("reader started......");
        
        try
        {
            while(true)
            {
               
                
                    String msg = br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Server terminated the chat!");
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
    
                    //System.out.println("Server: "+msg);
                    messageArea.append("Server: "+msg+"\n");

            }

        } 
        catch (Exception e) {
                    
            //e.printStackTrace();
            System.out.println("Connection is Closed!");

        }

        };
        new Thread(r1).start();


    }
    //start writing
    public void startWriting()
    {
        //thread-data user lega and the send karega client tak
        System.out.println("writer started....");
        Runnable r2=()->{
        try
        {
           while(!socket.isClosed())
           {
                
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();

                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }

           }
           System.out.println("Connection is Closed!");

           
        }
        catch(Exception e)
        {
           // e.printStackTrace();
           System.out.println("Connection is Closed!");
        }

        };
        
        new Thread(r2).start();

    }


    public static void main(String []args){

        System.out.println("This is client.");
        new Client();

        
    }

    
}
