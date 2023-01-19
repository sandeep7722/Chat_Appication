import java.net.*;
import java.io.*;
import java.util.*;



class Server{

    ServerSocket server;
    Socket socket;
    
    BufferedReader br;
    PrintWriter out;


    //constructor....
    public Server(){
        try{
        server=new ServerSocket(7777);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting......");
        socket=server.accept();
        
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out=new PrintWriter(socket.getOutputStream());

        startReading();
        startWriting();


        }
        catch(Exception e){
            //todo: handle exeption 
            e.printStackTrace();


        }

    }

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
                        System.out.println("Client terminated the chat!");

                        socket.close();

                        break;
                    }
    
                    System.out.println("Client: "+msg);
                    

            }
            System.out.println("Connection is Closed!");
        }
        catch(Exception e)
        {
           // e.printStackTrace();
           System.out.println("Connection is Closed!");
        }
        };
        new Thread(r1).start();


    }

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
        System.out.println("This is server....going to start server.");

        new Server();

    }
}