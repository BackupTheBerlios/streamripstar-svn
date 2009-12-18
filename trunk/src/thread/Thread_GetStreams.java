package thread;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import gui.Gui_StreamBrowser;

import java.util.ResourceBundle;
import java.util.Vector;

/**
 * This gets the genres from the website
 * and put it show in Gui_StreamBrowser
 * @author eule
 *
 */
public class Thread_GetStreams extends Thread
{
	private boolean killMe = false;	//if true; ignore all other
	private Gui_StreamBrowser streamBrowser = null;
	private String genre = "";
	private Vector<String[]> streamsPG = null;
	private Vector<String[]> streamTmp = null;
	private ResourceBundle trans = null;
	
	public Thread_GetStreams(Gui_StreamBrowser streamBrowser,String genre,ResourceBundle trans) {
		this.streamBrowser = streamBrowser;
		this.genre = genre;
		this.trans = trans;
	}
	
	public void killMe() {
		killMe = true;
		streamBrowser.setStatusText("Try to abort fetching streams");
	}
	
	public void run() {
		//set the abort button enable for killing this thread
		streamBrowser.setAbortButtonEnable(true);
		
		//set status loading
		streamBrowser.setStatusText(trans.getString("GetStreams.loading"));
		
		//disable clicking on the genre
		streamBrowser.disableModelClick(true);
		
		//receive streams from site in Vector...
		if(!killMe) {
			streamBrowser.getControlHttp().getStreamsPerGenre(genre);
		}
		
		//...and get this vector
		if(!killMe) {
			streamsPG = streamBrowser.getControlHttp().getStreams();
		}
		
		//filter for bitrates
		if(!killMe) {
			//if filter checkbox is enabled -> filter
			if(streamBrowser.getFilterGui().filterBitratesIsEnabled()) {
				streamTmp = streamBrowser.getFilterGui().filterBitrates(streamsPG);
				if(streamTmp != null) {
					streamsPG = streamTmp;
				}
			}
		}
		
		//filter for Types
		if(!killMe) {
			if(streamBrowser.getFilterGui().filterTypesIsEnabled()) {
				if(streamBrowser.getFilterGui().filterTypesIsEnabled()) {
					streamsPG = streamBrowser.getFilterGui().filterTypes(streamsPG);
				}
			}
		}
		
		//remove all
		if(!killMe) {
			streamBrowser.removeAllFromTable(streamBrowser.getBrowseModel());
		}
		
		//set filtered 
		if(streamBrowser.getFilterGui().filterTypesIsEnabled() ||
				streamBrowser.getFilterGui().filterBitratesIsEnabled()) {
			streamBrowser.getFilterGui().setFiltered(true);
		} else {
			streamBrowser.getFilterGui().setFiltered(false);
		}
		
		
		if(!killMe) {
			//add stream recursive
			for(int i=0 ; i < streamsPG.capacity() ;i++) {
				if(killMe) {
					streamBrowser.getFilterGui().setFilterdStreamVector(streamsPG);
					break;
				}
					
				Object[] tmp = new Object[9];
				
				tmp[0] = i;						//nr
				tmp[1] = streamsPG.get(i)[2];	//genre
				tmp[2] = streamsPG.get(i)[0];	//description
				tmp[3] = streamsPG.get(i)[3];	//now Playing
				try {//Listeners
					tmp[4] = Integer.valueOf(streamsPG.get(i)[4].subSequence(0, 
							streamsPG.get(i)[4].indexOf("/")).toString());
					//max Listener
					tmp[5] = Integer.valueOf(streamsPG.get(i)[4].subSequence(
							streamsPG.get(i)[4].indexOf("/")+1,
							streamsPG.get(i)[4].length()).toString());
				} catch (NumberFormatException f) {
					tmp[4] = -1;
					tmp[5] = -1;
				}
				try{
					tmp[6] = Integer.valueOf(streamsPG.get(i)[5]);	//Bitrate
				} catch (NumberFormatException f) { tmp[6] = -1; }
				tmp[7] = streamsPG.get(i)[6];	//Type
				tmp[8] = streamsPG.get(i)[1];	//Website
					
				//add to model
				streamBrowser.getBrowseModel().addRow(tmp);
			}
		}
		if(!killMe) {
			//set the filtered vector as streamVector for later use
			streamBrowser.getFilterGui().setFilterdStreamVector(streamsPG);
		}
		
		//set the abort button disable
		streamBrowser.setAbortButtonEnable(false);
		
		//enable clicking on the genre
		streamBrowser.disableModelClick(false);
		
		//set new status
		if(killMe == true)
			streamBrowser.setStatusText(trans.getString("GetStreams.abort"));
		else
			streamBrowser.setStatusText(trans.getString("GetStreams.done"));
	}
}
