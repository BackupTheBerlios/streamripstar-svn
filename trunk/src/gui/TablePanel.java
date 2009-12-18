package StreamRipStar;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Gui_TablePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private Object[] tableheader ={"Status","Stream","Aktual Title"};
	private String[][] Daten = {};
	private DefaultTableModel model = new DefaultTableModel(Daten,tableheader)
		{private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int rowIndex, int columnIndex){return false;}
		@Override
		public Class<?> getColumnClass(int col) {
			return col == 0 ? ImageIcon.class : Object.class;
		}};
	private Control_Stream controlStreams;
	private JTTable table = new JTTable(model);  
	private Gui_StreamRipStar mainGui = null;
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private ImageIcon recordIcon = new ImageIcon((URL)getClass().getResource("/Icons/record_middle.png"));
	
	private JPopupMenu popup;
	
	public Gui_TablePanel(Control_Stream controlStreams,Gui_StreamRipStar mainGui) {
		this.controlStreams = controlStreams;
		this.mainGui = mainGui;

		setLayout(new BorderLayout());
    
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        setSize(new Dimension(650,400));

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.changeSelection(0,0,true,false );
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new CellMouseListener());
		//set the width for all headers
		table.getColumn(tableheader[0]).setMaxWidth(50);
		table.getTableHeader().setReorderingAllowed(false);
			
		
		
		setLanguage(); //Must be at last, because the the identifier changes
		
		DropTargetListener dropTargetListener = new DropTargetListener()
		{
			public void dragEnter(DropTargetDragEvent e) {}
			public void dragExit(DropTargetEvent e) {}
			public void dragOver(DropTargetDragEvent e) {}
			public void drop(DropTargetDropEvent e) {
				try {
					Transferable tr = e.getTransferable();
					DataFlavor[] flavors = tr.getTransferDataFlavors();
					for (int i = 0; i < flavors.length; i++) {
						
						//for Windows: tested with Windows XP 
						if(flavors[i].isFlavorJavaFileListType()) {
							e.acceptDrop (DnDConstants.ACTION_COPY);
							List<?> files = (List<?>) tr.getTransferData(flavors[i]);
							String url = files.get(0).toString();
							
							if(url.endsWith(".pls") || url.endsWith(".m3u")) {
								System.out.println("Right extension");
								new Gui_Import(getMainGui(),getControlStreams(), url);
							} else {
								System.out.println("Wrong extension");
							}
							e.dropComplete(true);
							return;
						}
						
						//for Linux: tested with KDE 3 +4
						if(flavors[i].isFlavorTextType()) {
							String tmp ="";
							e.acceptDrop (DnDConstants.ACTION_COPY);
							Vector<InputStreamReader> x = new Vector<InputStreamReader>(0,1);
							x.add(((InputStreamReader)tr.getTransferData(flavors[i])));
							BufferedReader br = new BufferedReader(x.get(0));
							while(( tmp = br.readLine()) != null) {
								URI fileURI = new URI(tmp);
								String url = URLDecoder.decode(fileURI.getPath(),"UTF-8");
								if(url.endsWith(".pls") || url.endsWith(".m3u")) {
									System.out.println("Right extension");
									new Gui_Import(getMainGui(),getControlStreams() ,url);
								} else {
									System.out.println("Wrong extension");
								}

							}
							br.close(); 
							e.dropComplete(true);
							return;
						}
					}
				}
				catch (Throwable t) { t.printStackTrace(); }
				// Ein Problem ist aufgetreten
				e.rejectDrop();
			}
			public void dropActionChanged( DropTargetDragEvent e) {}
		};
		
		new DropTarget(table,dropTargetListener);
		new DropTarget(this,dropTargetListener);
		table.getRowSorter().toggleSortOrder(1);
	}
	
	private void setLanguage() {
		try 
		{
			tableheader[0] = trans.getString("status");
			tableheader[1] = trans.getString("streamname");
			tableheader[2] = trans.getString("curTitle");
			model.setColumnIdentifiers(tableheader);
		}
		
		catch ( MissingResourceException e ) { 
			      System.err.println( e ); 
		}
	}

	//gets the width of the "name" and "title" column
	public int[] getColumnWidths(){
		int[] widths = new int[3];
		widths[2] = table.getColumn(table.getColumnName(0)).getWidth();
		widths[0] = table.getColumn(table.getColumnName(1)).getWidth();
		widths[1] = table.getColumn(table.getColumnName(2)).getWidth();

		return widths;
	}

	//set the width of "name" and "title" column
	public void setColumWidths(int[] widths)
	{
		table.getColumn(table.getColumnName(1)).setPreferredWidth(widths[0]);
		table.getColumn(table.getColumnName(2)).setPreferredWidth(widths[1]);
		table.getColumn(table.getColumnName(0)).setPreferredWidth(widths[2]);
	}
	
	/**
	 * removes the selected row from the table
	 */
	public void removeStreamfromTable() {
		model.removeRow(table.convertRowIndexToModel(
				table.getSelectedRow()));
	}
	
	
	public void fillTableWithStreams() {
		Vector<Stream> tmp = controlStreams.getStreamVector();
		for(int i=0; i < tmp.capacity() ;i++) {
			model.addRow(tmp.get(i).getBase());
		}
	}
	
	public void addLastStreamFromVector()
	{
		Vector<Stream> tmp = controlStreams.getStreamVector();
		int length = tmp.capacity();
		model.addRow(tmp.get(length-1).getBase());
	}
	
//	This method adds Data to the table
	public void addData(Object[] data){
		model.addRow(data);
	}
	
	/**
	 * Get the id in xml-file of the selected stream.
	 * @return Integer of index in xml-file
	 */
	public int getSelectedStreamID() {
		int id = 0;
		id = table.convertRowIndexToModel(table.getSelectedRow());
		id = controlStreams.getStreamVector().get(id).id;
		return id;
	}
	
	/**
	 * look for the id of the selected stream and
	 * search with this id in all streams for the right one.
	 * @return: the selected stream in table
	 */
	public Stream getSelectedStream() {
		int id = table.convertRowIndexToModel(table.getSelectedRow());
		return controlStreams.getStreamVector().get(id);
	}
	
	/**
	 * Look in der StreamVector, witch collects all streams
	 * for an stream with the given ID
	 * @param id: the ID for the stream in xml-file
	 * @return The Stream with the given ID
	 */
	public Stream getStreamByID(int id) {
		Vector<Stream> x = controlStreams.getStreamVector();
		for(int i=0; i< x.capacity(); i++) {
			if(x.get(i).id == id) {
				return x.get(i);
			}
		}
		return null;
	}
	
	public String getSelectedName()
	{
		if( table.getSelectedRow() < 0 )
			return null;
		Object content = table.getValueAt(table.getSelectedRow(),1);
		if(content != null)
		{
			return content.toString();
		}
		else
			return null;
	}
	
	/**
	 * Looks, if something in the table is selected
	 * @return: true, if something is selected
	 */
	public boolean isTHSelected() {
		if( table.getSelectedRow() < 0 ) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * 
	 * @param track	- will be displayed als title (including error
	 * @param row - the appropriate row
	 * @param rec - if true, set status "Rec."; else ""
	 */
	public void setSelectedCurrentNameCellAndTitle(String track, int row,boolean rec)	{
		table.setValueAt(track,row ,2);
		if(rec==true)
			table.setValueAt(recordIcon,row ,0);
		else
			table.setValueAt(null,row ,0);
	}

	public void setNameValue(String newName,int row){
		table.setValueAt(newName, row, 1);
	}
	
	public void setNameValueWithConvert(String newName,int row){
		table.setValueAt(newName, table.convertRowIndexToView(row), 1);
	}
	
	public int getSelectedRow(){
		return table.getSelectedRow();
	}
	
	public String getNameFromRow(int row){
		return (table.getValueAt(row, 1).toString());
	}
	
	public String getNameFromRowForUpdate(int row){
		return (model.getValueAt(row, 1).toString());
	}
	
	public DefaultTableModel getModel(){
		return model;
	}
	
//	search for the line with the name name
	public int getNewRowForName(String vName){
		for(int i=0;i< model.getRowCount();i++){
			if(model.getValueAt(i, 1).toString().equals(vName))
				return i;
		}
		return 0;
	}
	
	public int getNewRowForNameForUpdate(String vName)
	{
		for(int i=0;i< model.getRowCount();i++)
		{
			if(table.getValueAt(i, 1).toString().equals(vName))
				return i;
		}
		return 0;
	}
	
	public void startMusikPlayer() {
		Stream stream = getSelectedStream();
		//Test if a relay stream is running and connect to them
		//else will connect to stream address directly 
		if(stream.getStatus() && stream.connectToRelayCB) {
			System.out.println("Debug: Running relay stream found. Connecting to...: \"http://127.0.0.1:"+stream.relayServerPortTF);
			controlStreams.startMp3Player("http://127.0.0.1:"+stream.relayServerPortTF);
			
		} else if(stream.address != null  && !stream.address.equals("")) {
			System.out.println("Debug: No relaystream found. Connecting to Internet");
			controlStreams.startMp3Player(stream.address);
			
		} else {
			System.err.println("error while fetching adress");
		}
	}
	
	public Gui_StreamRipStar getMainGui() {
		return mainGui;
	}
	
	public Control_Stream getControlStreams() {
		return controlStreams;
	}
	
	/**
	 * the method looks in the settings what to do
	 * when a cell was double clicked. It provides
	 * all thinks to start
	 */
	public void startAction() {
		int action = 0;
		int selCol = table.getSelectedColumn();
		
		//get and set action for the first Column
		//witch shows the state of a stream
		
		//Column: status
		if(selCol==0) {
			action = mainGui.getAction(0);
		}
		
		//Column: name
		if(selCol==1) {
			action = mainGui.getAction(1);
		}
		
		//column: current track
		if(selCol==2) {
			action = mainGui.getAction(2);
		}
		
		//0 is doing nothing
		if (action != 0 ) {
			Stream stream = getSelectedStream();
			if (stream != null) {	
				//action = 1: Open Webbrowser
				if(action == 1 ) {
					if(stream.website != null  && !stream.website.equals("")) {
						controlStreams.startWebBrowser(stream.website);
					} else
						JOptionPane.showMessageDialog(mainGui,
								trans.getString("setWebsite"));
				}
				
				//action = 2: edit Stream
				if(action == 2) {
					mainGui.editStream();
				}
				
				//action = 3: switching between start record and stop record
				if (action == 3) {
					//if ripping -> stop it
					if(stream.getStatus())
						mainGui.stopRippingSelected();
					//else -> start ripping
					else
						mainGui.startRippingSelected();
				}
				
				//action = 5: play stream in media player
				if (action == 4) {
					startMusikPlayer();
				}
			}
			else
				System.err.println("can't find stream in vector");
		}
	}

	
//	Listener
	public class CellMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()==2) {
				startAction();
			}
		}
		
		public void mousePressed(MouseEvent e){
			if(e.getButton() == MouseEvent.BUTTON3) {
				int row = table.rowAtPoint(e.getPoint());
				
				//only selection was a row
				if(row >= 0) {
					table.setRowSelectionInterval(row,row);
					popup.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
				}
			}
		}
	}
	
	public void setTTablePopup(JPopupMenu popup) {
		this.popup = popup;
	}
}

