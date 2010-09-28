package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This GUI provides an interface to control the internal audio player.
 * You have buttons to hear: last Stream, next stream, start stream and
 * stop stream. In addition you have a volume control slider to adjust
 * the volume. A Field where the actual title is shown, is there too.
 */
public class InternAudioControlPanel extends JPanel{
	private static final long serialVersionUID = -1810951909523547564L;
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private ImageIcon lastStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/questionmark.png"));
	private ImageIcon nextStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/questionmark.png"));
	private ImageIcon startStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/player.png"));
	private ImageIcon stopStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/stop-audio.png"));
	
	private JButton startPlayingButton = new JButton("Start Playing",startStreamIcon);
	private JButton stopPlayingButton = new JButton("Stop Playing",stopStreamIcon);
	private JButton lastStreamButton = new JButton("Play previous Stream",lastStreamIcon);
	private JButton nextPlayingButton = new JButton("Play Next Stream",nextStreamIcon);
	
	private JTextField titleArea = new JTextField();
	private JSlider audioSlider = new JSlider();
	
	private JPanel panel = new JPanel();
	private Gui_StreamRipStar mainGui = null;
	
	/**
	 * 
	 * @param mainGui
	 */
	public InternAudioControlPanel(Gui_StreamRipStar mainGui) {
		this.mainGui = mainGui;
		
		setLayout(new BorderLayout());
		add(panel,BorderLayout.WEST);
		add(audioSlider,BorderLayout.EAST);
		add(titleArea,BorderLayout.SOUTH);
		
		panel.add(lastStreamButton);
		panel.add(startPlayingButton);
		panel.add(stopPlayingButton);
		panel.add(nextPlayingButton);
		
		titleArea.setEditable(false);

		setCorrectedLayout();
		setTextUnderIcons(false);
		
		//mainGUi.getFontForTextUnderIcons();
		
		audioSlider.addChangeListener(new VolumeChangeListener());
		stopPlayingButton.addActionListener(new StopPlayMusikListener());
	}
	
	public void setCorrectedLayout() {
		startPlayingButton.setHorizontalTextPosition(SwingConstants.CENTER);
		startPlayingButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		startPlayingButton.setBorderPainted(false);
		startPlayingButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		stopPlayingButton.setHorizontalTextPosition(SwingConstants.CENTER);
		stopPlayingButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		stopPlayingButton.setBorderPainted(false);
		stopPlayingButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		lastStreamButton.setHorizontalTextPosition(SwingConstants.CENTER);
		lastStreamButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		lastStreamButton.setBorderPainted(false);
		lastStreamButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		nextPlayingButton.setHorizontalTextPosition(SwingConstants.CENTER);
		nextPlayingButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		nextPlayingButton.setBorderPainted(false);
		nextPlayingButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
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
				System.err.println("Translation Error: Error while setting the translation in InternAudioControlPanel");
				System.err.println(e.getMessage());
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
	
	
	public void setToolTipsAll() 
	{
		audioSlider.setToolTipText(trans.getString("toolTip.audioSlider"));
		stopPlayingButton.setToolTipText(trans.getString("toolTip.stopHearMusicButton"));
	}
	
	public int getAudioVolume() 
	{
		return audioSlider.getValue();
	}
	
	public void setAudioVolume(int newVolume)
	{
		audioSlider.setValue(newVolume);
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
			mainGui.setVolume(audioSlider.getValue());
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
}
