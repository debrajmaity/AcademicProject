import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class Client {

	/**
	 * @param args
	 */
	
	private static int clientID;
	
	private static HashMap<Integer,Integer> quorumset;
	
	private static int clockcounter=0;
	
	
	private static boolean canSendRequestMessage=true;
	
	private static int MessageCounter=40;
	
	private static boolean LOCKED=false;
	
	private static String CurrentLockedMessage="";
	
	private static boolean Semaphore=false;
	
	private static boolean sentInquire=false;
	
	public static int countREQUEST=0;
	public static int countLOCKED=0;
	public static int countRELEASE=0;
	public static int countRELINQUISH=0;
	public static int countFAILED=0;
	public static int countINQUIRE=0;
	
	private ArrayList<String> INQUIRELIST=new ArrayList<String>();
	
	public static Client clientnd;
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		int delay=0;
		String ipadd="";
		try {
			ipadd = InetAddress.getLocalHost().getHostName();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Client cl=null;
		/*if(ipadd.equals("net01.utdallas.edu")){
			clientnd=new Client(1);
			delay=8000;
		}else if(ipadd.equals("net02.utdallas.edu")){
			clientnd=new Client(2);
			delay=7500;
		}else if(ipadd.equals("net03.utdallas.edu")){
			clientnd=new Client(3);
			delay=7000;
		}else if(ipadd.equals("net04.utdallas.edu")){
			clientnd=new Client(4);
			delay=6500;
		}else if(ipadd.equals("net05.utdallas.edu")){
			clientnd=new Client(5);
			delay=6000;
		}else if(ipadd.equals("net06.utdallas.edu")){
			clientnd=new Client(6);
			delay=5500;
		}else if(ipadd.equals("net07.utdallas.edu")){
			clientnd=new Client(7);
			delay=5000;
		}*/
		
		clientnd=new Client(2);
		delay=1000;
		
		
		clientnd.createServer();
		
		try {
			Thread.currentThread().sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(MessageCounter!=0){
			clientnd.SendRequestMessage();
			MessageCounter--;
		}
		
		/*new Thread()
		{
				    public void run() {
				        while(true){
				        	
				        	//System.out.println("Thread for processing Messages started");
				        	clientnd.processMessageQueue();
				        	
				        	
				        }
				    }
		}.start();*/
		
		
	}
	
	
	public Client(int id){
		clientID=id;
		
		if(clientID==1){
			quorumset= new HashMap<Integer,Integer>(3);
		}else if(clientID==2){
			quorumset= new HashMap<Integer,Integer>(4);
		}else if(clientID==3){
			quorumset= new HashMap<Integer,Integer>(3);
			
		}else if(clientID==4){
			
			quorumset= new HashMap<Integer,Integer>(4);
			
		}else if(clientID==5){
			
			quorumset= new HashMap<Integer,Integer>(4);
		}else if(clientID==6){
			quorumset= new HashMap<Integer,Integer>(3);
		}else if(clientID==7){
			quorumset= new HashMap<Integer,Integer>(4);
		}
	}
	
	public void createServer(){
		
		int portnum=NodeConfig.getClientport(clientID);
		new clientServer(portnum).start();
	}
	
	public void connectClient(String message){
		String hostname="";
		int portnum=0;
			
		hostname=NodeConfig.getclienthostname(clientID);	//Connect to Self
		portnum=NodeConfig.getClientport(clientID);
		new clientConnection(hostname,portnum,message).start();
		
		if(clientID==1){
				
				hostname=NodeConfig.getclienthostname(clientID+1);	//Connect to Client2
				portnum=NodeConfig.getClientport(clientID+1);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID+3);	//Connect to Client4
				portnum=NodeConfig.getClientport(clientID+3);
				new clientConnection(hostname,portnum,message).start();
				
				
			}else if(clientID==2){
				hostname=NodeConfig.getclienthostname(clientID+1);	//Connect to Client3
				portnum=NodeConfig.getClientport(clientID+1);     //Change the value clientID+1
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID+2);	//Connect to Client4
				portnum=NodeConfig.getClientport(clientID+2);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID+5);	//Connect to Client7
				portnum=NodeConfig.getClientport(clientID+5);
				new clientConnection(hostname,portnum,message).start();
				
				
			}else if(clientID==3){
				
				hostname=NodeConfig.getclienthostname(clientID-2);	//Connect to Client1
				portnum=NodeConfig.getClientport(clientID-2);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID+4);	//Connect to Client7
				portnum=NodeConfig.getClientport(clientID+4);
				new clientConnection(hostname,portnum,message).start();
				
			}else if(clientID==4){
				
				hostname=NodeConfig.getclienthostname(clientID+1);	//Connect to Client5
				portnum=NodeConfig.getClientport(clientID+1);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID+2);	//Connect to Client6
				portnum=NodeConfig.getClientport(clientID+2);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID+3);	//Connect to Client7
				portnum=NodeConfig.getClientport(clientID+3);
				new clientConnection(hostname,portnum,message).start();
				
				
			}else if(clientID==5){
				
				hostname=NodeConfig.getclienthostname(clientID-3);	//Connect to Client2
				portnum=NodeConfig.getClientport(clientID-3);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID-2);	//Connect to Client3
				portnum=NodeConfig.getClientport(clientID-2);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID+1);	//Connect to Client6
				portnum=NodeConfig.getClientport(clientID+1);
				new clientConnection(hostname,portnum,message).start();
				
				
			}else if(clientID==6){
				hostname=NodeConfig.getclienthostname(clientID-5);	//Connect to Client1
				portnum=NodeConfig.getClientport(clientID-5);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID-3);	//Connect to Client3
				portnum=NodeConfig.getClientport(clientID-3);
				new clientConnection(hostname,portnum,message).start();
						
			}else if(clientID==7){
				
				hostname=NodeConfig.getclienthostname(clientID-4);	//Connect to Client3
				portnum=NodeConfig.getClientport(clientID-4);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID-3);	//Connect to Client4
				portnum=NodeConfig.getClientport(clientID-3);
				new clientConnection(hostname,portnum,message).start();
				
				hostname=NodeConfig.getclienthostname(clientID-2);	//Connect to Client5
				portnum=NodeConfig.getClientport(clientID-2);
				new clientConnection(hostname,portnum,message).start();
				
				
			}
		
	}
	
	
	
	public HashMap<Integer,Integer> getQuorumState(int ClientID){
		
		return quorumset;
		
	}
	
	
	//Added synchronization
	public synchronized void setQuorumState(String message){
		
		int clientno=getClientID(message);
		String messagetype= getMessageType(message);
		
		int state=0;
		//System.out.println("setting QuorumState for incoming message "+ message); 
		 // In case Locked message is received state will be 1 and if FAILED TYPE is RECEIVED state will be -1
		if(messagetype.equals(NodeConfig.LOCKED)){
			state=1;
		}else if(messagetype.equals(NodeConfig.FAILED)){
			state=-1;
		}
		
		//System.out.println("setQuorumState: ClientID "+ clientno+ "State"+ state);
		
		//quorumset.put(clientno, state);	
		
		if(clientID==1){
			
			if(clientno==1||clientno==2||clientno==4){
			
				quorumset.put(clientno, state);
			}
				
		}else if(clientID==2){
			if(clientno==2||clientno==3||clientno==4||clientno==7){
				
				quorumset.put(clientno, state);
			}
			
		}else if(clientID==3){
			if(clientno==1||clientno==3||clientno==7){
				
				quorumset.put(clientno, state);
			}
			
		}else if(clientID==4){
			if(clientno==4||clientno==5||clientno==6||clientno==7){
				
				quorumset.put(clientno, state);
			}
			
		}else if(clientID==5){
			if(clientno==2||clientno==3||clientno==5||clientno==6){
				
				quorumset.put(clientno, state);
			}
			
		}else if(clientID==6){
			if(clientno==1||clientno==3||clientno==6){
				
				quorumset.put(clientno, state);
			}
			
		}else if(clientID==7){
			if(clientno==3||clientno==4||clientno==5||clientno==7){
				
				quorumset.put(clientno, state);
			}
			
		}
	}
	
	
	//Synchronization is not req here
	private boolean CheckClientFailedResponse(){
		
		int num=0;
		boolean response=false;
		
		Log.write("CheckClientFailedResponse quorum" + quorumset.values());
		
		for (Integer value : quorumset.values()) {
		    if(value==-1){
		    	response=true;
		    	break;
		    }
		}
		
		Log.write("CheckClientFailedResponse response" + response);
		return response;
		
	}
	
	private boolean CanEnterCriticalSection(){
		int num=0;
		boolean response=false;
		
		Log.write("CanEnterCriticalSection quorum" + quorumset.values());
		
		int MAXSIZE=getquorumsetsize();
		
		for (Integer value : quorumset.values()) {
		    if(value==1){
		    	response=true;
		    	num++;
		    }else{
		    	response=false;
		    	break;
		    }
		}
		
		//check if the quorum size has been reached it max
		//num++;
		if(num<MAXSIZE){
			response=false;
		}
		//System.out.println("CheckingCriticalSectionEntry:"+ num);
		
		Log.write("CanEnterCriticalSection response: " + response);
		return response;
	}
	
	
	//Get the size of the each of the quorum set based on the clientID
	private int getquorumsetsize(){
		int SIZE=0;
		
		if(clientID==1){
			
			//SIZE=2;
			SIZE=3;
			 			
		}else if(clientID==2){
			SIZE=4;
			//SIZE=2;
		}else if(clientID==3){
			SIZE=3;
		}else if(clientID==4){
			SIZE=4;
		}else if(clientID==5){
			SIZE=4;
		}else if(clientID==6){
			SIZE=3;
		}else if(clientID==7){
			SIZE=4;
		}
		
		return SIZE;
		
	} 
	
	//Send a message
	
	public synchronized void SendMessage(String output_message, int whichclient){
		
		String senderhostname=NodeConfig.getclienthostname(whichclient);	//Connect to Client6
		int clientportnum=NodeConfig.getClientport(whichclient);
		
		new clientConnection(senderhostname,clientportnum,output_message).start();
	        
			
	}
	
	// Send a Request Message
	
	public void SendRequestMessage(){
		String reqmsg=getMessageFormat(NodeConfig.REQUEST,clientID);
		connectClient(reqmsg);
	}
	
	
	public synchronized void ProcessIncomingMessage(String Message_IN){
		
		String Message_TYPE=this.getMessageType(Message_IN);
		int Message_Clock=this.getClockvalue(Message_IN);
		int Message_ClientID=this.getClientID(Message_IN);
		
		this.setClockCounter(Message_Clock);
		
		if(Message_TYPE.equals(NodeConfig.REQUEST)){
			int pos=PriorityQueue.add(Message_IN);
			countREQUEST++;
			if(!LOCKED){
				LOCKED=true;
				CurrentLockedMessage=PriorityQueue.poll();
				int getclientID=getClientID(CurrentLockedMessage);
				SendMessage(getMessageFormat(NodeConfig.LOCKED, clientID),getclientID);
				
				Log.write("Sent LOCKED Message to client "+ getclientID);
				
			}else{
				
				 int isPreceding=PriorityQueue.CompareMessage(CurrentLockedMessage, Message_IN);
				 
				 
				 if(isPreceding==0){
					 	String sendFailedMsg=this.getMessageFormat(NodeConfig.FAILED, clientID);
					 	this.SendMessage(sendFailedMsg, Message_ClientID);
					 	
					 	Log.write("Sent FAILED Message to current waiting due to higher incoming message client "+ Message_ClientID);
				 }else{
					 
					 if(!sentInquire){
						 sentInquire=true;
						 int getclientID=getClientID(CurrentLockedMessage);			//This can also be store in a static variable
						 String sendInquireMsg=this.getMessageFormat(NodeConfig.INQUIRE, clientID);
						 this.SendMessage(sendInquireMsg, getclientID);
						 
						 Log.write("Sent INQUIRY Message to client "+ getclientID);
						 
					 }else{
						 if(pos==0){
							    String sendFailedMsg=this.getMessageFormat(NodeConfig.FAILED, clientID);
							    for(int i=1;i<PriorityQueue.size();i++){
							    	int clientid=getClientID(PriorityQueue.get(i));
							    	this.SendMessage(sendFailedMsg, clientid);
							    	Log.write("Sent FAILED Message to current waiting message due to higher incoming message client "+ clientid);
							    }
							 	
							 	
						 }
					 }
				 }
				
				
			}
			
		}else if(Message_TYPE.equals(NodeConfig.LOCKED)){
			countLOCKED++;
			setQuorumState(Message_IN);
			if(CanEnterCriticalSection()){
				Writer output;
				try {
					output = new BufferedWriter(new FileWriter("input.txt",true));
					output.append("ClientID "+InetAddress.getLocalHost().getHostName()+" MessageCount: "+MessageCounter+"\n");
					output.append("REQUEST:"+countREQUEST+"\n");
					output.append("LOCKED:"+countLOCKED+"\n");
					output.append("RELEASED:"+countRELEASE+"\n");
					output.append("FAILED:"+countFAILED+"\n");
					output.append("RELINQUISH:"+countRELINQUISH+"\n");
					output.append("INQUIRE:"+countINQUIRE+"\n");
					output.close();
										
					//finishedCriticalSection=true;
					
					quorumset.clear();	
					
					//Send Release message to all the client in the quorum
					String releasemessage=getMessageFormat(NodeConfig.RELEASE,clientID);
					connectClient(releasemessage);				
					resetMessageCounts(); //  the place to count messages
					//Thread.currentThread().sleep(1000);
					
					Log.write("Sent RELEASE Message to all clients ");
					
					if(MessageCounter!=0){
						clientnd.SendRequestMessage();
						MessageCounter--;
						Log.write("Sent REQUEST Message to all clients in quorum ");
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}else if(Message_TYPE.equals(NodeConfig.FAILED)){
			countFAILED++;
			setQuorumState(Message_IN);
			
			for(String message : INQUIRELIST) {
	    		int clientnum= getClientID(message);
	    		SendMessage(getMessageFormat(NodeConfig.RELINQUISH,clientID),clientnum); 
	    		
	    		Log.write("Sent RELINQUISH Message to client " +clientnum );
			}
			
			INQUIRELIST.clear();
			
			
		}else if(Message_TYPE.equals(NodeConfig.INQUIRE)){
			countINQUIRE++;
			
			
	    	int clientnum=getClientID(Message_IN);
	    	if(CheckClientFailedResponse()){
	    		SendMessage(getMessageFormat(NodeConfig.RELINQUISH,clientID),clientnum);
	    		
	    		Log.write("Sent RELINQUISH Message to client " +clientnum );
	    	}else{
	    		INQUIRELIST.add(Message_IN);
	    	}
			
			
		}else if(Message_TYPE.equals(NodeConfig.RELEASE)){
			countRELEASE++;
			sentInquire=false;
			
			if(!PriorityQueue.isEmpty()){
				CurrentLockedMessage=PriorityQueue.poll();
    			int clientnum=getClientID(CurrentLockedMessage);
    			SendMessage(getMessageFormat(NodeConfig.LOCKED,clientID),clientnum);
    			LOCKED=true;
    			
    			Log.write("Sent LOCKED Message to client "+ clientnum + "after getting RELEASE message from client " + Message_ClientID);
    			
    		}else{
    			LOCKED=false;
    			CurrentLockedMessage="";
    		}
			
			
		}else if(Message_TYPE.equals(NodeConfig.RELINQUISH)){
			countRELINQUISH++;
			sentInquire=false;
	    	PriorityQueue.add(CurrentLockedMessage);
	    	
	    	CurrentLockedMessage=PriorityQueue.poll();
	    	int clientnum=getClientID(CurrentLockedMessage);
	    	
	    	SendMessage(getMessageFormat(NodeConfig.LOCKED,clientID),clientnum); 
			
	    	Log.write("Sent LOCKED Message to client "+ clientnum + "after getting reliquish message from client " + Message_ClientID);
			
		}
			
		
	} 
	
	
	public int getClockvalue(String msg){
		
		// message format "REQUEST:TIME_STAMP:CLIENTID"
		StringTokenizer message_token = new StringTokenizer(msg, ":");
	    String tokens[] = new String[message_token.countTokens()];
	    
	    int i=0;
	    while (message_token.hasMoreTokens()) {

	        tokens[i] = message_token.nextToken();
	        //System.out.println(tokens[i]);
	        i++;
	    }
	    
	    return Integer.parseInt(tokens[1]);
		
	}
	
	 public int getClientID(String msg){
		
		// message format "REQUEST:TIME_STAMP:CLIENTID"
		StringTokenizer message_token = new StringTokenizer(msg, ":");
	    String tokens[] = new String[message_token.countTokens()];
	    
	    int i=0;
	    while (message_token.hasMoreTokens()) {

	        tokens[i] = message_token.nextToken();
	        //System.out.println(tokens[i]);
	        i++;
	    }
	    
	    return Integer.parseInt(tokens[2]);
		
	}
	
	public String getMessageType(String msg){
		
		// message format "REQUEST:TIME_STAMP:CLIENTID"
		StringTokenizer message_token = new StringTokenizer(msg, ":");
	    String tokens[] = new String[message_token.countTokens()];
	    
	    int i=0;
	    while (message_token.hasMoreTokens()) {

	        tokens[i] = message_token.nextToken();
	        //System.out.println(tokens[i]);
	        i++;
	    }
	    
	    return tokens[0];
	}
	
	
	//Create the message format
	
	public synchronized String getMessageFormat(String requesttype,int Clientnum){
		
		
		int clck=setClockCounter(0);
				
		String message=requesttype+":"+clck+":"+Clientnum;
				
		return message;
	}
	
	
	public int getClockCounter(){
		
		return clockcounter;
	}
	
	public synchronized int setClockCounter(int remoteClock){
		
		if((remoteClock+1)>(clockcounter+1)){
			clockcounter=remoteClock+1;
		}else
			clockcounter++;
		
		return clockcounter;
	}
	
	public void resetMessageCounts(){
		countREQUEST=0;
		countLOCKED=0;
		countRELEASE=0;
		countRELINQUISH=0;
		countFAILED=0;
		countINQUIRE=0;
	}
	
	public void processMessageQueue()
	{
		System.out.println("checking Message present in MESSAGEQUEUE :" + MessageQueue.isEmpty() );
		
		if(MessageQueue.isEmpty()){
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}else{
			
			//Just check here if there is any synchronization problem
			String inmsg=MessageQueue.poll();
			System.out.println("processing Message from MESSAGEQUEUE :" + inmsg );
			
			Log.write("processing Message from MESSAGEQUEUE :" + inmsg);
			
			ProcessIncomingMessage(inmsg);
			System.out.println("Finished processing Message from MESSAGEQUEUE: " + inmsg);
			
			Log.write("Finished processing Message from MESSAGEQUEUE: " + inmsg);
			return;
			
			
		}
	}

}
