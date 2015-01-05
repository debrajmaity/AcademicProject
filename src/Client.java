import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Client {

	static Integer count = 0;
	static SendHelper sendObject[]= null;
	static int iClientId = 0;
	static Random rand = new Random();
	static int Quorumsize=4;
	static int[] countQuorum=new int[Quorumsize];	
	static NodeDetails arrNodeDetails[]=null;
	static int currentDataobj=0;
	static int currentReqID=0;
	static boolean canSendReq=true;
	static Integer readRequestSentTo = 0;
	static Integer countattempt=7;
	//This is based total number of quorum. It should be 16.

	public static void main(String[] args) {

		String stIpAddress = "";
		ServerSocket ss;
		Configuration confObj = null;

		try {

			confObj = new Configuration();
			stIpAddress = InetAddress.getLocalHost().getHostName();
			iClientId = confObj.getClientNodeId(stIpAddress);
			int DEFAULT_DATA_SOCKET_SIZE = 128 * 1024;
			InetAddress iaddr = InetAddress.getByName(stIpAddress);
			iaddr = InetAddress.getByAddress(stIpAddress, iaddr.getAddress());
			InetSocketAddress addr;
			addr = new InetSocketAddress(iaddr,confObj.getPortNumber(stIpAddress));
			ss = new ServerSocket();
			ss.bind(addr, 0);
			ss.setReceiveBufferSize(DEFAULT_DATA_SOCKET_SIZE);

			//Changed the nodedetail array
			arrNodeDetails = confObj.getServerDetails();
			sendObject=new SendHelper[arrNodeDetails.length+1];
			for(int i =0;i<arrNodeDetails.length;i++)
				sendObject[arrNodeDetails[i].iNodeId] = new SendHelper(arrNodeDetails[i].stIpAddress,arrNodeDetails[i].iPortNo,arrNodeDetails[i].iNodeId);		

			count = 1; 
			while( count < arrNodeDetails.length ) {
				sendObject[count].start();
				count++;
			}

			ClientRecvHelper recvHelperObj[]=new ClientRecvHelper[7];
			while(count < arrNodeDetails.length){
				recvHelperObj[count] = new ClientRecvHelper(ss);
				recvHelperObj[count].start();
				count++;
			}

			System.out.println("Client "+iClientId+" has established connection successfully");

			count =0;
			Message m=null;
			while(countattempt>0){
				synchronized(countattempt){
					//Randomly choose the data object on which read or write needs to be performed
					canSendReq=false;
					initialiseQuorumset();
					currentDataobj=genRandomNumber(3);
					currentReqID=genRandomNumber(9);

					if(currentReqID==1){
						//Send write message						
						m= new Message(iClientId,4,1,currentDataobj);	
						int cnt=1;
						while( cnt < arrNodeDetails.length ) {
							sendObject[cnt].WriteObject(m);
							cnt++;
						}
					}else{
						//Send Read message
						m= new Message(iClientId,1,0,currentDataobj);
						readRequestSentTo = genRandomNumber(7);
						sendObject[readRequestSentTo].WriteObject(m);

					}


					//Wait for 20 milliseconds
					waitTIMER();
					//waitTimer(2000);

				}
				continue;
			}

			System.exit(0);

		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public synchronized void recvInformation(Message m) throws IOException{
		synchronized(countattempt){
		int inReqID=m.RequestID;


		if(inReqID==1||inReqID==2){

			setQuorumList(m.NodeNo);
			if(hasQuorumstateCompleted(m.NodeNo)){
				//Wait for one time_unit and send the message
				waitTimer(100);
				Message m_out=null;
				if(inReqID==1){
					m_out=new Message(iClientId,2,0,currentDataobj);
					sendObject[readRequestSentTo].WriteObject(m_out);
				}
				else if(inReqID==2) {
					m_out=new Message(iClientId,5,0,currentDataobj);
					int cnt=0;
					while( cnt < arrNodeDetails.length ) {
						sendObject[cnt].WriteObject(m_out);
						cnt++;
					}
				}

				//Wait for another time_unit
				//waitTimer(100);
				countattempt--;
				canSendReq=true;
				currentDataobj=0;
				currentReqID=0;
			}
			}

		}
	}

	private static int genRandomNumber(int n){
		return rand.nextInt(n) + 1;

	}

	private static void waitTimer(int n){
		try {
			Thread.currentThread().sleep(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private boolean hasQuorumstateCompleted(int serverID){

		//Make below variables as static variables.
		//int[] quorumlist=getQuorumList(1);
		//int[] count=new int[]{0,0,0,0};

		for(int i=0;i<Quorumsize;i++){
			if(countQuorum[i]==getQuorumList(i+1).length)
				return true;
		}

		return false;
	}


	private int[] getQuorumList(int setnum){
		int[] list=null;
		if(setnum==1)
			list= new int[]{1,2,4,5};
		else if(setnum==2)
			list= new int[]{1,3,6,7};
		else if(setnum==3)
			list= new int[]{4,5,6,7};
		else if(setnum==4)
			list= new int[]{2,4,3,5};

		return list;
	}

	private void setQuorumList(int iServerId){

		for(int i=0;i<Quorumsize;i++){
			int setsize=(getQuorumList(i+1)).length;
			for(int j=0;j<setsize;j++){
				if((getQuorumList(i+1))[j]==iServerId){
					countQuorum[i]++;
					break;
				}
			}

		}	

	}

	private static void initialiseQuorumset(){


		for(int i=0;i<Quorumsize;i++){
			countQuorum[i]=0;
		}
	}

	private static void waitTIMER(){
		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {

				Message m=null;
				if(currentReqID==1){
					//Send write failure to all the server
					m= new Message(iClientId,6,0,currentDataobj);
					int cnt=0;
					while( cnt < arrNodeDetails.length ) {
						try {
							sendObject[cnt].WriteObject(m);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cnt++;
					}
				}else{
					//Send read failure to all the server
					m= new Message(iClientId,3,0,currentDataobj);

					try {
						sendObject[readRequestSentTo].WriteObject(m);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}



				//waitTimer(100);
				countattempt--;
				canSendReq=true;
				currentDataobj=0;
				currentReqID=0;
				initialiseQuorumset();


			}
		}, 2000);
	}

}
