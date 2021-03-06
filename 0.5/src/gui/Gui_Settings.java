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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import control.Control_GetPath;
import control.Control_TestStartFirst;

public class Gui_Settings extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JPanel pathPanel = new JPanel();
	private JPanel languagePanel = new JPanel();
	private JPanel actionPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel commonPanel = new JPanel();

	private JTextField ripperPathField = new JTextField("",30) ;
	private JTextField shoutcastPlayer = new JTextField("",30) ;
	private JTextField generellPathField = new JTextField("",30) ;
	private JTextField fileBrowserField = new JTextField("",30) ;
	private JTextField webBrowserField = new JTextField("",30) ;
	
	private ImageIcon findIcon = new ImageIcon((URL)getClass().getResource("/Icons/open_small.png"));
	private ImageIcon langIcon = new ImageIcon((URL)getClass().getResource("/Icons/lang_small.png"));
	private ImageIcon saveAndExitIcon = new ImageIcon((URL)getClass().getResource("/Icons/ok_small.png"));
	private ImageIcon saveIcon = new ImageIcon((URL)getClass().getResource("/Icons/save_small.png"));
	private ImageIcon abortIcon = new ImageIcon((URL)getClass().getResource("/Icons/abort_small.png"));
	
	private JLabel ripLabel = new JLabel("Path to streamripper: ");
	private JLabel mediaPlayer = new JLabel("Path to mp3 player: ");
	private JLabel generellPathLabel = new JLabel("Generell Save : ");
	private JLabel fileBrowserLabel = new JLabel("Path to filemanager");
	private JLabel webBrowserLabel = new JLabel("Path to webbrowser");
	private JLabel reqRestart =  new JLabel("Chances require programmrestart");
	private JLabel explainActionLabel = new JLabel("What to to, when doubleclicking on a Field: ");
	private JLabel statusLabel= new JLabel("Status :");
	private JLabel nameLabel= new JLabel("Name :");
	private JLabel currentTrackLabel = new JLabel("Current Track: ");
	private JLabel windowClosing = new JLabel("Action when clicking on closing window");
	private JLabel lnfLabel = new JLabel("All installed Look and Feels");
	
	private JTextArea translationTA = new JTextArea();
	private JScrollPane translationSP = new JScrollPane(translationTA);
	
	private JButton abortButton = new JButton("Abort", abortIcon);
	private JButton saveAndExitButton = new JButton ("OK", saveAndExitIcon);
	private JButton saveButton = new JButton("Save", saveIcon);
	private JButton browseRipper = new JButton(findIcon);
	private JButton browseMP3Player = new JButton(findIcon);
	private JButton browseGenerellPath = new JButton(findIcon);
	private JButton browseFileBrowserPath = new JButton(findIcon);
	private JButton browseWebBrowserPath = new JButton(findIcon);

	private JFileChooser dirChooser;

	private String[] languages = {"English", "German"};
	private String[] actions = {"none","Open Browser","edit Stream","start/stop", "play Stream"};
	private String[] windowActions = {"do nothing", "Exit StreamRipStar", "Send in Systemtray"};
	private String[] lookAndFeelList;
	
	private String lookAndFeelBox_className = null;
	
	private JComboBox langMenu = new JComboBox(languages);
	private JComboBox statusBox = new JComboBox(actions);
	private JComboBox nameBox = new JComboBox(actions);
	private JComboBox currentTrackBox = new JComboBox(actions);
	private JComboBox windowActionBox = new JComboBox(windowActions);
	private JComboBox LookAndFeelBox = new JComboBox();
	
	private JCheckBox activeTrayIcon = new JCheckBox("Show Systemtray (requires restart)");
	private JCheckBox showTextCheckBox = new JCheckBox("Show Text under Icons");
	private JCheckBox useInternalAudioPlayerCB = new JCheckBox("Use internal check box (Requires gstreamer installed");
	private JCheckBox useAnotherLnfBox = new JCheckBox("Use another Look and Feel");

	private UIManager.LookAndFeelInfo[] lookAndFeelInfos;

	private JTabbedPane settingsPane = new JTabbedPane();

	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	private Gui_StreamRipStar mainGui = null;
	
	public Gui_Settings(Gui_StreamRipStar mainGui) {
		super(mainGui, "Preferences");
		this.mainGui = mainGui;
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		
		//create rest of components at runtime
		lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
		lookAndFeelList = new String[lookAndFeelInfos.length];
		
		for(int i=0; i < lookAndFeelInfos.length; i++) {
			lookAndFeelList[i] = lookAndFeelInfos[i].getName();
		}
		
		LookAndFeelBox = new JComboBox(lookAndFeelList);
		
		setLayout(new BorderLayout());
		
		translationTA.setEditable(false);
		
		settingsPane.addTab("General", commonPanel);
		settingsPane.addTab("Path",pathPanel);
		settingsPane.addTab("Action",actionPanel);
		settingsPane.addTab("Language",langIcon,languagePanel);
		
		add(settingsPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pathPanel.setLayout(new GridBagLayout());
		languagePanel.setLayout(new GridBagLayout());
		actionPanel.setLayout(new GridBagLayout());
		buttonPanel.setLayout(new GridBagLayout());
		commonPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets( 2, 5, 2, 5);
		c.anchor = GridBagConstraints.NORTHWEST;
		
//1. line: Path to streamripper
		c.gridy = 0;
		c.gridx = 0;
		pathPanel.add(ripLabel,c);
		c.gridx = 1;
		c.weightx = 1.0;
		pathPanel.add(ripperPathField,c);
		c.gridx = 2;
		c.weightx = 0.0;
		pathPanel.add(browseRipper,c);
//2. line: Path to shoutcast player
		c.gridy = 1;
		c.gridx = 0;
		pathPanel.add(mediaPlayer,c);
		c.gridx = 1;
		c.weightx = 1.0;
		pathPanel.add(shoutcastPlayer,c);
		c.gridx = 2;
		c.weightx = 0.0;
		pathPanel.add(browseMP3Player,c);
//3. line: path to generall savepath for the stream
		c.gridy = 2;
		c.gridx = 0;
		pathPanel.add(generellPathLabel,c);
		c.gridx = 1;
		c.weightx = 1.0;
		pathPanel.add(generellPathField,c);
		c.gridx = 2;
		c.weightx = 0.0;
		pathPanel.add(browseGenerellPath,c);
//4. line: path ot webbrowser
		c.gridy = 3;
		c.gridx = 0;
		pathPanel.add(webBrowserLabel,c);
		c.gridx = 1;
		c.weightx = 1.0;
		pathPanel.add(webBrowserField,c);
		c.gridx = 2;
		c.weightx = 0.0;
		pathPanel.add(browseWebBrowserPath,c);
//5. line: path ot fielbrowser
		c.gridy = 4;
		c.gridx = 0;
		pathPanel.add(fileBrowserLabel,c);
		c.gridx = 1;
		c.weightx = 1.0;		
		pathPanel.add(fileBrowserField,c);
		c.gridx = 2;
		c.weightx = 0.0;
		pathPanel.add(browseFileBrowserPath,c);
		
//Language Panel
	//1. line on lang: langchange
		c.weightx = 0.0;
		c.gridy = 0;
		c.gridx = 0;
		languagePanel.add(langMenu, c);
		c.weightx = 1.0;
		c.gridx = 1;
		languagePanel.add(new JLabel(""), c);
	//2. line on lang: langchange
		c.weightx = 0.0;
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 2;
		languagePanel.add(reqRestart,c);
	//3. line on lang: langchange
		c.weightx = 0.0;
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 2;
		languagePanel.add(new JLabel(" "),c);
	//4. line on lang: langchange
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		languagePanel.add(translationSP,c);
		
//ACTIONPANEL
	//1. Line: explain what you are doing
		c.insets = new Insets( 5, 5, 10, 5);
		c.weightx = 0.0;
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 7;
		actionPanel.add(explainActionLabel,c);
		c.gridx = 0;
		c.weightx = 1;
		actionPanel.add(new JLabel(""),c);
	//2. Line: click on status
		c.insets = new Insets( 2, 30, 2, 5);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridy = 1;
		c.gridx = 0;
		actionPanel.add(statusLabel,c);
		c.gridx = 1;
		actionPanel.add(statusBox,c);
	//3. Line: click on Name
		c.gridy = 2;
		c.gridx = 0;
		actionPanel.add(nameLabel,c);
		c.gridx = 1;
		actionPanel.add(nameBox,c);
	//4. Line: click on current Track
		c.gridy = 3;
		c.gridx = 0;
		actionPanel.add(currentTrackLabel,c);
		c.gridx = 1;
		actionPanel.add(currentTrackBox,c);
		
//CommonPanel
	//1.Line: activeTrayIcon
		c.insets = new Insets( 2, 5, 2, 5);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth=2;
		commonPanel.add(activeTrayIcon,c);
	//2.Line: activeTrayIcon
		c.gridwidth=1;
		c.gridy = 1;
		commonPanel.add(windowClosing,c);
		c.gridx = 1;
		commonPanel.add(windowActionBox,c);
		c.gridx = 2;
		c.weightx = 1;
		commonPanel.add(new JLabel(""),c);
	//3.Line: activeTrayIcon
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth=2;
		commonPanel.add(useAnotherLnfBox,c);
		c.gridy = 3;
		c.gridwidth=1;
		commonPanel.add(lnfLabel,c);
		c.gridx = 1;
		commonPanel.add(LookAndFeelBox,c);
		c.gridx = 2;
		c.weightx = 1;
		commonPanel.add(new JLabel(""),c);
	//4.Line: Show Text?
		c.gridy = 4;
		c.gridx = 0;
		commonPanel.add(showTextCheckBox,c);
	//5.Line: Shall we use the internal audio Player ?
		c.gridy = 5;
		c.gridx = 0;
		commonPanel.add(useInternalAudioPlayerCB,c);

//BUTTONPANEL
		c.insets = new Insets( 5, 5,5 ,5 );
		c.weightx = 0;
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 1;
		buttonPanel.add(saveAndExitButton,c);
		c.gridx = 1;
		buttonPanel.add(saveButton,c);
		c.weightx = 1.0;
		c.gridx = 2;
		buttonPanel.add(new JLabel(""),c);
		c.weightx = 0.0;
		c.gridx = 3;
		buttonPanel.add(abortButton,c);
		
		statusBox.setSelectedIndex(2);
		nameBox.setSelectedIndex(4);
		currentTrackBox.setSelectedIndex(3);

		dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		abortButton.addActionListener(new ExitListener());
		saveButton.addActionListener(new SaveListener());
		saveAndExitButton.addActionListener(new SaveAndExitListener());
		
		browseMP3Player.addActionListener(new MP3Listener());
		browseRipper.addActionListener(new RipperPathListener());
		browseGenerellPath.addActionListener(new BrowseListener());
		browseWebBrowserPath.addActionListener(new WebBrowserListener());
		browseFileBrowserPath.addActionListener(new FileBrowserListener());
		activeTrayIcon.addActionListener(new ChangeTrayFields());
		useAnotherLnfBox.addActionListener(new ChangeTrayFields());
		
		//set new language
		setLanguage();
		//load the old settings from file and set the right components active
		load();
		//look for the right index in combobox, if the old value was != null
		if(lookAndFeelBox_className != null) {
			for(int i=0; i < lookAndFeelInfos.length; i++) {
				if(lookAndFeelInfos[i].getClassName().equals(lookAndFeelBox_className)) {
					LookAndFeelBox.setSelectedIndex(i);
					break;
				}
			}
		}
		
		repaintCommon();
		
		//set size of window
		//pack together
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
		setVisible(true);
		
        //escape for exit
        KeyStroke escStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        //register all Strokes
        getRootPane().registerKeyboardAction(new ExitListener(), escStroke,
                JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	public void setLanguage() {
		try {
			//title of window
			setTitle(trans.getString("pref"));
			
			//tabs
			settingsPane.setTitleAt(0, trans.getString("tab.general"));
			settingsPane.setTitleAt(1, trans.getString("tab.path"));
			settingsPane.setTitleAt(2, trans.getString("tab.action"));
			settingsPane.setTitleAt(3, trans.getString("tab.languages")+ " - language");

			//general panel
			activeTrayIcon.setText(trans.getString("showSysTray"));
			showTextCheckBox.setText(trans.getString("showTextUnderIcons"));
			useInternalAudioPlayerCB.setText(trans.getString("settings.useInternalAudioPlayer"));
			windowClosing.setText(trans.getString("actionX"));
			windowActionBox.removeAllItems();
			windowActionBox.addItem(trans.getString("X.doNothing"));
			windowActionBox.addItem(trans.getString("X.Exit"));
			windowActionBox.addItem(trans.getString("X.inTray"));

			//action panel , I don't know how to do it easier
			explainActionLabel.setText(trans.getString("whenClickAction"));
			actions[0]=trans.getString("X.doNothing");
			actions[1]=trans.getString("action.OpenBrowser");
			actions[2]=	trans.getString("action.editStream");
			actions[3]=	trans.getString("action.startStop");
			actions[4]=	trans.getString("action.playStream");
			
			statusBox.removeAllItems();
			statusBox.addItem(actions[0]);
			statusBox.addItem(actions[1]);
			statusBox.addItem(actions[2]);
			statusBox.addItem(actions[3]);
			statusBox.addItem(actions[4]);
			
			nameBox.removeAllItems();
			nameBox.addItem(actions[0]);
			nameBox.addItem(actions[1]);
			nameBox.addItem(actions[2]);
			nameBox.addItem(actions[3]);
			nameBox.addItem(actions[4]);
			
			currentTrackBox.removeAllItems();
			currentTrackBox.addItem(actions[0]);
			currentTrackBox.addItem(actions[1]);
			currentTrackBox.addItem(actions[2]);
			currentTrackBox.addItem(actions[3]);
			currentTrackBox.addItem(actions[4]);
			
			//path panel
			ripLabel.setText(trans.getString("pathStreamripper"));
			mediaPlayer.setText(trans.getString("pathToMp3Player"));
			generellPathLabel.setText(trans.getString("genSavePath"));
			fileBrowserLabel.setText(trans.getString("filebrowserPath"));
			webBrowserLabel.setText(trans.getString("webBrowserPath"));
			
			//languages panel
			reqRestart.setText(trans.getString("reqRestart"));
			translationTA.setText(trans.getString("settings.translationTA"));

			
			//buttons
			abortButton.setText(trans.getString("abortButton"));
			saveAndExitButton.setText(trans.getString("okButton"));
			saveButton.setText(trans.getString("save"));
			
			//labels
			statusLabel.setText(trans.getString("status"));
			nameLabel.setText(trans.getString("streamname"));
			currentTrackLabel.setText(trans.getString("curTitle"));
			lnfLabel.setText(trans.getString("Settings.lnfLabel"));
			useAnotherLnfBox.setText(trans.getString("Settings.useAnotherLnfBox"));

		} catch ( MissingResourceException e ) { 
		      e.printStackTrace(); 
	    }		
	}
	
	private Gui_Settings gimme() {
		return this;
	}

	public class WebBrowserListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dirChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int i = dirChooser.showOpenDialog(gimme());
			if(i == JFileChooser.APPROVE_OPTION) {
				webBrowserField.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	public class FileBrowserListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dirChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int i = dirChooser.showOpenDialog(gimme());
			if(i == JFileChooser.APPROVE_OPTION) {
				fileBrowserField.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	
	public class RipperPathListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dirChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int i = dirChooser.showOpenDialog(gimme());
			if(i == JFileChooser.APPROVE_OPTION) {
				ripperPathField.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	
	public class MP3Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dirChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int i = dirChooser.showOpenDialog(gimme());
			if(i == JFileChooser.APPROVE_OPTION) {
				shoutcastPlayer.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	public class BrowseListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int i = dirChooser.showOpenDialog(gimme());
			if(i == JFileChooser.APPROVE_OPTION) {
				generellPathField.setText(dirChooser.getSelectedFile().toString());
			}
		}
	}
	
	public class SaveAndExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			save();
			dispose();
		}
	}
	
	public class SaveListener implements ActionListener	{
		public void actionPerformed(ActionEvent e) {
			save();
		}
	}
	
	/**
	 * Saves all preferences in an xml file:
	 * 	file "Settings-StreamRipStar.xml" 
	 *
	 */
	public void save() {
		String savePath =  new Control_GetPath().getStreamRipStarPath();
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance(); 
		
		try {
			XMLEventWriter writer = outputFactory.createXMLEventWriter(
					new FileOutputStream(savePath+"/Settings-StreamRipStar.xml" ) );
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			
			//header for the file
			XMLEvent header = eventFactory.createStartDocument();
			XMLEvent startRootSettings = eventFactory.createStartElement( "", "", "Settings" );

			XMLEvent activeIconCB_S = eventFactory.createAttribute( "activeTrayIcon",  String.valueOf( activeTrayIcon.isSelected()));
			XMLEvent lookAndFeelCB_S = eventFactory.createAttribute( "useAnotherLnfBox",  String.valueOf( useAnotherLnfBox.isSelected()));
			XMLEvent showTextCB_S = eventFactory.createAttribute( "showTextCB", String.valueOf( showTextCheckBox.isSelected()) );
			XMLEvent useInternalAudioPlayerCB_S = eventFactory.createAttribute( "useInternalAudioPlayerCB", String.valueOf( useInternalAudioPlayerCB.isSelected()) );
			XMLEvent ripperPathTF_S = eventFactory.createAttribute( "ripperPathTF", ripperPathField.getText()); 
			XMLEvent shoutcastTF_S = eventFactory.createAttribute( "shoutcastTF", shoutcastPlayer.getText()); 
			XMLEvent generellPathTF_S = eventFactory.createAttribute( "generellPathTF", generellPathField.getText()); 
			XMLEvent fileBrowserTF_S = eventFactory.createAttribute( "fileBrowserTF", fileBrowserField.getText()); 
			XMLEvent webBrowserTF_S = eventFactory.createAttribute( "webBrowserTF", webBrowserField.getText()); 
			XMLEvent statusBox_index = eventFactory.createAttribute( "statusBox_index", String.valueOf( statusBox.getSelectedIndex())); 
			XMLEvent nameBox_inde = eventFactory.createAttribute( "nameBox_index", String.valueOf( nameBox.getSelectedIndex())); 
			XMLEvent currentTrackBox_index = eventFactory.createAttribute( "currentTrackBox_index", String.valueOf( currentTrackBox.getSelectedIndex())); 
			XMLEvent langMenu_index = eventFactory.createAttribute( "langMenu_index", String.valueOf( langMenu.getSelectedIndex()));
			XMLEvent windowActionBox_index = eventFactory.createAttribute( "windowActionBox_index",  "1"); 
			if(activeTrayIcon.isSelected()) {
				windowActionBox_index = eventFactory.createAttribute( "windowActionBox_index",  String.valueOf( windowActionBox.getSelectedIndex())); 
			}
			XMLEvent lookAndFeel_index = eventFactory.createAttribute( "LookAndFeelBox_className",  "null"); 
			if(useAnotherLnfBox.isSelected()) {
				lookAndFeel_index = eventFactory.createAttribute( "LookAndFeelBox_className",lookAndFeelInfos[LookAndFeelBox.getSelectedIndex()].getClassName());
			}
			XMLEvent endRoot = eventFactory.createEndElement( "", "", "Settings" ); 
			XMLEvent endDocument = eventFactory.createEndDocument();
			
			//finally write into file
			writer.add( header ); 
			writer.add( startRootSettings );
			writer.add( activeIconCB_S ); 
			writer.add( lookAndFeelCB_S ); 
			writer.add( showTextCB_S ); 
			writer.add( useInternalAudioPlayerCB_S ); 
			writer.add( ripperPathTF_S ); 
			writer.add( shoutcastTF_S ); 
			writer.add( generellPathTF_S ); 
			writer.add( fileBrowserTF_S ); 
			writer.add( webBrowserTF_S ); 
			writer.add( statusBox_index ); 
			writer.add( nameBox_inde ); 
			writer.add( currentTrackBox_index ); 
			writer.add( langMenu_index ); 
			writer.add( windowActionBox_index ); 
			writer.add( lookAndFeel_index ); 
			writer.add( endRoot ); 
			writer.add( endDocument ); 
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} 
		
		String[] path = new String[5];
		path[0] = ripperPathField.getText();
		path[1] = shoutcastPlayer.getText();
		path[2] = generellPathField.getText();
		path[3] = webBrowserField.getText();
		path[4] = fileBrowserField.getText();
		
		String newlnfClassName = null;
		if(useAnotherLnfBox.isSelected()) {
			newlnfClassName = lookAndFeelInfos[LookAndFeelBox.getSelectedIndex()].getClassName();
		}
		
		int[] actions = new int[4];
		actions[0] = statusBox.getSelectedIndex();
		actions[1] = nameBox.getSelectedIndex();
		actions[2] = currentTrackBox.getSelectedIndex();
		actions[3] = windowActionBox.getSelectedIndex();
		
		mainGui.setNewRuntimePrefs(actions,showTextCheckBox.isSelected(),activeTrayIcon.isSelected(),
				newlnfClassName,useInternalAudioPlayerCB.isSelected());
		mainGui.getControlStream().setPaths(path);
	}
	
	/**
	 * load the settings from xml file
	 * and set checkboxes, textfields etc. correct
	 */
	public void load() {
		String loadPath =  new Control_GetPath().getStreamRipStarPath();
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser;
			parser = factory.createXMLStreamReader( new FileInputStream(loadPath+"/Settings-StreamRipStar.xml" ) );
			while ( parser.hasNext() ) { 
	 
				switch ( parser.getEventType() ) { 
					case XMLStreamConstants.START_DOCUMENT: 
						System.out.println( "Loading file Settings-StreamRipStar.xml" ); 
						break; 
				 
				    case XMLStreamConstants.END_DOCUMENT: 
				    	System.out.println( "End of read settings " ); 
				    	parser.close(); 
				    	break; 
				 
				    case XMLStreamConstants.START_ELEMENT: 
				    	for ( int i = 0; i < parser.getAttributeCount(); i++ ) {
				    		if(parser.getAttributeLocalName( i ).equals("activeTrayIcon")) {
				    			activeTrayIcon.setSelected(Boolean.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("showTextCB")) {
				    			showTextCheckBox.setSelected(Boolean.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("useInternalAudioPlayerCB")) {
				    			useInternalAudioPlayerCB.setSelected(Boolean.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("ripperPathTF")) {
				    			ripperPathField.setText(parser.getAttributeValue(i));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("shoutcastTF")) {
				    			shoutcastPlayer.setText(parser.getAttributeValue(i));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("generellPathTF")) {
				    			generellPathField.setText(parser.getAttributeValue(i));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("fileBrowserTF")) {
				    			fileBrowserField.setText(parser.getAttributeValue(i));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("webBrowserTF")) {
				    			webBrowserField.setText(parser.getAttributeValue(i));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("statusBox_index")) {
				    			statusBox.setSelectedIndex(Integer.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("nameBox_index")) {
				    			nameBox.setSelectedIndex(Integer.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("currentTrackBox_index")) {
				    			currentTrackBox.setSelectedIndex(Integer.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("langMenu_index")) {
				    			langMenu.setSelectedIndex(Integer.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("windowActionBox_index")) {
				    			windowActionBox.setSelectedIndex(Integer.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("useAnotherLnfBox")) {
				    			useAnotherLnfBox.setSelected(Boolean.valueOf(parser.getAttributeValue(i)));
				    		}
				    		else if (parser.getAttributeLocalName( i ).equals("LookAndFeelBox_className")) {
				    			String tmp = parser.getAttributeValue(i);
				    			if(tmp.equals("null")) {
				    				lookAndFeelBox_className = null;
				    			} else {
				    				lookAndFeelBox_className = parser.getAttributeValue(i);
				    			}
				    		}
				    	}
				    	break; 
				 
				    default: 
				    	break; 
				  }
				parser.next(); 
			}

		} catch (FileNotFoundException e) {
			System.err.println("No configuartion file found: Settings-StreamRipStar.xml");
			fillWithFoundPrograms();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void fillWithFoundPrograms() {
		String[][] programms = new Control_TestStartFirst().searchPrograms();
		
		if(programms != null) {
			ripperPathField.setText(programms[3][0]);
			shoutcastPlayer.setText(programms[2][0]);
			webBrowserField.setText(programms[1][0]);
			fileBrowserField.setText(programms[0][0]);
		}
	}


	public void repaintCommon() {
		if(activeTrayIcon.isSelected()) {
			windowActionBox.setEnabled(true);
			windowClosing.setEnabled(true);
		}
		else {
			windowActionBox.setEnabled(false);
			windowClosing.setEnabled(false);
			//set status: close when click on x
			windowActionBox.setSelectedIndex(1);
		}
		
		if(useAnotherLnfBox.isSelected()) {
			LookAndFeelBox.setEnabled(true);
			lnfLabel.setEnabled(true);
		} else {
			LookAndFeelBox.setEnabled(false);
			lnfLabel.setEnabled(false);
		}
	}
	
	/**
	 * return the own object
	 * @return the own object
	 */
	public Gui_Settings getMe() {
		return this;
	}
	
	/**
	 * only hide the window
	 */
	public class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	

	public class ChangeTrayFields implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			repaintCommon();
		}
	}
}
