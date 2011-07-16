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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
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

import control.SRSOutput;

import misc.Stream;


public class Gui_Stream4AllOptions extends JDialog implements WindowListener {
	private static final long serialVersionUID = 1L;

	private ImageIcon saveAndExitIcon = new ImageIcon((URL)getClass().getResource("/Icons/ok_small.png"));
	private ImageIcon saveIcon = new ImageIcon((URL)getClass().getResource("/Icons/save_small.png"));
	private ImageIcon abortIcon = new ImageIcon((URL)getClass().getResource("/Icons/abort_small.png"));
	private ImageIcon questIcon = new ImageIcon((URL)getClass().getResource("/Icons/questionmark_small.png"));
	private ImageIcon findIcon = new ImageIcon((URL)getClass().getResource("/Icons/open_small.png"));
	private JButton openRemotePlayList = new JButton(findIcon);
	private JButton metaFileChooser = new JButton(findIcon);
	private JButton exernProgramMeta = new JButton(findIcon);
	
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
	private JTextField patternField = new JTextField("%X%S");
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
	private JButton questButton = new JButton("Help",questIcon);
	
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

	private JFileChooser dirChooser = new JFileChooser();
	
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	private Gui_StreamRipStar mainGui = null;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private ResourceBundle toolTips = ResourceBundle.getBundle("translations.ToolTips");
    	
	/**
	 * 
	 * @param mainGui	: the mainframe (StreamRipStars window)
	 */
	public Gui_Stream4AllOptions(Gui_StreamRipStar mainGui) {
		
		super(mainGui);
		this.mainGui = mainGui;
		
		//Set basic proportions and Objects
		init();
		//disable all Components for default
		disableAllComponents();

		//set title
		setTitle("Save Options For EVERY STREAM");
	
		//set Language for all textfiels, Labels etc
		setLanguage();
		
		patternCheckBox.setSelected(true);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
	}
	
	/**
	 * Disable all components, that can be saved with save()
	 */
	public void disableAllComponents(){
		streamNameTF.setEnabled(false);
		streamURLTF.setEnabled(false);
		streamWebsiteTF.setEnabled(false);
		streamCommentTF.setEnabled(false);
		streamGenreTF.setEnabled(false);
		allInOneSaveField.setEnabled(false);
		lengthRecordHourField.setEnabled(false);
		lengthRecordMinuteField.setEnabled(false);
		lengthRecordSecondField.setEnabled(false);
		maxSizeMBField.setEnabled(false);
		useragentField.setEnabled(false);
		runIndexField.setEnabled(false);
		patternField.setEnabled(false);
		realyPortField.setEnabled(false);
		realyConnectionsField.setEnabled(false);
		proxyField.setEnabled(false);
		skipTracksField.setEnabled(false);
		timeoutReconnectField.setEnabled(false);
		codesetRelayTF.setEnabled(false);
		codesetMetadataTF.setEnabled(false);
		codesetID3TF.setEnabled(false);
		codesetFilesysTF.setEnabled(false);
		xsOffsetTF.setEnabled(false);
		xsPadding1TF.setEnabled(false);
		xsPadding2TF.setEnabled(false);
		xsSearch1TF.setEnabled(false);
		xsSearch2TF.setEnabled(false);
		xsSilenceTF.setEnabled(false);
		freeArgumentTF .setEnabled(false);
		metaDataRuleFileTF.setEnabled(false);
		interfaceTF.setEnabled(false);
		externalCmdMetaDataTF.setEnabled(false);
		createRelayFileTF.setEnabled(false);

		allInOneCheckBox.setEnabled(false);
		lengthRecordCheckBox.setEnabled(false);
		maxSizeMBCheckBox.setEnabled(false);
		useragentCheckBox.setEnabled(false);
		runIndexCheckBox.setEnabled(false);
		patternCheckBox.setEnabled(false);
		relayServerCheckBox.setEnabled(false);
		relayConnect.setEnabled(false);
		createRelayFileCB.setEnabled(false);
		proxyCheckBox.setEnabled(false);
		skipTracksCheckBox.setEnabled(false);
		timeoutReconnectCheckBox.setEnabled(false);
		dCreateInvTracksCheckBox.setEnabled(false);
		overWriteAllCheckBox.setEnabled(false);
		overWriteNeverCheckBox.setEnabled(false);
		overWriteLargerCheckBox.setEnabled(false);
		overWriteVersionCheckBox.setEnabled(false);
		dontOverCheckBox.setEnabled(false);
		noDirForStreamCheckBox.setEnabled(false);
		dontScanPortCheckBox.setEnabled(false);
		truncateCheckBox.setEnabled(false);
		dontReconnectCheckBox.setEnabled(false);
		codesetRelayCB.setEnabled(false);
		codesetMetadataCB.setEnabled(false);
		codesetID3CB.setEnabled(false);
		codesetFilesysCB.setEnabled(false);
		xs2CB.setEnabled(false);
		xsOffsetCB.setEnabled(false);
		xsPaddingCB.setEnabled(false);
		xsSearchCB.setEnabled(false);
		xsSilenceCB.setEnabled(false);
		id3tagV1CB.setEnabled(false);
		id3tagV2CB.setEnabled(false);
		metaDataRuleFileCB.setEnabled(false);
		interfaceCB.setEnabled(false);
		externalCmdMetaDataCB.setEnabled(false);
		freeArgumentCB.setEnabled(false);

		realyConnectionsLabel.setEnabled(false);
		StreamNameLabel.setEnabled(false);
		StreamURLLabel.setEnabled(false);
		StreamWebsiteLabel.setEnabled(false);
		StreamCommentLabel.setEnabled(false);
		StreamGenreLabel.setEnabled(false);
		
		openRemotePlayList.setEnabled(false);
		metaFileChooser.setEnabled(false);
		exernProgramMeta.setEnabled(false);
	}
	
	
	//Set all Button etc
	private void init() {
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
		c.gridx = 1;
		relayServerPanel.add(realyPortField,c);
		c.gridy = 1;
		c.gridx = 0;
		relayServerPanel.add(realyConnectionsLabel,c);
		c.gridx = 1;
		relayServerPanel.add(realyConnectionsField,c);
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
		c.gridwidth = 1;
		c.gridx = 1;
		c.weightx = 1.0;
		otherOptionsPanel.add(metaDataRuleFileTF,c);
		c.weightx = 0.0;
		c.gridx = 2;
		otherOptionsPanel.add(metaFileChooser,c);
		c.gridwidth = 1;
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
		c.weightx = 0.5;
		buttonPanel.add(new JLabel("  "),c);
		c.gridx = 5;
		c.weightx = 0.5;
		buttonPanel.add(questButton,c);
		c.gridx = 6;
		c.weightx = 0.5;
		buttonPanel.add(new JLabel("  "),c); 	//Label make abort Button stay on right side
		c.gridx = 7;
		c.weightx = 0.0;
		buttonPanel.add(abortButton,c); 	//Label make abort Button stay on right side
		
		openRemotePlayList.addActionListener(new RemotePlayListListener());
		metaFileChooser.addActionListener(new MetaFileChooserListener());
		exernProgramMeta.addActionListener(new ExternProgramMetaListener());
		
		MouseListener aMouseListener = new EnableMouseListener();
		
		StreamWebsiteLabel.addMouseListener(aMouseListener);
		StreamCommentLabel.addMouseListener(aMouseListener);
		StreamGenreLabel.addMouseListener(aMouseListener);
		streamWebsiteTF.addMouseListener(aMouseListener);
		streamCommentTF.addMouseListener(aMouseListener);
		streamGenreTF.addMouseListener(aMouseListener);
		allInOneSaveField.addMouseListener(aMouseListener);
		lengthRecordHourField.addMouseListener(aMouseListener);
		lengthRecordMinuteField.addMouseListener(aMouseListener);
		lengthRecordSecondField.addMouseListener(aMouseListener);
		maxSizeMBField.addMouseListener(aMouseListener);
		useragentField.addMouseListener(aMouseListener);
		runIndexField.addMouseListener(aMouseListener);
		patternField.addMouseListener(aMouseListener);
		realyPortField.addMouseListener(aMouseListener);
		realyConnectionsField.addMouseListener(aMouseListener);
		proxyField.addMouseListener(aMouseListener);
		skipTracksField.addMouseListener(aMouseListener);
		timeoutReconnectField.addMouseListener(aMouseListener);
		codesetRelayTF.addMouseListener(aMouseListener);
		codesetMetadataTF.addMouseListener(aMouseListener);
		codesetID3TF.addMouseListener(aMouseListener);
		codesetFilesysTF.addMouseListener(aMouseListener);
		xsOffsetTF.addMouseListener(aMouseListener);
		xsPadding1TF.addMouseListener(aMouseListener);
		xsPadding2TF.addMouseListener(aMouseListener);
		xsSearch1TF.addMouseListener(aMouseListener);
		xsSearch2TF.addMouseListener(aMouseListener);
		xsSilenceTF.addMouseListener(aMouseListener);
		freeArgumentTF.addMouseListener(aMouseListener);
		metaDataRuleFileTF.addMouseListener(aMouseListener);
		interfaceTF.addMouseListener(aMouseListener);
		externalCmdMetaDataTF.addMouseListener(aMouseListener);
		createRelayFileTF.addMouseListener(aMouseListener);
		
		allInOneCheckBox.addMouseListener(aMouseListener);
		lengthRecordCheckBox.addMouseListener(aMouseListener);
		maxSizeMBCheckBox.addMouseListener(aMouseListener);
		useragentCheckBox.addMouseListener(aMouseListener);
		runIndexCheckBox.addMouseListener(aMouseListener);
		relayServerCheckBox.addMouseListener(aMouseListener);
		createRelayFileCB.addMouseListener(aMouseListener);
		proxyCheckBox.addMouseListener(aMouseListener);
		skipTracksCheckBox.addMouseListener(aMouseListener);
		timeoutReconnectCheckBox.addMouseListener(aMouseListener);
		patternCheckBox.addMouseListener(aMouseListener);
		overWriteAllCheckBox.addMouseListener(aMouseListener);
		overWriteNeverCheckBox.addMouseListener(aMouseListener);
		overWriteLargerCheckBox.addMouseListener(aMouseListener);
		overWriteVersionCheckBox.addMouseListener(aMouseListener);
		dontOverCheckBox.addMouseListener(aMouseListener);
		truncateCheckBox.addMouseListener(aMouseListener);
		noDirForStreamCheckBox.addMouseListener(aMouseListener);
		dCreateInvTracksCheckBox.addMouseListener(aMouseListener);
		relayConnect.addMouseListener(aMouseListener);
		dontReconnectCheckBox.addMouseListener(aMouseListener);
		dontScanPortCheckBox.addMouseListener(aMouseListener);
		
		//add ActionListener to all Buttons
		saveButton.addActionListener(new SaveListener());
		saveAndExitButton.addActionListener(new SaveAndExitListener());
		abortButton.addActionListener(new ExitListener());
		questButton.addActionListener(new HelpListener());
		
		codesetRelayCB.addMouseListener(aMouseListener);
		codesetMetadataCB.addMouseListener(aMouseListener);
		codesetID3CB.addMouseListener(aMouseListener);
		codesetFilesysCB.addMouseListener(aMouseListener);
		xs2CB.addMouseListener(aMouseListener);
		xsOffsetCB.addMouseListener(aMouseListener);
		xsPaddingCB.addMouseListener(aMouseListener);
		xsSearchCB.addMouseListener(aMouseListener);
		xsSilenceCB.addMouseListener(aMouseListener);
		freeArgumentCB.addMouseListener(aMouseListener);
		metaDataRuleFileCB.addMouseListener(aMouseListener);
		interfaceCB.addMouseListener(aMouseListener);
		externalCmdMetaDataCB.addMouseListener(aMouseListener);
		id3tagV1CB.addMouseListener(aMouseListener);
		id3tagV2CB.addMouseListener(aMouseListener);
		
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

		setSize(new Dimension(650,700));
		//get new dimension of the window
        Dimension frameDim = getSize();
    	
        //get resolution
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        
        //calculates the app. values
        int x = (screenDim.width - frameDim.width)/2;
        int y = (screenDim.height - frameDim.height)/2;
        
        //set location
        setLocation(x, y);
        
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
        //escape for exit
        KeyStroke escStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        //register all Strokes
        getRootPane().registerKeyboardAction(new ExitListener(), escStroke,
                JComponent.WHEN_IN_FOCUSED_WINDOW);		
	}

	private void setLanguage() {
		try {
			//title
    		setTitle(trans.getString("Stream4All.title")+" ");

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
			questButton.setText(trans.getString("helpButton"));
    	} catch ( MissingResourceException e ) { 
		      SRSOutput.getInstance().logE( e.getMessage() ); 
	    }
	}
	
	
	/**
	 * save every options, that is enabled in every stream
	 */
	public void save() {
		//get Vector with all streams
		Vector<Stream> allStreams = mainGui.getControlStream().getStreamVector();
		
		for(int i=0; i < allStreams.capacity(); i++) {
			if(streamWebsiteTF.isEnabled()) {
				allStreams.get(i).comment = streamCommentTF.getText();	
			}
			
			if(streamWebsiteTF.isEnabled()) {
				allStreams.get(i).website = streamWebsiteTF.getText();
			}
			
			if(streamGenreTF.isEnabled()) {
				allStreams.get(i).genre = streamGenreTF.getText();
			}
			
			if(allInOneCheckBox.isEnabled()) {
				allStreams.get(i).singleFileCB = allInOneCheckBox.isSelected();
				allStreams.get(i).singleFileTF = allInOneSaveField.getText();
			}
			
			if(allInOneSaveField.isEnabled()) {
				allStreams.get(i).singleFileTF = allInOneSaveField.getText();
			}
			
			if(lengthRecordCheckBox.isEnabled()) {
				allStreams.get(i).maxTimeCB = lengthRecordCheckBox.isSelected();
			}
			
			if(lengthRecordHourField.isEnabled()) {
				allStreams.get(i).maxTimeHHTF = lengthRecordHourField.getText();
			}
			
			if(lengthRecordMinuteField.isEnabled()) {
				allStreams.get(i).maxTimeMMTF = lengthRecordMinuteField.getText();
			}
			
			if(lengthRecordSecondField.isEnabled()) {
				allStreams.get(i).maxTimessTF = lengthRecordSecondField.getText();
			}
			
			if(maxSizeMBCheckBox.isEnabled()) {
				allStreams.get(i).maxMBCB = maxSizeMBCheckBox.isSelected();
			}
			
			if(maxSizeMBField.isEnabled()) {
				allStreams.get(i).maxMBTF = maxSizeMBField.getText();
			}
			
			if(runIndexCheckBox.isEnabled()) {
				allStreams.get(i).sequenzCB = runIndexCheckBox.isSelected();
			}
			
			if(runIndexField.isEnabled()) {
				allStreams.get(i).sequenzTF = runIndexField.getText();
			}
			
			if(patternCheckBox.isEnabled()) {
				allStreams.get(i).patternCB = patternCheckBox.isSelected();
			}
			
			if(patternField.isEnabled()) {
				allStreams.get(i).patternTF = patternField.getText();
			}
			
			if(overWriteVersionCheckBox.isEnabled()) {
				if(overWriteVersionCheckBox.isSelected())
					allStreams.get(i).completeCB = 0;
				else if (overWriteAllCheckBox.isSelected())
					allStreams.get(i).completeCB = 1;
				else if (overWriteNeverCheckBox.isSelected())
					allStreams.get(i).completeCB = 2;
				else 
					allStreams.get(i).completeCB = 3;
			}
			
			if(truncateCheckBox.isEnabled()) {
				allStreams.get(i).cutSongIncompleteCB = truncateCheckBox.isSelected();
			}
			
			if(dontOverCheckBox.isEnabled()) {
				allStreams.get(i).neverOverIncompCB = dontOverCheckBox.isSelected();
			}
			
			if(noDirForStreamCheckBox.isEnabled()) {
				allStreams.get(i).noDirEveryStreamCB = noDirForStreamCheckBox.isSelected();
			}
			
			if(dCreateInvTracksCheckBox.isEnabled()) {
				allStreams.get(i).noIndiviSongsCB = dCreateInvTracksCheckBox.isSelected();
			}
			
			if(relayServerCheckBox.isEnabled()) {
				allStreams.get(i).createReayCB = relayServerCheckBox.isSelected();
			}
			
			if(relayConnect.isEnabled()) {
				allStreams.get(i).connectToRelayCB = relayConnect.isSelected();
			}
			
			if(createRelayFileCB.isEnabled()) {
				allStreams.get(i).createPlaylistRelayCB = createRelayFileCB.isSelected();
			}
			
			if(dontScanPortCheckBox.isEnabled()) {
				allStreams.get(i).dontSearchAltPortCB = dontScanPortCheckBox.isSelected();
			}
			
			if(dontReconnectCheckBox.isEnabled()) {
				allStreams.get(i).dontAutoReconnectCB = dontReconnectCheckBox.isSelected();
			}
			
			if(timeoutReconnectCheckBox.isEnabled()) {
				allStreams.get(i).timeoutReconnectCB = timeoutReconnectCheckBox.isSelected();
			}
			
			if(proxyCheckBox.isEnabled()) {
				allStreams.get(i).proxyCB = proxyCheckBox.isSelected();
			}
			
			if(useragentCheckBox.isEnabled()) {
				allStreams.get(i).useragentCB = useragentCheckBox.isSelected();
			}
	
			if(skipTracksCheckBox.isEnabled()) {
				allStreams.get(i).countBeforStartCB = skipTracksCheckBox.isSelected();
			}
			
			if(metaDataRuleFileCB.isEnabled()) {
				allStreams.get(i).metaDataCB = metaDataRuleFileCB.isSelected();
			}
			
			if(interfaceCB.isEnabled()) {
				allStreams.get(i).interfaceCB = interfaceCB.isSelected();
			}
			
			if(externalCmdMetaDataCB.isEnabled()) {
				allStreams.get(i).externMetaDataCB = externalCmdMetaDataCB.isSelected();
			}
			
			if(freeArgumentCB.isEnabled()) {
				allStreams.get(i).extraArgsCB = freeArgumentCB.isSelected();
			}
			
			if(codesetRelayCB.isEnabled()) {
				allStreams.get(i).CSRelayCB = codesetRelayCB.isSelected();
			}
			
			if(codesetMetadataCB.isEnabled()) {
				allStreams.get(i).CSMetaCB = codesetMetadataCB.isSelected();
			}
			
			if(codesetID3CB.isEnabled()) {
				allStreams.get(i).CSIDTagCB = codesetID3CB.isSelected();
			}
			
			if(codesetFilesysCB.isEnabled()) {
				allStreams.get(i).CSFileSysCB = codesetFilesysCB.isSelected();
			}
			
			if(xs2CB.isEnabled()) {
				allStreams.get(i).XS2CB= xs2CB.isSelected();
			}
			
			if(xsOffsetCB.isEnabled()) {
				allStreams.get(i).SPDelayCB = xsOffsetCB.isSelected();
			}
			
			if(xsPaddingCB.isEnabled()) {
				allStreams.get(i).SPExtraCB = xsPaddingCB.isSelected();
			}
			
			if(xsSearchCB.isEnabled()) {
				allStreams.get(i).SPWindowCB= xsSearchCB.isSelected();
			}
			
			if(xsSilenceCB.isEnabled()) {
				allStreams.get(i).SPSilenceCB = xsSilenceCB.isSelected();
			}
			
			if(id3tagV1CB.isEnabled()) {
				allStreams.get(i).IDV1CB = id3tagV1CB.isSelected();
			}
			
			if(id3tagV2CB.isEnabled()) {
				allStreams.get(i).IDV2CB = id3tagV2CB.isSelected();
			}
			
			if(realyPortField.isEnabled()) {
				allStreams.get(i).relayServerPortTF = realyPortField.getText();
			}
			
			if(realyConnectionsField.isEnabled()) {
				allStreams.get(i).maxConnectRelayTF = realyConnectionsField.getText();
			}
			
			if(realyConnectionsField.isEnabled()) {
				allStreams.get(i).relayPlayListTF = createRelayFileTF.getText();
			}
			
			if(timeoutReconnectField.isEnabled()) {
				allStreams.get(i).timeOutReonTF = timeoutReconnectField.getText();
			}
			
			if(proxyField.isEnabled()) {
				allStreams.get(i).proxyTF = proxyField.getText();
			}
			
			if(useragentField.isEnabled()) {
				allStreams.get(i).useragentTF = useragentField.getText();
			}
			
			if(skipTracksField.isEnabled()) {
				allStreams.get(i).sciptSongsTF = skipTracksField.getText();
			}
			
			if(metaDataRuleFileTF.isEnabled()) {
				allStreams.get(i).metaDataFileTF = metaDataRuleFileTF.getText();
			}
			
			if(interfaceTF.isEnabled()) {
				allStreams.get(i).interfaceTF = interfaceTF.getText();
			}
			
			if(externalCmdMetaDataTF.isEnabled()) {
				allStreams.get(i).externTF = externalCmdMetaDataTF.getText();
			}
			
			if(freeArgumentTF.isEnabled()) {
				allStreams.get(i).extraArgsTF = freeArgumentTF.getText();
			}
			
			if(codesetRelayTF.isEnabled()) {
				allStreams.get(i).CSRelayTF = codesetRelayTF.getText();
			}
			
			if(codesetMetadataTF.isEnabled()) {
				allStreams.get(i).CSMetaDataTF = codesetMetadataTF.getText();
			}
			
			if(codesetID3TF.isEnabled()) {
				allStreams.get(i).CSIDTF = codesetID3TF.getText();
			}
			
			if(codesetFilesysTF.isEnabled()) {
				allStreams.get(i).CSFileSysTF = codesetFilesysTF.getText();
			}
			
			if(xsOffsetTF.isEnabled()) {
				allStreams.get(i).SPDelayTF = xsOffsetTF.getText();
			}
			
			if(xsPadding1TF.isEnabled()) {
				allStreams.get(i).SPExtraTF1 = xsPadding1TF.getText();
			}
			
			if(xsPadding2TF.isEnabled()) {
				allStreams.get(i).SPExtraTF2 = xsPadding2TF.getText();
			}
			
			if(xsSearch1TF.isEnabled()) {
				allStreams.get(i).SPWindowTF1 = xsSearch1TF.getText();
			}
			
			if(xsSearch2TF.isEnabled()) {
				allStreams.get(i).SPWindowTF2 = xsSearch2TF.getText();
			}
			
			if(xsSilenceTF.isEnabled()) {
				allStreams.get(i).SPSilenceTF = xsSilenceTF.getText();
			}
		}
	}
	
	public Gui_Stream4AllOptions getMe() {
		return this;
	}
	
//	Listener
	
	public class ExitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			dispose();
			
		}
	}
	
	public class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			save();
			mainGui.getControlStream().saveStreamVector();
		}
	}

	/**
	 * Shows an help dialog about what to do in this window
	 */
	public class HelpListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(getMe(),trans.getString("Stream4All.helpText"));
		}
	}
	
	public class SaveAndExitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			save();
			mainGui.getControlStream().saveStreamVector();
			dispose();
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
	/**
	 * adds a Listener, witch looks for the selected component and invert its status, witch
	 * means: if the components is enabled, disable it and vice versa 
	 * @author Eule
	 *
	 */
	public class EnableMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				if(e.getSource().equals(streamWebsiteTF) || e.getSource().equals(StreamWebsiteLabel)) {
					if(streamWebsiteTF.isEnabled()) {
						streamWebsiteTF.setEnabled(false);
						StreamWebsiteLabel.setEnabled(false);
					} else {
						streamWebsiteTF.setEnabled(true);
						StreamWebsiteLabel.setEnabled(true);
					}
				}

				else if(e.getSource().equals(streamCommentTF) || e.getSource().equals(StreamCommentLabel)) {
					if(streamCommentTF.isEnabled()) {
						streamCommentTF.setEnabled(false);
						StreamCommentLabel.setEnabled(false);
					} else {
						streamCommentTF.setEnabled(true);
						StreamCommentLabel	.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(streamGenreTF) || e.getSource().equals(StreamGenreLabel)) {
					if(streamGenreTF.isEnabled()) {
						streamGenreTF.setEnabled(false);
						StreamGenreLabel.setEnabled(false);
					} else {
						streamGenreTF.setEnabled(true);
						StreamGenreLabel.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(allInOneCheckBox)) {
					if(allInOneCheckBox.isEnabled()) {
						allInOneCheckBox.setEnabled(false);
					} else {
						allInOneCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(allInOneSaveField)) {
					if(allInOneSaveField.isEnabled()) {
						allInOneSaveField.setEnabled(false);
					} else {
						allInOneSaveField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(lengthRecordCheckBox)) {
					if(lengthRecordCheckBox.isEnabled()) {
						lengthRecordCheckBox.setEnabled(false);
					} else {
						lengthRecordCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(lengthRecordHourField)) {
					if(lengthRecordHourField.isEnabled()) {
						lengthRecordHourField.setEnabled(false);
					} else {
						lengthRecordHourField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(lengthRecordMinuteField)) {
					if(lengthRecordMinuteField.isEnabled()) {
						lengthRecordMinuteField.setEnabled(false);
					} else {
						lengthRecordMinuteField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(lengthRecordSecondField)) {
					if(lengthRecordSecondField.isEnabled()) {
						lengthRecordSecondField.setEnabled(false);
					} else {
						lengthRecordSecondField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(maxSizeMBCheckBox)) {
					if(maxSizeMBCheckBox.isEnabled()) {
						maxSizeMBCheckBox.setEnabled(false);
					} else {
						maxSizeMBCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(maxSizeMBField)) {
					if(maxSizeMBField.isEnabled()) {
						maxSizeMBField.setEnabled(false);
					} else {
						maxSizeMBField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(runIndexCheckBox)) {
					if(runIndexCheckBox.isEnabled()) {
						runIndexCheckBox.setEnabled(false);
					} else {
						runIndexCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(runIndexField)) {
					if(runIndexField.isEnabled()) {
						runIndexField.setEnabled(false);
					} else {
						runIndexField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(patternCheckBox)) {
					if(patternCheckBox.isEnabled()) {
						patternCheckBox.setEnabled(false);
					} else {
						patternCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(patternField)) {
					if(patternField.isEnabled()) {
						patternField.setEnabled(false);
					} else {
						patternField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(overWriteAllCheckBox) || e.getSource().equals(overWriteNeverCheckBox) || 
						e.getSource().equals(overWriteLargerCheckBox) || e.getSource().equals(overWriteVersionCheckBox)) {
					if(overWriteAllCheckBox.isEnabled()) {
						overWriteAllCheckBox.setEnabled(false);
						overWriteNeverCheckBox.setEnabled(false);
						overWriteLargerCheckBox.setEnabled(false);
						overWriteVersionCheckBox.setEnabled(false);
					} else {
						overWriteAllCheckBox.setEnabled(true);
						overWriteNeverCheckBox.setEnabled(true);
						overWriteLargerCheckBox.setEnabled(true);
						overWriteVersionCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(dontOverCheckBox)) {
					if(dontOverCheckBox.isEnabled()) {
						dontOverCheckBox.setEnabled(false);
					} else {
						dontOverCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(truncateCheckBox)) {
					if(truncateCheckBox.isEnabled()) {
						truncateCheckBox.setEnabled(false);
					} else {
						truncateCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(noDirForStreamCheckBox)) {
					if(noDirForStreamCheckBox.isEnabled()) {
						noDirForStreamCheckBox.setEnabled(false);
					} else {
						noDirForStreamCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(dCreateInvTracksCheckBox)) {
					if(dCreateInvTracksCheckBox.isEnabled()) {
						dCreateInvTracksCheckBox.setEnabled(false);
					} else {
						dCreateInvTracksCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(relayServerCheckBox)) {
					if(relayServerCheckBox.isEnabled()) {
						relayServerCheckBox.setEnabled(false);						
					} else {
						relayServerCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(realyPortField)) {
					if(realyPortField.isEnabled()) {
						realyPortField.setEnabled(false);
					
					} else {
						realyPortField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(realyConnectionsLabel)) {
					if(realyConnectionsLabel.isEnabled()) {
						realyConnectionsLabel.setEnabled(false);
					} else {
						realyConnectionsLabel.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(realyConnectionsField)) {
					if(realyConnectionsField.isEnabled()) {
						realyConnectionsField.setEnabled(false);
					} else {
						realyConnectionsField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(createRelayFileCB)) {
					if(createRelayFileCB.isEnabled()) {
						createRelayFileCB.setEnabled(false);
					} else {
						createRelayFileCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(createRelayFileTF)) {
					if(createRelayFileTF.isEnabled()) {
						createRelayFileTF.setEnabled(false);
						openRemotePlayList.setEnabled(false);
					} else {
						createRelayFileTF.setEnabled(true);
						openRemotePlayList.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(relayConnect)) {
					if(relayConnect.isEnabled()) {
						relayConnect.setEnabled(false);
					} else {
						relayConnect.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(dontScanPortCheckBox)) {
					if(dontScanPortCheckBox.isEnabled()) {
						dontScanPortCheckBox.setEnabled(false);
					} else {
						dontScanPortCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(dontReconnectCheckBox)) {
					if(dontReconnectCheckBox.isEnabled()) {
						dontReconnectCheckBox.setEnabled(false);
					} else {
						dontReconnectCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(timeoutReconnectCheckBox)) {
					if(timeoutReconnectCheckBox.isEnabled()) {
						timeoutReconnectCheckBox.setEnabled(false);
					} else {
						timeoutReconnectCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(timeoutReconnectField)) {
					if(timeoutReconnectField.isEnabled()) {
						timeoutReconnectField.setEnabled(false);
					} else {
						timeoutReconnectField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(proxyCheckBox)) {
					if(proxyCheckBox.isEnabled()) {
						proxyCheckBox.setEnabled(false);
					} else {
						proxyCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(proxyField)) {
					if(proxyField.isEnabled()) {
						proxyField.setEnabled(false);
					} else {
						proxyField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(useragentCheckBox)) {
					if(useragentCheckBox.isEnabled()) {
						useragentCheckBox.setEnabled(false);
					} else {
						useragentCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(useragentField)) {
					if(useragentField.isEnabled()) {
						useragentField.setEnabled(false);
					} else {
						useragentField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(skipTracksCheckBox)) {
					if(skipTracksCheckBox.isEnabled()) {
						skipTracksCheckBox.setEnabled(false);
					} else {
						skipTracksCheckBox.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(skipTracksField)) {
					if(skipTracksField.isEnabled()) {
						skipTracksField.setEnabled(false);
					} else {
						skipTracksField.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(metaDataRuleFileCB)) {
					if(metaDataRuleFileCB.isEnabled()) {
						metaDataRuleFileCB.setEnabled(false);
					} else {
						metaDataRuleFileCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(metaDataRuleFileTF)) {
					if(metaDataRuleFileTF.isEnabled()) {
						metaDataRuleFileTF.setEnabled(false);
						metaFileChooser.setEnabled(false);
					} else {
						metaDataRuleFileTF.setEnabled(true);
						metaFileChooser.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(interfaceCB)) {
					if(interfaceCB.isEnabled()) {
						interfaceCB.setEnabled(false);
					} else {
						interfaceCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(interfaceTF)) {
					if(interfaceTF.isEnabled()) {
						interfaceTF.setEnabled(false);
					} else {
						interfaceTF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(externalCmdMetaDataCB)) {
					if(externalCmdMetaDataCB.isEnabled()) {
						externalCmdMetaDataCB.setEnabled(false);
					} else {
						externalCmdMetaDataCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(externalCmdMetaDataTF)) {
					if(externalCmdMetaDataTF.isEnabled()) {
						externalCmdMetaDataTF.setEnabled(false);
						exernProgramMeta.setEnabled(false);
					} else {
						externalCmdMetaDataTF.setEnabled(true);
						exernProgramMeta.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(freeArgumentCB)) {
					if(freeArgumentCB.isEnabled()) {
						freeArgumentCB.setEnabled(false);
					} else {
						JOptionPane.showMessageDialog(getMe(),"<html>"+
								toolTips.getString("freeArgumentATT"));
						freeArgumentCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(freeArgumentTF)) {
					if(freeArgumentTF.isEnabled()) {
						freeArgumentTF.setEnabled(false);
					} else {
						JOptionPane.showMessageDialog(getMe(),"<html>"+
								toolTips.getString("freeArgumentATT"));
						freeArgumentTF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xs2CB)) {
					if(xs2CB.isEnabled()) {
						xs2CB.setEnabled(false);
					} else {
						xs2CB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetRelayCB)) {
					if(codesetRelayCB.isEnabled()) {
						codesetRelayCB.setEnabled(false);
					} else {
						codesetRelayCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetRelayTF)) {
					if(codesetRelayTF.isEnabled()) {
						codesetRelayTF.setEnabled(false);
					} else {
						codesetRelayTF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetMetadataCB)) {
					if(codesetMetadataCB.isEnabled()) {
						codesetMetadataCB.setEnabled(false);
					} else {
						codesetMetadataCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetMetadataTF)) {
					if(codesetMetadataTF.isEnabled()) {
						codesetMetadataTF.setEnabled(false);
					} else {
						codesetMetadataTF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetID3CB)) {
					if(codesetID3CB.isEnabled()) {
						codesetID3CB.setEnabled(false);
					} else {
						codesetID3CB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetID3TF)) {
					if(codesetID3TF.isEnabled()) {
						codesetID3TF.setEnabled(false);
					} else {
						codesetID3TF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetFilesysTF)) {
					if(codesetFilesysTF.isEnabled()) {
						codesetFilesysTF.setEnabled(false);
					} else {
						codesetFilesysTF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(codesetFilesysCB)) {
					if(codesetFilesysCB.isEnabled()) {
						codesetFilesysCB.setEnabled(false);
					} else {
						codesetFilesysCB.setEnabled(true);
					}
				}

				
				else if(e.getSource().equals(xsOffsetCB)) {
					if(xsOffsetCB.isEnabled()) {
						xsOffsetCB.setEnabled(false);
					} else {
						xsOffsetCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsOffsetTF)) {
					if(xsOffsetTF.isEnabled()) {
						xsOffsetTF.setEnabled(false);
					} else {
						xsOffsetTF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsPaddingCB)) {
					if(xsPaddingCB.isEnabled()) {
						xsPaddingCB.setEnabled(false);
					} else {
						xsPaddingCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsPadding1TF)) {
					if(xsPadding1TF.isEnabled()) {
						xsPadding1TF.setEnabled(false);
					} else {
						xsPadding1TF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsPadding2TF)) {
					if(xsPadding2TF.isEnabled()) {
						xsPadding2TF.setEnabled(false);
					} else {
						xsPadding2TF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsSearchCB)) {
					if(xsSearchCB.isEnabled()) {
						xsSearchCB.setEnabled(false);
					} else {
						xsSearchCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsSearch1TF)) {
					if(xsSearch1TF.isEnabled()) {
						xsSearch1TF.setEnabled(false);
					} else {
						xsSearch1TF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsSearch2TF)) {
					if(xsSearch2TF.isEnabled()) {
						xsSearch2TF.setEnabled(false);
					} else {
						xsSearch2TF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsSilenceTF)) {
					if(xsSilenceTF.isEnabled()) {
						xsSilenceTF.setEnabled(false);
					} else {
						xsSilenceTF.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(xsSilenceCB)) {
					if(xsSilenceCB.isEnabled()) {
						xsSilenceCB.setEnabled(false);
					} else {
						xsSilenceCB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(id3tagV1CB)) {
					if(id3tagV1CB.isEnabled()) {
						id3tagV1CB.setEnabled(false);
					} else {
						id3tagV1CB.setEnabled(true);
					}
				}
				
				else if(e.getSource().equals(id3tagV2CB)) {
					if(id3tagV2CB.isEnabled()) {
						id3tagV2CB.setEnabled(false);
					} else {
						id3tagV2CB.setEnabled(true);
					}
				}
				
			}
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
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
