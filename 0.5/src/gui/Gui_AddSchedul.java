package gui;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DateFormatter;

import misc.SchedulJob;
import misc.Stream;

import thread.Thread_Control_Schedul;


import control.Control_Stream;

public class Gui_AddSchedul extends JDialog implements WindowListener{
	private static final long serialVersionUID = 1L;

	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private ImageIcon abortIcon = new ImageIcon((URL)getClass().getResource("/Icons/abort_small.png"));
	private ImageIcon addIcon = new ImageIcon((URL)getClass().getResource("/Icons/ok_small.png"));
	
	private int width = 300;
	private int high = 350;
	
	private JPanel datePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	private JFormattedTextField startTimeTF = null;
	private JFormattedTextField stopTimeTF = null;
	private JFormattedTextField startDateTF = null;
	private JFormattedTextField stopDateTF = null;
	
	private JTextField commentTF = new JTextField();
	
	private JButton addButton = new JButton("OK",addIcon);
	private JButton abortButton = new JButton("Abort",abortIcon);
	
	private JLabel startTimeLabel = new JLabel("Start Time:  At");
	private JLabel stopTimeLabel = new JLabel ("Stop Time:   At");
	private JLabel atStartTime = new JLabel("  At");
	private JLabel atStopTime = new JLabel("  At");
	private JLabel commentLabel = new JLabel("Comment");
	
	private JCheckBox enableCB = new JCheckBox("Enable this job",true);
	private JCheckBox onceCB = new JCheckBox("once",true);
	private JCheckBox dailyCB = new JCheckBox("daily");
	private JCheckBox weeklyCB = new JCheckBox("weekly");
	
	private JComboBox nameBox;
	private String[] streamNames;
	
	private Vector<Stream> streams;
	private boolean createNew = false;
	private SchedulJob oldJob = null; 
	private Thread_Control_Schedul controlJob = null;
	private Gui_SchedulManager schedulManager = null;
	
	
	
	/**
	 * Contructor for an existing stream. Expect a full configured job for getting all fields
	 * @param createNew: should I create a new stream?
	 * @param controlStreams: the object that controlls all controll streams
	 * @param controlDB: object to control the xml-file access
	 * @param oldJob: the job witch should be updated
	 */
	public Gui_AddSchedul(Gui_SchedulManager schedulManager, boolean createNew,
			Control_Stream controlStreams, Thread_Control_Schedul controlJob, SchedulJob oldJob)
	{
		super(schedulManager,"Add SchedulJob");
		this.schedulManager = schedulManager;
		this.createNew = createNew;
		this.controlJob = controlJob;
		this.oldJob = oldJob;
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		//set new language
		setLanguage();
		
		addWindowListener(this);

		// only one state of HOWOFTEN can be selected (logical)
		ButtonGroup howOftenGroup = new ButtonGroup();
		howOftenGroup.add(onceCB);
		howOftenGroup.add(dailyCB);
		howOftenGroup.add(weeklyCB);
		//if this is a new job, enter time from now
		if(createNew) {
			
			Calendar now = Calendar.getInstance();
			DateFormat df = DateFormat.getDateInstance (DateFormat.SHORT);
			startDateTF = new JFormattedTextField(new DateFormatter(df));
			startDateTF.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(now.getTime()));
			
			DateFormat df2 = DateFormat.getDateInstance (DateFormat.SHORT);
			stopDateTF = new JFormattedTextField(new DateFormatter(df2));
			stopDateTF.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(now.getTime()));
			
			DateFormat df3 = DateFormat.getTimeInstance (DateFormat.SHORT);
			startTimeTF = new JFormattedTextField(new DateFormatter(df3));
			startTimeTF.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(now.getTime()));
			
			DateFormat df4 = DateFormat.getTimeInstance (DateFormat.SHORT);
			stopTimeTF = new JFormattedTextField(new DateFormatter(df4));
			stopTimeTF.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(now.getTime()));
			
			// create combobox with all streamNames in it
			streams = controlStreams.getStreamVector();
			//how many names do we have?
			streamNames = new String[streams.capacity()];
			// copy every name into box
			for(int i=0; i < streams.capacity(); i++) {
				streamNames[i] = streams.get(i).name;
			}
			
			//finally create nameBox
			nameBox = new JComboBox(streamNames);
			
			
		} else {
			//how often should be recorded?
			int howOften = oldJob.getJobCount();
			if(howOften == 0) {
				dailyCB.setSelected(true);
			} else if (howOften == 1) {
				weeklyCB.setSelected(true);
			}
			
			//set comment
			commentTF.setText(oldJob.getComment());

			//the set right time
			Calendar now = Calendar.getInstance();
		//set Start Time
			now.setTimeInMillis(oldJob.getStartTime());
			
			DateFormat df = DateFormat.getDateInstance (DateFormat.SHORT);
			startDateTF = new JFormattedTextField(new DateFormatter(df));
			startDateTF.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(now.getTime()));
			
			DateFormat df3 = DateFormat.getTimeInstance (DateFormat.SHORT);
			startTimeTF = new JFormattedTextField(new DateFormatter(df3));
			startTimeTF.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(now.getTime()));

		// Set Stop time
			now.setTimeInMillis(oldJob.getStopTime());
			
			DateFormat df2 = DateFormat.getDateInstance (DateFormat.SHORT);
			stopDateTF = new JFormattedTextField(new DateFormatter(df2));
			stopDateTF.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(now.getTime()));
			
			DateFormat df4 = DateFormat.getTimeInstance (DateFormat.SHORT);
			stopTimeTF = new JFormattedTextField(new DateFormatter(df4));
			stopTimeTF.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(now.getTime()));
			
			//create combobox with all streamNames in it
			streams = controlStreams.getStreamVector();
			//how many names do we have?
			streamNames = new String[streams.capacity()];
			// copy every name into box
			for(int i=0; i < streams.capacity(); i++) {
				streamNames[i] = streams.get(i).name;
			}
			
			//finally create nameBox
			nameBox = new JComboBox(streamNames);
			
			//look for the right streamID and select the appropriate name
			for(int i=0; i < streams.capacity(); i++) {
				if(streams.get(i).id == oldJob.getStreamID()) {
					nameBox.setSelectedIndex(i);
					break;
				}
			}
			//set job enabled
			enableCB.setSelected(oldJob.isJobenabled());
		}
		
		// set Layouts
		setLayout(new BorderLayout());
		datePanel.setLayout(new GridBagLayout());
		buttonPanel.setLayout(new GridBagLayout());
		
		// add components
		add(datePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		
		//set Constraints
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 2, 2, 2, 2);
		
		c.gridwidth = 6;
		c.weightx = 0.0;
		c.gridy = 0;
		c.gridx = 0;
		datePanel.add(nameBox,c);
		
		//for better look
		c.gridy = 1;
		datePanel.add(new JLabel(" "),c);
		
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.gridy = 2;
		c.gridx = 0;
		datePanel.add(startTimeLabel,c);
		c.gridx = 1;
		datePanel.add(startDateTF,c);
		c.gridx = 2;
		datePanel.add(atStartTime,c);
		c.gridx = 3;
		datePanel.add(startTimeTF,c);
		
		c.gridy = 3;
		c.gridx = 0;
		datePanel.add(stopTimeLabel,c);
		c.gridx = 1;
		datePanel.add(stopDateTF,c);
		c.gridx = 2;
		datePanel.add(atStopTime,c);
		c.gridx = 3;
		datePanel.add(stopTimeTF,c);
		
		//for better look
		c.gridy = 4;
		datePanel.add(new JLabel(" "),c);
		
		c.insets = new Insets( 0, 0, 0, 0);
		c.gridy = 5;
		c.gridx = 0;
		c.gridwidth = 5;
		datePanel.add(onceCB,c);
		c.gridy = 6;
		datePanel.add(dailyCB,c);
		c.gridy = 7;
		datePanel.add(weeklyCB,c);
		c.gridwidth = 1;
		
		//for better look
		c.gridy = 8;
		datePanel.add(new JLabel(" "),c);
		
		c.gridy = 9;
		datePanel.add(commentLabel,c);
		//c.weightx = 1.0;
		c.gridwidth = 5;
		c.gridx = 1;
		datePanel.add(commentTF,c);
		c.gridy = 10;
		c.gridx = 0;
		datePanel.add(new JLabel(" "),c);
		c.gridy = 11;
		datePanel.add(enableCB,c);

		c.insets = new Insets( 7, 7, 7, 7);
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridy = 0;
		c.gridx = 0;
		buttonPanel.add(addButton,c);
		c.gridx = 1;
		c.weightx = 1.0;
		buttonPanel.add(new JLabel(" "),c);
		c.gridx = 2;
		c.weightx = 0.0;
		buttonPanel.add(abortButton,c);
		
		//add listeners
		addButton.addActionListener(new AddListener());
		abortButton.addActionListener(new AbortListener());
		
		// grahical
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // get resolution
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        // calculates the app. values
        int x = (screenDim.width - Integer.valueOf(width))/2;
        int y = (screenDim.height - Integer.valueOf(high))/2;
        // set location
        setLocation(x, y);
		// setSize
        setSize(new Dimension(Integer.valueOf(width),Integer.valueOf(high)));
        // make visible
		setVisible(true);
	}
	
	/**
	 * updates all textes with a new language
	 */
	public void setLanguage() {
		try {
				
			//buttons
			addButton.setText(trans.getString("AddJob.addButton"));
			abortButton.setText(trans.getString("AddJob.abortButton"));
			
			//Labels
			startTimeLabel.setText(trans.getString("AddJob.startTimeLabel"));
			stopTimeLabel.setText(trans.getString("AddJob.stopTimeLabel"));
			atStartTime.setText("   "+trans.getString("AddJob.atStartTime"));
			atStopTime.setText("   "+trans.getString("AddJob.atStopTime"));
			commentLabel.setText(trans.getString("AddJob.commentLabel"));
			
			//checkboxes
			enableCB.setText(trans.getString("AddJob.enableCB"));
			onceCB.setText(trans.getString("AddJob.onceCB"));
			dailyCB.setText(trans.getString("AddJob.dailyCB"));
			weeklyCB.setText(trans.getString("AddJob.weeklyCB"));
		} catch ( MissingResourceException e ) { 
		      System.err.println( e ); 
	    }
	}
	
	/**
	 * 
	 * @return: this object
	 */
	public Gui_AddSchedul getMe() {
		return this;
	}
	
	/**
	 * close the window without save anything
	 */
	public class AbortListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			dispose();
		}
	}
	
	/**
	 * saves the schedul job in the xml-file and in the 
	 * job - array 
	 */
	public class AddListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			DateFormat df = DateFormat.getDateInstance (DateFormat.SHORT);
			DateFormat tf = DateFormat.getTimeInstance (DateFormat.SHORT);
			try {
				
				//startTime
				Calendar now = Calendar.getInstance();
				Calendar now2 = Calendar.getInstance();
				Date recDate = df.parse(startDateTF.getText());
				Date recTime = tf.parse(startTimeTF.getText());
				now.setTime(recDate);
				now2.setTime(recTime);
				now.set(Calendar.HOUR_OF_DAY, now2.get(Calendar.HOUR_OF_DAY));
				now.set(Calendar.MINUTE, now2.get(Calendar.MINUTE));

				//get stop day
				Calendar now3 = Calendar.getInstance();
				Calendar now4 = Calendar.getInstance();
				Date stopRecDate = df.parse(stopDateTF.getText());
				Date stopRecTime = tf.parse(stopTimeTF.getText());
				now3.setTime(stopRecDate);
				now4.setTime(stopRecTime);
				now3.set(Calendar.HOUR_OF_DAY, now4.get(Calendar.HOUR_OF_DAY));
				now3.set(Calendar.MINUTE, now4.get(Calendar.MINUTE));

				//get "how often record"
				int howOften = -1; 	//-1 = once
				
				if(dailyCB.isSelected()) {
					howOften = 0;
				} else if (weeklyCB.isSelected()) {
					howOften = 1;
				}
				
				//get stream index
				int index = streams.get(nameBox.getSelectedIndex()).id;
				
				//if this job is a new one -> save in xml-file
				//and add to vector and table	
				if(createNew) {
					SchedulJob job = new SchedulJob(SchedulJob.getNewID(),index,now.getTimeInMillis(),now3.getTimeInMillis()
								,howOften,enableCB.isSelected(),commentTF.getText());
					controlJob.addToSchedulVector(job);
					schedulManager.addSchedulJobToTable(job);
					dispose();
					
				} else {
					if(controlJob.jobStillExist(oldJob.getSchedulID())) {
						oldJob.setComment(commentTF.getText());
						oldJob.setJobCounts(howOften);
						oldJob.setJobEnabled(enableCB.isSelected());
						oldJob.setStartTime(now.getTimeInMillis());
						oldJob.setStopTime(now3.getTimeInMillis());
						oldJob.setStreamID(index);
						controlJob.saveScheduleVector();
						schedulManager.updateTable(oldJob);
						dispose();
					} else {
						JOptionPane.showMessageDialog(getMe(),trans.getString("jobDoesntExistAnymore"));
					}
					
				}
				
				
			} catch (ParseException e1) {
				e1.printStackTrace();
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
