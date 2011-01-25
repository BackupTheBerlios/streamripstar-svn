package control;

/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import misc.StreamRipStar;

/**
 * @author Johannes Putzke
 * With this class you get all Methods to log our outputs
 */
public class SRSOutput 
{
	
	public static enum LOGLEVEL{Nothing,Error,Normal,Debug};
	private int logLevel = 1;			// 1 means loglevel = normal
	private SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd '-' HH:mm:ss ");
	private static SRSOutput instance = null;
	private BufferedWriter writer = null;
	
	
	/**
	 * This Constructor initalizise the outputstream writer an
	 */
	private SRSOutput()
	{
		String path = new Control_GetPath().getStreamRipStarPath() + "/output.log";
		
		try 
		{
			writer = new BufferedWriter(new FileWriter(path,true));
			log("StreamRipStar in version "+StreamRipStar.releaseVersion+
					" and revision "+ StreamRipStar.releaseRevision + " has been started");	
		}
		catch (IOException e) 
		{
			System.err.println("Error in SRSOutput: Can't open the file: "+path);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Get the object from this class. If no one exist, one will created from this method.
	 * @return The log-object
	 */
	public static SRSOutput getInstance()
	{
		if(instance == null)
		{
			instance = new SRSOutput();
		}
		
		return instance;
	}
	
	
	/**
	 * Set the loglevel for this session. All logs with higher priority will
	 * be logged, too. If a wrong value is given, the loglevel normal is used
	 * 
	 * @param loglevel the maximum loglevel
	 */
	public void setLoglevel(LOGLEVEL loglevel)
	{
		if(loglevel == LOGLEVEL.Nothing)
		{
			logLevel = -1;
		}
		
		else if(loglevel == LOGLEVEL.Error)
		{
			logLevel = 0;
		}
		
		else if(loglevel == LOGLEVEL.Normal)
		{
			logLevel = 1;
		}
		
		else if(loglevel == LOGLEVEL.Debug)
		{
			logLevel = 2;
		}
		
		else
		{
			logLevel = 1;
		}
	}
	
	/**
	 * get the loglevel for this session. All logs with higher priority will
	 * be logged, too. If a wrong value is given, the loglevel normal is used
	 * 
	 * @return the current loglevel
	 */
	public LOGLEVEL getLoglevel()
	{
		if(logLevel == -1)
		{
			return LOGLEVEL.Nothing;
		}
		
		else if(logLevel == 0)
		{
			return LOGLEVEL.Error;
		}
		
		else if(logLevel == 1)
		{
			return LOGLEVEL.Normal;
		}
		
		else if(logLevel == 2)
		{
			return LOGLEVEL.Debug;
		}
		
		else
		{
			return LOGLEVEL.Normal;
		}
	}
	
	
	/**
	 * Normal log to output file and to the console. The message to the log file
	 * gets the current date+time and the message-format (here normal output)
	 * 
	 * @param logMessage The message you want to log
	 * @return true, if everything worked. Else false
	 */
	public synchronized boolean log(String logMessage)
	{
		if(logLevel >= 1) {
			try {
				//first print to console
				System.out.println("Message: "+logMessage);
				
				//then to the file
				writer.write("Message: "+formatter.format(new Date())
										+":\n"+logMessage+"\n\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
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
		if(logLevel >= 0) {
			try {
				//first print to console
				System.err.println("Error: "+logMessage);
				
				//then to the file
				writer.write("Error: "+formatter.format(new Date())
										+":\n"+logMessage+"\n\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
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
		if(logLevel >= 2) {
			try {
				//first print to console
				System.out.println("Debug: "+logMessage);
				
				//then to the file
				writer.write("Debug: "+formatter.format(new Date())
										+":\n"+logMessage+"\n\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
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
		SRSOutput.getInstance().logD("sollte nicht zu sehen sein");
		SRSOutput.getInstance().setLoglevel(LOGLEVEL.Error);
		SRSOutput.getInstance().logE("sollte da sein");
		SRSOutput.getInstance().log("sollte nicht zu sehen sein");
		SRSOutput.getInstance().logD("sollte nicht zu sehen sein");
		SRSOutput.getInstance().setLoglevel(LOGLEVEL.Debug);
		SRSOutput.getInstance().logD("sollte da sein");
	}
}
