package control;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import misc.StreamRipStar;

/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

/**
 * @author Johannes Putzke
 * With this class you get all Methods to log our outputs
 */
public class SRSOutput 
{
	private static SRSOutput instance = null;
	private BufferedWriter writer = null;
	
	public static String logLevel_e = "e";
	public static String logLevel_d = "d";
	public static String logLevel_i = "i";
	
	/**
	 * This Constructor initalizise the outputstream writer an
	 */
	private SRSOutput()
	{
		String path = new Control_GetPath().getStreamRipStarPath() + "/output.log";
		
		try 
		{
			writer = new BufferedWriter(new FileWriter(path));
			log("StreamRipStar in version "+StreamRipStar.releaseVersion+
					" and revision "+ StreamRipStar.releaseRevision + " has been started");	
		}
		catch (IOException e) 
		{
			System.err.println("Error in SRSOutput: Can't open the file: "+path);
			e.printStackTrace();
		}
	}
	
	public static SRSOutput getInstance()
	{
		if(instance == null)
		{
			instance = new SRSOutput();
		}
		
		return instance;
	}
	
	/**
	 * Normal log to output file and to the console. The message to the log file
	 * gets the current date+time and the messageformat (here normal output)
	 * 
	 * @param logMessage The message you want to log
	 * @return true, if everything worked. Else false
	 */
	public synchronized boolean log(String logMessage)
	{
		try {
			//first print to console
			System.out.println("Message: "+logMessage);
			
			//then to the file
			writer.write("Message: "+DateFormat.getDateInstance(DateFormat.LONG).format(new Date())
									+":\n"+logMessage+"\n\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Error log to output file and to the console. The message to the log file
	 * gets the current date+time and the messageformat (here Error output)
	 * 
	 * @param logMessage The error message you want to log
	 * @return true, if everything worked. Else false
	 */
	public synchronized boolean logE(String logMessage)
	{
		try {
			//first print to console
			System.err.println("Error: "+logMessage);
			
			//then to the file
			writer.write("Error: "+DateFormat.getDateInstance(DateFormat.LONG).format(new Date())
									+":\n"+logMessage+"\n\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Debug log to output file and to the console. The message to the log file
	 * gets the current date+time and the messageformat (here Debug output)
	 * 
	 * @param logMessage The error message you want to log
	 * @return true, if everything worked. Else false
	 */
	public synchronized boolean logD(String logMessage)
	{
		try {
			//first print to console
			System.out.println("Debug: "+logMessage);
			
			//then to the file
			writer.write("Debug: "+DateFormat.getDateInstance(DateFormat.LONG).format(new Date())
									+":\n"+logMessage+"\n\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * For testing
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		SRSOutput.getInstance().log("Das ist eine Testnachricht");
		SRSOutput.getInstance().log("Das ist eine Testnachricht und zwar die 2. Mal sehen\n wie sie mit Zeilenumbruch aussieht");
		SRSOutput.getInstance().logE("Das ist eine Errornachricht");
	}
}
