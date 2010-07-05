package control;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

public class Control_Threads {
	private boolean tableIsFilledWithStreams = false;
	
	public boolean askForLoadingScheduls() {
		return tableIsFilledWithStreams;
	}
	
	public void free() {
		tableIsFilledWithStreams = true;
	}
}
