package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This GUI provides an interface to control the internal audio player.
 * You have buttons to hear: last Stream, next stream, start stream and
 * stop stream. In addition you have a volume control slider to adjust
 * the volume. A Field where the actual title is shown, is there too.
 */
public class InternAudioControlPanel extends JPanel{
	private static final long serialVersionUID = -1810951909523547564L;

	private ImageIcon lastStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/record.png"));
	private ImageIcon nextStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/record.png"));
	private ImageIcon startStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/player.png"));
	private ImageIcon stopStreamIcon = new ImageIcon((URL)getClass().getResource("/Icons/stop-audio.png"));
	
	private JButton startPlayingButton = new JButton("Start Playing",startStreamIcon);
	private JButton stopPlayingButton = new JButton("Stop Playing",stopStreamIcon);
	private JButton lastStreamButton = new JButton("Play previous Stream",lastStreamIcon);
	private JButton nextPlayingButton = new JButton("Play Next Stream",nextStreamIcon);
	
	private JTextField titleArea = new JTextField();
	private JSlider audioSlider = new JSlider();
	
	private Gui_StreamRipStar mainGui = null;
	
	/**
	 * 
	 * @param mainGui
	 */
	public InternAudioControlPanel(Gui_StreamRipStar mainGui) {
		this.mainGui = mainGui;
		
		//configure the slider for the voluemcontrol
		audioSlider.setMinimum(0);
		audioSlider.setMaximum(100);
		//set default Value
		audioSlider.setValue(80);
		//set jump intervall : on every click 10 percent
		audioSlider.setMinorTickSpacing(25);
		//in how many spaces should the text shown?
		audioSlider.setMajorTickSpacing(50);
		audioSlider.setMaximumSize(new Dimension(100,40));
		audioSlider.setMinimumSize(new Dimension(100,40));
		audioSlider.setPreferredSize(new Dimension(100,40));
		//The Orientation
		audioSlider.setOrientation(JSlider.HORIZONTAL);
		audioSlider.setPaintTicks(true);  
		audioSlider.setPaintLabels(true); 
		audioSlider.setPaintTrack(true);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridy = 0;
		c.gridx = 0;
		add(lastStreamButton,c);
		c.gridx = 1;
		add(startPlayingButton,c);
		c.gridx = 2;
		add(stopPlayingButton,c);
		c.gridx = 3;
		add(nextPlayingButton,c);
		c.gridx = 4;
		add(audioSlider,c);
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 5;
		c.weighty = 1.0;
		c.weightx = 1.0;
		add(titleArea,c);
	}
}
