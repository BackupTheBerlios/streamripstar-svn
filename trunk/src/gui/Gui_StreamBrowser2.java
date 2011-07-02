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
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import misc.Stream;
import thread.Thread_GetStreams_FromShoutcast;
import control.Control_GetPath;
import control.Control_http_Shoutcast_2;
import control.SRSOutput;
import control.Shoutcast2;

public class Gui_StreamBrowser2 extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	
	private ResourceBundle toolTips = ResourceBundle.getBundle("translations.ToolTips");
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private Control_http_Shoutcast_2 controlHttp = new Control_http_Shoutcast_2();
	private Object[] browseHeader = {"ID","Name", "Playing Now",
			"Listeners","Bitrate","Type"};
	
	private String[][] allData = {};
	
	private DefaultTableModel browseModel = new DefaultTableModel(allData,browseHeader) {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int rowIndex, int columnIndex){return false;}
		@Override
        public Class<?> getColumnClass( int column ) {
			
            switch( column ){
                case 0: return Integer.class;	// ID at Shoutcast
                case 3: return Integer.class;	// listeners
                case 4: return Integer.class;	// Bitrate
                default: return String.class;
            }
        }};
	
	
	//JTree and components
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Shoutcast" );
    private DefaultTreeCellRenderer renderer;
    private DefaultTreeModel treeModel;
	private JTree browseTree = new JTree(); 
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
		
	//Icons for Iconbars
	//Icons are from oxygen project for kde4. Licenced under gpl 
	private ImageIcon saveIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/save.png"));
	private ImageIcon hearMusicIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/player_start.png"));
	private ImageIcon stopHearMusicIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/player_stop.png"));
	private ImageIcon abortIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/cancel.png"));
	private ImageIcon addAndRecordIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/save_quick2.png"));
	private ImageIcon filterIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/filter.png"));
	private ImageIcon startRecordIcon = new ImageIcon((URL)getClass().getResource("/Icons/record_small.png"));
	private ImageIcon hearMusicIconForPopUp = new ImageIcon((URL)getClass().getResource("/Icons/player_small.png"));
	private ImageIcon saveIconForPopUp = new ImageIcon((URL)getClass().getResource("/Icons/save_small.png"));
	private ImageIcon leafIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/leaf-24.png"));
	private ImageIcon openIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/root.png"));
	private ImageIcon nextPageIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/nextPage.png"));
	private ImageIcon previousIcon = new ImageIcon((URL)getClass().getResource("/Icons/streambrowser/previousPage.png"));
	
	//buttons for Iconbars
	private JButton addToStreamRipStarButton = new JButton("Add to StreamRipStar",saveIcon);	
	private JButton listenToButton = new JButton ("Hear it",hearMusicIcon);
	private JButton stopListeningToButton = new JButton ("Stop playing",stopHearMusicIcon);
	private JButton abortButton = new JButton("Abort",abortIcon);
	private JButton addAndRecButton = new JButton("Add and Record", addAndRecordIcon);
	private JButton filterButton = new JButton("Filter", filterIcon );
	private JButton nextPageButton = new JButton("Next Page", nextPageIcon );
	private JButton lastPageButton = new JButton("Previous Page", previousIcon );
	
	private JLabel pagesLabel = new JLabel("Page 0 of 0");
	
	//label for status
	private JLabel stautsLabel = new JLabel("");
	
	//popup menu
	private JPopupMenu selShowPopup = new JPopupMenu();
	private JPopupMenu tablePopup = new JPopupMenu();
	
	//for controling the columns
	private JCheckBoxMenuItem showDescriptionColumn = new JCheckBoxMenuItem("Show Description Column");
	private JCheckBoxMenuItem showPlayNowColumn = new JCheckBoxMenuItem("Show Now Playing Column");
	private JCheckBoxMenuItem showListenersColumn = new JCheckBoxMenuItem("Show Listener Column");
	private JCheckBoxMenuItem showBitrateColumn = new JCheckBoxMenuItem("Show Bitrate Column");
	private JCheckBoxMenuItem showTypeColumn = new JCheckBoxMenuItem("Show Type Column");
	
	//for controling the popup in the table with the streams
	private JMenuItem startRecordMenuItem = new JMenuItem("Start Recording (Save And Start)",startRecordIcon);
	private JMenuItem saveMenuItem = new JCheckBoxMenuItem("Save Stream",saveIconForPopUp);
	private JMenuItem hearMenuItem = new JMenuItem("Hear Stream In Media Player",hearMusicIconForPopUp);
	
	//is Column shown?
	private boolean[] isColumnShow = new boolean[6];
	// 0 boolean isIDShown = true 	MUST EVER BE TRUE
	// 1 boolean isNameShown = true;
	// 2 boolean isPlayNowShown = true;
	// 3 boolean isListenerShown = true;
	// 4 boolean isBitrateShown = true;
	// 5 boolean isTypeShown = true;
	
	private JSlider audioSlider = new JSlider();
	private Thread_GetStreams_FromShoutcast getStreams = null;
	
	//width of every column
	private int[] columnWidths = new int[6];

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
	
	//the genre we loaded at last
	private String selectedGenre = "";
	private boolean isKeyword = false;
	
	public Gui_StreamBrowser2(Gui_StreamRipStar StreamRipStar) {
		super("StreamBrowser");
		this.StreamRipStar = StreamRipStar;

		renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(openIcon);
		renderer.setClosedIcon(openIcon);
		renderer.setLeafIcon(leafIcon);
		browseTree.setCellRenderer(renderer);
		treeModel = new DefaultTreeModel(root);
		browseTree.setModel(treeModel);
		
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
	
	
	/**
	 * set tooltips to the buttons
	 */
	public void setToolTips() {
		listenToButton.setToolTipText(toolTips.getString("StreamBrowser.listen"));
		stopListeningToButton.setToolTipText(toolTips.getString("StreamBrowser.stopListening"));
		audioSlider.setToolTipText(toolTips.getString("StreamBrowser.audioSlider"));
		addToStreamRipStarButton.setToolTipText(toolTips.getString("StreamBrowser.addToStreamRipStar"));
		addAndRecButton.setToolTipText(toolTips.getString("StreamBrowser.addAndRecord"));
		abortButton.setToolTipText(toolTips.getString("StreamBrowser.abort"));
		filterButton.setToolTipText(toolTips.getString("StreamBrowser.filter"));
		lastPageButton.setToolTipText(toolTips.getString("StreamBrowser.lastPage"));
		nextPageButton.setToolTipText(toolTips.getString("StreamBrowser.nextPage"));
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
			stopListeningToButton.setText(trans.getString("iconPanel.stopListeningToButton"));
			addToStreamRipStarButton.setText(trans.getString("iconPanel.addToStreamRipStarButton"));
			addAndRecButton.setText(trans.getString("iconPanel.addAndRecButton"));
			abortButton.setText(trans.getString("iconPanel.abortButton"));
			filterButton.setText(trans.getString("iconPanel.filterButton"));
			lastPageButton.setText(trans.getString("iconPanel.lastPage"));
			nextPageButton.setText(trans.getString("iconPanel.nextPage"));
			
			if (newFont != null) {
				listenToButton.setFont(newFont);
				stopListeningToButton.setFont(newFont);
				addToStreamRipStarButton.setFont(newFont);
				addAndRecButton.setFont(newFont);
				abortButton.setFont(newFont);
				filterButton.setFont(newFont);
				lastPageButton.setFont(newFont);
				nextPageButton.setFont(newFont);
			}
		} else {
			listenToButton.setText(null);
			stopListeningToButton.setText(null);
			addToStreamRipStarButton.setText(null);
			addAndRecButton.setText(null);
			abortButton.setText(null);
			filterButton.setText(null);
			lastPageButton.setText(null);
			nextPageButton.setText(null);
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
		listenToButton.addActionListener( new PlayMusikListener() );
		stopListeningToButton.addActionListener( new StopPlayingMusikListener() );
		audioSlider.addChangeListener(new ValueumChangeListener());
		addToStreamRipStarButton.addActionListener( new AddToStreamRipStarListener() );
		abortButton.addActionListener(new AbortThreadsListener());
		addAndRecButton.addActionListener(new AddAndStartRecordListener());
		filterButton.addActionListener(new FilterListener());
		nextPageButton.addActionListener(new NextPageListener());
		lastPageButton.addActionListener(new LastPageListener());
		
		addWindowListener(this);
		
		//no extra space for the genretable
		splitView.setResizeWeight(0.0d);
		//set space for the genrepane
		splitView.setDividerLocation(dividerLocation);
		
		//add a listener to the streamslist on the left side
		browseTree.addMouseListener(new TreeListener());
		
		//at the beginning, the browser is not loading
		setAbortButtonEnable(false);
		
		//update the Page x of x accessible
		this.updatePageBar();
		
		
	}
	
	private void setLanguage() {
		try {
			//iconPanel
			listenToButton.setText(trans.getString("iconPanel.listenToButton"));
			stopListeningToButton.setText(trans.getString("iconPanel.stopListeningToButton"));
			addToStreamRipStarButton.setText(trans.getString("iconPanel.addToStreamRipStarButton"));
			addAndRecButton.setText(trans.getString("iconPanel.addAndRecButton"));
			abortButton.setText(trans.getString("iconPanel.abortButton"));
			filterButton.setText(trans.getString("iconPanel.filterButton"));
			nextPageButton.setText(trans.getString("iconPanel.nextPage"));
			lastPageButton.setText(trans.getString("iconPanel.lastPage"));
			pagesLabel.setText(trans.getString("iconPanel.page")+" 0 "+trans.getString("iconPanel.of")+ " 0");
			
			//popup - ColumControl
			showDescriptionColumn.setText(trans.getString("ColumControl.showDescriptionColumn"));
			showPlayNowColumn.setText(trans.getString("ColumControl.showPlayNowColumn"));
			showListenersColumn.setText(trans.getString("ColumControl.showListenersColumn"));
			showBitrateColumn.setText(trans.getString("ColumControl.showBitrateColumn"));
			showTypeColumn.setText(trans.getString("ColumControl.showTypeColumn"));
	
			//set Table Header
			browseHeader[0] = trans.getString("Header.Nr");
			browseHeader[1] = trans.getString("Header.Description");
			browseHeader[2] = trans.getString("Header.PlayingNow");
			browseHeader[3] = trans.getString("Header.Listeners");
			browseHeader[4] = trans.getString("Header.Bitrate");
			browseHeader[5] = trans.getString("Header.Type");
			browseModel.setColumnIdentifiers(browseHeader);
			
			//the table popup
			startRecordMenuItem.setText(trans.getString("popup.startRecStreamBrowser"));
			saveMenuItem.setText(trans.getString("popup.save"));
			hearMenuItem.setText(trans.getString("popup.hear"));
			
		} catch ( MissingResourceException e ) { 
		      SRSOutput.getInstance().logE( e.getMessage() ); 
		}
	}
	
	/**
	 * Create the tree, where all genres are listed. Get the genres
	 * from somewhere else
	 */
	public void createBrowseTree() {
		try {
				
			String[][] genres = new Shoutcast2().getGenres();
			
				//add an option to search for free values in Shoutcast
				root.add(new DefaultMutableTreeNode(trans.getObject("GetGenres.search")) );
				
				//create for every node with their leaves 
				for(int i=0; i < genres.length; i++) {
					if(genres[i].length > 0) {
						
						//create the node for the leaves 
						DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(genres[i][0]);
						
						//the the node the the root node
						root.add(tmp);
						
						//now create and add every leaves to the node
						for(int j=1; j < genres[i].length; j++) {
							tmp.add(new DefaultMutableTreeNode(genres[i][j]));
						}
					}
				}
				//update the view 
				updateUITree();
			}
		catch (NullPointerException e) {
			SRSOutput.getInstance().logE("Failed to load genres");
			setStatusText("Failed to load genres",false);
		}
	}
	
	public DefaultMutableTreeNode getTreeRoot() {
		return root;
	}
	
	public JTree getTree() {
		return browseTree;
	}
	
	public Control_http_Shoutcast_2  getControlHttp() {
		return controlHttp;
	}
	
	/**
	 * Show messages to the use
	 * @param statusText
	 */
	public void setStatusText(String statusText, boolean isErrorMessage) {
		if(!isErrorMessage) 
		{
			stautsLabel.setForeground(Color.BLACK);
		}
		else 
		{
			stautsLabel.setForeground(Color.RED);
		}
		stautsLabel.setText(statusText);
	}

	public synchronized void updateUITree() {
		browseTree.expandRow(0);
		treeModel.reload();
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
		nextPageButton.setEnabled(!enable);
		lastPageButton.setEnabled(!enable);
	}
	
	
	/**
	 * Build Iconbars and add a simple look
	 */
	public void buildIconBars() {
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
		
		//set a simple look
		listenToButton.setHorizontalTextPosition(SwingConstants.CENTER);
		listenToButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		listenToButton.setBorderPainted(false);
		stopListeningToButton.setHorizontalTextPosition(SwingConstants.CENTER);
		stopListeningToButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		stopListeningToButton.setBorderPainted(false);
		addToStreamRipStarButton.setHorizontalTextPosition(SwingConstants.CENTER);
		addToStreamRipStarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		addToStreamRipStarButton.setBorderPainted(false);
		addAndRecButton.setHorizontalTextPosition(SwingConstants.CENTER);
		addAndRecButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		addAndRecButton.setBorderPainted(false);
		abortButton.setHorizontalTextPosition(SwingConstants.CENTER);
		abortButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		abortButton.setBorderPainted(false);
		filterButton.setHorizontalTextPosition(SwingConstants.CENTER);
		filterButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		filterButton.setBorderPainted(false);
		nextPageButton.setHorizontalTextPosition(SwingConstants.CENTER);
		nextPageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		nextPageButton.setBorderPainted(false);
		lastPageButton.setHorizontalTextPosition(SwingConstants.CENTER);
		lastPageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		lastPageButton.setBorderPainted(false);
		
		//add Buttons to CommIconBar
		commonIconBar.add(addToStreamRipStarButton);
		commonIconBar.add(addAndRecButton);
		commonIconBar.addSeparator();
		commonIconBar.add(listenToButton);
		commonIconBar.add(stopListeningToButton);
		commonIconBar.add(audioSlider);
		commonIconBar.addSeparator();
		commonIconBar.add(abortButton);
		commonIconBar.add(filterButton);
		commonIconBar.addSeparator();
		commonIconBar.add(lastPageButton);
		commonIconBar.add(nextPageButton);	
		commonIconBar.add(pagesLabel);
		commonIconBar.addSeparator();
		
		//set the color of all button the backgroundcolor
		//from the iconbar and disable der Border
		commonIconBar.setBackground(new Color(238,238,238,255));
		
		//Build JPopupMenu
		selShowPopup.add(showDescriptionColumn);
		selShowPopup.add(showPlayNowColumn);
		selShowPopup.add(showListenersColumn);
		selShowPopup.add(showBitrateColumn);
		selShowPopup.add(showTypeColumn);
		
		tablePopup.add(startRecordMenuItem);
		tablePopup.add(saveMenuItem);
		tablePopup.add(hearMenuItem);
		
		//add Listerner
		showDescriptionColumn.addActionListener(new ShowRightColumnOrder(1));
		showPlayNowColumn.addActionListener(new ShowRightColumnOrder(2));
		showListenersColumn.addActionListener(new ShowRightColumnOrder(3));
		showBitrateColumn.addActionListener(new ShowRightColumnOrder(4));
		showTypeColumn.addActionListener(new ShowRightColumnOrder(5));
		
		startRecordMenuItem.addActionListener(new AddAndStartRecordListener());
		saveMenuItem.addActionListener( new AddToStreamRipStarListener() );
		hearMenuItem.addActionListener( new PlayMusikListener() );
		
		
		showDescriptionColumn.setSelected(isColumnShow[1]);
		showPlayNowColumn.setSelected(isColumnShow[2]);
		showListenersColumn.setSelected(isColumnShow[3]);
		showBitrateColumn.setSelected(isColumnShow[4]);
		showTypeColumn.setSelected(isColumnShow[5]);
	
	}

	/**
	 * removes recursive all cells from a model
	 * the defaultTableModel must be given on startup
	 * @param model
	 */
	public synchronized void removeAllFromTable(DefaultTableModel model) {
		int rowCount = model.getRowCount();
		for(int i=rowCount; i>0;i--)
			model.removeRow(i-1);
	}
	
	public Gui_StreamBrowser2 getMe() {
		return this;
	}
	

	/**
	 * look for:
	 * 	[0] = address
	 * 	[1] = name
	 * from the selected stream and return this String
	 * @return
	 */
	public String[] getStreamInfo() {
		String[] address = new String[2];
		
		//get selectet Streams
		if(browseTable.getSelectedRow() >= 0) {	
			Vector<String[]> streams = null;
				
			//get Nr. of Stream
			Object content = browseTable.getValueAt(browseTable.getSelectedRow(),0);
			int nr = Integer.valueOf(content.toString());
			
			//get Streams in Vector
			//if the stream is filtered, get the filtered stream vector
			//else the original
			if(filter.isFiltered()) {
				streams = filter.getFilteredStreamVector();
				SRSOutput.getInstance().log("Use Filtered StreamVector");
			} else {
				streams = controlHttp.getStreams();
				SRSOutput.getInstance().log("Use Normal StreamVector");
			}
			
			if(streams != null) {
				//save main address from site (e.g www.shoutcast.com)
				String url  = controlHttp.getBaseAddress()+streams.get(nr)[5];
				
				//get address from .pls file
				address[0] = controlHttp.getfirstStreamFromURL(url);
				address[1] = streams.get(nr)[0];
			} else {
				SRSOutput.getInstance().logE("Gets an empty FILTERED stream vector: can't get info");
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
			String name = getStreamInfo()[1];
			
			//if url = "" it fails
			if(url == null || url.equals("")) {
				JOptionPane.showMessageDialog(getMe()
						,trans.getString("Message.emptyString"));
				SRSOutput.getInstance().logE(trans.getString("Message.emptyString"));
			} else {
				StreamRipStar.getTabel().startMusicPlayerWithUrl(url, name);
				SRSOutput.getInstance().log(url);
			}
		} else
			JOptionPane.showMessageDialog(getMe()
					,trans.getString("select"));
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
						SRSOutput.getInstance().log( "Loading file Streambrowser.xml" ); 
						break; 
				 
				    case XMLStreamConstants.END_DOCUMENT: 
				    	SRSOutput.getInstance().log( "End of read settings " ); 
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
			SRSOutput.getInstance().logE("No configuartion file found: Streambrowser.xml");
		} catch (XMLStreamException e) {
			if(e.getMessage().startsWith("Message: Premature end of file.")) {
				SRSOutput.getInstance().logE("Error: Streambrowser: XML file had an unexpected end");
			} else {
				SRSOutput.getInstance().logE("Error while reading the streambrowser settings");
			}
		}
	}
	
	public void setThreadToNull() {
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
		try {
			for(int i=0; i<columnWidths.length; i++) {
				if( isColumnShow[i] ) {
					browseTable.getColumn(browseHeader[i])
						.setPreferredWidth(columnWidths[i]);
				}
			}
			browseTable.getColumn(browseHeader[0]).setMaxWidth(50);
		} catch (IllegalArgumentException e) {
			for(int i=0; i<columnWidths.length; i++) {
				isColumnShow[i] = true;
			}
		}
	}
	
	/**
	 * save the columns width into the array columnWidths
	 * and run methode save() to save it into file
	 */
	public void saveColumnWidth() {
		try {
			//get the widths of any cell that is shown
			
			for(int i=0; i < isColumnShow.length ;i++) {
				if(isColumnShow[i]) {
					columnWidths[i] = browseTable.getColumn(browseHeader[i]).getWidth();
				}
			}
		} catch (IllegalArgumentException e) {
			//fallback values will activate
			for(int i=0; i < isColumnShow.length ;i++) {
				isColumnShow[i] = true;
				columnWidths[i] = 50;
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
					//play selected stream in media player
					playStream();
				}
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
	class PlayMusikListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			playStream();
		}
	}
	
	
	/**
	 * Stop the internal player playing music
	 */
	class StopPlayingMusikListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			StreamRipStar.getTabel().stopInternalAudioPlayer();
		}
	}
	
	class AbortThreadsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			abortLoadingGenresAndStreams();
		}	
	}
	
	/**
	 * Stops the threads for loading streams and genres in the table
	 */
	public void abortLoadingGenresAndStreams() {
		if(getStreams != null) {
			getStreams.killMe();
			getStreams = null;
		}
	}
	
	/**
	 * Reloads the genres from the website. Before fill the generes
	 * in the table, delete all old geners
	 *  
	 * @author Johannes Putzke
	 */
	class ReloadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//stop all loading Threads for the table and the tree
			abortLoadingGenresAndStreams();
			
			//delete all old tree nodes
			root.removeAllChildren();
			Gui_StreamBrowser2.this.updateUITree();
			
			//create the new one
			createBrowseTree();
		}
	}
	
	/**
	 * Toogle the filterbar when is filterbar button is clicked
	 * 
	 * @author Johannes Putzke
	 */
	class FilterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(filter.isVisible())
				filter.setVisible(false);
			else
				filter.setVisible(true);
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
					SRSOutput.getInstance().logE("Error while fetching streamaddress\n");
				}
				else {
					Gui_StreamOptions dialog = new Gui_StreamOptions(null,StreamRipStar,true,true,false);
					dialog.setBasics(content);
				}
			} else
				//if nothing is selected, open popup
				JOptionPane.showMessageDialog(getMe()
						,trans.getString("select"));
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
					JOptionPane.showMessageDialog(getMe(), trans.getString("Message.emptyString"));
					SRSOutput.getInstance().logE("Error while fetching streamaddress\n");
				}
				else {
					Gui_StreamOptions tmpAddStream = new Gui_StreamOptions(
							null,StreamRipStar, true, false,false);
					tmpAddStream.setBasics(content);
					tmpAddStream.save();
					Stream tmpStream = tmpAddStream.getStream();
					if(tmpStream != null) {
						StreamRipStar.startRippingUnselected(tmpStream);
						SRSOutput.getInstance().log("Debug: Adding stream from Streambrowser" +
								"and start recording");
					}
				}
			} else
				//if nothing is selected, open popup
				JOptionPane.showMessageDialog(getMe()
						,trans.getString("select"));
		}
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
	 * activate/deactivate and updates the information for the pages
	 * in the status par
	 */
	public void updatePageBar() {
		//update the amount of pages
		pagesLabel.setText(trans.getString("iconPanel.page")+ " " +controlHttp.getCurrentPage()+ " " 
				+trans.getString("iconPanel.of")+ " " + controlHttp.getTotalPages());
		
		//activate the correct one
		if(controlHttp.getCurrentPage() <= 1) {
			lastPageButton.setEnabled(false);
		} else {
			lastPageButton.setEnabled(true);
		}
		
		if(controlHttp.getCurrentPage() >= controlHttp.getTotalPages()) {
			nextPageButton.setEnabled(false);
		} else {
			nextPageButton.setEnabled(true);
		}
	}
	
	/**
	 * Loads the last page from the website for this results
	 */
	public class LastPageListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//if the last page is shown, do not load the next last page
			if(controlHttp.getCurrentPage() <= 1) {
				SRSOutput.getInstance().log("Can load the previous page, because the current page is the first one");
				setStatusText("Can load the previous page, because the current page is the first one",false);
			} else {
				getStreams = new Thread_GetStreams_FromShoutcast(getMe(),selectedGenre.replace(" ", "%20"),trans,isKeyword,false,true);
	    		getStreams.start();
			}
		}
	}
	
	/**
	 * loads the next page for this results from the website
	 * 
	 */
	public class NextPageListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			
			//if the last page is shown, do not load the next page
			if(controlHttp.getCurrentPage() >= controlHttp.getTotalPages()) {
				SRSOutput.getInstance().log("Can load the next page, because the current page is the last one");
				setStatusText("Can load the next page, because the current page is the last one",false);
			} else {
				getStreams = new Thread_GetStreams_FromShoutcast(getMe(),selectedGenre.replace(" ", "%20"),trans,isKeyword,true,false);
	    		getStreams.start();
			}
		}
	}
	
	/**
	 * get the volum control from the streambrowser
	 */
	public JSlider getAudioSlider() {
		return audioSlider;
	}
	
	/**
	 * This Listener looks for a click an the JTree
	 * and execute the command to fill the table
	 * streams
	 */
	public class TreeListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e){
			boolean stop = false;
			
			//get selected path and look for last element
			if(e.getClickCount() == 2 && !disableClick) {
				if(browseTree.getSelectionPath() != null) {
					selectedGenre = browseTree.getSelectionPath()
						.getLastPathComponent().toString();

					if(selectedGenre.equals(trans.getObject("GetGenres.search"))) {
						selectedGenre =	JOptionPane.showInputDialog(getMe(),
								trans.getString("Message.search"),
								trans.getString("Message.searchHeader"),
                                JOptionPane.PLAIN_MESSAGE);
						if(selectedGenre == null) {
							stop = true;
						} else {
							isKeyword = true;
							getStreams = new Thread_GetStreams_FromShoutcast(getMe(),selectedGenre.replace(" ", "%20"),trans,isKeyword,false,false);
				    		getStreams.start();
						}

					} else {
				    	//fill table with streams
				    	if(!stop) {
				    		isKeyword = false;
				    		getStreams = new Thread_GetStreams_FromShoutcast(getMe(),selectedGenre.replace(" ", "%20"),trans, isKeyword,false,false);
				    		getStreams.start();
				    	}
					}
				}
			}
		}
	}
	
	/**
	 * Is called, when the value is changened from the intern audio player
	 *
	 */
	public class ValueumChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			StreamRipStar.setVolume(audioSlider.getValue());
		} 
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {

		if(getStreams != null) {
			getStreams.killMe();
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
