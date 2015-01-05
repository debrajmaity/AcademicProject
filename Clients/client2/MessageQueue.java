import java.util.ArrayList;
import java.util.StringTokenizer;	


public class MessageQueue {


private static ArrayList<String> Messagequeue=new ArrayList<String>();

//Add message format "REQUEST:TIME_STAMP:CLIENTID"
public static synchronized int add(String message){
	
	
	int position=0;
	
	Messagequeue.add(message);
	position=Messagequeue.size()-1;

    System.out.println("Added message in MESSAGE QUEUE: "+message);
    Log.write("Added message in MESSAGE QUEUE: "+message);
    
    return position;	
	
}


public static synchronized String poll(){
	
	if(Messagequeue.isEmpty()){
		return "EMPTY";
	}else{
		
		String element=Messagequeue.get(0);
		Messagequeue.remove(0);
		
		return element;
	}
}


public static int size(){
	if(Messagequeue.isEmpty()){
		return -1;
	}else{
	    return Messagequeue.size();	
	}
}

public static boolean isEmpty(){
	return Messagequeue.isEmpty();
}



}
