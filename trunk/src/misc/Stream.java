package misc;

import thread.Thread_UpdateName;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

//This class include its own streamripper process and name,address,website
public class Stream
{	
	public static int activeStreams = 0; //Number of ripping streams
	public static int lastID = 1;	//the id for a next stream to create
	
	//numbers
	public int id = 0;
	public short completeCB = 0;	//0=Version;1=ever;2=never;3=larger
	
	//Strings
	public String name;		
	public String address;
	public String website = "";
	public String genre = "";
	public String comment = "";
	public String singleFileTF = "";
	public String maxTimeHHTF = "";
	public String maxTimeMMTF = "";
	public String maxTimessTF = "";
	public String maxMBTF = "";
	public String sequenzTF = "";
	public String patternTF = "";
	public String relayServerPortTF = "";
	public String maxConnectRelayTF = "";
	public String relayPlayListTF = "";
	public String timeOutReonTF = "";
	public String proxyTF = "";
	public String useragentTF = "";
	public String sciptSongsTF = "";
	public String metaDataFileTF = "";
	public String interfaceTF = "";
	public String externTF = "";
	public String extraArgsTF = "";
	public String CSRelayTF = "";
	public String CSMetaDataTF = "";
	public String CSIDTF = "";
	public String CSFileSysTF = "";
	public String SPDelayTF = "";
	public String SPExtraTF1 = "";
	public String SPExtraTF2 = "";
	public String SPWindowTF1= "";
	public String SPWindowTF2= "";
	public String SPSilenceTF= "";
	
	public String send_name = "-";
	public String send_serverName = "-";
	public String send_bitrate = "-";
	public String send_metainvervall = "-";
	
	//bools
	public boolean singleFileCB = false;
	public boolean maxTimeCB = false;
	public boolean maxMBCB = false;
	public boolean sequenzCB = false;
	public boolean patternCB = false;
	public boolean cutSongIncompleteCB = false;
	public boolean neverOverIncompCB = false;
	public boolean noDirEveryStreamCB = false;
	public boolean noIndiviSongsCB = false;
	public boolean createReayCB= false;
	public boolean connectToRelayCB = false;
	public boolean createPlaylistRelayCB = false;
	public boolean dontSearchAltPortCB  = false;
	public boolean dontAutoReconnectCB = false;
	public boolean timeoutReconnectCB = false;
	public boolean proxyCB = false;
	public boolean useragentCB = false;
	public boolean countBeforStartCB = false;
	public boolean metaDataCB = false;
	public boolean interfaceCB = false;
	public boolean externMetaDataCB = false;
	public boolean extraArgsCB = false;
	public boolean CSRelayCB = false;
	public boolean CSMetaCB = false;
	public boolean CSIDTagCB = false;
	public boolean CSFileSysCB = false;
	public boolean XS2CB = false;
	public boolean SPDelayCB = false;
	public boolean SPExtraCB = false;
	public boolean SPWindowCB = false;
	public boolean SPSilenceCB = false;
	public boolean IDV1CB = false;
	public boolean IDV2CB = false;
	
	//runtime generated content
	private String exeCommand = ""; 
	private Boolean isRipping = false;	//the the stream ripping?
	private Process ownStreamripperProcess = null;
	private Thread_UpdateName updateName = null;
	private Boolean userStopptRecodring = false;

	/**
	 * Creates an object stream, including the name and the id.
	 * Over the id all options can be accessed. 
	 * 
	 * @param streamName: The name of this stream (shown in table)
	 * @param streamID: The stream ID in xml-file
	 */
	public Stream(String streamName, int streamID) {
		name = streamName;
		id = streamID;
	}

	
	public String getExeCommand() {
		return exeCommand;
	}
	
	public void setExeCommand(String command) {
		exeCommand = command;
	}
	
	/**
	 * Stop recording streams
	 * @param userStopped if the user stopped it
	 */
	public void setStop(boolean userStopped) {
		userStopptRecodring = userStopped;
		
		isRipping=false;
		if(ownStreamripperProcess!=null) {
			ownStreamripperProcess.destroy();
			ownStreamripperProcess=null;
		}

		if(updateName != null) {
			updateName.killMe();
			updateName=null;
		}
		activeStreams--;
	}
	
	/**
	 * Returns the value, if the user has stop the last recording from a stream
	 * @return true, if he stopped it. Else false
	 */
	public boolean userStoppedRecording()
	{
		return this.userStopptRecodring;
	}
	
	public String[] getMetaData() {
		String[] metaData = new String[4];
		metaData[0] = send_name; 
		metaData[1] = send_serverName;
		metaData[2] = send_bitrate;
		metaData[3] = send_metainvervall;
		return metaData;
	}
	
	public void setMetaData(String[] data) {
		send_name = data[0]; //send Streamname
		send_serverName = data[1]; //send Server name
		send_bitrate = data[2]; //bitrate
		send_metainvervall = data[3]; //metaintervall
	}
	
	//increase the number of recording streams
	public synchronized void increaseRippingCount() {
		activeStreams++;
	}
	
	//decrease the number of recording streams 
	public synchronized void decreaseRippingCount() {
		activeStreams--;
	}

	/**
	 * Return all necessary information for adding a stream
	 * to the table in the mainwindow.
	 * @return object includes the shown information
	 * 			 in table + the stream id
	 */
	public Object[] getBase() {	
		Object[] x = {null,name,"",id};
		return x;
	}
	
	/**
	 * Put the name of this stream in an object
	 * @return: an object including the stream name
	 */
	public Object[] getNameAsObject() {
		Object[] x = {name,id};
		return x;
	}
	
	/**
	 * returns true, if stream is ripping
	 * 
	 */
	public Boolean getStatus() {
		return isRipping;
	}
	
	public void setProcess(Process p) {
		ownStreamripperProcess = p;
	}
	
	public Process getProcess() {
		return ownStreamripperProcess;
	}
	
	public void setStatus(Boolean status) {
		this.isRipping = status;
	}
	
	public Thread_UpdateName getUpdateName() {
		return updateName;
	}
	
	public void setUpdateName(Thread_UpdateName updateName) {
		this.updateName = updateName; 
	}
	
	public synchronized static int getNewStreamID() {
		lastID++;
		return lastID-1;
	}
}
