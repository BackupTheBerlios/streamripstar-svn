package thread;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import gui.*;
import control.*;

/**
 * starts filling the table with streams
 */
public class FillTableWithStreams extends Thread {

	private StreamsControler controlStreams = null;
	private Gui_TablePanel tablePanel = null;
	private ThreadsStartControl controlThreads = null;
	
	public FillTableWithStreams(StreamsControler controlStreams,TablePanel tablePanel,ThreadsStartControl controlThreads) {
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
