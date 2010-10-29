package control;

import gui.Gui_searchUpdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import misc.StreamRipStar;

/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johanes Putzke*/
/* eMail: die_eule@gmx.net*/  

/**
 * Connect to the website where the information can be found for
 * new releases and more informations.
 */
public class SearchUpdate extends Thread {

	private boolean killThread = false;
	private String updateURLString = "http://streamripstar.berlios.de/streamripstar.update";
	private URL updateURL;
	private BufferedReader bw;
	private String text = "";
	private String revision = "";
	private String name = "";
	private String download = "";
	private Gui_searchUpdate gui;
	
	public SearchUpdate(Gui_searchUpdate gui) {
		this.gui = gui;
	}
	
	//start the thread
	public void run() {
		try {
			if(!killThread) {
				//create the url to the update file
				updateURL = new URL(updateURLString);
				
				//create and open the connection
				URLConnection connection = updateURL.openConnection();
				
				//must set the useragent to mozilla, else will receive the stream itselfs
				connection.addRequestProperty("User-Agent", "Mozilla/5.0");
				
				//open the reader to the update file
				bw = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				while(!killThread && (text = bw.readLine()) != null) {
					//look for the stable part
					if(text.equals("[Stable]")) {
						//now read the next information
						revision = bw.readLine().substring(9);
						name = bw.readLine().substring(5);
						download = bw.readLine().substring(9);
					}
				}
			}
			
			if(!killThread) {
				
				//look, if the revision is the same -> this version is up to date
				if(Integer.valueOf(revision) == StreamRipStar.releaseRevision) {
					gui.setAllOk();
				} 
				
				//look, if the version of StreamRipStar is a new one! -> this version is up to date
				else if(Integer.valueOf(revision) < StreamRipStar.releaseRevision) {
					gui.setNewVersion();
				} 
				
				//an new version is available
				else {
					gui.setNewVersionAvailable(revision, name, download);
				}
			}

		} catch (IOException e) {
			gui.setFailedToFetchInformation();
		} catch (NumberFormatException e) {
			
		} finally {
			try {
				if(bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				SRSOutput.getInstance().logE("Can't close the connection while fetching update information");
			}
		}
	}
	
	/**
	 * Stops this Thread
	 */
	public void killThread() {
		killThread = true;
	}
	
}
