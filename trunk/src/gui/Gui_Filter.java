package gui;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import control.SRSOutput;

@SuppressWarnings("serial")
public class Gui_Filter extends JPanel {
	private boolean DEBUG = false;
	
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private JTextField minBitrateTF = new JTextField("128",4);
	private JTextField maxBitrateTF = new JTextField("999",4);
	
	private JCheckBox filterBitrateCB = new JCheckBox("Filter Bitrates");
	private JCheckBox filterTypeCB = new JCheckBox("Filter Types");
	
	private JCheckBox showMP3Streams = new JCheckBox("Show MP3 Streams");
	private JCheckBox showAACStreams = new JCheckBox("Show AAC+ Streams");
	private JCheckBox showUnknownStreams = new JCheckBox("Show Unknown Streams");
	
	private JLabel minBitrateLabel = new JLabel("min bitrate: ");
	private JLabel maxBitrateCBLabel = new JLabel("max bitrate: ");
	
	private JButton filterNowButton = new JButton("Filter Now");
	
	private JPanel bitratePanel = new JPanel();
	private JPanel typePanel = new JPanel();
	
	private TitledBorder bitrateTitle = BorderFactory.createTitledBorder("Filter Bitrates");
	private TitledBorder typeTitle = BorderFactory.createTitledBorder("Filter Types");
	
	private boolean visible = false;
	private boolean isFiltered = false;
	
	private Vector<String[]> streamsPG = null;
	
	Gui_StreamBrowser2 streamBrowser = null;
	
	/**
	 * creates a gui where you can enter bitrates and 
	 * types for filtering.
	 * the visibility must be given on start 
	 */
	public Gui_Filter(Gui_StreamBrowser2 streamBrowser,boolean visible){
		super();
		this.visible = visible;
		this.streamBrowser = streamBrowser;
		//build visible components
		init();
		//set checkboxes disabled / enabled
		updateGuis();
		setLanguage();
	}
	
	/**
	 * 
	 */
	private void init() {
		
		//set Layouts
		setLayout(new GridBagLayout());
		bitratePanel.setLayout(new GridBagLayout());
		typePanel.setLayout(new GridBagLayout());
		
		//set Borders
		bitratePanel.setBorder(bitrateTitle);
		typePanel.setBorder(typeTitle);
		
		//set Constrains defaults
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		//put bitrate and type panel in main panel
		c.weighty= 0.0;
		c.weightx = 1.0;
		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets( 5, 0, 0, 0);
		add(bitratePanel,c);
		c.gridy = 1;
		add(typePanel,c);
		c.gridy = 2;
		c.weighty = 0.0;
		add(filterNowButton,c);
		c.gridy = 3;
		c.weighty = 1.0;
		add(new JLabel(),c);

		
		
		//fill bitrate panel
		c.weighty= 0.0;
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 2;
		c.insets = new Insets( 0, 0, 0, 0);
		bitratePanel.add(filterBitrateCB,c);
		c.insets = new Insets( 0, 10, 0, 0);
		c.gridwidth = 1;
		c.gridy = 1;
		bitratePanel.add(minBitrateLabel,c);
		c.insets = new Insets( 0, 0, 0, 0);
		c.gridx = 1;
		bitratePanel.add(minBitrateTF,c);
		c.gridy = 2;
		c.gridx = 0;
		c.insets = new Insets( 0, 10, 0, 0);
		bitratePanel.add(maxBitrateCBLabel,c);
		c.insets = new Insets( 0, 0, 0, 0);
		c.gridx = 1;
		bitratePanel.add(maxBitrateTF,c);
		
		//fill type panel	
		c.weighty= 0.0;
		c.gridy = 0;
		c.gridx = 0;
		typePanel.add(filterTypeCB,c);
		c.gridy = 1;
		c.insets = new Insets( 0, 10, 0, 0);
		typePanel.add(showMP3Streams,c);
		c.gridy = 2;
		typePanel.add(showAACStreams,c);
		c.gridy = 3;
		typePanel.add(showUnknownStreams,c);
		
		//add Listener
		filterBitrateCB.addActionListener(new ChangeStatusListener());
		filterTypeCB.addActionListener(new ChangeStatusListener());
		filterNowButton.addActionListener(new FilterListener());
		
		//set visible or not
		setVisible(visible);
	}
	
	private void setLanguage() {
		try {
			filterBitrateCB.setText(trans.getString("Filter.enableBitrateFilter"));
			filterTypeCB.setText(trans.getString("Filter.enableTypeFilter"));
			showMP3Streams.setText(trans.getString("Filter.StreamMP3"));
			showAACStreams.setText(trans.getString("Filter.StreamAAc"));
			showUnknownStreams.setText(trans.getString("Filter.StreamUnk"));
			minBitrateLabel.setText(trans.getString("Filter.minBit"));
			maxBitrateCBLabel.setText(trans.getString("Filter.maxBit"));
			filterNowButton.setText(trans.getString("Filter.FilterButton"));
			
			bitrateTitle.setTitle(trans.getString("Filter.Title.Bitrates"));
			typeTitle.setTitle(trans.getString("Filter.Title.Types"));
		} catch ( MissingResourceException e ) { 
		      SRSOutput.getInstance().logE( e.getMessage() ); 
		}
	}
	
	/**
	 * set fields enable or disable when changing the status 
	 * from a checkbox
	 */
	public void updateGuis() {
		if(filterBitrateCB.isSelected()) {
			minBitrateLabel.setEnabled(true);
			maxBitrateCBLabel.setEnabled(true);
			maxBitrateTF.setEnabled(true);
			minBitrateTF.setEnabled(true);
		} else {
			minBitrateLabel.setEnabled(false);
			maxBitrateCBLabel.setEnabled(false);
			maxBitrateTF.setEnabled(false);
			minBitrateTF.setEnabled(false);
		}
		
		if(filterTypeCB.isSelected()) {
			showMP3Streams.setEnabled(true);
			showAACStreams.setEnabled(true);
			showUnknownStreams.setEnabled(true);
		} else {
			showMP3Streams.setEnabled(false);
			showAACStreams.setEnabled(false);
			showUnknownStreams.setEnabled(false);
		}
	}
	
	class ChangeStatusListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateGuis();
		}
	}
	
	public void setFiltered(boolean newFilterStatus) {
		isFiltered = newFilterStatus;
	}
	
	public boolean isFiltered() {
		return isFiltered;
	}
	
	public Vector<String[]> getFilteredStreamVector() {
		return streamsPG;
	}
	
	public void setFilterdStreamVector(Vector<String[]> vector) {
		streamsPG = vector;
	}
	
	class FilterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Vector<String[]> streamTmp = null;
			
			//set status loading
			streamBrowser.setStatusText("Filter streams",false);
			
			//...and get this vector
			streamsPG = streamBrowser.getControlHttp().getStreams();
			
			//if filter checkbox is enabled -> filter
			if(streamsPG != null && streamsPG.capacity() > 0) {
				if(streamBrowser.getFilterGui().filterBitratesIsEnabled()) {
					streamTmp = streamBrowser.getFilterGui().filterBitrates(streamsPG);
					if(streamTmp != null) {
						streamsPG = streamTmp;
					}
				}
				
				//filter for Types
				if(streamBrowser.getFilterGui().filterTypesIsEnabled()) {
					if(streamBrowser.getFilterGui().filterTypesIsEnabled()) {
						streamTmp = streamBrowser.getFilterGui().filterTypes(streamsPG);
						streamsPG = streamTmp;				
					}
				}
				
				//remove all
				streamBrowser.removeAllFromTable(streamBrowser.getBrowseModel());
	
				//set filtered 
				if(streamBrowser.getFilterGui().filterTypesIsEnabled() ||
						streamBrowser.getFilterGui().filterBitratesIsEnabled()) {
					setFiltered(true);
				} else {
					setFiltered(false);
				}
				
				//add stream recursive
				for(int i=0 ; i < streamsPG.capacity(); i++) {
					
					Object[] tmp = new Object[8];
					
					tmp[0] = i;						//nr
					tmp[1] = streamsPG.get(i)[0];	//description
					tmp[2] = streamsPG.get(i)[1];	//now Playing
					tmp[3] = streamsPG.get(i)[6];	//now Genre
					try {
						tmp[4] = Integer.valueOf(streamsPG.get(i)[2]);	//listeners
					} catch (NumberFormatException f) { 
						tmp[4] = -1;
					}
					
					try{
						tmp[5] = Integer.valueOf(streamsPG.get(i)[3]);	//Bitrate
					} catch (NumberFormatException f) { tmp[5] = -1; }
					tmp[6] = streamsPG.get(i)[4];	//Type
					tmp[7] = streamsPG.get(i)[7];	//Website
						
					//add to model
					streamBrowser.getBrowseModel().addRow(tmp);
				}
				//set status loading
				streamBrowser.setStatusText("Filter done!. Ready for new Action",false);
			}
		}
	}
	
	
	/**
	 * Shows if the streams should be filtered for
	 * bitrates or not. If yes return true.
	 * @return
	 */
	public boolean filterBitratesIsEnabled() {
		return filterBitrateCB.isSelected();
	}
	
	/**
	 * Shows if the stream should be filtered for
	 * types or not. If yes return true.
	 * @return
	 */
	public boolean filterTypesIsEnabled() {
		return filterTypeCB.isSelected();
	}
	
	/**
	 * This method filters a collection of streams for bitrates and returns
	 * only it in an Vecor Strings.
	 * If an error appears it returns null;
	 * @param oldStreams
	 * @return
	 */
	public Vector<String[]> filterBitrates( Vector<String[]> oldStreams) {
		//contains filtered streams
		Vector<String[]> newStreams = new Vector<String[]>(0,1);
		int minVal = 0;
		int maxVal = 0;
		int streamBitrate = -1;
		
		//try to get integer values from the gui
		try {
			minVal = Integer.valueOf(minBitrateTF.getText());
			maxVal = Integer.valueOf(maxBitrateTF.getText());
			
			// Negative bitrate Values makes no sense
			if(minVal < 0) {
				throw new NumberFormatException();
			}
			
			if(oldStreams.capacity()>0) {
				for(int i=0; i < oldStreams.capacity(); i++) {
					try {
						//bitrate of an Stream
						streamBitrate = Integer.valueOf(oldStreams.get(i)[3]);
					} catch(NumberFormatException e) {
						if(DEBUG) {
							SRSOutput.getInstance().log("Wrong Bitrate in Stream");
						}
						//don't add it
						streamBitrate = -1;
					}
					
					//if it is in the right value -> add to new stream
					if(streamBitrate >= minVal && streamBitrate <= maxVal) {
						newStreams.add(oldStreams.get(i));
					}
				}
			}
			return newStreams;
			
		} catch(NumberFormatException e) {
			if(DEBUG) {
				SRSOutput.getInstance().log("Wrong Bitrate in Stream");
			}
			JOptionPane.showMessageDialog(streamBrowser
					,"Wrong Value in a bitrate field");
			return null;
		}
	}
	
	/**
	 * This method filters a collection of streams for types and returns
	 * only the matching in an Vector Strings.
	 * 
	 * @param oldStreams
	 * @return
	 */
	public Vector<String[]> filterTypes( Vector<String[]> oldStreams) {
		//contains filtered streams
		Vector<String[]> newStreams = new Vector<String[]>(0,1);
		
		if(oldStreams.capacity()>0) {
			for(int i=0; i < oldStreams.capacity(); i++) {
				
				//add mp3 streams
				if(showMP3Streams.isSelected() && oldStreams.get(i)[4].toLowerCase().startsWith("mp3")) {
					newStreams.add(oldStreams.get(i));
				//add AAC+ streams
				} else if(showAACStreams.isSelected() && oldStreams.get(i)[4].startsWith("AAC+")) {
					newStreams.add(oldStreams.get(i));
				//the rest of streams
				} else if(showUnknownStreams.isSelected() && oldStreams.get(i)[4].trim().equals("")){
					newStreams.add(oldStreams.get(i));
				}
			}
		}
		return newStreams;
	}
	
	/**
	 * This method gets the settings for checked
	 * 
	 * 
	 * @return
	 */
	public String[] getSaveSettings() {
		String[] save = new String[7];
		
		save[0] = String.valueOf(filterBitrateCB.isSelected());
		save[1] = String.valueOf(filterTypeCB.isSelected());
		
		save[2] = String.valueOf(showMP3Streams.isSelected());
		save[3] = String.valueOf(showAACStreams.isSelected());
		save[4] = String.valueOf(showUnknownStreams.isSelected());
		
		save[5] = String.valueOf(minBitrateTF.getText());
		save[6] = String.valueOf(maxBitrateTF.getText());
		
		return save;
	}
	
	/**
	 * set Settings for the gui
	 * @param settings
	 */
	public void loadSettings(String[] settings) {
		filterBitrateCB.setSelected(Boolean.valueOf(settings[0]));
		filterTypeCB.setSelected(Boolean.valueOf(settings[1]));
		
		showMP3Streams.setSelected(Boolean.valueOf(settings[2]));
		showAACStreams.setSelected(Boolean.valueOf(settings[3]));
		showUnknownStreams.setSelected(Boolean.valueOf(settings[4]));
		
		minBitrateTF.setText(settings[5]);
		maxBitrateTF.setText(settings[6]);
	}
}
