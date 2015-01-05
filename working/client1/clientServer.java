import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;




public class clientServer extends Thread{
	private int portnum;
	private boolean servercreated=false;
	private static boolean terminate=false;
	private ServerSocket serversocket=null;
	private Socket clientsocket=null;
	//private PrintWriter out;
	//private BufferedReader in;
	
	clientServer(int port){
		this.portnum=port;
	}
	
	public void run() {
		//int port=3324;
		
		try{
			this.serversocket=new ServerSocket(portnum);
		}catch(IOException e){
			
			System.err.println("Couldn't listen to the port "+portnum);
			System.exit(1);
			
		}
					
			servercreated=true;
			//System.out.println("Connected to local port"+clientsocket.getLocalPort());
			 while (true) {
		            try {
		            	clientsocket=serversocket.accept();
		            } catch (IOException e) {
		                System.out.println("I/O error: " + e);
		            }
		            
		            //System.out.println("Connection excepted from client " + clientsocket.getRemoteSocketAddress());
		            //System.out.println("Creating new thread for processing the client request");
		            // new thread for a client
		            new clientServerProcess(clientsocket).start();
		            
		            //System.out.println("Checking termination status...");
		           
		     }
			 
			// System.out.println("System terminated");
					
				
	}
		
	
	
	public void terminate(){
		if(servercreated){
		     try {
		    	//out.close();
				//in.close();
				//clientsocket.close();
				serversocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }else{
		   System.out.println("Server not created. No termination allowed."); 
	   }
		
	}
	
	/*public void SendMessage(){
		if(servercreated){
				try {
					out = new PrintWriter(clientsocket.getOutputStream(), true);
					
					in = new BufferedReader( new InputStreamReader(clientsocket.getInputStream()));
					
					String outline,inline;
					//ReadWriteProtocol rw=new ReadWriteProtocol();
					
					/*outline = rw.processInput(null);*/
			       // out.println("Hello");
			 
			      //  out.println("Sending message");
			       /* while ((inline = in.readLine()) != null) {
			             //outline = rw.processInput(inline);  
			        	System.out.println("From Client:"+inline); 
			        	outline = inline; 
			             out.println(outline);
			             if (outline.equals("Bye."))
			                 break;
			        }*/
			       
			/*		} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}else {
				 System.out.println("Server not created. Message can't be send!");
			}       
	 }*/
	
	public static boolean getterminationstate(){
		
		return terminate;
	}
	
	public static void setterminationstate(boolean ter){
		
		terminate=ter;
	}

}
