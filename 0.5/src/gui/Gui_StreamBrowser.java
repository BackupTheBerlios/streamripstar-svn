package gui;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import misc.Stream;

import thread.Thread_GetGenre;
import thread.Thread_GetStreams;


import control.Control_GetPath;
import control.Control_http_Shoutcast;

public class Gui_StreamBrowser extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	
	private ResourceBundle toolTips = ResourceBundle.getBundle("translations.ToolTips");
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private Control_http_Shoutcast controlHttp = new Control_http_Shoutcast();
	private Object[] browseHeader = {"Nr.","Genre","Description", "Playing Now",
			"Listeners","Max. Listeners","Bitrate","Type","Website"};
	
	private String[][] allData = {};
	
	private DefaultTableModel browseModel = new DefaultTableModel(allData,browseHeader) {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int rowIndex, int columnIndex){return false;}
		@Override
        public Class<?> getColumnClass( int column ) {
			
            switch( column ){
                case 0: return Integer.class;
                case 4: return Integer.class;	//listeners
                case 5: return Integer.class;	//max Listeners
                case 6: return Integer.class;	//bitrate
                default: return String.class;
            }
        }};
	
	
	//JTree and components
	DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Shoutcast" ); 
	private JTree browseTree = new JTree(root); 
	private JPanel mainPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel iconPanel = new JPanel();
		
	private Gui_JTTable browseTable = new Gui_JTTable(browseModel);
	private JScrollPane browsePane  = new JScrollPane(browseTable);
	private JScrollPane genrePane = new JScrollPane(browseTree);
	private JSplitPane splitView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT
			,genrePane,browsePane);

	//set different Iconbars
	private JToolBar commonIconBar = new JToolBar();
	private JToolBar mediaIconBar = new JToolBar();
		
	//Icons for Iconbars
	private ImageIcon saveIcon = new ImageIcon((URL)getClass().getResource("/Icons/SB_save.png"));
	private ImageIcon hearMusicIcon = new ImageIcon((URL)getClass().getResource("/Icons/SB_play.png"));
	private ImageIcon abortIcon = new ImageIcon((URL)getClass().getResource("/Icons/SB_cancel.png"));
	private ImageIcon shoutCastIcon = new ImageIcon((URL)getClass().getResource("/Icons/SB_search.png"));
	private ImageIcon addAndRecordIcon = new ImageIcon((URL)getClass().getResource("/Icons/SB_save_quick.png"));
	private ImageIcon showColumnIcon = new ImageIcon((URL)getClass().getResource("/Icons/SB_showColumns.png"));
	private ImageIcon filterIcon = new ImageIcon((URL)getClass().getResource("/Icons/SB_filter.png"));
	private ImageIcon startRecordIcon = new ImageIcon((URL)getClass().getResource("/Icons/record_small.png"));
	private ImageIcon hearMusicIconForPopUp = new ImageIcon((URL)getClass().getResource("/Icons/player_small.png"));
	private ImageIcon saveIconForPopUp = new ImageIcon((URL)getClass().getResource("/Icons/save_small.png"));
	
	//buttons for Iconbars
	private JButton addToStreamRipStarButton = new JButton("Add to StreamRipStar",saveIcon);	
	private JButton listenToButton = new JButton ("Hear it",hearMusicIcon);
	private JButton abortButton = new JButton("Abort",abortIcon);
	private JButton shoutCastButton = new JButton("ShoutCast",shoutCastIcon);
	private JButton addAndRecButton = new JButton("Add and Record", addAndRecordIcon);
	private JButton showColumnButton = new JButton("Control Columns",showColumnIcon);
	private JButton filterButton = new JButton("Filter", filterIcon );
	
	//label for status
	private JLabel stautsLabel = new JLabel("Do nothing");
	
	//popup menu
	private JPopupMenu selShowPopup = new JPopupMenu();
	private JPopupMenu tablePopup = new JPopupMenu();
	
	//for controling the columns
	private JCheckBoxMenuItem showGenreColumn = new JCheckBoxMenuItem("Show Genre Column");
	private JCheckBoxMenuItem showDescriptionColumn = new JCheckBoxMenuItem("Show Description Column");
	private JCheckBoxMenuItem showPlayNowColumn = new JCheckBoxMenuItem("Show Now Playing Column");
	private JCheckBoxMenuItem showListenersColumn = new JCheckBoxMenuItem("Show Listener Column");
	private JCheckBoxMenuItem showMaxListenersColumn = new JCheckBoxMenuItem("Show Max. Listener Column");
	private JCheckBoxMenuItem showBitrateColumn = new JCheckBoxMenuItem("Show Bitrate Column");
	private JCheckBoxMenuItem showTypeColumn = new JCheckBoxMenuItem("Show Type Column");
	private JCheckBoxMenuItem showWebsitewColumn = new JCheckBoxMenuItem("Show Website Column");
	
	//for controling the popup in the table with the streams
	private JMenuItem startRecordMenuItem = new JMenuItem("Start Recording (Save And Start)",startRecordIcon);
	private JMenuItem saveMenuItem = new JCheckBoxMenuItem("Save Stream",saveIconForPopUp);
	private JMenuItem hearMenuItem = new JMenuItem("Hear Stream In Media Player",hearMusicIconForPopUp);
	
	//is Column shown?
	private boolean[] isColumnShow = new boolean[9];
	// 0 boolean isNrShown = true 	MUST EVER BE TRUE
	// 1 boolean isGenreShown = true;
	// 2 boolean isDescriptionShown = true;
	// 3 boolean isPlayNowShown = true;
	// 4 boolean isListenerShown = true;
	// 5 boolean isListenerShown = true;
	// 6 boolean isBitrateShown = true;
	// 7 boolean isTypeShown = true;
	// 8 boolean isWebsiteShown = true;
	
	private Thread_GetStreams getStreams = null;
	private Thread_GetGenre getGenres = null;
	
	//width of every column
	private int[] columnWidths = new int[9];

	//main window object of StreamRipStar
	private Gui_StreamRipStar StreamRipStar = null;
	private Gui_Filter filter= new Gui_Filter(this, false);
	
	//default window size (also needed in load())
	private int high = 530;
	private int width = 620;
	private int dividerLocation = 60;
	
	//disable an reaction of a click in one of 
	//the table models
	private boolean disableClick = false;
	
	public Gui_StreamBrowser(Gui_StreamRipStar StreamRipStar) {
		super("StreamBrowser");
		this.StreamRipStar = StreamRipStar;
		//make all columns visible
		for(int i=0;i<isColumnShow.length;i++)
			isColumnShow[i] = true;
		//set the columns to 70 for fail save
		for(int i=0;i<columnWidths.length;i++)
			columnWidths[i] = 70;
		
		//load properties
		load();
		//update gui of filter
		filter.updateGuis();
		//update language
		setLanguage();
		//set text under Icons?
		showTextUnderIcons(StreamRipStar.showTextUnderIcons(),StreamRipStar.getFontForTextUnderIcons());
		//sort columns
		sortColumns();
		//set up visible contents
		createGui();
		//update the column width
		setAllColumnWidths();
		//create Tree
		createBrowseTree();
		//set tooltips
		setToolTips();

	}
	
	
	//set tooltips to the buttons
	public void setToolTips() {
		listenToButton.setToolTipText(toolTips.getString("StreamBrowser.listen"));
		addToStreamRipStarButton.setToolTipText(toolTips.getString("StreamBrowser.addToStreamRipStar"));
		shoutCastButton.setToolTipText(toolTips.getString("StreamBrowser.shoutcast"));
		showColumnButton.setToolTipText(toolTips.getString("StreamBrowser.Columns"));
		addAndRecButton.setToolTipText(toolTips.getString("StreamBrowser.addAndRecord"));
		abortButton.setToolTipText(toolTips.getString("StreamBrowser.abort"));
		filterButton.setToolTipText(toolTips.getString("StreamBrowser.filter"));
	}
	
	
	/**
	 * places the test under icons or delete it
	 * @param visibility: if true, show the text
	 * @param newFont: set the text with an new Font. If font == null then dont use an new font
	 */
	public void showTextUnderIcons(Boolean visibility, Font newFont) {
		//if the text is shown
		if(visibility) {
			listenToButton.setText(trans.getString("iconPanel.listenToButton"));
			addToStreamRipStarButton.setText(trans.getString("iconPanel.addToStreamRipStarButton"));
			shoutCastButton.setText(trans.getString("iconPanel.shoutCastButton"));
			showColumnButton.setText(trans.getString("iconPanel.showColumnButton"));
			addAndRecButton.setText(trans.getString("iconPanel.addAndRecButton"));
			abortButton.setText(trans.getString("iconPanel.abortButton"));
			filterButton.setText(trans.getString("iconPanel.filterButton"));
			
			if (newFont != null) {
				listenToButton.setFont(newFont);
				addToStreamRipStarButton.setFont(newFont);
				shoutCastButton.setFont(newFont);
				showColumnButton.setFont(newFont);
				addAndRecButton.setFont(newFont);
				abortButton.setFont(newFont);
				filterButton.setFont(newFont);
			}
		} else {
			listenToButton.setText(null);
			addToStreamRipStarButton.setText(null);
			shoutCastButton.setText(null);
			showColumnButton.setText(null);
			addAndRecButton.setText(null);
			abortButton.setText(null);
			filterButton.setText(null);
		}
	}
	
	//create visible content
	public void createGui() {	
		//resize next column only
		browseTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		//not allow moving the column, because in column[0]
		//streamRipStar expect the nr. 
		browseTable.getTableHeader().setReorderingAllowed(false);
		//autosorter for rows
		browseTable.setAutoCreateRowSorter(true);
		//set layouts
		mainPanel.setLayout(new BorderLayout());
		topPanel.setLayout(new GridBagLayout());
		FlowLayout fLayout = new FlowLayout();
		iconPanel.setLayout(fLayout);
		
		//set alignment
		fLayout.setAlignment(FlowLayout.LEFT);
		
		//add panels
		getContentPane().add(mainPanel);
		mainPanel.add(topPanel, BorderLayout.CENTER);
		mainPanel.add(iconPanel, BorderLayout.PAGE_START);
		mainPanel.add(stautsLabel, BorderLayout.SOUTH);
		mainPanel.add(filter, BorderLayout.EAST);

		//set Constrains defaults
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		
		//Add the Splitpane that contains both panes (genre and stream)
		c.weighty= 1.0;
		c.weightx = 1.0;
		c.gridy = 0;
		c.gridx = 0;
		topPanel.add(splitView,c);
		
		//add IconBars on top
		iconPanel.add(commonIconBar);
//		iconPanel.add(mediaIconBar);
		
		//build Iconbars
		buildIconBars();
		
		
		//grahical
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //get resolution
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        //calculates the app. values
        int x = (screenDim.width - Integer.valueOf(width))/2;
        int y = (screenDim.height - Integer.valueOf(high))/2;
        //set location
        setLocation(x, y);
		//setSize
        setSize(new Dimension(Integer.valueOf(width),Integer.valueOf(high)));
        //make visible
		setVisible(true);
		
		//Listeners

		browseTable.addMouseListener( new BrowseMouseListener() );
		browseTable.getTableHeader().setComponentPopupMenu(selShowPopup);
		listenToButton.addActionListener( new playMusikListener() );
		addToStreamRipStarButton.addActionListener( new AddToStreamRipStarListener() );
		showColumnButton.addMouseListener( new ShowPopupListener() );
		abortButton.addActionListener(new AbortThreadsListener());
		addAndRecButton.addActionListener(new AddAndStartRecordListener());
		filterButton.addActionListener(new FilterListener());
		shoutCastButton.addActionListener(new GetGenreListener());
		addWindowListener(this);
		
		//no extra space for the genretable
		splitView.setResizeWeight(0.0d);
		//set space for the genrepane
		splitView.setDividerLocation(dividerLocation);
		
		//
		browseTree.addMouseListener(new TreeListener());
	}
	
	private void setLanguage() {
		try {
			//iconPanel
			listenToButton.setText(trans.getString("iconPanel.listenToButton"));
			addToStreamRipStarButton.setText(trans.getString("iconPanel.addToStreamRipStarButton"));
			shoutCastButton.setText(trans.getString("iconPanel.shoutCastButton"));
			showColumnButton.setText(trans.getString("iconPanel.showColumnButton"));
			addAndRecButton.setText(trans.getString("iconPanel.addAndRecButton"));
			abortButton.setText(trans.getString("iconPanel.abortButton"));
			filterButton.setText(trans.getString("iconPanel.filterButton"));
			
			//popup - ColumControl
			showGenreColumn.setText(trans.getString("ColumControl.showGenreColumn"));
			showDescriptionColumn.setText(trans.getString("ColumControl.showDescriptionColumn"));
			showPlayNowColumn.setText(trans.getString("ColumControl.showPlayNowColumn"));
			showListenersColumn.setText(trans.getString("ColumControl.showListenersColumn"));
			showMaxListenersColumn.setText(trans.getString("ColumControl.showMaxListenersColumn"));
			showBitrateColumn.setText(trans.getString("ColumControl.showBitrateColumn"));
			showTypeColumn.setText(trans.getString("ColumControl.showTypeColumn"));
			showWebsitewColumn.setText(trans.getString("ColumControl.showWebsitewColumn"));
	
			//set Table Header
			browseHeader[0] = trans.getString("Header.Nr");
			browseHeader[1] = trans.getString("Header.Genre");
			browseHeader[2] = trans.getString("Header.Description");
			browseHeader[3] = trans.getString("Header.PlayingNow");
			browseHeader[4] = trans.getString("Header.Listeners");
			browseHeader[5] = trans.getString("Header.MaxListeners");
			browseHeader[6] = trans.getString("Header.Bitrate");
			browseHeader[7] = trans.getString("Header.Type");
			browseHeader[8] = trans.getString("Header.Website");
			browseModel.setColumnIdentifiers(browseHeader);
			
			//the table popup
			startRecordMenuItem.setText(trans.getString("popup.startRecStreamBrowser"));
			saveMenuItem.setText(trans.getString("popup.save"));
			hearMenuItem.setText(trans.getString("popup.hear"));
			
		} catch ( MissingResourceException e ) { 
		      System.err.println( e ); 
		}
	}
	
	public void createBrowseTree() {
		if (getGenres == null) {
			getGenres = new Thread_GetGenre(getMe(),trans);
			getGenres.start();
			System.out.println("loading genres...");
		} else 
			System.out.println("It seems, that it already loading...");
	}
	
	public DefaultMutableTreeNode getTreeRoot() {
		return root;
	}
	
	public JTree getTree() {
		return browseTree;
	}
	
	public Control_http_Shoutcast  getControlHttp() {
		return controlHttp;
	}
	
	public void setStatusText(String statusText) {
		stautsLabel.setText(statusText);
	}
	
	public void updateUITree() {
		browseTree.expandRow(0);
	}
	
	public DefaultTableModel getBrowseModel() {
		return browseModel;
	}
	
	public JTable getBrowseTable() {
		return browseTable;
	}
	
	public void disableModelClick(boolean click) {
		disableClick = click;
	}
	
	public void setAbortButtonEnable(boolean enable) {
		abortButton.setEnabled(enable);
	}
	
	
	/**
	 * Build Iconbars and add a simple look
	 */
	public void buildIconBars() {
		//set a simple look
		listenToButton.setHorizontalTextPosition(SwingConstants.CENTER);
		listenToButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		listenToButton.setBorderPainted(false);
		addToStreamRipStarButton.setHorizontalTextPosition(SwingConstants.CENTER);
		addToStreamRipStarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		addToStreamRipStarButton.setBorderPainted(false);
		shoutCastButton.setHorizontalTextPosition(SwingConstants.CENTER);
		shoutCastButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		shoutCastButton.setBorderPainted(false);
		showColumnButton.setHorizontalTextPosition(SwingConstants.CENTER);
		showColumnButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		showColumnButton.setBorderPainted(false);
		addAndRecButton.setHorizontalTextPosition(SwingConstants.CENTER);
		addAndRecButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		addAndRecButton.setBorderPainted(false);
		abortButton.setHorizontalTextPosition(SwingConstants.CENTER);
		abortButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		abortButton.setBorderPainted(false);
		filterButton.setHorizontalTextPosition(SwingConstants.CENTER);
		filterButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		filterButton.setBorderPainted(false);
		
		//add Buttons to CommIconBar
		commonIconBar.add(addToStreamRipStarButton);
		commonIconBar.add(listenToButton);
		commonIconBar.add(showColumnButton);
		commonIconBar.add(addAndRecButton);
		commonIconBar.add(abortButton);
		commonIconBar.add(filterButton);
		
		//add Buttons to MediaIconBar
		mediaIconBar.add(shoutCastButton);
		
		//set the color of all button the backgroundcolor
		//from the iconbar and disable der Border
		commonIconBar.setBackground(new Color(238,238,238,255));
		mediaIconBar.setBackground(new Color(238,238,238,255));

		
		//Build JPopupMenu
		selShowPopup.add(showGenreColumn);
		selShowPopup.add(showDescriptionColumn);
		selShowPopup.add(showPlayNowColumn);
		selShowPopup.add(showListenersColumn);
		selShowPopup.add(showMaxListenersColumn);
		selShowPopup.add(showBitrateColumn);
		selShowPopup.add(showTypeColumn);
		selShowPopup.add(showWebsitewColumn);
		
		tablePopup.add(startRecordMenuItem);
		tablePopup.add(saveMenuItem);
		tablePopup.add(hearMenuItem);
		
		//add Listerner
		showGenreColumn.addActionListener(new ShowRightColumnOrder(1));
		showDescriptionColumn.addActionListener(new ShowRightColumnOrder(2));
		showPlayNowColumn.addActionListener(new ShowRightColumnOrder(3));
		showListenersColumn.addActionListener(new ShowRightColumnOrder(4));
		showMaxListenersColumn.addActionListener(new ShowRightColumnOrder(5));
		showBitrateColumn.addActionListener(new ShowRightColumnOrder(6));
		showTypeColumn.addActionListener(new ShowRightColumnOrder(7));
		showWebsitewColumn.addActionListener(new ShowRightColumnOrder(8));
		
		startRecordMenuItem.addActionListener(new AddAndStartRecordListener());
		saveMenuItem.addActionListener( new AddToStreamRipStarListener() );
		hearMenuItem.addActionListener( new playMusikListener() );
		
		showGenreColumn.setSelected(isColumnShow[1]);
		showDescriptionColumn.setSelected(isColumnShow[2]);
		showPlayNowColumn.setSelected(isColumnShow[3]);
		showListenersColumn.setSelected(isColumnShow[4]);
		showMaxListenersColumn.setSelected(isColumnShow[5]);
		showBitrateColumn.setSelected(isColumnShow[6]);
		showTypeColumn.setSelected(isColumnShow[7]);
		showWebsitewColumn.setSelected(isColumnShow[8]);
	
	}

	/**
	 * removes recursive all cells from a model
	 * the defaultTableModel must be given on startup
	 * @param model
	 */
	public void removeAllFromTable(DefaultTableModel model) {
		int rowCount = model.getRowCount();
		for(int i=rowCount; i>0;i--)
			model.removeRow(i-1);
	}
	
	public Gui_StreamBrowser getMe() {
		return this;
	}
	

	/**
	 * look for:
	 * 	[0] = address
	 * 	[1] = name
	 * 	[2] = website
	 * from the selected stream and return this String
	 * @return
	 */
	public String[] getStreamInfo() {
		String[] address = new String[3];
		
		//get selectet Streams
		if(browseTable.getSelectedRow() >= 0) {	
			Vector<String[]> streams = null;
				
			//get Nr. of Stream
			Object content = browseTable.getValueAt(browseTable.getSelectedRow(),0);
			int nr = Integer.valueOf(content.toString());
			System.out.println("Debug: Selected Line nummber: "+nr);
			
			//get Streams in Vector
			//if the stream is filtered, get the filtered stream vector
			//else the original
			if(filter.isFiltered()) {
				streams = filter.getFilteredStreamVector();
				System.out.println("Use Filtered StreamVector");
			} else {
				streams = controlHttp.getStreams();
				System.out.println("Use Normal StreamVector");
			}
			
			if(streams != null) {
				//save main address from site (e.g www.shoutcast.com)
				String url  = controlHttp.getBaseAddress();
				
				//add stream specific url (e.g. /sbin/index.html)
				url += streams.get(nr)[7];
				
				//get address from .pls file
				address[0] = controlHttp.getfirstStreamFromURL(url);
				address[1] = streams.get(nr)[0];
				address[2] = streams.get(nr)[1];
			} else {
				System.err.println("Gets an empty FILTERED stream vector: can't get info");
			}
		}
		return address;
	}
	
	/**
	 * this method gets the url from the selected stream
	 * and start an the media player with it
	 */
	public void playStream() {
		//if a row is selected
		if(browseTable.getSelectedRow() >= 0) {
			//get url
			String url = getStreamInfo()[0];
			
			//if url = "" it fails
			if(url == null || url.equals("")) {
				JOptionPane.showMessageDialog(getMe()
						,StreamRipStar.getTrans().getString("Message.emptyString"));
				System.err.println(StreamRipStar.getTrans().getString("Message.emptyString"));
			}
			else {
				StreamRipStar.getControlStream().startMp3Player(url);
				System.out.println(url);
			}
		} else
			JOptionPane.showMessageDialog(getMe()
					,StreamRipStar.getTrans().getString("select"));
	}
	
	/**
	 * This class save properties from this gui
	 * into a file
	 */
	public void save() {
		String savePath =  new Control_GetPath().getStreamRipStarPath();
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance(); 
		int[] intOptions = new int[12];
		String[] strOptions = new String[7];
		
		try {
			XMLEventWriter writer = outputFactory.createXMLEventWriter(
					new FileOutputStream(savePath+"/Streambrowser.xml" ) );
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();

			//save Intgers of the widths of any cell that is shown
			for(int i=0; i < isColumnShow.length ;i++) {
				if(isColumnShow[i]) {
					intOptions[i+3] = browseTable.getColumn(browseHeader[i]).getWidth();
				} else {
					//for failsave
					intOptions[i+3] = 30;
				}
			}

			//save Strings
			//filter settings
			String[] x = filter.getSaveSettings();
			for(int i=0;i<x.length;i++) {
				strOptions[i] = x[i];
			}
			
			//header for the file
			XMLEvent header = eventFactory.createStartDocument();
			XMLEvent startRootSettings = eventFactory.createStartElement( "", "", "Settings" );

			XMLEvent height = eventFactory.createAttribute( "height",  String.valueOf( getSize().height)); 
			XMLEvent width= eventFactory.createAttribute( "width",  String.valueOf( getSize().width)); 
			XMLEvent divLocation = eventFactory.createAttribute( "divLocation",  String.valueOf( splitView.getDividerLocation())); 
			
			XMLEvent i0 = eventFactory.createAttribute( "i0",  String.valueOf( intOptions[0] ));
			XMLEvent i1 = eventFactory.createAttribute( "i1",  String.valueOf( intOptions[1] ));
			XMLEvent i2 = eventFactory.createAttribute( "i2",  String.valueOf( intOptions[2] ));
			XMLEvent i3 = eventFactory.createAttribute( "i3",  String.valueOf( intOptions[3] ));
			XMLEvent i4 = eventFactory.createAttribute( "i4",  String.valueOf( intOptions[4] ));
			XMLEvent i5 = eventFactory.createAttribute( "i5",  String.valueOf( intOptions[5] ));
			XMLEvent i6 = eventFactory.createAttribute( "i6",  String.valueOf( intOptions[6] ));
			XMLEvent i7 = eventFactory.createAttribute( "i7",  String.valueOf( intOptions[7] ));
			XMLEvent i8 = eventFactory.createAttribute( "i8",  String.valueOf( intOptions[8] ));
			XMLEvent i9 = eventFactory.createAttribute( "i9",  String.valueOf( intOptions[9] ));
			XMLEvent i10 = eventFactory.createAttribute( "i10",  String.valueOf( intOptions[10] ));
			XMLEvent i11 = eventFactory.createAttribute( "i11",  String.valueOf( intOptions[11] ));
			
			XMLEvent s0 = eventFactory.createAttribute( "s0",  String.valueOf( strOptions[0] ));
			XMLEvent s1 = eventFactory.createAttribute( "s1",  String.valueOf( strOptions[1] ));
			XMLEvent s2 = eventFactory.createAttribute( "s2",  String.valueOf( strOptions[2] ));
			XMLEvent s3 = eventFactory.createAttribute( "s3",  String.valueOf( strOptions[3] ));
			XMLEvent s4 = eventFactory.createAttribute( "s4",  String.valueOf( strOptions[4] ));
			XMLEvent s5 = eventFactory.createAttribute( "s5",  String.valueOf( strOptions[5] ));
			XMLEvent s6 = eventFactory.createAttribute( "s6",  String.valueOf( strOptions[6] ));
			
			XMLEvent b0 = eventFactory.createAttribute( "b0",  String.valueOf( isColumnShow[0] ));
			XMLEvent b1 = eventFactory.createAttribute( "b1",  String.valueOf( isColumnShow[1] ));
			XMLEvent b2 = eventFactory.createAttribute( "b2",  String.valueOf( isColumnShow[2] ));
			XMLEvent b3 = eventFactory.createAttribute( "b3",  String.valueOf( isColumnShow[3] ));
			XMLEvent b4 = eventFactory.createAttribute( "b4",  String.valueOf( isColumnShow[4] ));
			XMLEvent b5 = eventFactory.createAttribute( "b5",  String.valueOf( isColumnShow[5] ));
			XMLEvent b6 = eventFactory.createAttribute( "b6",  String.valueOf( isColumnShow[6] ));
			XMLEvent b7 = eventFactory.createAttribute( "b7",  String.valueOf( isColumnShow[7] ));
			XMLEvent b8 = eventFactory.createAttribute( "b8",  String.valueOf( isColumnShow[8] ));
			
			XMLEvent endRoot = eventFactory.createEndElement( "", "", "Settings" ); 
			XMLEvent endDocument = eventFactory.createEndDocument();
			
			//finally write into file
			writer.add( header ); 
			writer.add( startRootSettings );
			
			writer.add( height ); 
			writer.add( width ); 
			writer.add( divLocation ); 
			writer.add( i0 ); 
			writer.add( i1 ); 
			writer.add( i2 ); 
			writer.add( i3 ); 
			writer.add( i4 ); 
			writer.add( i5 ); 
			writer.add( i6 ); 
			writer.add( i7 ); 
			writer.add( i8 ); 
			writer.add( i9 ); 
			writer.add( i10 ); 
			writer.add( i11 ); 
			writer.add( s0 ); 
			writer.add( s1 ); 
			writer.add( s2); 
			writer.add( s3 ); 
			writer.add( s4 ); 
			writer.add( s5 ); 
			writer.add( s6 ); 
			writer.add( b0 ); 
			writer.add( b1 ); 
			writer.add( b2 ); 
			writer.add( b3 ); 
			writer.add( b4 ); 
			writer.add( b5 ); 
			writer.add( b6 ); 
			writer.add( b7 ); 
			writer.add( b8 ); 
			
			writer.add( endRoot ); 
			writer.add( endDocument ); 
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * This Method loads the settings for gui_StreamBrowser
	 * (sBProps) on startup and save it 
	 */
	public void load() {
		String[] strOptions = new String[7];
		String loadPath =  new Control_GetPath().getStreamRipStarPath();
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser;
			parser = factory.createXMLStreamReader( new FileInputStream(loadPath+"/Streambrowser.xml" ) );
			while ( parser.hasNext() ) { 
	 
				switch ( parser.getEventType() ) { 
					case XMLStreamConstants.START_DOCUMENT: 
						System.out.println( "Loading file Streambrowser.xml" ); 
						break; 
				 
				    case XMLStreamConstants.END_DOCUMENT: 
				    	System.out.println( "End of read settings " ); 
				    	parser.close(); 
				    	break; 
				 
				    case XMLStreamConstants.START_ELEMENT: 
				    	for ( int i = 0; i < parser.getAttributeCount(); i++ ) {
				    		if(parser.getAttributeLocalName( i ).equals("")) {
				    			
				    		} else if (parser.getAttributeLocalName( i ).equals("height")) {
				    			high = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("width")) {
				    			width = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("divLocation")) {
				    			dividerLocation = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i3")) {
				    			columnWidths[0] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i4")) {
				    			columnWidths[1] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i5")) {
				    			columnWidths[2] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i6")) {
				    			columnWidths[3] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i7")) {
				    			columnWidths[4] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i8")) {
				    			columnWidths[5] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i9")) {
				    			columnWidths[6] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i10")) {
				    			columnWidths[7] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("i11")) {
				    			columnWidths[8] = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("s0")) {
				    			strOptions[0] = parser.getAttributeValue(i);
				    		} else if (parser.getAttributeLocalName( i ).equals("s1")) {
				    			strOptions[1] = parser.getAttributeValue(i);
				    		} else if (parser.getAttributeLocalName( i ).equals("s2")) {
				    			strOptions[2] = parser.getAttributeValue(i);
				    		} else if (parser.getAttributeLocalName( i ).equals("s3")) {
				    			strOptions[3] = parser.getAttributeValue(i);
				    		} else if (parser.getAttributeLocalName( i ).equals("s4")) {
				    			strOptions[4] = parser.getAttributeValue(i);
				    		} else if (parser.getAttributeLocalName( i ).equals("s5")) {
				    			strOptions[5] = parser.getAttributeValue(i);
				    		} else if (parser.getAttributeLocalName( i ).equals("s6")) {
				    			strOptions[6] = parser.getAttributeValue(i);
				    		} else if (parser.getAttributeLocalName( i ).equals("b0")) {
				    			isColumnShow[0] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b1")) {
				    			isColumnShow[1] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b2")) {
				    			isColumnShow[2] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b3")) {
				    			isColumnShow[3] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b4")) {
				    			isColumnShow[4] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b5")) {
				    			isColumnShow[5] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b6")) {
				    			isColumnShow[6] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b7")) {
				    			isColumnShow[7] = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("b8")) {
				    			isColumnShow[8] = Boolean.valueOf(parser.getAttributeValue(i));
				    		}
				    	}
				    	filter.loadSettings(strOptions);
				    	break; 
				 
				    default: 
				    	break; 
				  }
				parser.next(); 
			}

		} catch (FileNotFoundException e) {
			System.err.println("No configuartion file found: Streambrowser.xml");
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void setThreadToNull() {
		getGenres = null;
		getStreams = null;
	}
	
	/**
	 * this method sort the columns in right order
	 * but only this one, which should be visible
	 */
	public void sortColumns() {
		//remove all columns first
		for( int i=browseTable.getColumnCount(); i > 0 ;i-- ) {
			browseTable.removeColumn(browseTable.getColumnModel().getColumn(i-1));
		}
		
		//and now set up only the columns witch should be visible
		for(int j=0 ; j < isColumnShow.length; j++) {
			if(isColumnShow[j]) {
				browseTable.addColumn(new TableColumn(j));
			}
		}
		
		//setTheWidth
		setAllColumnWidths();
	}

	/**
	 * this method set the width of an column
	 * if its visible. the width is saved in
	 * an array of Integer (columnWidths[])
	 */
	public void setAllColumnWidths() {
		for(int i=0; i<columnWidths.length; i++) {
			if( isColumnShow[i] ) {
				browseTable.getColumn(browseHeader[i])
					.setPreferredWidth(columnWidths[i]);
			}
		}
	}
	
	/**
	 * save the columns width into the array columnWidths
	 * and run methode save() to save it into file
	 */
	public void saveColumnWidth() {
		//get the widths of any cell that is shown
		for(int i=0; i < isColumnShow.length ;i++) {
			if(isColumnShow[i]) {
				columnWidths[i] = browseTable.getColumn(browseHeader[i]).getWidth();
			}
		}
		save();
	}
	
	/**
	 * get the JPanel with components for filtering
	 * @return
	 */
	public Gui_Filter getFilterGui() {
		return filter;
	}
//
//	Listener
//
	/**
	 *This Class get the selected Stream and execute the
	 *method to play the stream in the media player 
	 */
	public class BrowseMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()==2) {
				//only if table is filled with streams
				if(browseModel.getColumnCount()>0 ) {
					//get selected genre from table
					Object selectedStream =	browseTable.getValueAt(browseTable.getSelectedRow(),0);
					
					//debugmessage
					System.out.println("Debug: "+selectedStream.toString());
					
					//play selected stream in media player
					playStream();
				}
			}
			//Right-click button for menu
			if(e.getButton() == 3) {
				System.out.println("Debug: rightlick debug");
			}
		}
		
		public void mousePressed(MouseEvent e){
			if(e.getButton() == MouseEvent.BUTTON3) {
				int row = browseTable.rowAtPoint(e.getPoint());
				
				//only selection was a row
				if(row >= 0) {
					browseTable.setRowSelectionInterval(row,row);
					tablePopup.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
				}
			}
		}
	}

	
	/**
	 * Play selected stream in media player
	 */
	class playMusikListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			playStream();
		}
	}
	
	class AbortThreadsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(getGenres != null) {
				getGenres.killMe();
				getGenres = null;
			}
			
			if(getStreams != null) {
				getStreams.killMe();
				getStreams = null;
			}

		}
		
	}
	
	class FilterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(filter.isVisible())
				filter.setVisible(false);
			else
				filter.setVisible(true);
		}
	}
	
	/**
	 * load the genres from www.shoutcast.com
	 *
	 */
	class GetGenreListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			createBrowseTree();
		}
	}

	
	/**
	 * This Class gets content of a selected stream
	 * and open the dialog (Gui_AddStream) to add this
	 * stream to StreamRipStar
	 */
	class AddToStreamRipStarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//if a row is selected
			if(browseTable.getSelectedRow() >= 0) {
				//get info for selected stream
				String[] content = getStreamInfo();
				
				//if url = "" it fails
				if(content[0].equals("")) {
					System.err.println("Error while fetching streamaddress\n");
				}
				else {
					Gui_StreamOptions dialog = new Gui_StreamOptions(null,StreamRipStar,true,true,false);
					dialog.setBasics(content);
				}
			} else
				//if nothing is selected, open popup
				JOptionPane.showMessageDialog(getMe()
						,StreamRipStar.getTrans().getString("select"));
		}
	}
	
	/**
	 * Add the selected Stream to StreamRipStar without any 
	 * questions and start recording them
	 */
	class AddAndStartRecordListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//if a row is selected
			if(browseTable.getSelectedRow() >= 0) {
				//get info for selected stream
				String[] content = getStreamInfo();
				//[1] is name
				
				//if url = "" it fails
				if(content[0] == null || content[0].equals("")) {
					JOptionPane.showMessageDialog(getMe(), StreamRipStar.getTrans().getString("Message.emptyString"));
					System.err.println("Error while fetching streamaddress\n");
				}
				else {
					Gui_StreamOptions tmpAddStream = new Gui_StreamOptions(
							null,StreamRipStar, true, false,false);
					tmpAddStream.setBasics(content);
					tmpAddStream.save();
					Stream tmpStream = tmpAddStream.getStream();
					if(tmpStream != null) {
						StreamRipStar.startRippingUnselected(tmpStream);
						System.out.println("Debug: Adding stream from Streambrowser" +
								"and start recording");
					}
				}
			} else
				//if nothing is selected, open popup
				JOptionPane.showMessageDialog(getMe()
						,StreamRipStar.getTrans().getString("select"));
		}
	}
	
	public class ShowPopupListener implements MouseListener {
			public void mouseClicked(MouseEvent e){
				//save the layout from all columns
				saveColumnWidth();
				//place the popup under the button. orientation: left 
		    		selShowPopup.show(showColumnButton,
		    				showColumnButton.getX()-showColumnButton.getWidth(),
		    				showColumnButton.getY()+showColumnButton.getHeight());
			}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
	}
	
	
	
	/**
	 *This class change the visibility of a cell and
	 * sort the columns
	 */
	class ShowRightColumnOrder implements ActionListener {
		int index = 0;
		public ShowRightColumnOrder(int index) {
			this.index = index;
		}
		public void actionPerformed(ActionEvent e) {
			//change the status
			changeStatus(index);
			//1 .update View
			sortColumns();
			//2. save
			save();
		}
		
		public void changeStatus(int index) {
			if(isColumnShow[index]) {
				isColumnShow[index] = false;
			} else {
				isColumnShow[index] = true;
			}
		}
	}
	
	/**
	 * This Listener looks for a click an the JTree
	 * and execute the command to fill the table
	 * streams
	 */
	public class TreeListener implements MouseListener {
		public void mouseClicked(MouseEvent e){
			boolean stop = false;
			//get selected path and look for last element
			if(e.getClickCount() == 1 && !disableClick) {
				if(browseTree.getSelectionPath() != null) {
					String selectedGenre = browseTree.getSelectionPath()
						.getLastPathComponent().toString();
					
					//replace some genres with right values
					if(selectedGenre.equals("-=[Top 25 Streams]=-"))
						selectedGenre = "TopTen";
					if(selectedGenre.equals("Hip-Hop/Rap"))
						selectedGenre ="Hip%20Hop";
					//catch search defaultTreeNode
					if(selectedGenre.equals(trans.getObject("GetGenres.search"))) {
						selectedGenre =	JOptionPane.showInputDialog(getMe(),
								trans.getString("Message.search"),
								trans.getString("Message.searchHeader"),
                                JOptionPane.PLAIN_MESSAGE);
						if(selectedGenre == null) {
							stop = true;
						}

					}
							
			    	System.out.println("Debug: "+selectedGenre);
			    	//fill table with streams
			    	if(!stop) {
			    		getStreams = new Thread_GetStreams(getMe(),selectedGenre,trans);
			    		getStreams.start();
			    	}
				}
			}
		}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}


	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {

		if(getStreams != null) {
			getStreams.killMe();
		}
		if(getGenres != null) {
			getGenres.killMe();
		}
		saveColumnWidth();
		save();
		dispose();
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}