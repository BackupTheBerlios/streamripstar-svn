package control;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Control_PlayList
{
	public Control_PlayList()
	{
		
	}
	
	//returns a String that contains the streamdata
	// String[i][0] = Title
	// String[i][1] = Address
	public String[][] anlyseFile(String path)
	{

		File x = new File(path);
		SRSOutput.getInstance().log("Loading file ....");
		String[][] streams = null;
		Vector<String> titleVector = new Vector<String>(0,1);	//saves the Streamnames tmp
		Vector<String> addressVector = new Vector<String>(0,1);	//saves the Streamaddress tmp
		Vector<String> websiteVector = new Vector<String>(0,1);
		
		if(x.exists())
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(path));
				SRSOutput.getInstance().log("Successful loaded ....");
				
				String readBuffer = "";
				String address = "";
				String title = "";
				String website = "";
				
				if(path.endsWith(".pls"))
				{
					//Todo: filter for .mp3 .wma etc.
					while ((readBuffer = br.readLine())!= null)
					{
						if(!address.equals("") && readBuffer.startsWith("File"))
						{
							//add name, address temporary in Vector
							titleVector.addElement(address);
							addressVector.addElement(title);
							websiteVector.addElement(website);
							
							//reset address; title
							address = "";
							title = "";
							website = "";
						}
						
						if(readBuffer.trim().startsWith("File"))
						{
							address = readBuffer.substring(readBuffer.indexOf("=")+1, readBuffer.length());
						}
						
						if(readBuffer.trim().startsWith("Title"))
						{
							title = readBuffer.substring(readBuffer.indexOf("=")+1, readBuffer.length());
						}
						
						if(readBuffer.trim().startsWith("Website"))
						{
							website = readBuffer.substring(readBuffer.indexOf("=")+1, readBuffer.length());
						}
					}
					//add the last one
					titleVector.addElement(address);
					addressVector.addElement(title);
					websiteVector.addElement(website);
					
					//copy in to an String[][]
					
					streams = new String[titleVector.capacity()][3];
					
					for(int i=0;i<streams.length;i++)
					{
						streams[i][0] = titleVector.get(i).toString();
						streams[i][1] = addressVector.get(i).toString();
						streams[i][2] = websiteVector.get(i).toString();
					}
					return streams;
				}
				
				else if(path.endsWith("m3u"))
				{
					//first look if it is an extended m3u 
					readBuffer = br.readLine();
					
					//its an extm3u playlist
					if (readBuffer.trim().equals("#EXTM3U"))
					{
						while ((readBuffer = br.readLine())!= null)
						{
							if(readBuffer.trim().startsWith("#EXTINF:"))
							{
								address = readBuffer.substring(readBuffer.indexOf(",")+1, readBuffer.length());
								
								//And now read the next line and ignore all comments
								while((readBuffer = br.readLine()) != null)
								{
									if(readBuffer.trim().startsWith("#EXTSTREAMRIPSTAR:"))
									{
										website = readBuffer.substring(readBuffer.indexOf("=")+1, readBuffer.length());
									}
									
									if(!(readBuffer.trim().startsWith("#")))
									{
										title = readBuffer;
										break;
									}
								}
								titleVector.add(title);
								addressVector.add(address);
								websiteVector.add(website);
								title = "";
								address = "";
								website = "";
							}
						}
						//copy in to an String[][]
						
						streams = new String[titleVector.capacity()][3];
						
						for(int i=0;i<streams.length;i++)
						{
							streams[i][0] = titleVector.get(i).toString();
							streams[i][1] = addressVector.get(i).toString();
							streams[i][2] = websiteVector.get(i).toString();
						}
						return streams;
					}
					//its not an extm3u -> a normal m3u
					else
					{	
						if(!readBuffer.startsWith("#")|| !readBuffer.trim().equals(""))
						{
							titleVector.add("");
							addressVector.add(readBuffer);
							websiteVector.add("");
						}
						
						while((readBuffer = br.readLine())!=null)
						{
							if(!readBuffer.startsWith("#")|| !readBuffer.trim().equals(""))
							{
								titleVector.add("");
								addressVector.add(readBuffer);
								websiteVector.add("");
							}
							
							while((readBuffer = br.readLine())!=null)
							{
								if(!readBuffer.startsWith("#")|| !readBuffer.trim().equals(""))
								{
									titleVector.add("");
									addressVector.add(readBuffer);
									websiteVector.add("");
								}
							}
						}
						
						//copy in to an String[][]
						streams = new String[titleVector.capacity()][3];
						
						for(int i=0;i<streams.length;i++)
						{
							streams[i][1] = titleVector.get(i).toString();
							streams[i][0] = addressVector.get(i).toString();
							streams[i][2] = websiteVector.get(i).toString();
						}
						return streams;
					}
				}
				
				br.close();
			}
			catch ( FileNotFoundException e ) 
			{ 
			  SRSOutput.getInstance().logE( "Datei nicht gefunden" ); 
			}
			catch ( IOException e ) 
			{ 
			  SRSOutput.getInstance().logE( "I/O failed." ); 
			}
		}
		else
			SRSOutput.getInstance().log("Could not find file");
		return streams;
	}
	
	//You have in data[i] the 2 entries:
	//data[i][0]=Title
	//data[i][1]=address
	//data[i][2]=website
	//
	//The return String contains a "ready to write in file" String
	public String getPLSData(String[][] data)
	{
		String filteredData  = "";
		//First Line must be an indicator:
		filteredData += "[playlist]\n";
		//Set number of entries
		filteredData += "NumberOfEntries="+data.length+"\n";
		
		//add Data: File[n] and Title[n]
		for(int i=0;i<data.length;i++)
		{
			//e.g: "File1 = http://niceStram.com:8000"
			filteredData += "File"+(i+1)+"="+data[i][1]+"\n";
			//e.g: "Title1 = Nice Stream - 128Kbits"
			filteredData += "Title"+(i+1)+"="+data[i][0]+"\n";
			//this is only for StreamRipStar one. Hope all players
			//can read or ignore it
			filteredData += "Website"+(i+1)+"="+data[i][2]+"\n";
		}
		
		return filteredData;
	}
	
	//You have in data[i] the 2 entries:
	//data[i][0]=Title
	//data[i][1]=address
	//data[i][2]=website
	//
	//The return String contains a "ready to write in file" String
	public String getM3UData(String[][] data)
	{
		//StreamRipStar uses the extended m3u format to save m3u 
		String filteredData  = "";
		//first must be the extended indicator
		filteredData  += "#EXTM3U"+"\n";
		
		//add Data: File[n] and Title[n]
		for(int i=0;i<data.length;i++)
		{
			//  #EXTINF:fileLength in seconds ,Title
			// "#EXTINF:0,"Nice Stream - 128Kbits"
			filteredData += "#EXTINF:0,"+data[i][0]+"\n";
			// This is only for StreamRipStar:
			// add website EXTSTREAMRIPSTAR: Website
			filteredData += "#EXTSTREAMRIPSTAR:Website="+data[i][2]+"\n";
			//e.g: address only
			filteredData += data[i][1]+"\n";
		}
		
		return filteredData;
	}
}