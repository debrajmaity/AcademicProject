//package com.utd.aos;


import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class ReadWriteProtocol {
	

private static int clientscompleted=0;

private static ArrayList<Integer> whichclientcompleted=new ArrayList<Integer>();
public static void main(String arg[]) throws IOException, InterruptedException{
	
	/*ReadWriteProtocol readwritetest=new ReadWriteProtocol();
	String status=readwritetest.processInput("writeclient~writing at an offset~4");
	System.out.println("status:"+status);
	
	/*status=readwritetest.processInput("readclient");
	System.out.println("status:"+status);*/
	
	//status=sendWriterequest
}
	
	
public String processInput(String input) throws IOException, InterruptedException{
	String output=null;
	String hostname1="";
	String hostname2="";
	
	System.out.println("SERVERID:"+Server.ServerID);
	
	if(Server.ServerID==1){
		hostname1=this.getservername(2);
		hostname2=this.getservername(3);
	}else if(Server.ServerID==2){
		hostname1=this.getservername(1);
		hostname2=this.getservername(3);
	}else if(Server.ServerID==3){
		hostname1=this.getservername(1);
		hostname2=this.getservername(2);
	}
	
	if(input.startsWith("writecomplete")){
		StringTokenizer st = new StringTokenizer(input,":");
		st.nextToken();
		
		int ClientID=Integer.parseInt(st.nextToken());
		whichclientcompleted.add(ClientID);
		clientscompleted++;
		
		if(clientscompleted==7){
			sendterminatecommand();
		}
	  
	}else if(input.startsWith("readclient")){
	
		StringTokenizer st = new StringTokenizer(input,"~");
		st.nextToken();
		//String content=st.nextToken();
		String filename=st.nextToken();
		int offset=Integer.parseInt(st.nextToken());
		System.out.println("filename:"+filename);
		System.out.println("readoffset:"+offset);
		// Read request from client
		output=readFile(filename,offset);
		//return output;
	}else if (input.startsWith("writeclient")){
		
		//Write request from client
		String org_input=input;
		
		StringTokenizer st = new StringTokenizer(input,"~");
		st.nextToken();
		String filename=st.nextToken();
		String content=st.nextToken();
		
		int offset=Integer.parseInt(st.nextToken());
		System.out.println("offset:"+offset);
		System.out.println("content:"+content);
		output=writeFile(filename,content);
		System.out.println("OUTPUT:"+output);
		String output1,output2,output3;
		if(output.equalsIgnoreCase("success")){
		//send write request to the other server
			input=org_input.replace("writeclient~", "writeserver~");
			System.out.println("input:"+input);
			
			output1=sendWriterequest(input,hostname1,4545);
			
			System.out.println("Output1:"+output1);
			if(output1.equalsIgnoreCase("success")){
			
				input=org_input.replace("writeclient~", "writeserver~");
				System.out.println("input3:"+input);
				output2=sendWriterequest(input,hostname2,4545);
				
				System.out.println("output2:"+output2);
				if(output2.equalsIgnoreCase("success")){
					output="success";
				}else
					output="Server Write Error: All server couldn't be updated";
			}else
				output="Server Write Error: All server couldn't be updated";
				
		}else{
			output="Server Write Error: All server couldn't be updated";
		}
		//output3=sendWriterequest(input,"net03.utdallas.edu",4545);
		
	}else if (input.startsWith("writeserver")){
		
		//Write request from server
		
		StringTokenizer st = new StringTokenizer(input,"~");
		st.nextToken();
		String filename=st.nextToken();
		String content=st.nextToken();
		
		int offset=Integer.parseInt(st.nextToken());
		System.out.println("offset:"+offset);
		System.out.println("content:"+content);
		output=writeFile(filename,content);
		
		
	}
	
	 return output;
	
}

private String readFile(String filename,int offset){
	String output="";
	File file=new File("file/"+filename);
	
	if(!file.exists()){
	  	 output="File Not found!";
	  	 return output;
	}else{
		
		if(offset==-1){
				try{
					 
					  FileInputStream filestream = new FileInputStream(file);
					  
					  DataInputStream datastream = new DataInputStream(filestream);
					  BufferedReader br_reader = new BufferedReader(new InputStreamReader(datastream));
					  String strLine;
					  //Read File Line By Line
					  while ((strLine = br_reader.readLine()) != null)   {
					  // Print the content on the console
						  output=output+strLine+"\n";
					  }
					  //Close the input stream
					  datastream.close();
					  return output;
					}catch (Exception e){//Catch exception if any
						   System.err.println("Error: " + e.getMessage());
						   output="";
						   output="File Read error";
						   return output;
					  }
			}else{
					 try{
					 RandomAccessFile file_offset = new RandomAccessFile(file, "r");
					 if(offset>file.length())
							offset=(int)file.length();
					 file_offset.seek(offset);
					 byte[] buffer=new byte[(int)(file.length()-offset)];
          
					 file_offset.readFully(buffer, 0, (int)(file.length()-offset));
					 output = new String(buffer);
					 return output;
					 }catch(Exception e){
						   System.err.println("Error: " + e.getMessage());
						   output="";
						   output="File Read error";
						   return output;
					 }
					 
			}
		}
	
	}

/*private String writeFile(String filename,String content, int offset){
	//String out="";
	
	  try {
		     
          File file = new File("file/"+filename);
		  if(offset==-11){
			file.createNewFile();
		  }else{
          if(!file.exists()){
        	  return "file not found";
          }}
          RandomAccessFile file_offset = new RandomAccessFile(file, "rw");
			
		  
          if(offset>file.length()||offset==-1||offset==-11)
          	offset=(int)file.length();
          file_offset.seek(offset);
          //System.out.println("filesize"+file.length());
          byte[] buffer=new byte[(int)(file.length()-offset)];
          
          file_offset.readFully(buffer, 0, (int)(file.length()-offset));
          //System.out.println("buffer:"+buffer);
          // move the pointer to the offset
          file_offset.seek(offset);
         
          file_offset.writeBytes(content);
          file_offset.write(buffer);
          //System.out.println("filesize"+file.length());
          file_offset.close();
		  System.out.println("lengthafterwriting:"+file.length());
          return "success";
        
      } catch (IOException e) {
          System.out.println("IOException:"+e.getLocalizedMessage());
          //e.printStackTrace();
          return "Server write error";
      }
	
	
	
	
}*/


private String writeFile(String f, String s) throws IOException {
       
    FileWriter aWriter = new FileWriter(f, true);
    aWriter.write(s + "\n");
    aWriter.flush();
    aWriter.close();
    System.out.println("Writing to this server successful");
    return "success";
}

private String getservername(int serverid){
	
	String hostnm="";
	if(serverid==1){
		
		hostnm="net10.utdallas.edu";
	}else if(serverid==2){
		hostnm="net11.utdallas.edu";
	}else if(serverid==3){
		hostnm="net12.utdallas.edu";
	}
	
	System.out.println("GETHOSTNAME:"+hostnm);
	return hostnm;
	
}


	
protected String sendWriterequest(String input, String hostip, int port) throws IOException, InterruptedException {
	
	// Write a client program which connects to other servers and send the write request
	
	    Socket clientsocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
 
        try {
        	//int port=4545;
            clientsocket = new Socket(hostip, port);
            out = new PrintWriter(clientsocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Error connecting Host:"+e.getMessage());
			return "Error connecting Host";
            //System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O exception while connecting to the host"+e.getMessage());
			return "I/O connection Error";
           // System.exit(1);
        }
 
		System.out.println(" Connected to machine ip....."+ hostip);
        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
		//fromServer = in.readLine();
		/*if(!fromServer.isEmpty())
			System.out.println("Server: " + fromServer);*/
			
		fromUser = input;
        if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser);
        }
        while ((fromServer = in.readLine()) != null) {
			
            //System.out.println("Server: " + fromServer);
            /*if (fromServer.equals("Bye."))
                break;*/
            //fromUser = stdIn.readLine();
			
		
		if(fromServer.isEmpty())
				continue;
			else
			  break;
		
		//Thread.currentThread().sleep(100);
        }
		
		out.close();
        in.close();        
        clientsocket.close();
		return fromServer;
		
    }


	public void sendterminatecommand(){
		
		
		
		
		String input="terminate";
		String hostname="";
		int portnum=0;
		
				
		try {
			
			//Send terminate requests to all seven clients
			
			hostname=NodeConfig.getclienthostname(1);	//Connect to client1
			portnum=NodeConfig.getClientport(1);
			sendWriterequest(input,hostname,portnum);
			sendWriterequest(input,hostname,portnum+2);
			hostname=NodeConfig.getclienthostname(2);	//Connect to client2
			portnum=NodeConfig.getClientport(2);
			sendWriterequest(input,hostname,portnum);
			sendWriterequest(input,hostname,portnum+2);
			hostname=NodeConfig.getclienthostname(3);	//Connect to client3
			portnum=NodeConfig.getClientport(3);
			sendWriterequest(input,hostname,portnum);
			sendWriterequest(input,hostname,portnum+2);
			hostname=NodeConfig.getclienthostname(4);	//Connect to client4
			portnum=NodeConfig.getClientport(4);
			sendWriterequest(input,hostname,portnum);
			sendWriterequest(input,hostname,portnum+2);
			hostname=NodeConfig.getclienthostname(5);	//Connect to client5
			portnum=NodeConfig.getClientport(5);
			sendWriterequest(input,hostname,portnum);
			sendWriterequest(input,hostname,portnum+2);
			hostname=NodeConfig.getclienthostname(6);	//Connect to client6
			portnum=NodeConfig.getClientport(6);
			sendWriterequest(input,hostname,portnum);
			sendWriterequest(input,hostname,portnum+2);
			hostname=NodeConfig.getclienthostname(7);	//Connect to client7
			portnum=NodeConfig.getClientport(7);
			sendWriterequest(input,hostname,portnum);
			sendWriterequest(input,hostname,portnum+2);
			
			//Send ternimate request to Server
			sendWriterequest(input,"net10.utdallas.edu",4545);
			sendWriterequest(input,"net11.utdallas.edu",4545);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Server.canterminate=true;
		
		
	}
	
	
	public void delay(){
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}


