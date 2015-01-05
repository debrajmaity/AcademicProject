
	 import java.io.*;
	 import java.text.*;
	 import java.util.*;

public class Log {
	     protected static String defaultLogFile = "log.txt";
	    
	         public synchronized static void write(String s) {
	         try {
				write(defaultLogFile, s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	    
	         public static void write(String f, String s) throws IOException {
	         TimeZone tz = TimeZone.getTimeZone("EST"); // or PST, MID, etc ...
	         Date now = new Date();
	         DateFormat df = new SimpleDateFormat ("yyyy.mm.dd hh:mm:ss ");
	         df.setTimeZone(tz);
	         String currentTime = df.format(now);
	        
	         FileWriter aWriter = new FileWriter(f, true);
	         aWriter.write(currentTime + " " + s + "\n");
	         aWriter.flush();
	         aWriter.close();
	     }
}



