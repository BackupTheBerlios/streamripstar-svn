package thread;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import misc.Stream;

import org.gstreamer.*;
import org.gstreamer.elements.PlayBin2;

import control.SRSOutput;

import gui.Gui_StreamRipStar;

/**
 * Starts an instance for playing an stream with gstreamer. 
 * @author eule
 *
 */
public class AudioPlayer extends Thread{
	
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	private Stream stream;
	private Gui_StreamRipStar mainGui;
	private PlayBin2 playbin;
	
	public AudioPlayer(Stream stream, Gui_StreamRipStar mainGui ) {
		this.stream = stream;
		this.mainGui = mainGui;
	}
	
	public void run() {
		
		//say, we are loading the stream
		if (mainGui != null) {
			mainGui.setTitleForAudioPlayer("",trans.getString("audioplayer.loadingStream"),false);
		}
		
		try {
			Gst.init();
	        playbin = new PlayBin2("AudioPlayer");
	        try {
	        	//test if we should connect to the realy stream or to the internet
	        	if(stream.getStatus() && stream.connectToRelayCB) {
	        		playbin.setURI(new URI("http://127.0.0.1:"+stream.relayServerPortTF));
	        	} else {
	        		playbin.setURI(new URI(stream.address));
	        	}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			//try to catch the AUDIO - TAGS from the stream
			playbin.getBus().connect(new Bus.TAG() {
	
	            public void tagsFound(GstObject source, TagList tagList) {
	                for (String tagName : tagList.getTagNames()) {
	                    for (Object tagData : tagList.getValues(tagName)) {
	                    	mainGui.setTitleForAudioPlayer(stream.name ,tagData.toString(),false);
	                    }
	                }
	            }
	        });
			
			playbin.getBus().connect(new Bus.ERROR() {
				@Override
				public void errorMessage(GstObject source, int errorCode, String errorMessage) {
					//Cannot resolve hostname - no connection to the stream
					if(errorCode == 3 || errorCode == 4) {
						mainGui.setTitleForAudioPlayer("",trans.getString("audioplayer.noConnectionTo"),false);
					}
					
					//Cannot resolve hostname - no connection to the stream
					else if(errorCode == 6 || errorCode == 12) {
						mainGui.setTitleForAudioPlayer("",errorMessage,false);
					}
					
					//Cannot resolve hostname - no connection to the stream
					else if(errorCode == 1) {
						SRSOutput.getInstance().log(errorMessage);
					}
					
					//show all other error messages in the tray
					else {
						mainGui.setTitleForAudioPlayer("",errorMessage,false);
					}
					
					SRSOutput.getInstance().log("The Errorcode was:"+errorCode);
					SRSOutput.getInstance().log("The Errormessage was:"+errorMessage);
				}
			});
			
	        playbin.setState(org.gstreamer.State.PLAYING);
	        Gst.main();
	        playbin.setState(org.gstreamer.State.NULL);

		} catch (java.lang.UnsatisfiedLinkError e) {
			mainGui.showErrorMessageInPopUp(trans.getString("audioplayer.noGstreamerInstalled"));
		} catch (ExceptionInInitializerError e) {
			mainGui.showErrorMessageInPopUp(trans.getString("audioplayer.noGstreamerInstalled"));
		} catch (IllegalArgumentException e) {
			mainGui.showErrorMessageInPopUp(trans.getString("audioplayer.noGstreamerInstalled"));
		} catch (NoClassDefFoundError e) {
			mainGui.showErrorMessageInPopUp(trans.getString("audioplayer.noGstreamerInstalled"));
		}
	}
	
	/**
	 * Stop the playback of the thread with the player. It set the new 
	 * status message
	 */
	public void stopPlaying()
	{
		if(playbin != null)
		{
			playbin.setState(org.gstreamer.State.NULL);
			mainGui.setTitleForAudioPlayer("","",false);
		}
	}
	
	/**
	 * Set a now volume for the audio player
	 * @param volumePercent The new volume in percent
	 */
	public void setAudioVolum(int volumePercent)
	{
		if(playbin != null)
		{
			playbin.setVolumePercent(volumePercent);
		}
	}
}
