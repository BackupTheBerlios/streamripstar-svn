package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import control.SRSOutput;
import control.VolumeManager;
import gui.Gui_StreamRipStar;
import gui.VolumeControlGUI;

/**
 * This GUI provides an interface to control the internal audio player.
 * You have buttons to hear: last Stream, next stream, start stream and
 * stop stream. In addition you have a volume control slider to adjust
 * the volume. A Field where the actual title is shown, is there too.
 */
public class InternAudioControlPanel extends JPanel
{
	private static final long serialVersionUID = -1810951909523547564L;
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private ImageIcon lastStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/audioplayer/back2.png"));
	private ImageIcon nextStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/audioplayer/forward2.png"));
	private ImageIcon startStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/audioplayer/play2.png"));
	private ImageIcon stopStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/audioplayer/stop2.png"));
	
	private IconButton startPlayingButton = new IconButton("Start Playing",startStreamIcon);
	private IconButton stopPlayingButton = new IconButton("Stop Playing",stopStreamIcon);
	private IconButton lastStreamButton = new IconButton("Play previous Stream",lastStreamIcon);
	private IconButton nextPlayingButton = new IconButton("Play Next Stream",nextStreamIcon);
	
	private JTextField titleArea = new JTextField();
	private VolumeControlGUI audioSlider2;
	private VolumeManager vm;
	
	private JToolBar panel = new JToolBar();
	private Gui_StreamRipStar mainGui = null;
	
	public InternAudioControlPanel(Gui_StreamRipStar mainGui, VolumeManager vm) 
	{
		this.mainGui = mainGui;
		this.vm = vm;
		audioSlider2 = new VolumeControlGUI(this.vm);
		setLayout(new BorderLayout());
			
		panel.add(lastStreamButton);
		panel.add(startPlayingButton);
		panel.add(stopPlayingButton);
		panel.add(nextPlayingButton);
		
		titleArea.setEditable(false);
		panel.setBackground(new Color(238,238,238,255));
		panel.setFloatable(false);
		setTextUnderIcons(mainGui.showTextUnderIcons());
		setFontsAll(mainGui.getFontForTextUnderIcons());

		startPlayingButton.addActionListener(new PlayMusikListener());
		stopPlayingButton.addActionListener(new StopPlayMusikListener());
		lastStreamButton.addActionListener(new PlayPreviousStreamListener());
		nextPlayingButton.addActionListener(new PlayNextStreamListener());
		
		add(panel,BorderLayout.WEST);
		this.repaint();
		add(audioSlider2,BorderLayout.EAST);
		add(titleArea,BorderLayout.SOUTH);
	}

	public void setTextUnderIcons(boolean textEnabled)
	{
		if(textEnabled)
		{	
			try 
			{
				startPlayingButton.setText(trans.getString("internAudioCP.startPlaying"));
				stopPlayingButton.setText(trans.getString("internAudioCP.stopPlaying"));
				lastStreamButton.setText(trans.getString("internAudioCP.lastStream"));
				nextPlayingButton.setText(trans.getString("internAudioCP.nextStream"));
			}
			catch(MissingResourceException e)
			{
				SRSOutput.getInstance().logE("Translation Error: Error while setting the translation in InternAudioControlPanel");
				SRSOutput.getInstance().logE(e.getMessage());
			}
		} 
		else 
		{
			startPlayingButton.setText(null);
			stopPlayingButton.setText(null);
			lastStreamButton.setText(null);
			nextPlayingButton.setText(null);
		}
	}
	
	/**
	 * set the tooltips for all components in this Control panel. It provides
	 * some default tooltips and changes the language, if needed
	 */
	public void setToolTipsAll() 
	{
		audioSlider2.setToolTipText(trans.getString("toolTip.audioSlider"));
		stopPlayingButton.setToolTipText(trans.getString("toolTip.stopHearMusicButton"));
	}
	
	public void setFontsAll(Font newFont) 
	{
		startPlayingButton.setFont(newFont);
		stopPlayingButton.setFont(newFont);
		lastStreamButton.setFont(newFont);
		nextPlayingButton.setFont(newFont);
	}
	
	public int getAudioVolume() 
	{
		return audioSlider2.getVolume();
	}
	
	public void setAudioVolume(int newVolume)
	{
		audioSlider2.setVolume(newVolume);
	}
	
	/**
	 * Is called, when the value is changed from the intern audio player
	 *
	 */
	public class VolumeChangeListener implements ChangeListener 
	{
		@Override
		public void stateChanged(ChangeEvent arg0) 
		{
			mainGui.setVolume(audioSlider2.getVolume());
		} 
	}

	/**
	 * Stop the internal music player
	 * 
	 * @author Johannes Putzke	
	 */
	class StopPlayMusikListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			mainGui.getTabel().stopInternalAudioPlayer();
		}
	}
	
	/**
	 * Set an title in the title bar on the bottom the the hole panel.
	 * the parameter title is not checked, so it might be checked of 
	 * null before
	 * @param title The text you want to show in the title bar
	 * @param isErrorMessage
	 */
	public void setTitle(String title,boolean isErrorMessage) 
	{
		if(!isErrorMessage) 
		{
			titleArea.setForeground(Color.BLACK);
		}
		
		else 
		{
			titleArea.setForeground(Color.RED);	
		}
		titleArea.setText(title);
	}
	
	/**
	 * Play the previous stream. If no stream is selected, select and play
	 * the last stream.  
	 */
	class PlayPreviousStreamListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			mainGui.playPreviousStream();
		}
	}
	
	/**
	 * Play the previous stream. If no stream is selected, select the first stream in the list.
	 * If the last Stream is selected, play the first one.
	 */
	class PlayNextStreamListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			mainGui.playNextStream();
		}
	}
	
	/**
	 * Is called, when you like to hear music 
	 * @author Johannes Putzke	
	 */
	public class PlayMusikListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			mainGui.playSelectedStream();
		}
	}
}
