package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;

import misc.Stream;


public class Gui_StreamOptions extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;

	private ImageIcon saveAndExitIcon = new ImageIcon((URL)getClass().getResource("/Icons/ok_small.png"));
	private ImageIcon saveIcon = new ImageIcon((URL)getClass().getResource("/Icons/save_small.png"));
	private ImageIcon abortIcon = new ImageIcon((URL)getClass().getResource("/Icons/abort_small.png"));
	private ImageIcon findIcon = new ImageIcon((URL)getClass().getResource("/Icons/open_small.png"));
	
	/*
	 * mainOptions
	 *  |- baseOptionsPanel
	 * 	|- fileOptionsPanel
	 *  |- overWriteCompletePanel
	 * 	|- otherWriteRulesPanel
	 */
	private JPanel mainOptionsPanel = new JPanel();
	private JPanel baseOptionsPanel = new JPanel();
	private JPanel fileOptionsPanel = new JPanel();
	private JPanel overWriteCompletePanel = new JPanel();
	private JPanel otherWriteRulesPanel = new JPanel();
	
	
	/*
	 * moreOptionsPanel
	 * 	|- relayServerPanel
	 * 	|- otherOptionsPanel
	 *  |- connection settings panel
	 */
	private JPanel moreOptionsPanel = new JPanel();
	private JPanel relayServerPanel = new JPanel();
	private JPanel otherOptionsPanel = new JPanel();
	private JPanel connectionSettingsPanel = new JPanel();
	
	/*
	 * tab4Panel
	 *  |- idTagPanel
	 *  |- splitPointPane
	 *  |- codePanel
	 */
	private JPanel tab4Panel = new JPanel();
	private JPanel idTagPanel = new JPanel();
	private JPanel splitPointPanel = new JPanel();
	private JPanel codesetPanel = new JPanel();
	
	/*
	 * buttonPanel
	 */
	private JPanel buttonPanel = new JPanel();
//BaseOptions TextFields (TF means TextField)
	private JTextField streamNameTF = new JTextField(30);
	private JTextField streamURLTF = new JTextField(30);
	private JTextField streamWebsiteTF = new JTextField(30);
	private JTextField streamCommentTF = new JTextField(30);
	private JTextField streamGenreTF = new JTextField(30);
//MainOptionsTextFields
	private JTextField allInOneSaveField = new JTextField(20);
	private JTextField lengthRecordHourField = new JTextField(2);
	private JTextField lengthRecordMinuteField = new JTextField(2);
	private JTextField lengthRecordSecondField = new JTextField(2);
	private JTextField maxSizeMBField = new JTextField();
	private JTextField useragentField = new JTextField();
	private JTextField runIndexField = new JTextField("000");
	private JTextField patternField = new JTextField();
	private JTextField realyPortField = new JTextField("8000",5);
	private JTextField realyConnectionsField = new JTextField("0",5);
	private JTextField proxyField = new JTextField();
	private JTextField skipTracksField = new JTextField(4);
	private JTextField timeoutReconnectField = new JTextField(6);
//CodeSets Textfields	(TF = TextField)
	private JTextField codesetRelayTF = new JTextField(20);
	private JTextField codesetMetadataTF= new JTextField(20);
	private JTextField codesetID3TF = new JTextField(20);
	private JTextField codesetFilesysTF = new JTextField(20);
//SplitPoints TextFields	(TF = TextField)
	private JTextField xsOffsetTF = new JTextField(6);
	private JTextField xsPadding1TF= new JTextField(6);
	private JTextField xsPadding2TF = new JTextField(6);
	private JTextField xsSearch1TF = new JTextField(6);
	private JTextField xsSearch2TF = new JTextField(6);
	private JTextField xsSilenceTF = new JTextField(6);
//Free commandline options
	private JTextField freeArgumentTF = new JTextField(20);
//other Options
	private JTextField metaDataRuleFileTF = new JTextField();
	private JTextField interfaceTF = new JTextField();
	private JTextField externalCmdMetaDataTF = new JTextField();
	private JTextField createRelayFileTF = new JTextField();
//MainOption CheckBoxes
	private JCheckBox allInOneCheckBox = new JCheckBox("Save in single file: "); 
	private JCheckBox lengthRecordCheckBox = new JCheckBox("Max Length of the Rip [hh:MM:ss]");
	private JCheckBox maxSizeMBCheckBox = new JCheckBox("Max size of file [MB]");
	private JCheckBox useragentCheckBox = new JCheckBox("Useragent");
	private JCheckBox runIndexCheckBox = new JCheckBox("Add sequence number to output filenames");
	private JCheckBox patternCheckBox = new JCheckBox("Use a pattern to format the output file names");
	private JCheckBox relayServerCheckBox = new JCheckBox("Create a realy Server in port");
	private JCheckBox relayConnect = new JCheckBox("Play relaystream in medialplayer");
	private JCheckBox createRelayFileCB = new JCheckBox("Create a relay playlist file");
	private JCheckBox proxyCheckBox = new JCheckBox("Use HTTP proxy server. url: ");
	private JCheckBox skipTracksCheckBox = new JCheckBox("Number of skiped tracks before starting to rip");
	private JCheckBox timeoutReconnectCheckBox = new JCheckBox("Timeout to restart connection [second]");
	private JCheckBox dCreateInvTracksCheckBox = new JCheckBox("Don't create individual tracks");

//MoreOption CheckBoxes
	private JCheckBox overWriteAllCheckBox = new JCheckBox("Ever overwrite tracks in 'complete'");
	private JCheckBox overWriteNeverCheckBox = new JCheckBox("Never overwrite tracks in 'complete'");
	private JCheckBox overWriteLargerCheckBox = new JCheckBox("Overwrite tracks in 'complete' when new track is larger than the old");
	private JCheckBox overWriteVersionCheckBox = new JCheckBox("Don't overwrite tracks in 'complete'. Rename the old file instead");
	private JCheckBox dontOverCheckBox = new JCheckBox("Don't overwrite tracks in incomplete");
	private JCheckBox noDirForStreamCheckBox = new JCheckBox("Don't create a directory for each stream");
	private JCheckBox dontScanPortCheckBox = new JCheckBox("Don't scan for free ports if base port is not available");
	private JCheckBox truncateCheckBox = new JCheckBox("Truncate completed tracks in incomplete directory");
	private JCheckBox dontReconnectCheckBox = new JCheckBox("Don't auto-reconnect");
//Codeset CheckBoxes (CheckBox = CB)
	private JCheckBox codesetRelayCB = new JCheckBox("Specify codeset for the relay stream");
	private JCheckBox codesetMetadataCB= new JCheckBox("Specify codeset for metadata");
	private JCheckBox codesetID3CB = new JCheckBox("Specify codeset for id3 tags");
	private JCheckBox codesetFilesysCB = new JCheckBox("Specify codeset for the file system");
//SplitPoint CheckBoxes (CheckBox = CB)
	private JCheckBox xs2CB = new JCheckBox("Use new algorithm for silence detection (xs2)");
	private JCheckBox xsOffsetCB = new JCheckBox("Shift relative to metadata (msec)"); 
	private JCheckBox xsPaddingCB = new JCheckBox("Add extra to prev:next track (msec)");
	private JCheckBox xsSearchCB = new JCheckBox("Search window relative to metadata (msec)");
	private JCheckBox xsSilenceCB = new JCheckBox("Expected length of silence (msec)");
//KA CB(CheckBox = CB)
	private JCheckBox id3tagV1CB = new JCheckBox("add ID3V1 tags to output file");
	private JCheckBox id3tagV2CB = new JCheckBox("add ID3V2 tags to output file");
	private JCheckBox metaDataRuleFileCB = new JCheckBox("Parse metadata using rules in file");
	private JCheckBox interfaceCB = new JCheckBox("Rip from specified interface (e.g. eth0)");
	private JCheckBox externalCmdMetaDataCB = new JCheckBox("Run external command to fetch metadata");
	private JCheckBox freeArgumentCB = new JCheckBox("Add command arguments");
	
//IDTag Labels
	private JLabel realyConnectionsLabel = new JLabel("     Maximum connections to relay stream");
//BaseOptions Labels
	private JLabel StreamNameLabel = new JLabel("Streamname: ");
	private JLabel StreamURLLabel = new JLabel("Stream URL: ");
	private JLabel StreamWebsiteLabel = new JLabel("Stream Website: ");
	private JLabel StreamCommentLabel = new JLabel("Stream comment: ");
	private JLabel StreamGenreLabel = new JLabel("Stream Genre: ");
	
	private JButton abortButton = new JButton("Abort",abortIcon);
	private JButton saveButton = new JButton("Save",saveIcon);
	private JButton saveAndExitButton = new JButton("OK",saveAndExitIcon);
	private JButton openRemotePlayList = new JButton(findIcon);
	private JButton metaFileChooser = new JButton(findIcon);
	private JButton exernProgramMeta = new JButton(findIcon);
	
	private TitledBorder codeSetTitle = BorderFactory.createTitledBorder("Codesets");
	private TitledBorder splitPointTitle = BorderFactory.createTitledBorder("Splitpoint Options (mp3 only)");
	private TitledBorder overWriteCompleteTitle = BorderFactory.createTitledBorder("Overwrite rules for 'complete'");
	private TitledBorder otherOverWriteTitle = BorderFactory.createTitledBorder("Other overwrite rules");
	private TitledBorder connectionTitle = BorderFactory.createTitledBorder("Connection settings");
	private TitledBorder idTagTitle = BorderFactory.createTitledBorder("ID3 tag");
	private TitledBorder baseOptionsTitle = BorderFactory.createTitledBorder("Basic options");
	private TitledBorder fileOptionsTitle = BorderFactory.createTitledBorder("File options");
	private TitledBorder relayServerTitle = BorderFactory.createTitledBorder("Relay server options");		
	private TitledBorder otherOptionsTitle = BorderFactory.createTitledBorder("Other Options");

	private JFileChooser dirChooser = new JFileChooser();;
	
	private Gui_StreamRipStar mainGui = null;
    private JTabbedPane tabbedPane = new JTabbedPane();
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
    private ResourceBundle toolTips = ResourceBundle.getBundle("translations.ToolTips");
    private boolean createNewStream = true;
    private boolean setVisible = true;
    private boolean dontExit = false;
    private boolean defaultStream = false;
    private Stream stream = null;
    	
	/**
	 * 
	 * @param streamID	: the ID representing the stream in the xml-file
	 * @param mainGui	: the mainframe (StreamRipStars window)
	 * @param createNewStream : Should with this window create a new stream?
	 * @param defaultStream : true if you want to edit the defaultStream
	 */
	public Gui_StreamOptions(Stream stream, Gui_StreamRipStar mainGui,
			boolean createNewStream, boolean setVisible,boolean defaultStream) {
		
		super();
		this.stream = stream;
		this.setVisible = setVisible;
		this.mainGui = mainGui;
		this.createNewStream = createNewStream;
		this.defaultStream = defaultStream;
		
		Stream tmpStream = null;
		
		if(createNewStream && !defaultStream) {
			setTitle("Create New Stream");
			tmpStream = new Stream("",Stream.getNewStreamID());
			realyPortField.setText(""+(8000+tmpStream.id));
		} 
		//Set basic proportions and Objects
		init();
		//Set the best default values
		setDefaults();
		
		if(createNewStream && !defaultStream) {
			//load the default stream
			this.stream = mainGui.getControlStream().getDefaultStream();
			
			// if stream exist (!= null) load this components
			if(this.stream != null)
				load();
			
			//set new stream to stream
			this.stream = tmpStream;
		}
		
		if(!createNewStream) {
			if(stream != null)
				load();
			else
				this.stream = new Stream("defaultName",0);
			
			if(!defaultStream) {
				setTitle("Stream Options");
			}
			else {
				setTitle("Edit Default Options");
				streamNameTF.setEditable(false);
				streamURLTF.setEditable(false);
			}
		}
		
		
		//set Language for all textfiels, Labels etc
		setLanguage();
		//after set the best Values -> repaint guis
		repaintMainGui();
		repaintCodesetAndSplitpointPanel();
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
	}
	
	//Set all Button etc
	private void init() {
		
		//If we create a new stream we mark the fields, witch
		//must be filled for running a stream
		if(createNewStream) {
			streamNameTF.setBackground(Color.ORANGE);
			streamURLTF.setBackground(Color.ORANGE);
		}
		
		setLayout(new BorderLayout());
		
		tabbedPane.addTab("File & write rules",mainOptionsPanel);
		tabbedPane.addTab("Relay server & connection options",moreOptionsPanel);
		tabbedPane.addTab("Codeset & Splitpoints", tab4Panel);
		
		mainOptionsPanel.setLayout(new GridBagLayout());
		baseOptionsPanel.setLayout(new GridBagLayout());
		fileOptionsPanel.setLayout(new GridBagLayout());
		relayServerPanel.setLayout(new GridBagLayout());
		otherOptionsPanel.setLayout(new GridBagLayout());
		
		moreOptionsPanel.setLayout(new GridBagLayout());
		overWriteCompletePanel.setLayout(new GridBagLayout());
		otherWriteRulesPanel.setLayout(new GridBagLayout());
		
		connectionSettingsPanel.setLayout(new GridBagLayout());
		codesetPanel.setLayout(new GridBagLayout());
		splitPointPanel.setLayout(new GridBagLayout());
		tab4Panel.setLayout(new GridBagLayout());
		buttonPanel.setLayout(new GridBagLayout());
		idTagPanel.setLayout(new GridBagLayout());
		
		//set TitledBorder for better look
		codesetPanel.setBorder(codeSetTitle);
		splitPointPanel.setBorder(splitPointTitle);
		overWriteCompletePanel.setBorder(overWriteCompleteTitle);
		otherWriteRulesPanel.setBorder(otherOverWriteTitle);
		connectionSettingsPanel.setBorder(connectionTitle);
		idTagPanel.setBorder(idTagTitle);	
		baseOptionsPanel.setBorder(baseOptionsTitle);
		fileOptionsPanel.setBorder(fileOptionsTitle);
		relayServerPanel.setBorder(relayServerTitle);	
		otherOptionsPanel.setBorder(otherOptionsTitle);
		
		ButtonGroup overWriteGroup = new ButtonGroup();
		overWriteGroup.add(overWriteAllCheckBox);
		overWriteGroup.add(overWriteNeverCheckBox);
		overWriteGroup.add(overWriteLargerCheckBox);
		overWriteGroup.add(overWriteVersionCheckBox);
		
		overWriteAllCheckBox.setSelected(true);
		overWriteNeverCheckBox.setSelected(true);
		overWriteLargerCheckBox.setSelected(true);
		
		if(setVisible) {
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets( 2, 2, 2, 2);
	
	//base option Panel -> in mainPanel		
			c.weightx = 0.0;
			c.gridy = 0;
			c.gridx = 0;
			baseOptionsPanel.add(StreamNameLabel,c);
			c.weightx = 1.0;
			c.gridx = 1;
			baseOptionsPanel.add(streamNameTF,c);
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0.0;
			baseOptionsPanel.add(StreamURLLabel,c);
			c.weightx = 1.0;
			c.gridx = 1;
			baseOptionsPanel.add(streamURLTF,c);
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 0.0;
			baseOptionsPanel.add(StreamWebsiteLabel,c);
			c.weightx = 1.0;
			c.gridx = 1;
			baseOptionsPanel.add(streamWebsiteTF,c);
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 0.0;
			baseOptionsPanel.add(StreamCommentLabel,c);
			c.weightx = 1.0;
			c.gridx = 1;
			baseOptionsPanel.add(streamCommentTF,c);
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 0.0;
			baseOptionsPanel.add(StreamGenreLabel,c);
			c.weightx = 1.0;
			c.gridx = 1;
			baseOptionsPanel.add(streamGenreTF,c);
			
	//file option Panel -> in mainpanel
			c.insets = new Insets( 1, 1, 1, 1);
			c.weightx = 0.0;
			c.gridy = 0;
			c.gridx = 0;
			fileOptionsPanel.add(allInOneCheckBox,c);
			c.weightx = 1.0;
			c.gridx = 1;
			c.gridwidth = 12;
			fileOptionsPanel.add(allInOneSaveField,c);
			c.gridwidth = 1;
			c.weightx = 0.0;
			c.gridx = 0;
			c.gridy = 1;
			fileOptionsPanel.add(lengthRecordCheckBox,c);
			c.gridx = 1;
			fileOptionsPanel.add(lengthRecordHourField,c);
			c.gridx = 3;
			fileOptionsPanel.add(lengthRecordMinuteField,c);
			c.gridx = 5;
			fileOptionsPanel.add(lengthRecordSecondField,c);
			c.gridx = 2;
			fileOptionsPanel.add(new JLabel(":"),c);
			c.gridx = 4;
			fileOptionsPanel.add(new JLabel(":"),c);
			c.gridx = 6;
			c.weightx = 1.0;
			fileOptionsPanel.add(new JLabel(" "),c);
	
			c.weightx = 0.0;
			c.gridx = 0;
			c.gridy = 2;
			fileOptionsPanel.add(maxSizeMBCheckBox,c);
			c.gridx = 1;
			c.gridwidth = 12;
			fileOptionsPanel.add(maxSizeMBField,c);
	
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 3;
			fileOptionsPanel.add(runIndexCheckBox,c);
			c.gridwidth = 12;
			c.gridx = 1;
			fileOptionsPanel.add(runIndexField,c);
	
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 4;
			fileOptionsPanel.add(patternCheckBox,c);
			c.gridwidth = 12;
			c.gridx = 1;
			fileOptionsPanel.add(patternField,c);
	
			//overwrite rules for complete -> main options
			c.weightx = 1.0;
			c.gridx = 0;
			c.gridy = 0;
			overWriteCompletePanel.add(overWriteVersionCheckBox,c );
			c.gridy = 1;
			overWriteCompletePanel.add(overWriteAllCheckBox,c );
			c.gridy = 2;
			overWriteCompletePanel.add(overWriteNeverCheckBox,c );
			c.gridy = 3;
			overWriteCompletePanel.add(overWriteLargerCheckBox,c );
			
			c.gridy = 0;
			otherWriteRulesPanel.add(truncateCheckBox,c );
			c.gridy = 1;
			otherWriteRulesPanel.add(dontOverCheckBox,c );
			c.gridy = 2;
			otherWriteRulesPanel.add(noDirForStreamCheckBox,c );
			c.gridy = 3;
			otherWriteRulesPanel.add(dCreateInvTracksCheckBox,c );
			
			//pack together main Option panel
			c.insets = new Insets( 10, 5, 5, 5);
			c.weightx = 1.0;
			c.gridy = 0;
			c.gridx = 0;
			mainOptionsPanel.add(baseOptionsPanel,c);
			c.insets = new Insets( 2, 2, 2, 2);
			c.gridy = 1;
			mainOptionsPanel.add(fileOptionsPanel,c);
			c.gridy = 2;
			mainOptionsPanel.add(overWriteCompletePanel,c);
			c.gridy = 3;
			mainOptionsPanel.add(otherWriteRulesPanel,c);
			c.weighty = 1.0;
			c.gridy = 4;
			mainOptionsPanel.add(new JPanel(),c);
			
	
		//TAB3 - tab4Panel
			//codeset panel -> tab3
			c.weightx = 0.0;
			c.gridwidth = 1;
			c.insets = new Insets( 0, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			codesetPanel.add(codesetRelayCB,c);
			c.gridx = 1;
			c.weightx = 1.0;
			codesetPanel.add(codesetRelayTF,c);
			c.weightx = 0.0;
			c.gridx = 0;
			c.gridy = 1;
			codesetPanel.add(codesetMetadataCB,c);
			c.gridx = 1;
			codesetPanel.add(codesetMetadataTF,c);
			c.gridx = 0;
			c.gridy = 2;
			codesetPanel.add(codesetID3CB,c);
			c.gridx = 1;
			codesetPanel.add(codesetID3TF,c);
			c.gridx = 0;
			c.gridy = 3;
			codesetPanel.add(codesetFilesysCB,c);
			c.gridx = 1;
			codesetPanel.add(codesetFilesysTF,c);
			
			//splitpoint panel -> tab3
			c.gridx = 0;
			c.gridy = 0;
			splitPointPanel.add( xs2CB ,c);
			c.gridy = 1;
			splitPointPanel.add( xsOffsetCB ,c);
			c.gridx = 1;
			splitPointPanel.add( xsOffsetTF ,c);
			c.gridx = 0;
			c.gridy = 2;
			splitPointPanel.add( xsPaddingCB ,c);
			c.gridx = 1;
			splitPointPanel.add( xsPadding1TF ,c);
			c.gridx = 2;
			splitPointPanel.add( new JLabel(":") ,c);
			c.gridx = 3;
			splitPointPanel.add( xsPadding2TF ,c);
			c.gridx = 0;
			c.gridy = 3;
			splitPointPanel.add( xsSearchCB,c);
			c.gridx = 1;
			splitPointPanel.add( xsSearch1TF,c);
			c.gridx = 2;
			splitPointPanel.add( new JLabel(":") ,c);
			c.gridx = 3;
			splitPointPanel.add( xsSearch2TF,c);
			c.gridx = 0;
			c.gridy = 4;
			splitPointPanel.add( xsSilenceCB ,c);
			c.gridx = 1;
			splitPointPanel.add( xsSilenceTF ,c);
			c.weightx = 1.0;
			c.gridx = 4;
			splitPointPanel.add( new JLabel() ,c);
	
			//idTag panel
			c.weighty = 0.0;
			c.weightx = 1.0;	
			c.gridx = 0;
			c.gridy = 0;
			idTagPanel.add(id3tagV1CB,c);
			c.gridy = 1;
			idTagPanel.add(id3tagV2CB,c);
				
			//pack together -> tab3
			c.insets = new Insets( 10, 5, 5, 5);
			c.weightx = 1.0;
			c.gridx = 0;
			c.gridy = 0;
			tab4Panel.add( codesetPanel ,c);
			c.insets = new Insets( 2, 2, 2, 2);
			c.gridy = 1;
			tab4Panel.add( splitPointPanel ,c);
			c.gridy = 2;
			tab4Panel.add( idTagPanel ,c);
			c.gridy = 3;
			c.weighty = 1.0;					
			tab4Panel.add( new JLabel() ,c);	//keep panels on top
	
		//TAB 2 - moreOptionsPanel
			//relay server options -> moreOptionsPanel
			c.weighty = 0.0;	
			c.weightx = 0.0;
			c.insets = new Insets( 0, 0, 0, 0);
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 0;
			relayServerPanel.add(relayServerCheckBox,c);
			c.gridwidth = 2;
			c.gridx = 1;
			c.weightx = 1.0;
			relayServerPanel.add(realyPortField,c);
			c.weightx = 0.0;
			c.gridy = 1;
			c.gridx = 0;
			c.gridwidth = 1;
			relayServerPanel.add(realyConnectionsLabel,c);
			c.gridx = 1;
			c.weightx = 1.0;
			relayServerPanel.add(realyConnectionsField,c);
			c.weightx = 0.0;
			c.gridy = 2;
			c.gridx = 0;
			relayServerPanel.add(relayConnect,c);
			c.gridy = 3;
			c.gridx = 0;
			relayServerPanel.add(createRelayFileCB,c);
			c.weightx = 1.0;
			c.gridx = 1;
			relayServerPanel.add(createRelayFileTF,c);
			c.weightx = 0.0;
			c.gridx = 2;
			relayServerPanel.add(openRemotePlayList,c);
			
	
			//other options panel -> moreOptionsPanel
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridwidth = 1;
			c.gridy = 0;
			c.gridx = 0;
			otherOptionsPanel.add(skipTracksCheckBox,c);
			c.gridx = 1;
			otherOptionsPanel.add(skipTracksField,c);
			c.weightx = 0.0;
			c.gridy = 1;
			c.gridx = 0;
			otherOptionsPanel.add(metaDataRuleFileCB,c);
			c.gridx = 1;
			c.weightx = 1.0;
			otherOptionsPanel.add(metaDataRuleFileTF,c);
			c.weightx = 0.0;
			c.gridx = 2;
			otherOptionsPanel.add(metaFileChooser,c);
			c.gridy = 2;
			c.gridx = 0;
			otherOptionsPanel.add(interfaceCB,c);
			c.gridx = 1;
			otherOptionsPanel.add(interfaceTF,c);
			c.gridy = 3;
			c.gridx = 0;
			otherOptionsPanel.add(externalCmdMetaDataCB,c);
			c.gridx = 1;
			c.gridwidth = 1;
			otherOptionsPanel.add(externalCmdMetaDataTF,c);
			c.gridx = 2;
			c.gridwidth = 1;
			otherOptionsPanel.add(exernProgramMeta,c);
			c.weightx = 0.0;
			c.gridx = 0;
			c.gridy = 4;
			c.gridwidth = 1;
			otherOptionsPanel.add( freeArgumentCB ,c);
			c.gridx = 1;
			c.gridwidth = 2;
			c.weightx = 1.0;
			otherOptionsPanel.add( freeArgumentTF ,c);	
			 
			//connection panel -> moreOptionsPanel
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 0;
			connectionSettingsPanel.add(dontScanPortCheckBox,c );
			c.gridy = 1;
			connectionSettingsPanel.add(dontReconnectCheckBox,c );
			c.gridwidth = 1;
			c.gridy = 2;
			connectionSettingsPanel.add(timeoutReconnectCheckBox,c);
			c.gridx = 1;
			connectionSettingsPanel	.add(timeoutReconnectField,c);
			c.gridy = 3;
			c.gridx = 0;
			connectionSettingsPanel.add(proxyCheckBox,c);
			c.gridx = 1;
			c.weightx = 1.0;
			c.gridwidth = 2;
			connectionSettingsPanel.add(proxyField,c);
			c.gridwidth = 1;
			c.weightx = 0.0;
			c.gridx = 0;
			c.gridy = 4;
			connectionSettingsPanel.add(useragentCheckBox,c);c.gridwidth = 1;
			c.gridwidth = 2;
			c.weightx = 1.0;
			c.gridx = 1;
			connectionSettingsPanel.add(useragentField,c);
			
			
			c.insets = new Insets( 10, 2, 2, 2);
			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridy = 0;
			moreOptionsPanel.add(relayServerPanel,c );
			c.gridy = 1;
			moreOptionsPanel.add(connectionSettingsPanel,c );
			c.gridy = 2;
			moreOptionsPanel.add(otherOptionsPanel ,c );
			c.gridy = 3;
			c.weighty = 1.0;
			moreOptionsPanel.add(new JLabel(""),c);
			
	//ButtonPanel    
	//first visible line: Buttons
			c.insets = new Insets( 7, 7, 7, 7);
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.0;
			buttonPanel.add(saveAndExitButton,c);		//add OK Button left first
			c.gridx = 1;
			buttonPanel.add(saveButton,c);				//add save Button left first
			c.gridx = 4;
			c.weightx = 1.0;
			buttonPanel.add(new JLabel("  "),c); 	//Label make abort Button stay on right side
			c.gridx = 5;
			c.weightx = 0.0;
			buttonPanel.add(abortButton,c); 	//Label make abort Button stay on right side
			
			openRemotePlayList.addActionListener(new RemotePlayListListener());
			metaFileChooser.addActionListener(new MetaFileChooserListener());
			exernProgramMeta.addActionListener(new ExternProgramMetaListener());
			
			allInOneCheckBox.addActionListener(new RepaintMainListener());
			lengthRecordCheckBox.addActionListener(new RepaintMainListener());
			maxSizeMBCheckBox.addActionListener(new RepaintMainListener());
			useragentCheckBox.addActionListener(new RepaintMainListener());
			runIndexCheckBox.addActionListener(new RepaintMainListener());
			relayServerCheckBox.addActionListener(new RepaintMainListener());
			createRelayFileCB.addActionListener(new RepaintMainListener());
			proxyCheckBox.addActionListener(new RepaintMainListener());
			skipTracksCheckBox.addActionListener(new RepaintMainListener());
			timeoutReconnectCheckBox.addActionListener(new RepaintMainListener());
			patternCheckBox.addActionListener(new RepaintMainListener());
	
			saveButton.addActionListener(new SaveListener());
			saveAndExitButton.addActionListener(new SaveAndExitListener());
			abortButton.addActionListener(new ExitListener());
			
			codesetRelayCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			codesetMetadataCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			codesetID3CB.addActionListener(new RepaintCodesetAndSplitPointListener());
			codesetFilesysCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			xs2CB.addActionListener(new RepaintCodesetAndSplitPointListener());
			xsOffsetCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			xsPaddingCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			xsSearchCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			xsSilenceCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			freeArgumentCB.addActionListener(new FreeArgumentsListener());
			metaDataRuleFileCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			interfaceCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			externalCmdMetaDataCB.addActionListener(new RepaintCodesetAndSplitPointListener());
			
			//set tooltip time: 0.2 seconds before see it and 1 Minute before hide it
			ToolTipManager.sharedInstance().setInitialDelay(1000);
			ToolTipManager.sharedInstance().setReshowDelay(1000);
			ToolTipManager.sharedInstance().setDismissDelay(60000);
			
			//setToolTips
			allInOneSaveField.setToolTipText(toolTips.getString("singleFileField"));
			allInOneCheckBox.setToolTipText(toolTips.getString("singleFile"));
			maxSizeMBCheckBox.setToolTipText(toolTips.getString("maxSize"));
			maxSizeMBField.setToolTipText(toolTips.getString("maxSizeField"));
			lengthRecordCheckBox.setToolTipText(toolTips.getString("maxLength"));
			lengthRecordHourField.setToolTipText(toolTips.getString("maxLengthHours"));
			lengthRecordMinuteField.setToolTipText(toolTips.getString("maxLengthMinutes"));
			lengthRecordSecondField.setToolTipText(toolTips.getString("maxLengthSeconds"));
			useragentCheckBox.setToolTipText(toolTips.getString("useragent"));
			useragentField.setToolTipText(toolTips.getString("useragentField"));
			runIndexCheckBox.setToolTipText(toolTips.getString("runIndex"));
			runIndexField.setToolTipText(toolTips.getString("runIndexField"));
			patternCheckBox.setToolTipText(toolTips.getString("pattern"));
			patternField.setToolTipText(toolTips.getString("singleFileField"));
			relayServerCheckBox.setToolTipText(toolTips.getString("relayServer"));
			realyPortField.setToolTipText(toolTips.getString("relayServerField"));
			realyConnectionsLabel.setToolTipText(toolTips.getString("maxConnections"));
			realyConnectionsField.setToolTipText(toolTips.getString("maxConnectionsField"));
			relayConnect.setToolTipText(toolTips.getString("relayConnect"));
			proxyCheckBox.setToolTipText(toolTips.getString("proxy"));
			proxyField.setToolTipText(toolTips.getString("proxyField"));
			skipTracksCheckBox.setToolTipText(toolTips.getString("skipTrack"));
			skipTracksField.setToolTipText(toolTips.getString("skipTrackField"));
			timeoutReconnectCheckBox.setToolTipText(toolTips.getString("timeout"));
			timeoutReconnectField.setToolTipText(toolTips.getString("timeoutField"));
			
			overWriteAllCheckBox.setToolTipText(toolTips.getString("overWrite")+toolTips.getString("overWriteAlways"));
			overWriteNeverCheckBox.setToolTipText(toolTips.getString("overWrite")+toolTips.getString("overWriteNever"));
			overWriteLargerCheckBox.setToolTipText(toolTips.getString("overWrite")+toolTips.getString("overWriteLarger"));
			overWriteVersionCheckBox.setToolTipText(toolTips.getString("overWrite")+toolTips.getString("overWriteVersion"));
			dontOverCheckBox.setToolTipText(toolTips.getString("dontOverIncom"));
			truncateCheckBox.setToolTipText(toolTips.getString("truncate"));
			noDirForStreamCheckBox.setToolTipText(toolTips.getString("noStreamDir"));
			dontScanPortCheckBox.setToolTipText(toolTips.getString("dontScanPorts"));
			dontReconnectCheckBox.setToolTipText(toolTips.getString("dontReconnect"));
			dCreateInvTracksCheckBox.setToolTipText(toolTips.getString("dCInvTracks"));
			createRelayFileCB.setToolTipText(toolTips.getString("createRelayPlaylist"));
			createRelayFileTF.setToolTipText(toolTips.getString("createRelayPlaylistTF"));
			metaDataRuleFileCB.setToolTipText(toolTips.getString("metaDataRuleFileCB"));
			interfaceCB.setToolTipText(toolTips.getString("interfaceCB"));
			interfaceTF.setToolTipText(toolTips.getString("interfaceTF"));
			metaDataRuleFileTF.setToolTipText(toolTips.getString("metaDataRuleFileTF"));
			externalCmdMetaDataCB.setToolTipText(toolTips.getString("externalCmdMetaDataCB"));
			externalCmdMetaDataTF.setToolTipText(toolTips.getString("externalCmdMetaDataTF"));
			freeArgumentCB.setToolTipText(toolTips.getString("freeArgumentATT")
					+toolTips.getString("freeArgumentCB"));
			freeArgumentTF.setToolTipText(toolTips.getString("freeArgumentATT")
					+toolTips.getString("freeArgumentTF"));
			codesetRelayCB.setToolTipText(toolTips.getString("codeset"));
			codesetMetadataCB.setToolTipText(toolTips.getString("codeset"));
			codesetID3CB.setToolTipText(toolTips.getString("codeset"));
			codesetFilesysCB.setToolTipText(toolTips.getString("codeset"));
			codesetRelayTF.setToolTipText(toolTips.getString("codesetTF"));
			codesetMetadataTF.setToolTipText(toolTips.getString("codesetTF"));
			codesetID3TF.setToolTipText(toolTips.getString("codesetTF"));
			codesetFilesysTF.setToolTipText(toolTips.getString("codesetTF"));
			
			xs2CB.setToolTipText(toolTips.getString("xs2CB"));
			xsOffsetCB.setToolTipText(toolTips.getString("xs"));
			xsPaddingCB.setToolTipText(toolTips.getString("xs"));
			xsSearchCB.setToolTipText(toolTips.getString("xs"));
			xsSilenceCB.setToolTipText(toolTips.getString("xs"));
			
			xsOffsetTF.setToolTipText(toolTips.getString("xsTime"));
			xsPadding1TF.setToolTipText(toolTips.getString("xsTime"));
			xsPadding2TF.setToolTipText(toolTips.getString("xsTime"));
			xsSearch1TF.setToolTipText(toolTips.getString("xsTime"));
			xsSearch2TF.setToolTipText(toolTips.getString("xsTime"));
			xsSilenceTF.setToolTipText(toolTips.getString("xsTime"));
			
			id3tagV1CB.setToolTipText(toolTips.getString("id3tagV1CB"));
			id3tagV2CB.setToolTipText(toolTips.getString("id3tagV2CB"));
			
			add(tabbedPane, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
			
			//set size of window
			//pack together
	
//			setSize(new Dimension(650,700));
			pack();
			//get new dimension of the window
	        Dimension frameDim = getSize();
	    	
	        //get resolution
	        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	        
	        //calculates the app. values
	        int x = (screenDim.width - frameDim.width)/2;
	        int y = (screenDim.height - frameDim.height)/2;
	        
	        //set location
	        setLocation(x, y);
	        
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);

			
	        //escape for exit
	        KeyStroke escStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
	        //register all Strokes
	        getRootPane().registerKeyboardAction(new ExitListener(), escStroke,
	                JComponent.WHEN_IN_FOCUSED_WINDOW);
		}		
	}
	
	/**
	 * This method select all check buttons
	 * for the default. If no config can be found
	 * this settings are used.
	 * Standard is save, which means that keep all
	 * tracks, available.
	 */
	private void setDefaults() {
		overWriteVersionCheckBox.setSelected(true);
		overWriteLargerCheckBox.setSelected(true);
		dontOverCheckBox.setSelected(true);
		id3tagV2CB.setSelected(true);
	}
	
	
	public void setBasics(String[] basics) {
		streamNameTF.setText(basics[1]);
		streamURLTF.setText(basics[0]);
		streamWebsiteTF.setText(basics[2]);
	}
	
	private void setLanguage() {
		try {
			
    		if(createNewStream) {
    			//the title if a new stream is created
        		setTitle(trans.getString("newStream"));
			} else if (defaultStream) {
				//the title when the default options are editable
        		setTitle(trans.getString("editDefaultOptions"));
			} else if (stream != null){
				//the options when a stream should edit
        		setTitle(trans.getString("editStream")+" "+stream.name);
			} else {
				//for all other options // fallback
				setTitle(trans.getString("editStream")+" "+stream.name);
			}
    		
    		//tabs
    		tabbedPane.setTitleAt(0, trans.getString("tab0"));
    		tabbedPane.setTitleAt(1, trans.getString("tab1"));
    		tabbedPane.setTitleAt(2, trans.getString("tab2"));
    		
    		//first JPanel
    		baseOptionsTitle.setTitle(trans.getString("baseOption.title"));
    		StreamNameLabel.setText(trans.getString("StreamNameLabel"));
    		StreamURLLabel.setText(trans.getString("StreamURLLabel"));
    		StreamWebsiteLabel.setText(trans.getString("StreamWebsiteLabel"));
    		StreamCommentLabel.setText(trans.getString("StreamCommentLabel"));
    		StreamGenreLabel.setText(trans.getString("StreamGenreLabel"));
    		
    		//fileOptionsPanel
    		fileOptionsTitle.setTitle(trans.getString("fileOptionsPanel.title"));
    		allInOneCheckBox.setText(trans.getString("fileOptionsPanel.saveSingleFile"));
    		lengthRecordCheckBox.setText(trans.getString("fileOptionsPanel.maxLength"));
    		maxSizeMBCheckBox.setText(trans.getString("fileOptionsPanel.maxSize"));
    		runIndexCheckBox.setText(trans.getString("fileOptionsPanel.addSequence"));
    		patternCheckBox.setText(trans.getString("fileOptionsPanel.usePattern"));
    		
    		//overWriteCompletePanel
    		overWriteCompleteTitle.setTitle(trans.getString("overWriteCompletePanel.title"));
    		overWriteAllCheckBox.setText(trans.getString("overWriteCompletePanel.OverwriteEverComp"));
    		overWriteNeverCheckBox.setText(trans.getString("overWriteCompletePanel.OverwriteNeverComp"));
    		overWriteLargerCheckBox.setText(trans.getString("overWriteCompletePanel.OverwriteLargerComp"));
    		overWriteVersionCheckBox.setText(trans.getString("overWriteCompletePanel.OverwriteVersionComp"));
    		
    		//otherOptionsPanel
    		otherOverWriteTitle.setTitle(trans.getString("otherWriteRulesPanel.title"));
    		truncateCheckBox.setText(trans.getString("otherWriteRulesPanel.Truncate"));
    		dontOverCheckBox.setText(trans.getString("otherWriteRulesPanel.dontOverIncomp"));
    		noDirForStreamCheckBox.setText(trans.getString("otherWriteRulesPanel.dontCreatDir"));
    		dCreateInvTracksCheckBox.setText(trans.getString("otherWriteRulesPanel.dCreateInvTracks"));
    		
    		//relayServerPanel
    		relayServerTitle.setTitle(trans.getString("relayServerPanel.title"));
    		relayServerCheckBox.setText(trans.getString("relayServerPanel.realyServer"));
    		realyConnectionsLabel.setText("      "+trans.getString("relayServerPanel.maxConnections"));
    		relayConnect.setText(trans.getString("relayServerPanel.playReal"));
    		createRelayFileCB.setText(trans.getString("relayServerPanel.createPlaylist"));
    		
    		//otherOptionsPanel
    		otherOptionsTitle.setTitle(trans.getString("otherOptionsPanel.title"));
    		skipTracksCheckBox.setText(trans.getString("otherOptionsPanel.skiptedTracks"));
    		metaDataRuleFileCB.setText(trans.getString("otherOptionsPanel.metaDataRuleFile"));
    		interfaceCB.setText(trans.getString("otherOptionsPanel.interface"));
    		externalCmdMetaDataCB.setText(trans.getString("otherOptionsPanel.externalCmdMetaData"));
    		freeArgumentCB.setText(trans.getString("otherOptionsPanel.freeArgument"));
    		
    		//connectionSettingsPanel
    		connectionTitle.setTitle(trans.getString("connectionSettingsPanel.title"));
    		proxyCheckBox.setText(trans.getString("connectionSettingsPanel.httpProxy"));
    		timeoutReconnectCheckBox.setText(trans.getString("connectionSettingsPanel.timeout"));
    		useragentCheckBox.setText(trans.getString("connectionSettingsPanel.Useragent"));
    		dontScanPortCheckBox.setText(trans.getString("connectionSettingsPanel.DontScan"));
    		dontReconnectCheckBox.setText(trans.getString("connectionSettingsPanel.DontAutoReco"));
    		
    		//codesetPanel
    		codeSetTitle.setTitle(trans.getString("codesetPanel.title"));
    		codesetRelayCB.setText(trans.getString("codesetPanel.codesetRelay"));
    		codesetMetadataCB.setText(trans.getString("codesetPanel.codesetMetadata"));
    		codesetID3CB.setText(trans.getString("codesetPanel.codesetID3"));
    		codesetFilesysCB.setText(trans.getString("codesetPanel.codesetFilesys"));

    		//splitPointPanel
    		splitPointTitle.setTitle(trans.getString("splitPointPanel.title"));
    		xs2CB.setText(trans.getString("splitPointTitle.xs2"));
    		xsOffsetCB.setText(trans.getString("splitPointTitle.xsOffset"));
    		xsPaddingCB.setText(trans.getString("splitPointTitle.xsPadding"));
    		xsSearchCB.setText(trans.getString("splitPointTitle.xsSearch"));
    		xsSilenceCB.setText(trans.getString("splitPointTitle.xsSilence"));
    		
    		//idTagPanel
    		idTagTitle.setTitle(trans.getString("idTagPanel.title"));
    		id3tagV1CB.setText(trans.getString("idTagPanel.id3tagV1"));
    		id3tagV2CB.setText(trans.getString("idTagPanel.id3tagV2"));

    		//buttons
    		abortButton.setText(trans.getString("abortButton"));
    		saveButton.setText(trans.getString("save"));
			saveAndExitButton.setText(trans.getString("okButton"));
    	} catch ( MissingResourceException e ) { 
		      System.err.println( e ); 
	    }
	}
	
	public void repaintCodesetAndSplitpointPanel() {
		if(codesetRelayCB.isSelected()){
			codesetRelayTF.setEditable(true);
		} else {
			codesetRelayTF.setEditable(false);
		}
		
		if(codesetMetadataCB.isSelected()) {
			codesetMetadataTF.setEditable(true);
		} else {
			codesetMetadataTF.setEditable(false);
		}
		if(codesetID3CB.isSelected()) {
			codesetID3TF.setEditable(true);
		} else {
			codesetID3TF.setEditable(false);
		}
		if(codesetFilesysCB.isSelected()) {
			codesetFilesysTF.setEditable(true);
		} else {
			codesetFilesysTF.setEditable(false);
		}
		
		//SplitpointCBs
		if(xsOffsetCB.isSelected()) {
			xsOffsetTF.setEditable(true);
		} else {
			xsOffsetTF.setEditable(false);
		}
		if(xsPaddingCB.isSelected()) {
			xsPadding1TF.setEditable(true);
			xsPadding2TF.setEditable(true);
		} else {
			xsPadding1TF.setEditable(false);
			xsPadding2TF.setEditable(false);
		}
		if(xsSearchCB.isSelected()) {
			xsSearch1TF.setEditable(true);
			xsSearch2TF.setEditable(true);
		} else {
			xsSearch1TF.setEditable(false);
			xsSearch2TF.setEditable(false);
		}
		if(xsSilenceCB.isSelected()) {
			xsSilenceTF.setEditable(true);
		} else {
			xsSilenceTF.setEditable(false);
		}
		
		//extra argument panel
		if(freeArgumentCB.isSelected()) {
			freeArgumentTF.setEditable(true);
		} else {
			freeArgumentTF.setEditable(false);
		}
		
		if(metaDataRuleFileCB.isSelected()) {
			metaDataRuleFileTF.setEditable(true);
		} else {
			metaDataRuleFileTF.setEditable(false);
		}
		
		if(	interfaceCB.isSelected()) {
			interfaceTF.setEditable(true);
		} else {
			interfaceTF.setEditable(false);
		}
		
		if(externalCmdMetaDataCB.isSelected()) {
			externalCmdMetaDataTF.setEditable(true);
		} else {
			externalCmdMetaDataTF.setEditable(false);
		}
	}
	
		
	public  void repaintMainGui(){
		if (allInOneCheckBox.isSelected())
			allInOneSaveField.setEditable(true);
		else
			allInOneSaveField.setEditable(false);

		if (lengthRecordCheckBox.isSelected()) {
			lengthRecordHourField.setEditable(true);
			lengthRecordMinuteField.setEditable(true);
			lengthRecordSecondField.setEditable(true);
		} else {
			lengthRecordHourField.setEditable(false);
			lengthRecordMinuteField.setEditable(false);
			lengthRecordSecondField.setEditable(false);
		}
		
		if (maxSizeMBCheckBox.isSelected())
			maxSizeMBField.setEditable(true);
		else
			maxSizeMBField.setEditable(false);
		
		if (useragentCheckBox.isSelected())
			useragentField.setEditable(true);
		else
			useragentField.setEditable(false);
			
		if (runIndexCheckBox.isSelected())
			runIndexField.setEditable(true);
		else
			runIndexField.setEditable(false);
				
		if (relayServerCheckBox.isSelected()) {
			
			if(!createNewStream)
				realyPortField.setEditable(true);
			realyConnectionsLabel.setEnabled(true);
			realyConnectionsField.setEditable(true);
			relayConnect.setEnabled(true);
			createRelayFileCB.setEnabled(true);
		} else {
			realyPortField.setEditable(false);
			realyConnectionsLabel.setEnabled(false);
			realyConnectionsField.setEditable(false);
			relayConnect.setEnabled(false);
			createRelayFileCB.setEnabled(false);
		}
		
		if(createRelayFileCB.isSelected()) {
			createRelayFileTF.setEditable(true);
		} else {
			createRelayFileTF.setEditable(false);
		}
		
		
		if (patternCheckBox.isSelected())
			patternField.setEditable(true);
		else
			patternField.setEditable(false);
		
		if (proxyCheckBox.isSelected())
			proxyField.setEditable(true);
		else
			proxyField.setEditable(false);
			
		if (skipTracksCheckBox.isSelected())
			skipTracksField.setEditable(true);
		else
			skipTracksField.setEditable(false);
		
		if (timeoutReconnectCheckBox.isSelected())
			timeoutReconnectField.setEditable(true);
		else	
			timeoutReconnectField.setEditable(false);
	}
	
	public void save() {
		//the name and the url must entered at least
		if((streamNameTF.getText().trim().equals("") 
				|| streamURLTF.getText().trim().equals("")) && !defaultStream)
		{
			dontExit=true;
			JOptionPane.showMessageDialog(getMe(),trans.getString("noNameOrUrl"));
		}
		//normal save
		else {
			dontExit = false;
			//save all checkbox-states as boolean
			//(selected or not)
			stream.singleFileCB = allInOneCheckBox.isSelected();
			stream.maxTimeCB = lengthRecordCheckBox.isSelected();
			stream.maxMBCB = maxSizeMBCheckBox.isSelected();
			stream.sequenzCB = runIndexCheckBox.isSelected();
			stream.patternCB = patternCheckBox.isSelected();
			
			if(overWriteVersionCheckBox.isSelected())
				stream.completeCB = 0;
			else if (overWriteAllCheckBox.isSelected())
				stream.completeCB = 1;
			else if (overWriteNeverCheckBox.isSelected())
				stream.completeCB = 2;
			else 
				stream.completeCB = 3;

			stream.cutSongIncompleteCB = truncateCheckBox.isSelected();
			stream.neverOverIncompCB = dontOverCheckBox.isSelected();
			stream.noDirEveryStreamCB = noDirForStreamCheckBox.isSelected();
			stream.noIndiviSongsCB = dCreateInvTracksCheckBox.isSelected();
			stream.createReayCB = relayServerCheckBox.isSelected();
			stream.connectToRelayCB = relayConnect.isSelected();
			stream.createPlaylistRelayCB = createRelayFileCB.isSelected();
			stream.dontSearchAltPortCB = dontScanPortCheckBox.isSelected();
			stream.dontAutoReconnectCB = dontReconnectCheckBox.isSelected();
			stream.timeoutReconnectCB = timeoutReconnectCheckBox.isSelected();
			stream.proxyCB = proxyCheckBox.isSelected();
			stream.useragentCB = useragentCheckBox.isSelected();
			stream.countBeforStartCB = skipTracksCheckBox.isSelected();
			stream.metaDataCB = metaDataRuleFileCB.isSelected();
			stream.interfaceCB = interfaceCB.isSelected();
			stream.externMetaDataCB = externalCmdMetaDataCB.isSelected();
			stream.extraArgsCB = freeArgumentCB.isSelected();
			stream.CSRelayCB = codesetRelayCB.isSelected();
			stream.CSMetaCB = codesetMetadataCB.isSelected();
			stream.CSIDTagCB = codesetID3CB.isSelected();
			stream.CSFileSysCB = codesetFilesysCB.isSelected();
			stream.XS2CB= xs2CB.isSelected();
			stream.SPDelayCB = xsOffsetCB.isSelected();
			stream.SPExtraCB = xsPaddingCB.isSelected();
			stream.SPWindowCB= xsSearchCB.isSelected();
			stream.SPSilenceCB = xsSilenceCB.isSelected();
			stream.IDV1CB = id3tagV1CB.isSelected();
			stream.IDV2CB = id3tagV2CB.isSelected();
			
			//Save all Strings from textfiels
			stream.name = streamNameTF.getText();
			stream.address = streamURLTF.getText();
			stream.comment = streamCommentTF.getText();
			stream.website = streamWebsiteTF.getText();
			stream.genre = streamGenreTF.getText();
			stream.singleFileTF = allInOneSaveField.getText();
			stream.maxTimeHHTF = lengthRecordHourField.getText();
			stream.maxTimeMMTF = lengthRecordMinuteField.getText();
			stream.maxTimessTF = lengthRecordSecondField.getText();
			stream.maxMBTF = maxSizeMBField.getText();
			stream.sequenzTF = runIndexField.getText();
			stream.patternTF = patternField.getText();
			stream.relayServerPortTF = realyPortField.getText();
			stream.maxConnectRelayTF = realyConnectionsField.getText();
			stream.relayPlayListTF = createRelayFileTF.getText();
			stream.timeOutReonTF = timeoutReconnectField.getText();
			stream.proxyTF = proxyField.getText();
			stream.useragentTF = useragentField.getText();
			stream.sciptSongsTF = skipTracksField.getText();
			stream.metaDataFileTF = metaDataRuleFileTF.getText();
			stream.interfaceTF = interfaceTF.getText();
			stream.externTF = externalCmdMetaDataTF.getText();
			stream.extraArgsTF = freeArgumentTF.getText();
			stream.CSRelayTF = codesetRelayTF.getText();
			stream.CSMetaDataTF = codesetMetadataTF.getText();
			stream.CSIDTF = codesetID3TF.getText();
			stream.CSFileSysTF = codesetFilesysTF.getText();
			stream.SPDelayTF = xsOffsetTF.getText();
			stream.SPExtraTF1 = xsPadding1TF.getText();
			stream.SPExtraTF2 = xsPadding2TF.getText();
			stream.SPWindowTF1 = xsSearch1TF.getText();
			stream.SPWindowTF2 = xsSearch2TF.getText();
			stream.SPSilenceTF = xsSilenceTF.getText();
			
			if(defaultStream) {
				//set new default stream for runtime
				mainGui.getControlStream().setDefaultStream(stream);
				//and save it
				mainGui.getControlStream().saveDefaultStream();
			} else {
				//if a new Stream is created, get a fresh id
				if(createNewStream) {
					mainGui.getControlStream().addStreamToVector(stream);
					mainGui.getTabel().addLastStreamFromVector();
					mainGui.getControlStream().saveStreamVector();
				} else {
					//update the table panel
					mainGui.getTabel().setNameValueWithConvert(streamNameTF.getText(), 
							mainGui.getControlStream().getPlaceInStreamVector(stream.id));
	
				}
			}
		}
	}
	
	/**
	 * loads all options from the stream and set the
	 * checkboxes and the textfields with this values
	 */
	public void load()
	{
		//load all properties for the checkboxes
		allInOneCheckBox.setSelected(stream.singleFileCB);
		lengthRecordCheckBox.setSelected(stream.maxTimeCB);
		maxSizeMBCheckBox.setSelected(stream.maxMBCB);
		runIndexCheckBox.setSelected(stream.sequenzCB);
		patternCheckBox.setSelected(stream.patternCB);
		
		if(stream.completeCB == 0)
			overWriteVersionCheckBox.setSelected(true);
		else if (stream.completeCB == 1)
			overWriteAllCheckBox.setSelected(true);
		else if (stream.completeCB == 2)
			overWriteNeverCheckBox.setSelected(true);
		else
			overWriteLargerCheckBox.setSelected(true);
		
		truncateCheckBox.setSelected(stream.cutSongIncompleteCB);
		dontOverCheckBox.setSelected(stream.neverOverIncompCB);
		noDirForStreamCheckBox.setSelected(stream.noDirEveryStreamCB);
		dCreateInvTracksCheckBox.setSelected(stream.noIndiviSongsCB);
		relayServerCheckBox.setSelected(stream.createReayCB);
		relayConnect.setSelected(stream.connectToRelayCB);
		createRelayFileCB.setSelected(stream.createPlaylistRelayCB);
		dontScanPortCheckBox.setSelected(stream.dontSearchAltPortCB);
		dontReconnectCheckBox.setSelected(stream.dontAutoReconnectCB);
		timeoutReconnectCheckBox.setSelected(stream.timeoutReconnectCB);
		proxyCheckBox.setSelected(stream.proxyCB);
		useragentCheckBox.setSelected(stream.useragentCB);
		skipTracksCheckBox.setSelected(stream.countBeforStartCB);
		metaDataRuleFileCB.setSelected(stream.metaDataCB);
		interfaceCB.setSelected(stream.interfaceCB);
		externalCmdMetaDataCB.setSelected(stream.externMetaDataCB);
		freeArgumentCB.setSelected(stream.extraArgsCB);
		codesetRelayCB.setSelected(stream.CSRelayCB);
		codesetMetadataCB.setSelected(stream.CSMetaCB);
		codesetID3CB.setSelected(stream.CSIDTagCB);
		codesetFilesysCB.setSelected(stream.CSFileSysCB);
		xs2CB.setSelected(stream.XS2CB);
		xsOffsetCB.setSelected(stream.SPDelayCB);
		xsPaddingCB.setSelected(stream.SPExtraCB);
		xsSearchCB.setSelected(stream.SPWindowCB);
		xsSilenceCB.setSelected(stream.SPSilenceCB);
		id3tagV1CB.setSelected(stream.IDV1CB);
		id3tagV2CB.setSelected(stream.IDV2CB);
		
		//load all strings for the textfields
		streamNameTF.setText(stream.name);
		streamURLTF.setText(stream.address);
		streamCommentTF.setText(stream.comment);
		streamWebsiteTF.setText(stream.website);
		streamGenreTF.setText(stream.genre);
		allInOneSaveField.setText(stream.singleFileTF);
		lengthRecordHourField.setText(stream.maxTimeHHTF);
		lengthRecordMinuteField.setText(stream.maxTimeMMTF);
		lengthRecordSecondField.setText(stream.maxTimessTF);
		maxSizeMBField.setText(stream.maxMBTF);
		runIndexField.setText(stream.sequenzTF);
		patternField.setText(stream.patternTF);
		if(!createNewStream) {
			realyPortField.setText(stream.relayServerPortTF);
		}
		realyConnectionsField.setText(stream.maxConnectRelayTF);
		createRelayFileTF.setText(stream.relayPlayListTF);
		timeoutReconnectField.setText(stream.timeOutReonTF);
		proxyField.setText(stream.proxyTF);
		useragentField.setText(stream.useragentTF);
		skipTracksField.setText(stream.sciptSongsTF);
		metaDataRuleFileTF.setText(stream.metaDataFileTF);
		interfaceTF.setText(stream.interfaceTF);
		externalCmdMetaDataTF.setText(stream.externTF);
		freeArgumentTF.setText(stream.extraArgsTF);
		codesetRelayTF.setText(stream.CSRelayTF);
		codesetMetadataTF.setText(stream.CSMetaDataTF);
		codesetID3TF.setText(stream.CSIDTF);
		codesetFilesysTF.setText(stream.CSFileSysTF);
		xsOffsetTF.setText(stream.SPDelayTF);
		xsPadding1TF.setText(stream.SPExtraTF1);
		xsPadding2TF.setText(stream.SPExtraTF2);
		xsSearch1TF.setText(stream.SPWindowTF1);
		xsSearch2TF.setText(stream.SPWindowTF2);
		xsSilenceTF.setText(stream.SPSilenceTF);
	}
	
	public Gui_StreamOptions getMe() {
		return this;
	}
	
	/**
	 * returns the stream witch is edited at the moment
	 * @return
	 */
	public Stream getStream() {
		return stream;
	}
	
//	Listener
	
	public class ExitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			dispose();
			
		}
	}
	
	private class RepaintMainListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			repaintMainGui();
		}
	}
	
	private class RepaintCodesetAndSplitPointListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			repaintCodesetAndSplitpointPanel();
		}
	}
	
	public class RemotePlayListListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dirChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int i = dirChooser.showOpenDialog(getMe());
			if(i == JFileChooser.APPROVE_OPTION) {
				createRelayFileTF.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	public class MetaFileChooserListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dirChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int i = dirChooser.showOpenDialog(getMe());
			if(i == JFileChooser.APPROVE_OPTION) {
				metaDataRuleFileTF.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	public class ExternProgramMetaListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dirChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int i = dirChooser.showOpenDialog(getMe());
			if(i == JFileChooser.APPROVE_OPTION) {
				externalCmdMetaDataTF.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	private class FreeArgumentsListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if(freeArgumentCB.isSelected()) {
			JOptionPane.showMessageDialog(getMe(),"<html>"+
					toolTips.getString("freeArgumentATT"));
			}
			repaintCodesetAndSplitpointPanel();
		}
	}
	
	public class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			save();
			//if you have press save() on a new stream, it is not 
			//new anymore -> tell it 
			createNewStream = false;
			if(!dontExit) {
				mainGui.getControlStream().saveStreamVector();
			}
		}
	}
	
	public class SaveAndExitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			save();
			if(!dontExit) {
				mainGui.getControlStream().saveStreamVector();
				dispose();
			}
		}
	}
	
	public void windowClosing (WindowEvent e){
		dispose();
	}
	public void windowClosed (WindowEvent e) { }
	public void windowOpened (WindowEvent e) { }
	public void windowIconified (WindowEvent e) { }
	public void windowDeiconified (WindowEvent e) { }
	public void windowActivated (WindowEvent e) { }
	public void windowDeactivated (WindowEvent e) { }
}
