package StreamRipStar;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

/**
 * starts filling the table with streams
 */
public class Thread_FillTableWithStreams extends Thread {

	private Control_Stream controlStreams = null;
	private Gui_TablePanel tablePanel = null;
	private Control_Threads controlThreads = null;
	
	public Thread_FillTableWithStreams(Control_Stream controlStreams,Gui_TablePanel tablePanel,Control_Threads controlThreads) {
		this.controlStreams = controlStreams;
		this.tablePanel = tablePanel;
		this.controlThreads = controlThreads;
	}
	
	public void run() {
		//ask for control
		System.out.println("Thread: Ask for filling table with Streams");
		
		//start filling the table with streams
		if(controlStreams != null && tablePanel!= null) {
			System.out.println("Thread: filling table with streams");
	        controlStreams.loadStreamsOnStart();
			tablePanel.fillTableWithStreams();
			controlStreams.loadDefaultStreamOnStart();
		}
		
		//say: I'm ready
		controlThreads.free();
	}
}
