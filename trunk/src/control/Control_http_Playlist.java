package control;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johanes Putzke*/
/* eMail: die_eule@gmx.net*/

import gui.Gui_Infodialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;



public class Control_http_Playlist extends Thread{
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	private BufferedReader bw;
	private Boolean stopSearching = false;		//stop fetching the internet site
	private String url;
	private String text = "";
	private Gui_Infodialog dialog;
	
	/**
	 * The url must have the port included
	 * @param url
	 */
	public Control_http_Playlist(String url, Gui_Infodialog dialog) {
		this.url = url;
		this.dialog = dialog;
	}
	
	public void run() {
		try {
			if(!stopSearching) {
				if(!url.startsWith("http")) {
					url = "http://"+url;
				}
				
				//create the url to the website including the subsite
				URL shoutcast;
				
				if(url.endsWith("/")) {
					shoutcast = new URL(url+"played.html");
				} else {
					shoutcast = new URL(url+"/played.html");
				}

				if(!stopSearching) {
					URLConnection connection = shoutcast.openConnection();
					//must set the useragent to mozilla, else will receive the stream itselfs
					connection.addRequestProperty("User-Agent", "Mozilla/5.0");
					
					if(!stopSearching) {
						bw = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
						
						text = bw.readLine();
						
						//only look for old tracks, if we can read the html site and the site is
						//the status site of the shoutcast page
						if(text != null && text.contains("SHOUTcast Song History")) {
							text = text.substring(text.indexOf("<tr><td>Played @"));
							text = text.replace("Current Song","");
							text = text.replace("Played @</td><td>", "Played @<hr /></td><td>");
							text = text.replace("Song Title</b>", "Song Title</b><hr />");
							text = text.substring(0,text.indexOf("100%%")+6);
							text = text.replaceAll("</td>", "</td> &nbsp; &nbsp; &nbsp; &nbsp;");
						}
					}
				}
			}
			
		} catch (MalformedURLException e) {
			text = null;
		} catch (IOException e) {
			text = null;
		} finally {
			try {
				if(text == null) {
					dialog.setLastTrackLabelText(trans.getString("Info.error.getTracks"));
				} else {
					dialog.setLastTrackLabelText("<html>"+text+"</html>");
				}
				if(bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				SRSOutput.getInstance().logE("Can not close the connection for fetching the last tracks");
			}
		}
	}
	
	public void killThread() {
		stopSearching = true;
	}
}
