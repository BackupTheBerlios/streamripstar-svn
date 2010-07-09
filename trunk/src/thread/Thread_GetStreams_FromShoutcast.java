package thread;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import gui.Gui_StreamBrowser2;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * This gets the genres from the website
 * and put it show in Gui_StreamBrowser
 * @author eule
 *
 */
public class Thread_GetStreams_FromShoutcast extends Thread
{
	private boolean killMe = false;	//if true; ignore all other
	private boolean isKeyword = false;
	private boolean nextPage = false;
	private boolean lastPage = false;
	private Gui_StreamBrowser2 streamBrowser = null;
	private String genre = "";
	private Vector<String[]> streamsPG = null;
	private Vector<String[]> streamTmp = null;
	private ResourceBundle trans = null;
	
	public Thread_GetStreams_FromShoutcast(Gui_StreamBrowser2 streamBrowser,
			String genre,ResourceBundle trans, boolean isKeyword, boolean nextPage, boolean lastPage) {
		this.streamBrowser = streamBrowser;
		this.genre = genre;
		this.trans = trans;
		this.nextPage = nextPage;
		this.lastPage = lastPage;
		this.isKeyword = isKeyword;
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
	
		//should we load the next page?
		if(!killMe && nextPage) {
			streamBrowser.getControlHttp().nextPage();
		}
		
		//should we reset the counter for the pages?
		if(!killMe && !lastPage && !nextPage) {
			streamBrowser.getControlHttp().resetPages();
		}
		
		//should we load the last page?
		if(!killMe && lastPage) {
			streamBrowser.getControlHttp().prevoiousPage();
		}
		
		//receive streams from site in Vector...
		if(!killMe) {
			streamBrowser.getControlHttp().getStreamsPerGenre(genre, isKeyword);
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
					
				Object[] tmp = new Object[7];
				
				tmp[0] = i;						//nr
				tmp[1] = streamsPG.get(i)[0];	//description
				tmp[2] = streamsPG.get(i)[2];	//now Playing
				try {
					tmp[3] = Integer.valueOf(streamsPG.get(i)[3]);	//listeners
				} catch (NumberFormatException f) { 
					tmp[3] = -1;
				}
				
				try{
					tmp[4] = Integer.valueOf(streamsPG.get(i)[4]);	//Bitrate
				} catch (NumberFormatException f) { tmp[6] = -1; }
				tmp[5] = streamsPG.get(i)[5];	//Type
				tmp[6] = streamsPG.get(i)[1];	//Website
					
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
		
		//update the page information for the user
		streamBrowser.updatePageBar();
		
		//set new status
		if(killMe == true)
			streamBrowser.setStatusText(trans.getString("GetStreams.abort"));
		else
			streamBrowser.setStatusText(trans.getString("GetStreams.done"));
	}
}
