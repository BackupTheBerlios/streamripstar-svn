package thread;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/

import gui.*;

public class KeepTableSchedulUpdated extends Thread{
	
	private Gui_SchedulManager sM = null;
	private Control_Schedul control = null;
	private Boolean stop = false;
	
	public KeepTableSchedulUpdated(Gui_SchedulManager sM, Control_Schedul control) {
		this.sM = sM;
		this.control = control;
	}
	
	public void stopThread() {
		stop = true;
	}
	
	public void run() {
		try {
			while(!stop) {
				if(control.shouldUpdate()) {
					int selRow = sM.getSelectedRowNumber();
					sM.updateTable();
					//if a Row was selected select it again
					if(selRow > -1) {
						sM.setSelectedRow(selRow);
					}
					control.setUpdateView(false);
				}
				Thread.sleep(250);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
