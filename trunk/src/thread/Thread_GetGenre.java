package thread;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import gui.Gui_StreamBrowser;

import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * This gets the genres from the website
 * and put it show in Gui_StreamBrowser
 * @author eule
 *
 */
public class Thread_GetGenre extends Thread
{
	private boolean killMe = false;	//if true; ignore all other
	private Gui_StreamBrowser streamBrowser = null;
	private Vector<Vector<String>> genres = null;
	private ResourceBundle trans = null;
	
	public Thread_GetGenre(Gui_StreamBrowser streamBrowser, ResourceBundle trans) {
		this.streamBrowser = streamBrowser;
		this.trans = trans;
	}
	
	public void killMe() {
		killMe = true;
		streamBrowser.setStatusText(trans.getString("GetGenres.getGenres"));
	}
	
	public void run() {	
		//set the abort button enable for killing this thread
		streamBrowser.setAbortButtonEnable(true);
		
		//disable clicking in the jtree
		streamBrowser.disableModelClick(true);
		
		//set status message
		streamBrowser.setStatusText(trans.getString("GetGenres.getGenres"));
		
		//save Genres into Vectors
		if(!killMe) {
			streamBrowser.getControlHttp().getGenresFromWebsite();
		}
		
//		get Genres for Genres
		if(!killMe) {
		genres = streamBrowser.getControlHttp().getAllGenres();
		}
		
		//create root genres
		if(!killMe) {
			streamBrowser.getTreeRoot().add(
					new DefaultMutableTreeNode(trans.getObject("GetGenres.search")) );
			for(int i=0; i < genres.capacity(); i++) {
				if(killMe)
					break;
				DefaultMutableTreeNode tmp = 
					new DefaultMutableTreeNode((genres.get(i)).get(0));
				streamBrowser.getTreeRoot().add(tmp);
				for(int j=1; j < genres.get(i).capacity(); j++) {
					if(killMe)
						break;
					tmp.add(new DefaultMutableTreeNode(genres.get(i).get(j)));
				}
			}
			streamBrowser.updateUITree();
		}
		
		//only one genre can be shown at on time
		if(!killMe) {	
			streamBrowser.getTree().getSelectionModel().setSelectionMode
	        	(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		
		//enable clicking on the jtree
		streamBrowser.disableModelClick(false);
		
		//set new status message
		if(killMe == true)
			streamBrowser.setStatusText(trans.getString("GetGenres.abort"));
		else
			streamBrowser.setStatusText(trans.getString("GetGenres.done"));
		
		//set the abort button enable for killing this thread
		streamBrowser.setAbortButtonEnable(false);
		
		//delete the link to this thread
		streamBrowser.setThreadToNull();
	}
}
