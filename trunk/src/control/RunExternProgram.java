package control;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 


public class RunExternProgram implements Runnable  {
	private String path;
	
	public RunExternProgram(String path) {
		this.path = path;
	}
	
	public void run() {
		try {
			Runtime.getRuntime().exec(path);
		} catch (Exception e) {
		    System.err.println("Fehler beim Ausführen von"+path+e);
		}
	}
}
