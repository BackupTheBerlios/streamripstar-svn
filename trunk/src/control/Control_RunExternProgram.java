package control;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 


public class Control_RunExternProgram implements Runnable  {
	private String path;
	
	public Control_RunExternProgram(String path) {
		this.path = path;
	}
	
	public void run() {
		try {
			Runtime.getRuntime().exec(path);
		} catch (Exception e) {
		    SRSOutput.getInstance().logE("Fehler beim Ausführen von"+path+e);
		}
	}
}
