
public class NodeConfig {
	

	
	public static final String REQUEST="REQUEST";
	public static final String LOCKED="LOCKED";
	public static final String RELEASE="RELEASE";
	public static final String RELINQUISH="RELINQUISH";
	public static final String FAILED="FAILED";
	public static final String INQUIRE="INQUIRE";

	
	
	public static String getclienthostname(int NodeID){
		String hostname="";
		
		if(NodeID==1){
			
			//hostname="net01.utdallas.edu";
			hostname="localhost";
			
		}else if(NodeID==2){
		
			//hostname="net02.utdallas.edu";
			hostname="localhost";
			
		}else if(NodeID==3){
		
			//hostname="net03.utdallas.edu";
			hostname="localhost";
		
		} else if(NodeID==4){
			
			//hostname="net04.utdallas.edu";
			hostname="localhost";
			
		}else if(NodeID==5){
			//hostname="net05.utdallas.edu";
			hostname="localhost";
		}else if(NodeID==6){
			//hostname="net06.utdallas.edu";
			hostname="localhost";
			
		}else if(NodeID==7){
			//hostname="net07.utdallas.edu";
			hostname="localhost";
			
		}
		
		return hostname;
	}
	
	public static int getClientport(int clientNodeID2 ){
		int portnum=0;
		
		if(clientNodeID2==1){
			portnum=2000;
						
		}else if(clientNodeID2==2){
			portnum=2011;
							
		}else if(clientNodeID2==3){
			portnum=2022;
			
		}else if(clientNodeID2==4){
			portnum=2033;
			
		}else if(clientNodeID2==5){
			portnum=2044;
			
		}else if(clientNodeID2==6){
			portnum=2055;
			
		}else if(clientNodeID2==7){
			portnum=2066;
		}
		
		return portnum;
	}

}
