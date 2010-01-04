package misc;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/

/**
 * This class represents all information about a stream, which are variable from
 * its. Every stream can have 1..n of this schemas. With every single schema, you
 * can record a stream with different options, without changing the options. 
 */
public class RecordSchema {

	//the stream id of the stream. If the value is lower 0, this
	//schema is available for every stream.
	private int streamID = -1;
	private String schemaName = "Untitled Schema";
	
	//Options - Strings
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
	
	//Options - booleans
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
}
