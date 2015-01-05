
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class clientServerProcess extends Thread {

	protected Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
		
	 public clientServerProcess(Socket clientSocket) {
	        this.socket = clientSocket;
	    }

	 
	 	public void run(){
	 		try {
	 			
				out = new PrintWriter(socket.getOutputStream(), true);
				
				in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
				
				String outline,inline;
			
		        while ((inline = in.readLine()) != null) {
		    	  
		    	   MessageQueue.add(inline);
		        
		        }
		       
		      
		       
		       out.close();
		       in.close();
		       socket.close();
		      
              
		       
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	}
	 
	   
}
