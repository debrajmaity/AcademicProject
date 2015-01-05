import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class test {

	
public static void main(String args[]){

	String getime1=getCurrenttime();	
	
	delay();
	delay();
	
	String getime2=getCurrenttime();
	
	
	System.out.println("Time difference:"+ calculatetimeelapsed(getime1,getime2));
}	

public static long calculatetimeelapsed(String time1,String time2){
	
	 TimeZone tz = TimeZone.getTimeZone("EST"); // or PST, MID, etc ...
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss ");
    format.setTimeZone(tz);
    Date date1;
    Date date2;
    long difference=0;
	try {
		date1 = format.parse(time1);
		date2 = format.parse(time2);
        
        difference = date2.getTime() - date1.getTime(); 
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return difference;
   
}


public static String getCurrenttime(){
	
	TimeZone tz = TimeZone.getTimeZone("EST"); // or PST, MID, etc ...
   Date now = new Date();
   DateFormat df = new SimpleDateFormat ("hh:mm:ss ");
   df.setTimeZone(tz);
   String currentTime = df.format(now);
   
   return currentTime;
}

public static void delay(){
	try {
		Thread.currentThread().sleep(100);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
}
