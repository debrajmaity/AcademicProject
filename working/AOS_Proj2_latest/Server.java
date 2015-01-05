//package com.utd.aos;

import java.net.*;
import java.io.*;


public class Server {
	
	
	public static int ServerID;
	
	public static boolean canterminate=false;
	public static void main (String args[]) throws IOException, InterruptedException{
	
		try
		{
			InetAddress addr;
			String hostname;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			System.out.println("Server started at:"+ hostname);
			
			if(hostname.equals("net10.utdallas.edu")){
				 ServerID=1;
			}else if(hostname.equals("net11.utdallas.edu")){
				 ServerID=2;
			}else if(hostname.equals("net12.utdallas.edu")){
				 ServerID=3;
			}
		}
		catch (UnknownHostException ex)
		{
			System.out.println("Hostname can not be resolved");
			System.exit(1);
		}
		
		
		while(!canterminate){
		int port=4545;
		ServerSocket serversocket=null;
		try{
			serversocket=new ServerSocket(port);
		}catch(IOException e){
			
			System.err.println("Couldn't listen to the port "+port);
			System.exit(1);
			
		}
		
		Socket clientsocket=null;
		
		
		try {
			clientsocket=serversocket.accept();
			System.out.println("Connection Accepted");
		}catch(IOException e){
			System.err.println("Accept failed.");
            System.exit(1);
		}
		
		PrintWriter out = new PrintWriter(clientsocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader( new InputStreamReader(clientsocket.getInputStream()));
		
		String outline,inline;
		ReadWriteProtocol rw=new ReadWriteProtocol();
		
		/*outline = rw.processInput("client connect");
        out.println(outline);*/
 
        while ((inline = in.readLine()) != null) {
        	
        	if(inline.equals("terminate")){
        		canterminate=true;
        		out.println("success");
        		break;
        	}else{
             outline = rw.processInput(inline);
			 System.out.println("Server:"+outline);
             out.println(outline);
        	}
            /* if (outline.equals("Bye."))
                break;*/
        }
		
		out.close();
        in.close();
		clientsocket.close();
		serversocket.close();
		
		}
       
        
        
		
		
	}
	
	
	

}
