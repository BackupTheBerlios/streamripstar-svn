package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import misc.Stream;


import control.Control_http_Playlist;
import control.SRSOutput;

public class Gui_Infodialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	private Control_http_Playlist getLastTrack; 
	private Stream stream = null;
	
	private JLabel streamRipStarName = new JLabel("StreamRipStar Stream Name :");
	private JLabel streamName = new JLabel("Stream Name :");
	private JLabel serverName = new JLabel("Server Name :");
	private JLabel bitrate = new JLabel("Bitrate :");
	private JLabel metaVall = new JLabel("Metaintervall :");
	private JLabel commandExe = new JLabel("Execute Command :");
	private JLabel lastTrackLabel = new JLabel("Loading...");
	
	private JTextField streamRipStarNameField = new JTextField("");
	private JTextField streamNameField = new JTextField("");
	private JTextField serverNameField = new JTextField("");
	private JTextField bitrateField = new JTextField("");
	private JTextField metaVallField = new JTextField("");
	private JTextField commandExeField = new JTextField("");
	
	private JButton okButton = new JButton("ok");
	
	/**
	 * open a window where all information are shown, which are
	 * given with streamripper
	 * @param ownStream
	 * @param mainWin The mainwindow of streamripstar
	 */
	public Gui_Infodialog(Gui_StreamRipStar mainWin, Stream ownStream) {
		super(mainWin, "Information about "+ownStream.name);
		this.stream = ownStream;
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		setLanguage();
		fillWithData();
		buildGui();
		
		getLastTrack = new Control_http_Playlist(stream.address,this);
		getLastTrack.start();
		
		setVisible(true);
	}
	
	/**
	 * set a new language
	 *
	 */
	public void setLanguage() {
		try {
			streamRipStarName.setText(trans.getString("Info.streamRipStarName"));
			streamName.setText(trans.getString("Info.streamName"));
			serverName.setText(trans.getString("Info.serverName"));
			bitrate.setText(trans.getString("Info.bitrate"));
			metaVall.setText(trans.getString("Info.metaVall"));
			commandExe.setText(trans.getString("Info.commandExe"));
			lastTrackLabel.setText(trans.getString("Info.Loading"));
		} catch ( MissingResourceException e ) { 
		      SRSOutput.getInstance().logE( e.getMessage() ); 
	    }
	}
	
	public void buildGui()
	{
		JPanel panel = new JPanel();
		add(panel);
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets( 10, 10, 2, 10);
		c.gridx = 0;
		c.gridy = 0;
		panel.add(streamRipStarName,c);
		c.gridx = 1;
		panel.add(streamRipStarNameField,c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(streamName,c);
		c.gridx = 1;
		panel.add(streamNameField,c);
		c.gridx = 0;
		c.gridy = 2;
		panel.add(serverName,c);
		c.gridx = 1;
		panel.add(serverNameField,c);
		c.gridx = 0;
		c.gridy = 3;
		panel.add(bitrate,c);
		c.gridx = 1;
		panel.add(bitrateField,c);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(metaVall,c);
		c.gridx = 1;
		panel.add(metaVallField,c);
		c.gridx = 0;
		c.gridy = 5;
		panel.add(commandExe,c);
		c.gridx = 1;
		panel.add(commandExeField,c);
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 3;
		panel.add(lastTrackLabel,c);
		c.insets = new Insets( 10, 10, 5, 10);
		c.weightx = 2;
		c.gridy = 7;
		c.gridx = 0;
		c.gridwidth = 2;
		panel.add(okButton,c);
		
		okButton.addActionListener(new OKListener());
		
		pack();
		
        //get size of window
        Dimension frameDim = getSize();
        //get resolution
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        //calculates the app. values
        int x = (screenDim.width - frameDim.width)/2;
        int y = (screenDim.height - frameDim.height)/2;
        //set location
        setLocation(x, y);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void fillWithData()
	{
		streamRipStarNameField.setText(stream.name);
		streamNameField.setText(stream.getMetaData()[0]);
		serverNameField.setText(stream.getMetaData()[1]);
		bitrateField.setText(stream.getMetaData()[2]);
		metaVallField.setText(stream.getMetaData()[3]);
		commandExeField.setText(stream.getExeCommand());
		
		streamRipStarNameField.setEditable(false);
		streamNameField.setEditable(false);
		serverNameField.setEditable(false);
		bitrateField.setEditable(false);
		metaVallField.setEditable(false);
		commandExeField.setEditable(false);
	}
	
	/**
	 * Set the text for the label, where the last tracks are shown
	 * This manner is used by the Control_http_Playlist class
	 * 
	 * @param Text: The new html text;
	 */
	public void setLastTrackLabelText(String text) {
		
			lastTrackLabel.setText(text);

			//resize the window
			pack();
			
	        //get size of window
	        Dimension frameDim = getSize();
	        //get resolution
	        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	        //calculates the app. values
	        int x = (screenDim.width - frameDim.width)/2;
	        int y = (screenDim.height - frameDim.height)/2;
	        //set location
	        setLocation(x, y);
	        repaint();
	}
	
	/**
	 * Control the paint component, to catch error in the JLabel when set
	 * a wrong text in the JLabel
	 */
	public void paint(Graphics g){
		try {
			super.paint(g);
		} catch (ClassCastException e) {
			lastTrackLabel.setText(trans.getString("Info.error.setOldTracks"));
		}
	}
	
	/**
	 * 
	 * Close the window and stop getting the playlist from website
	 * @author Johannes Putzke
	 *
	 */
    public class OKListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(getLastTrack != null ) {
            	getLastTrack.killThread();
            }
            dispose();
        }
    }
}
