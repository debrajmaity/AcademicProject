import java.util.ArrayList;
import java.util.StringTokenizer;	


public class PriorityQueue {


private static ArrayList<String> priorityqueue=new ArrayList<String>();

//Add message format "REQUEST:TIME_STAMP:CLIENTID"
public static synchronized int add(String message){
	
	
	int position=0;
	
	StringTokenizer message_token = new StringTokenizer(message, ":");
    String tokens[] = new String[message_token.countTokens()];

    int i=0;
    while (message_token.hasMoreTokens()) {

        tokens[i] = message_token.nextToken();
        System.out.println(tokens[i]);
        i++;
    }
    
    if(priorityqueue.isEmpty()){
    	
    	priorityqueue.add(message);
    	position=priorityqueue.size()-1;
    	
    }else{
    	
    	int size=priorityqueue.size();
    	
    	int j=0;
    	boolean NOTINSERTED=true;
    	while(j<size){
    		
    		String message1=priorityqueue.get(j);
        	int value=CompareMessage(message,message1);
        	
        	if(value==0){
        		priorityqueue.add(j,message);
        		NOTINSERTED=false;
        		position=j;
        		break;
        	}else if(value==1){
        		j++;
        	}    		
    	}
    	
    	if(NOTINSERTED){
    		priorityqueue.add(message);
    		position=size-1;
    	}
    }
    
    System.out.println("Added message in WAITING QUEUE: "+message + " at position "+ position);
    
    return position;	
	
}


public synchronized static String poll(){
	
	if(priorityqueue.isEmpty()){
		return "EMPTY";
	}else{
		
		String element=priorityqueue.get(0);
		priorityqueue.remove(0);
		
		return element;
	}
}

public synchronized static String get(int index){
	if(!priorityqueue.isEmpty()){
		if(index<priorityqueue.size())
			return priorityqueue.get(index);
		else
			return "-1";
	}else
		return "EMPTY";
}


public static int size(){
	if(priorityqueue.isEmpty()){
		return -1;
	}else{
	    return priorityqueue.size();	
	}
}

public static boolean isEmpty(){
	return priorityqueue.isEmpty();
}


protected synchronized static int CompareMessage(String message1,String message2){
	
	StringTokenizer message_token1 = new StringTokenizer(message1, ":");
    String tokens1[] = new String[message_token1.countTokens()];
    
    int comparevalue=-1;
    int i=0;
    while (message_token1.hasMoreTokens()) {

        tokens1[i] = message_token1.nextToken();
        System.out.println(tokens1[i]);
        i++;
    }
    
    
    StringTokenizer message_token2 = new StringTokenizer(message2, ":");
    String tokens2[] = new String[message_token2.countTokens()];

    i=0;
    while (message_token2.hasMoreTokens()) {

        tokens2[i] = message_token2.nextToken();
        System.out.println(tokens2[i]);
        i++;
    }
    
    int val1=Integer.parseInt(tokens1[1]);
    int val2=Integer.parseInt(tokens2[1]);
    if(val1<val2){
    	comparevalue=0;   	
    }else if(val1>val2){
    	comparevalue=1;
    }else if(val1==val2){
    	
    	  int val11=Integer.parseInt(tokens1[2]);
    	  int val22=Integer.parseInt(tokens2[2]);
    	  
    	  if(val11<val22){
    		  comparevalue=0;
    	  }else {
    		  comparevalue=1;
    	  }
    	
    }
    
    return comparevalue;	
	
}


}
