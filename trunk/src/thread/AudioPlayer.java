package thread;

import java.net.URI;
import java.net.URISyntaxException;
import org.gstreamer.*;
import org.gstreamer.elements.PlayBin2;

import gui.Gui_StreamRipStar;

/**
 * Starts an instance for playing an stream with gstreamer. 
 * @author eule
 *
 */
public class AudioPlayer extends Thread{

	private String uri;
	private Gui_StreamRipStar mainGui;
	private  PlayBin2 playbin;
	
	public AudioPlayer(String uri, Gui_StreamRipStar mainGui ) {
		this.uri = uri;
		this.mainGui = mainGui;
	}
	
	public void run() {
		
		String[] args = {};
		Gst.init("AudioPlayer", args);
        playbin = new PlayBin2("AudioPlayer");
        try {
			playbin.setURI(new URI(uri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		playbin.getBus().connect(new Bus.TAG() {

            public void tagsFound(GstObject source, TagList tagList) {
                for (String tagName : tagList.getTagNames()) {
                    for (Object tagData : tagList.getValues(tagName)) {
                    	mainGui.setTitle(tagData.toString());
                    }
                }
            }
        });
        playbin.setState(org.gstreamer.State.PLAYING);
        Gst.main();
        playbin.setState(org.gstreamer.State.NULL);

	}
	
	public void stopPlaying() {
		playbin.setState(org.gstreamer.State.NULL);
		mainGui.setTitle(" NOT PLAYING");
	}
}
