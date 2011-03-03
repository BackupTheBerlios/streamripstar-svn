package gui;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import misc.SchedulJob;

import thread.Thread_Control_Schedul;
import thread.Thread_KeepTableSchedulUpdated;


import control.Control_Stream;
import control.SRSOutput;

public class Gui_SchedulManager extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private ImageIcon addIcon = new ImageIcon((URL)getClass().getResource("/Icons/add_small.png"));
	private ImageIcon editIcon = new ImageIcon((URL)getClass().getResource("/Icons/edit_small.png"));
	private ImageIcon deleteIcon = new ImageIcon((URL)getClass().getResource("/Icons/delete_small.png"));
	private ImageIcon closeIcon = new ImageIcon((URL)getClass().getResource("/Icons/ok_small.png"));
	
	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	private JButton addButton = new JButton("Add Task",addIcon);
	private JButton editButton = new JButton("Edit Task",editIcon);
	private JButton removeButton = new JButton("Remove Task",deleteIcon);
	private JButton closeButton = new JButton("Close",closeIcon);

	private Object[][] allData = {};
	
	private Object[] schedulHeader = {"ID","StreamID","Enabled","Stream Name",  "Start Time",
			"End Time","Comment"};
	
	private JPopupMenu popup = new JPopupMenu();
	private JMenuItem addItem = new JMenuItem("Add Schedule Job");
	private JMenuItem removeItem = new JMenuItem("Remove Schedule Job");
	private JMenuItem editItem = new JMenuItem("Edit Schedul Job");
	
	private DefaultTableModel model = new DefaultTableModel(allData,schedulHeader) {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int rowIndex, int columnIndex){return false;}
		@Override
        public Class<?> getColumnClass( int column ) {
            switch( column ){
                case 0: return Integer.class;
                case 2: return Boolean.class;
                case 1: return Integer.class;
                default: return String.class;
            }
        }
	};
    
	private Thread_KeepTableSchedulUpdated keepUpdate;
	
	private Gui_JTTable table = new Gui_JTTable(model);
	private JScrollPane scrollPane  = new JScrollPane(table);
	private Control_Stream controlStreams = null;
	private Thread_Control_Schedul controlJob = null;
	
	
	public Gui_SchedulManager(Control_Stream controlStreams, Thread_Control_Schedul controlJob) {
		super("Schedul Manager");
		this.controlStreams = controlStreams;
		this.controlJob = controlJob;
		
		//update language
		setLanguage();
		
		//add windowlistener
		addWindowListener(this);

		//set Layouts
		setLayout(new BorderLayout());
		tablePanel.setLayout(new GridBagLayout());
		buttonPanel.setLayout(new GridBagLayout());
		
		//add panels
		add(tablePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		//set Constrains defaults
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 7, 7, 7, 7);
		
		//add the popup to the table
		popup.add(addItem);
		popup.add(editItem);
		popup.add(removeItem);
		
		
		//add the button to the button panel
		c.weightx = 0.0;
		c.gridy = 0;
		c.gridx = 0;
		buttonPanel.add(addButton,c);
		c.gridx = 1;
		buttonPanel.add(editButton,c);
		c.gridx = 2;
		buttonPanel.add(removeButton,c);
		c.weightx = 1.0;
		c.gridx = 3;
		buttonPanel.add(new JLabel(" "),c);
		c.weightx = 0.0;
		c.gridx = 4;
		buttonPanel.add(closeButton,c);
		
		//add table
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridy = 0;
		c.gridx = 0;
		tablePanel.add(scrollPane,c);
		
		//resize next column only
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		//not allow moving the column, because in column[0]
		//streamRipStar expect the nr. 
		table.getTableHeader().setReorderingAllowed(false);
		//autosorter for rows
		table.setAutoCreateRowSorter(true);
		//set the ID-Fields invisible
		table.getColumn(schedulHeader[0]).setMinWidth(0);
		table.getColumn(schedulHeader[0]).setMaxWidth(0);
		table.getColumn(schedulHeader[1]).setMinWidth(0);
		table.getColumn(schedulHeader[1]).setMaxWidth(0);
		
		//add Listeners
		addButton.addActionListener(new AddListener());
		editButton.addActionListener(new EditListener());
		removeButton.addActionListener(new RemoveListener());
		closeButton.addActionListener(new CloseListener());
		
		addItem.addActionListener(new AddListener());
		editItem.addActionListener(new EditListener());
		removeItem.addActionListener(new RemoveListener());
		table.addMouseListener(new CellMouseListener());
		scrollPane.addMouseListener(new CellMouseListener());
		
		//graphical
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        //get resolution
//        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
//        //calculates the app. values
//        int x = (screenDim.width - Integer.valueOf(width))/2;
//        int y = (screenDim.height - Integer.valueOf(high))/2;
//        //set location
//        setLocation(x, y);
//		//setSize
//        setSize(new Dimension(Integer.valueOf(width),Integer.valueOf(high)));
//        //make visible
//		setVisible(true);
		
		 Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		pack();
		int x = (screenDim.width - Integer.valueOf(getSize().width))/2;
		int y = (screenDim.height - Integer.valueOf(getSize().height))/2;
		//set location
		setLocation(x, y);
		//make visible
		setVisible(true);
		
		fillTableWithSchedulJobs();
		keepUpdate = new Thread_KeepTableSchedulUpdated(this,controlJob);
		keepUpdate.start();
	}
	
	/**
	 * updates all texts with a new language
	 */
	public void setLanguage() {
		try {
			//for buttons
			addButton.setText(trans.getString("JobMan.AddButton"));
			editButton.setText(trans.getString("JobMan.editButton"));
			removeButton.setText(trans.getString("JobMan.removeButton"));
			
			//for the MenuItems
			addItem.setText(trans.getString("JobMan.AddButton"));
			editItem.setText(trans.getString("JobMan.editButton"));
			removeItem.setText(trans.getString("JobMan.removeButton"));
			
			//for table header
			schedulHeader[2] = trans.getString("JobMan.table.enable");
			schedulHeader[3] = trans.getString("JobMan.table.name");
			schedulHeader[4] = trans.getString("JobMan.table.start");
			schedulHeader[5] = trans.getString("JobMan.table.end");
			schedulHeader[6] = trans.getString("JobMan.table.comment");
			model.setColumnIdentifiers(schedulHeader);
		} catch ( MissingResourceException e ) { 
		      SRSOutput.getInstance().logE( e.getMessage() ); 
		}
	}
	
	/**
	 * Add an new Job with all data to the table
	 * @param job the new job
	 */
	public void addSchedulJobToTable(SchedulJob job) {
		try {
			Object[] x = new Object[7];
			x[0] = job.getSchedulID();
			x[1] = job.getStreamID();
			x[2] = job.isJobenabled() ;
			x[3] = controlStreams.getStreamByID(job.getStreamID()).name;
			//only show the time, if this is not a Job, which starts at the start of StreamRipStar
			if(job.getJobCount() != 3)
			{
				x[4] = job.getStartTimeAsLocaleTime();
				x[5] = job.getStopTimeAsLocaleTime();
			} else {
				x[4] = "At Start";
				x[5] = "Never";
			}
			x[6] = job.getComment();
			
			model.addRow(x);
		} catch (NullPointerException e) {
			SRSOutput.getInstance().logE("Corrupt schedulJob found. ignore it");
		}
	}

	
	/**
	 * 
	 *@return the this object
	 */
	public Gui_SchedulManager getMe() {
		return this;
	}
	
	/**
	 * Fill the table on start with all jobs witch are 
	 * in the vector of scheduljobs in control_scheduls
	 */
	public void fillTableWithSchedulJobs() {
		Vector<SchedulJob> schedulVector = controlJob.getScheduleVector();
		for(int i=0; i < schedulVector.capacity(); i++) {
			addSchedulJobToTable(schedulVector.get(i));
		}
	}
	

	
	/**
	 * updates the shown values in the table
	 * @param job: the table entry with should be updated
	 */
	public void updateTable(SchedulJob job)
	{
		int row = -1;
		//look where job is in table
		for(int i=0; i < table.getRowCount();i++)
		{
			int x = Integer.valueOf(table.getValueAt(table.convertRowIndexToModel(i), 0).toString());
			
			if(job.getSchedulID() == x)
			{
				row = table.convertRowIndexToModel(i);
				break;
			}
		}
		
		//update status
		table.setValueAt(job.isJobenabled(), row, 2);
		//update Name
		table.setValueAt(controlStreams.getStreamByID(job.getStreamID()).name, row, 3);
		
		//only show the time, if this is not a Job, which starts at the start of StreamRipStar
		if(job.getJobCount() != 3)
		{
			//update startTime
			table.setValueAt(job.getStartTimeAsLocaleTime(), row, 4);
			//update stopTime
			table.setValueAt(job.getStopTimeAsLocaleTime(), row, 5);
			//update comment
		}
		table.setValueAt(job.getComment(), row, 6);
	}
	
	/**
	 * this listener open a new Gui_addschedul window
	 * with empty fields
	 */
	public class AddListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			new Gui_AddSchedul(getMe(),true, controlStreams, controlJob, null);
		}
	}
	
	/**
	 * Edits an existing stream. For this, it look at the table,
	 * select the selected row and look in the first (hidden) column
	 * for the schedul ID. With this id, it gets the schedul job and
	 * start the edit gui.
	 */
	public class EditListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			editSchedulJob();
		}
	}
	
	private void editSchedulJob() {
		if(table.getSelectedRow() >= 0 ) {
			int x = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
			new Gui_AddSchedul(getMe(),false, controlStreams,
					controlJob,controlJob.getSchedulJobByID(x));
		}
	}
	
	/**
	 * removes all jobs from table and fill new with
	 * all jobs from the jobs vector
	 *
	 */
	public void updateTable() {
		for(int i=table.getRowCount(); i > 0 ; i--) {
			model.removeRow(i-1);
		}
		fillTableWithSchedulJobs();
	}
	
	/**
	 * Get the selected number from the table
	 * @return the selected line as int, -1 if nothing is selected
	 */
	public int getSelectedRowNumber()
	{
		return table.getSelectedRow();
	}
	
	public void setSelectedRow(int row)
	{
		if(table.getRowCount() > row)
		{
			table.setRowSelectionInterval(row, row);
		}
	}
	
	
	/**
	 * removes a stream from table, vector and from the xml-file
	 *
	 */
	public class RemoveListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			if(table.getSelectedRowCount() > 0) {
				int id = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
				
				//quest the user to remove the the job
				int i = JOptionPane.showConfirmDialog(getMe(),
						trans.getString("JobMan.requestDelete"),
						trans.getString("JobMan.reallyDelete"),JOptionPane.YES_NO_OPTION);
				
				//if yes...
				if (i == 0) {
					//if you waited a long time with the dialog, it
					//may happen, that the stream doesn't exist anymore.->check it
					if(controlJob.jobStillExist(id)) {
						//remove the selected row
						int selRow = table.convertRowIndexToModel(table.getSelectedRow());
						model.removeRow(selRow);
						
						//remove from Vector<scheduljob>
						controlJob.removeJobFromVector(id);
						
						//remove from xml-file
						controlJob.saveScheduleVector();
					} else {
						JOptionPane.showMessageDialog(getMe(),trans.getString("jobDoesntExistAnymore"));
					}
				}
			}
		}
	}
	
	public void windowClosing (WindowEvent e){
		if(keepUpdate != null)
			keepUpdate.stopThread();
		dispose();
	}
	public void windowClosed (WindowEvent e) { }
	public void windowOpened (WindowEvent e) { }
	public void windowIconified (WindowEvent e) { }
	public void windowDeiconified (WindowEvent e) { }
	public void windowActivated (WindowEvent e) { }
	public void windowDeactivated (WindowEvent e) { }

//	Listener
	public class CellMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()==2) {
				
				//if it was the left mouse button
				if(e.getButton() == MouseEvent.BUTTON1) {
					int row = table.getSelectedRow();
					
					//only open the dialog, if a row is selected
					if (row > -1) {
						editSchedulJob();
					}
				}
			}
		}
		
		public void mousePressed(MouseEvent e){
			
			//if the source was the right mouse button...
			if(e.getButton() == MouseEvent.BUTTON3) {
				int row = table.rowAtPoint(e.getPoint());
				
				//if a row is selected, show the popup with all options
				if(row >= 0) {
					//select the row
					table.setRowSelectionInterval(row,row);
					editItem.setEnabled(true);
					removeItem.setEnabled(true);
					popup.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
				}
				
				//else disable the unusable components
				else {
					editItem.setEnabled(false);
					removeItem.setEnabled(false);
					popup.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
				}
			}
		}
	}
	/**
	 * Closes the windows
	 *
	 */
	public class CloseListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Gui_SchedulManager.this.dispose();
		}
	}
	
}
