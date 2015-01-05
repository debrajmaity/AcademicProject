
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class clientConnection extends Thread {
	
	private String hostname;
	private int portnum;
	private Socket clientsocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
	
	private String Message_OUT="";
	public void run(){
		
		    boolean connected=false;
	        //Random rand = new Random();
	        
	        while(!connected){
			        
        			try {
			        	
			        	
				           	
			        	    System.out.println("Starting Client..... ");
				            clientsocket = new Socket(hostname, portnum);
				          
				            out = new PrintWriter(clientsocket.getOutputStream(), true);
				            in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
				            connected=true;
			        }
			         catch (UnknownHostException e) {
			            System.err.println("Error connecting Host:"+e.getMessage());
			            try {
							Thread.currentThread().sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			            connected=false;
			            continue;
			        } catch (IOException e) {
			            System.err.println("I/O exception while connecting to the host"+e.getMessage());
			            System.err.println("Error connecting Host:"+e.getMessage());
			            try {
							Thread.currentThread().sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			            connected=false;
			            continue;
			        }
			 
			        
			        String fromServer;
			        String fromUser;
			 
			        try {
												 
						 out.println(Message_OUT);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			 
			        out.close();
			        try {
						in.close();
						//stdIn.close();
						clientsocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
	        	}
			       
        	}
	//}
	
	
	
	clientConnection(String host,int port,String message){
		this.hostname=host;
		this.portnum=port;
		this.Message_OUT=message;
	}
	
	
}
