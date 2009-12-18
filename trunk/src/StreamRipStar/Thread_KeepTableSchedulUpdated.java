package StreamRipStar;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/


public class Thread_KeepTableSchedulUpdated extends Thread{
	
	private Gui_SchedulManager sM = null;
	private Thread_Control_Schedul control = null;
	private Boolean stop = false;
	
	public Thread_KeepTableSchedulUpdated(Gui_SchedulManager sM, Thread_Control_Schedul control) {
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
