package control;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johanes Putzke*/
/* eMail: die_eule@gmx.net*/  

import gui.Gui_StreamRipStar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import misc.Stream;


//This class include a Stream with all options
public class Control_Stream
{
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	private Gui_StreamRipStar mainGui = null;
	private Vector<Stream> streamVector = new Vector<Stream>(0,1);
	private Stream defaultStream= null;
	
	private String webBrowserPath = "";
	private String mp3PlayerPath = "";
	private String streamripperPath = "";
	private String fileBrowserPath = "";
	private String generalPath = "";
	
	public Control_Stream(Gui_StreamRipStar mainGui) {
		this.mainGui = mainGui;
	}
	
	/**
	 * saves all streams from the streamvector in 
	 * the "Streams.xml" file
	 */
	public void saveStreamVector() {
		String savePath =  new Control_GetPath().getStreamRipStarPath();
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance(); 
		try {
			XMLEventWriter writer = outputFactory.createXMLEventWriter( new FileOutputStream(savePath+"/Streams.xml" ) );
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			
			//header for the file
			XMLEvent header = eventFactory.createStartDocument();
			XMLEvent startRootAllStreams = eventFactory.createStartElement( "", "", "allStreams" ); 
			XMLEvent lastStreamID = eventFactory.createAttribute( "lastStreamID", String.valueOf( Stream.lastID )); 
			writer.add( header ); 
			writer.add( startRootAllStreams );
			writer.add( lastStreamID );
			
			for(int i=0;i < streamVector.capacity();i++) {
				//start creating a stream
				XMLEvent startStream = eventFactory.createStartElement( "", "", "Stream" );
				
				XMLEvent id = eventFactory.createAttribute( "id", String.valueOf(streamVector.get(i).id) ); 
				XMLEvent name = eventFactory.createAttribute( "name", streamVector.get(i).name  ); 
				XMLEvent completeCB = eventFactory.createAttribute( "completeCB", String.valueOf(streamVector.get(i).completeCB));
				XMLEvent address = eventFactory.createAttribute( "address", streamVector.get(i).address  );
				XMLEvent website = eventFactory.createAttribute( "website", streamVector.get(i).website  );
				XMLEvent genre = eventFactory.createAttribute( "genre", streamVector.get(i).genre  );
				XMLEvent comment = eventFactory.createAttribute( "comment", streamVector.get(i).comment  );
				XMLEvent singleFileTF = eventFactory.createAttribute( "singleFileTF", streamVector.get(i).singleFileTF  );
				XMLEvent maxTimeHHTF = eventFactory.createAttribute( "maxTimeHHTF", streamVector.get(i).maxTimeHHTF  );
				XMLEvent maxTimeMMTF = eventFactory.createAttribute( "maxTimeMMTF", streamVector.get(i).maxTimeMMTF  );
				XMLEvent maxTimessTF = eventFactory.createAttribute( "maxTimessTF", streamVector.get(i).maxTimessTF  );
				XMLEvent maxMBTF = eventFactory.createAttribute( "maxMBTF", streamVector.get(i).maxMBTF  );
				XMLEvent sequenzTF = eventFactory.createAttribute( "sequenzTF", streamVector.get(i).sequenzTF  );
				XMLEvent patternTF = eventFactory.createAttribute( "patternTF", streamVector.get(i).patternTF  );
				XMLEvent relayServerPortTF = eventFactory.createAttribute( "relayServerPortTF", streamVector.get(i).relayServerPortTF  );
				XMLEvent maxConnectRelayTF = eventFactory.createAttribute( "maxConnectRelayTF", streamVector.get(i).maxConnectRelayTF  );
				XMLEvent relayPlayListTF = eventFactory.createAttribute( "relayPlayListTF", streamVector.get(i).relayPlayListTF  );
				XMLEvent timeOutReonTF = eventFactory.createAttribute( "timeOutReonTF", streamVector.get(i).timeOutReonTF  );
				XMLEvent proxyTF = eventFactory.createAttribute( "proxyTF", streamVector.get(i).proxyTF  );
				XMLEvent useragentTF = eventFactory.createAttribute( "useragentTF", streamVector.get(i).useragentTF  );
				XMLEvent sciptSongsTF = eventFactory.createAttribute( "sciptSongsTF", streamVector.get(i).sciptSongsTF  );
				XMLEvent metaDataFileTF = eventFactory.createAttribute( "metaDataFileTF", streamVector.get(i).metaDataFileTF  );
				XMLEvent interfaceTF = eventFactory.createAttribute( "interfaceTF", streamVector.get(i).interfaceTF  );
				XMLEvent externTF = eventFactory.createAttribute( "externTF", streamVector.get(i).externTF  );
				XMLEvent extraArgsTF = eventFactory.createAttribute( "extraArgsTF", streamVector.get(i).extraArgsTF  );
				XMLEvent CSRelayTF = eventFactory.createAttribute( "CSRelayTF", streamVector.get(i).CSRelayTF );
				XMLEvent CSMetaDataTF = eventFactory.createAttribute( "CSMetaDataTF", streamVector.get(i).CSMetaDataTF  );
				XMLEvent CSIDTF = eventFactory.createAttribute( "CSIDTF", streamVector.get(i).CSIDTF  );
				XMLEvent CSFileSysTF = eventFactory.createAttribute( "CSFileSysTF", streamVector.get(i).CSFileSysTF  );
				XMLEvent SPDelayTF = eventFactory.createAttribute( "SPDelayTF", streamVector.get(i).SPDelayTF  );
				XMLEvent SPExtraTF1 = eventFactory.createAttribute( "SPExtraTF1", streamVector.get(i).SPExtraTF1  );
				XMLEvent SPExtraTF2 = eventFactory.createAttribute( "SPExtraTF2", streamVector.get(i).SPExtraTF2  );
				XMLEvent SPWindowTF1 = eventFactory.createAttribute( "SPWindowTF1", streamVector.get(i).SPWindowTF1  );
				XMLEvent SPWindowTF2 = eventFactory.createAttribute( "SPWindowTF2", streamVector.get(i).SPWindowTF2  );
				XMLEvent SPSilenceTF = eventFactory.createAttribute( "SPSilenceTF", streamVector.get(i).SPSilenceTF  );
				XMLEvent singleFileCB = eventFactory.createAttribute( "singleFileCB", String.valueOf(streamVector.get(i).singleFileCB));
				XMLEvent maxTimeCB = eventFactory.createAttribute( "maxTimeCB", String.valueOf(streamVector.get(i).maxTimeCB));
				XMLEvent maxMBCB = eventFactory.createAttribute( "maxMBCB", String.valueOf(streamVector.get(i).maxMBCB));
				XMLEvent sequenzCB = eventFactory.createAttribute( "sequenzCB", String.valueOf(streamVector.get(i).sequenzCB));
				XMLEvent patternCB = eventFactory.createAttribute( "patternCB", String.valueOf(streamVector.get(i).patternCB));
				XMLEvent cutSongIncompleteCB = eventFactory.createAttribute( "cutSongIncompleteCB", String.valueOf(streamVector.get(i).cutSongIncompleteCB));
				XMLEvent neverOverIncompCB = eventFactory.createAttribute( "neverOverIncompCB", String.valueOf(streamVector.get(i).neverOverIncompCB));
				XMLEvent noDirEveryStreamCB = eventFactory.createAttribute( "noDirEveryStreamCB", String.valueOf(streamVector.get(i).noDirEveryStreamCB));
				XMLEvent noIndiviSongsCB = eventFactory.createAttribute( "noIndiviSongsCB", String.valueOf(streamVector.get(i).noIndiviSongsCB));
				XMLEvent createReayCB = eventFactory.createAttribute( "createReayCB", String.valueOf(streamVector.get(i).createReayCB));
				XMLEvent connectToRelayCB = eventFactory.createAttribute( "connectToRelayCB", String.valueOf(streamVector.get(i).connectToRelayCB));
				XMLEvent createPlaylistRelayCB = eventFactory.createAttribute( "createPlaylistRelayCB", String.valueOf(streamVector.get(i).createPlaylistRelayCB));
				XMLEvent dontSearchAltPortCB = eventFactory.createAttribute( "dontSearchAltPortCB", String.valueOf(streamVector.get(i).dontSearchAltPortCB));
				XMLEvent dontAutoReconnectCB = eventFactory.createAttribute( "dontAutoReconnectCB", String.valueOf(streamVector.get(i).dontAutoReconnectCB));
				XMLEvent timeoutReconnectCB = eventFactory.createAttribute( "timeoutReconnectCB", String.valueOf(streamVector.get(i).timeoutReconnectCB));
				XMLEvent proxyCB = eventFactory.createAttribute( "proxyCB", String.valueOf(streamVector.get(i).proxyCB));
				XMLEvent useragentCB = eventFactory.createAttribute( "useragentCB", String.valueOf(streamVector.get(i).useragentCB));
				XMLEvent countBeforStartCB = eventFactory.createAttribute( "countBeforStartCB", String.valueOf(streamVector.get(i).countBeforStartCB));
				XMLEvent metaDataCB = eventFactory.createAttribute( "metaDataCB", String.valueOf(streamVector.get(i).metaDataCB));
				XMLEvent interfaceCB = eventFactory.createAttribute( "interfaceCB", String.valueOf(streamVector.get(i).interfaceCB));
				XMLEvent externMetaDataCB = eventFactory.createAttribute( "externMetaDataCB", String.valueOf(streamVector.get(i).externMetaDataCB));
				XMLEvent extraArgsCB = eventFactory.createAttribute( "extraArgsCB", String.valueOf(streamVector.get(i).extraArgsCB));
				XMLEvent CSRelayCB = eventFactory.createAttribute( "CSRelayCB", String.valueOf(streamVector.get(i).CSRelayCB));
				XMLEvent CSMetaCB = eventFactory.createAttribute( "CSMetaCB", String.valueOf(streamVector.get(i).CSMetaCB));
				XMLEvent CSIDTagCB = eventFactory.createAttribute( "CSIDTagCB", String.valueOf(streamVector.get(i).CSIDTagCB));
				XMLEvent CSFileSysCB = eventFactory.createAttribute( "CSFileSysCB", String.valueOf(streamVector.get(i).CSFileSysCB));
				XMLEvent XS2CB = eventFactory.createAttribute( "XS2CB", String.valueOf(streamVector.get(i).XS2CB));
				XMLEvent SPDelayCB = eventFactory.createAttribute( "SPDelayCB", String.valueOf(streamVector.get(i).SPDelayCB));
				XMLEvent SPExtraCB = eventFactory.createAttribute( "SPExtraCB", String.valueOf(streamVector.get(i).SPExtraCB));
				XMLEvent SPWindowCB = eventFactory.createAttribute( "SPWindowCB", String.valueOf(streamVector.get(i).SPWindowCB));
				XMLEvent SPSilenceCB = eventFactory.createAttribute( "SPSilenceCB", String.valueOf(streamVector.get(i).SPSilenceCB));
				XMLEvent IDV1CB = eventFactory.createAttribute( "IDV1CB", String.valueOf(streamVector.get(i).IDV1CB));
				XMLEvent IDV2CB = eventFactory.createAttribute( "IDV2CB", String.valueOf(streamVector.get(i).IDV2CB));
				XMLEvent exeCommand = eventFactory.createAttribute( "exeCommand", String.valueOf(streamVector.get(i).getExeCommand()));
				XMLEvent send_name = eventFactory.createAttribute( "send_name", String.valueOf(streamVector.get(i).send_name));
				XMLEvent send_serverName = eventFactory.createAttribute( "send_serverName", String.valueOf(streamVector.get(i).send_serverName));
				XMLEvent send_metainvervall = eventFactory.createAttribute( "send_metainvervall", String.valueOf(streamVector.get(i).send_metainvervall));
				XMLEvent send_bitrate = eventFactory.createAttribute( "send_bitrate", String.valueOf(streamVector.get(i).send_bitrate));
				
				XMLEvent endStream = eventFactory.createEndElement( "", "", "Stream" ); 
				
				//write this stream into file
				writer.add( startStream );
				writer.add( id ); 
				writer.add( name );
				writer.add( completeCB );
				writer.add( address );
				writer.add( website );
				writer.add( genre );
				writer.add( comment );
				writer.add( singleFileTF );
				writer.add( maxTimeHHTF );
				writer.add( maxTimeMMTF );
				writer.add( maxTimessTF);
				writer.add( maxMBTF );
				writer.add( sequenzTF );
				writer.add( patternTF );
				writer.add( relayServerPortTF );
				writer.add( maxConnectRelayTF );
				writer.add( relayPlayListTF );
				writer.add( timeOutReonTF );
				writer.add( proxyTF );
				writer.add( useragentTF );
				writer.add( sciptSongsTF );
				writer.add( metaDataFileTF );
				writer.add( interfaceTF );
				writer.add( externTF );
				writer.add( extraArgsTF );
				writer.add( CSRelayTF );
				writer.add( CSMetaDataTF );
				writer.add( CSIDTF );
				writer.add( CSFileSysTF );
				writer.add( SPDelayTF );
				writer.add( SPExtraTF1 );
				writer.add( SPExtraTF2 );
				writer.add( SPWindowTF1 );
				writer.add( SPWindowTF2 );
				writer.add( SPSilenceTF );
				writer.add( singleFileCB );
				writer.add( maxTimeCB );
				writer.add( maxMBCB );
				writer.add( sequenzCB );
				writer.add( patternCB );
				writer.add( cutSongIncompleteCB );
				writer.add( neverOverIncompCB );
				writer.add( noDirEveryStreamCB );
				writer.add( noIndiviSongsCB );
				writer.add( createReayCB);
				writer.add( connectToRelayCB );
				writer.add( createPlaylistRelayCB);
				writer.add( dontSearchAltPortCB);
				writer.add( dontAutoReconnectCB );
				writer.add( timeoutReconnectCB );
				writer.add( proxyCB);
				writer.add( useragentCB );
				writer.add( countBeforStartCB );
				writer.add( metaDataCB );
				writer.add( interfaceCB );
				writer.add( externMetaDataCB );
				writer.add( extraArgsCB );
				writer.add( CSRelayCB );
				writer.add( CSMetaCB);
				writer.add( CSIDTagCB );
				writer.add( CSFileSysCB );
				writer.add( XS2CB );
				writer.add( SPDelayCB );
				writer.add( SPExtraCB );
				writer.add( SPWindowCB );
				writer.add( SPSilenceCB );
				writer.add( IDV1CB );
				writer.add( IDV2CB );
				writer.add( exeCommand );
				writer.add( send_name );
				writer.add( send_serverName );
				writer.add( send_metainvervall );
				writer.add( send_bitrate );
				
				writer.add( endStream );
			}
			
			XMLEvent endRoot = eventFactory.createEndElement( "", "", "AllStreams" ); 
			XMLEvent endDocument = eventFactory.createEndDocument();
			writer.add( endRoot ); 
			writer.add( endDocument ); 
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * saves all streams from the streamvector in 
	 * the "Streams.xml" file
	 */
	public void saveDefaultStream() {
		String savePath =  new Control_GetPath().getStreamRipStarPath();
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance(); 
		try {
			XMLEventWriter writer = outputFactory.createXMLEventWriter( new FileOutputStream(savePath+"/DefaultStream.xml" ) );
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			
			//header for the file
			XMLEvent header = eventFactory.createStartDocument();
			XMLEvent startRootAllStreams = eventFactory.createStartElement( "", "", "defaultStream" ); 
			writer.add( header ); 
			writer.add( startRootAllStreams );

			XMLEvent startStream = eventFactory.createStartElement( "", "", "Stream" );
				
			XMLEvent id = eventFactory.createAttribute( "id", String.valueOf(defaultStream.id) ); 
			XMLEvent name = eventFactory.createAttribute( "name", defaultStream.name  ); 
			XMLEvent completeCB = eventFactory.createAttribute( "completeCB", String.valueOf(defaultStream.completeCB));
			XMLEvent address = eventFactory.createAttribute( "address", defaultStream.address  );
			XMLEvent website = eventFactory.createAttribute( "website", defaultStream.website  );
			XMLEvent genre = eventFactory.createAttribute( "genre", defaultStream.genre  );
			XMLEvent comment = eventFactory.createAttribute( "comment", defaultStream.comment  );
			XMLEvent singleFileTF = eventFactory.createAttribute( "singleFileTF", defaultStream.singleFileTF  );
			XMLEvent maxTimeHHTF = eventFactory.createAttribute( "maxTimeHHTF", defaultStream.maxTimeHHTF  );
			XMLEvent maxTimeMMTF = eventFactory.createAttribute( "maxTimeMMTF", defaultStream.maxTimeMMTF  );
			XMLEvent maxTimessTF = eventFactory.createAttribute( "maxTimessTF", defaultStream.maxTimessTF  );
			XMLEvent maxMBTF = eventFactory.createAttribute( "maxMBTF", defaultStream.maxMBTF  );
			XMLEvent sequenzTF = eventFactory.createAttribute( "sequenzTF", defaultStream.sequenzTF  );
			XMLEvent patternTF = eventFactory.createAttribute( "patternTF", defaultStream.patternTF  );
			XMLEvent relayServerPortTF = eventFactory.createAttribute( "relayServerPortTF", defaultStream.relayServerPortTF  );
			XMLEvent maxConnectRelayTF = eventFactory.createAttribute( "maxConnectRelayTF", defaultStream.maxConnectRelayTF  );
			XMLEvent relayPlayListTF = eventFactory.createAttribute( "relayPlayListTF", defaultStream.relayPlayListTF  );
			XMLEvent timeOutReonTF = eventFactory.createAttribute( "timeOutReonTF", defaultStream.timeOutReonTF  );
			XMLEvent proxyTF = eventFactory.createAttribute( "proxyTF", defaultStream.proxyTF  );
			XMLEvent useragentTF = eventFactory.createAttribute( "useragentTF", defaultStream.useragentTF  );
			XMLEvent sciptSongsTF = eventFactory.createAttribute( "sciptSongsTF", defaultStream.sciptSongsTF  );
			XMLEvent metaDataFileTF = eventFactory.createAttribute( "metaDataFileTF", defaultStream.metaDataFileTF  );
			XMLEvent interfaceTF = eventFactory.createAttribute( "interfaceTF", defaultStream.interfaceTF  );
			XMLEvent externTF = eventFactory.createAttribute( "externTF", defaultStream.externTF  );
			XMLEvent extraArgsTF = eventFactory.createAttribute( "extraArgsTF", defaultStream.extraArgsTF  );
			XMLEvent CSRelayTF = eventFactory.createAttribute( "CSRelayTF", defaultStream.CSRelayTF );
			XMLEvent CSMetaDataTF = eventFactory.createAttribute( "CSMetaDataTF", defaultStream.CSMetaDataTF  );
			XMLEvent CSIDTF = eventFactory.createAttribute( "CSIDTF", defaultStream.CSIDTF  );
			XMLEvent CSFileSysTF = eventFactory.createAttribute( "CSFileSysTF", defaultStream.CSFileSysTF  );
			XMLEvent SPDelayTF = eventFactory.createAttribute( "SPDelayTF", defaultStream.SPDelayTF  );
			XMLEvent SPExtraTF1 = eventFactory.createAttribute( "SPExtraTF1", defaultStream.SPExtraTF1  );
			XMLEvent SPExtraTF2 = eventFactory.createAttribute( "SPExtraTF2", defaultStream.SPExtraTF2  );
			XMLEvent SPWindowTF1 = eventFactory.createAttribute( "SPWindowTF1", defaultStream.SPWindowTF1  );
			XMLEvent SPWindowTF2 = eventFactory.createAttribute( "SPWindowTF2", defaultStream.SPWindowTF2  );
			XMLEvent SPSilenceTF = eventFactory.createAttribute( "SPSilenceTF", defaultStream.SPSilenceTF  );
			XMLEvent singleFileCB = eventFactory.createAttribute( "singleFileCB", String.valueOf(defaultStream.singleFileCB));
			XMLEvent maxTimeCB = eventFactory.createAttribute( "maxTimeCB", String.valueOf(defaultStream.maxTimeCB));
			XMLEvent maxMBCB = eventFactory.createAttribute( "maxMBCB", String.valueOf(defaultStream.maxMBCB));
			XMLEvent sequenzCB = eventFactory.createAttribute( "sequenzCB", String.valueOf(defaultStream.sequenzCB));
			XMLEvent patternCB = eventFactory.createAttribute( "patternCB", String.valueOf(defaultStream.patternCB));
			XMLEvent cutSongIncompleteCB = eventFactory.createAttribute( "cutSongIncompleteCB", String.valueOf(defaultStream.cutSongIncompleteCB));
			XMLEvent neverOverIncompCB = eventFactory.createAttribute( "neverOverIncompCB", String.valueOf(defaultStream.neverOverIncompCB));
			XMLEvent noDirEveryStreamCB = eventFactory.createAttribute( "noDirEveryStreamCB", String.valueOf(defaultStream.noDirEveryStreamCB));
			XMLEvent noIndiviSongsCB = eventFactory.createAttribute( "noIndiviSongsCB", String.valueOf(defaultStream.noIndiviSongsCB));
			XMLEvent createReayCB = eventFactory.createAttribute( "createReayCB", String.valueOf(defaultStream.createReayCB));
			XMLEvent connectToRelayCB = eventFactory.createAttribute( "connectToRelayCB", String.valueOf(defaultStream.connectToRelayCB));
			XMLEvent createPlaylistRelayCB = eventFactory.createAttribute( "createPlaylistRelayCB", String.valueOf(defaultStream.createPlaylistRelayCB));
			XMLEvent dontSearchAltPortCB = eventFactory.createAttribute( "dontSearchAltPortCB", String.valueOf(defaultStream.dontSearchAltPortCB));
			XMLEvent dontAutoReconnectCB = eventFactory.createAttribute( "dontAutoReconnectCB", String.valueOf(defaultStream.dontAutoReconnectCB));
			XMLEvent timeoutReconnectCB = eventFactory.createAttribute( "timeoutReconnectCB", String.valueOf(defaultStream.timeoutReconnectCB));
			XMLEvent proxyCB = eventFactory.createAttribute( "proxyCB", String.valueOf(defaultStream.proxyCB));
			XMLEvent useragentCB = eventFactory.createAttribute( "useragentCB", String.valueOf(defaultStream.useragentCB));
			XMLEvent countBeforStartCB = eventFactory.createAttribute( "countBeforStartCB", String.valueOf(defaultStream.countBeforStartCB));
			XMLEvent metaDataCB = eventFactory.createAttribute( "metaDataCB", String.valueOf(defaultStream.metaDataCB));
			XMLEvent interfaceCB = eventFactory.createAttribute( "interfaceCB", String.valueOf(defaultStream.interfaceCB));
			XMLEvent externMetaDataCB = eventFactory.createAttribute( "externMetaDataCB", String.valueOf(defaultStream.externMetaDataCB));
			XMLEvent extraArgsCB = eventFactory.createAttribute( "extraArgsCB", String.valueOf(defaultStream.extraArgsCB));
			XMLEvent CSRelayCB = eventFactory.createAttribute( "CSRelayCB", String.valueOf(defaultStream.CSRelayCB));
			XMLEvent CSMetaCB = eventFactory.createAttribute( "CSMetaCB", String.valueOf(defaultStream.CSMetaCB));
			XMLEvent CSIDTagCB = eventFactory.createAttribute( "CSIDTagCB", String.valueOf(defaultStream.CSIDTagCB));
			XMLEvent CSFileSysCB = eventFactory.createAttribute( "CSFileSysCB", String.valueOf(defaultStream.CSFileSysCB));
			XMLEvent XS2CB = eventFactory.createAttribute( "XS2CB", String.valueOf(defaultStream.XS2CB));
			XMLEvent SPDelayCB = eventFactory.createAttribute( "SPDelayCB", String.valueOf(defaultStream.SPDelayCB));
			XMLEvent SPExtraCB = eventFactory.createAttribute( "SPExtraCB", String.valueOf(defaultStream.SPExtraCB));
			XMLEvent SPWindowCB = eventFactory.createAttribute( "SPWindowCB", String.valueOf(defaultStream.SPWindowCB));
			XMLEvent SPSilenceCB = eventFactory.createAttribute( "SPSilenceCB", String.valueOf(defaultStream.SPSilenceCB));
			XMLEvent IDV1CB = eventFactory.createAttribute( "IDV1CB", String.valueOf(defaultStream.IDV1CB));
			XMLEvent IDV2CB = eventFactory.createAttribute( "IDV2CB", String.valueOf(defaultStream.IDV2CB));
			
			XMLEvent endStream = eventFactory.createEndElement( "", "", "Stream" ); 
			
			//write this stream into file
			writer.add( startStream );
			writer.add( id ); 
			writer.add( name );
			writer.add( completeCB );
			writer.add( address );
			writer.add( website );
			writer.add( genre );
			writer.add( comment );
			writer.add( singleFileTF );
			writer.add( maxTimeHHTF );
			writer.add( maxTimeMMTF );
			writer.add( maxTimessTF);
			writer.add( maxMBTF );
			writer.add( sequenzTF );
			writer.add( patternTF );
			writer.add( relayServerPortTF );
			writer.add( maxConnectRelayTF );
			writer.add( relayPlayListTF );
			writer.add( timeOutReonTF );
			writer.add( proxyTF );
			writer.add( useragentTF );
			writer.add( sciptSongsTF );
			writer.add( metaDataFileTF );
			writer.add( interfaceTF );
			writer.add( externTF );
			writer.add( extraArgsTF );
			writer.add( CSRelayTF );
			writer.add( CSMetaDataTF );
			writer.add( CSIDTF );
			writer.add( CSFileSysTF );
			writer.add( SPDelayTF );
			writer.add( SPExtraTF1 );
			writer.add( SPExtraTF2 );
			writer.add( SPWindowTF1 );
			writer.add( SPWindowTF2 );
			writer.add( SPSilenceTF );
			writer.add( singleFileCB );
			writer.add( maxTimeCB );
			writer.add( maxMBCB );
			writer.add( sequenzCB );
			writer.add( patternCB );
			writer.add( cutSongIncompleteCB );
			writer.add( neverOverIncompCB );
			writer.add( noDirEveryStreamCB );
			writer.add( noIndiviSongsCB );
			writer.add( createReayCB);
			writer.add( connectToRelayCB );
			writer.add( createPlaylistRelayCB);
			writer.add( dontSearchAltPortCB);
			writer.add( dontAutoReconnectCB );
			writer.add( timeoutReconnectCB );
			writer.add( proxyCB);
			writer.add( useragentCB );
			writer.add( countBeforStartCB );
			writer.add( metaDataCB );
			writer.add( interfaceCB );
			writer.add( externMetaDataCB );
			writer.add( extraArgsCB );
			writer.add( CSRelayCB );
			writer.add( CSMetaCB);
			writer.add( CSIDTagCB );
			writer.add( CSFileSysCB );
			writer.add( XS2CB );
			writer.add( SPDelayCB );
			writer.add( SPExtraCB );
			writer.add( SPWindowCB );
			writer.add( SPSilenceCB );
			writer.add( IDV1CB );
			writer.add( IDV2CB );
			
			writer.add( endStream );
			
			XMLEvent endRoot = eventFactory.createEndElement( "", "", "defaultStream" ); 
			XMLEvent endDocument = eventFactory.createEndDocument();
			writer.add( endRoot ); 
			writer.add( endDocument ); 
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * returns the default stream
	 * the basic stream is the stream, with the default values for
	 * an new created stream
	 * @return
	 */
	public Stream getDefaultStream() {
		return defaultStream;
	}
	
	/**
	 * set a new default Stream
	 */
	public void setDefaultStream(Stream newDefaultStream) {
		defaultStream = newDefaultStream;
	}
	
	/**
	 * gets all stream from xml-file and create
	 * for every stream in xml-file an object
	 */
	public void loadStreamsOnStart() {
		String loadPath =  new Control_GetPath().getStreamRipStarPath();
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser;
			parser = factory.createXMLStreamReader( new FileInputStream(loadPath+"/Streams.xml" ) );
			while ( parser.hasNext() ) { 
	 
				switch ( parser.getEventType() ) { 
					case XMLStreamConstants.START_DOCUMENT: 
						System.out.println( "Loading file Streams.xml" );
						parser.getEventType();
						break; 
				 
				    case XMLStreamConstants.END_DOCUMENT: 
				    	System.out.println( "End of read settings " ); 
				    	parser.close(); 
				    	break; 
				 
				    case XMLStreamConstants.START_ELEMENT:
				    	//create an stream
				    	Stream stream = new Stream("",0);

				    	//get all attributes from file and set the options
				    	for ( int i = 0; i < parser.getAttributeCount(); i++ ) {
				    		if(parser.getAttributeLocalName( i ).equals("lastStreamID")) {
				    			Stream.lastID = Integer.valueOf(parser.getAttributeValue( i ));
				    		} else if(parser.getAttributeLocalName( i ).equals("id")) {
				    			stream.id = Integer.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("name")) {
				    			stream.name = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("completeCB")) {
				    			stream.completeCB  = Short.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("address")) {
				    			stream.address  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("website")) {
				    			stream.website  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("genre")) {
				    			stream.genre  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("comment")) {
				    			stream.comment  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("singleFileTF")) {
				    			stream.singleFileTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimeHHTF")) {
				    			stream.maxTimeHHTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimeMMTF")) {
				    			stream.maxTimeMMTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimessTF")) {
				    			stream.maxTimessTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxMBTF")) {
				    			stream.maxMBTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("sequenzTF")) {
				    			stream.sequenzTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("patternTF")) {
				    			stream.patternTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("relayServerPortTF")) {
				    			stream.relayServerPortTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxConnectRelayTF")) {
				    			stream.maxConnectRelayTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("relayPlayListTF")) {
				    			stream.relayPlayListTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("timeOutReonTF")) {
				    			stream.timeOutReonTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("proxyTF")) {
				    			stream.proxyTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("useragentTF")) {
				    			stream.useragentTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("sciptSongsTF")) {
				    			stream.sciptSongsTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("metaDataFileTF")) {
				    			stream.metaDataFileTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("interfaceTF")) {
				    			stream.interfaceTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("externTF")) {
				    			stream.externTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("extraArgsTF")) {
				    			stream.extraArgsTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSRelayTF")) {
				    			stream.CSRelayTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSMetaDataTF")) {
				    			stream.CSMetaDataTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSIDTF")) {
				    			stream.CSIDTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSFileSysTF")) {
				    			stream.CSFileSysTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPDelayTF")) {
				    			stream.SPDelayTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPExtraTF1")) {
				    			stream.SPExtraTF1  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPExtraTF2")) {
				    			stream.SPExtraTF2  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPWindowTF1")) {
				    			stream.SPWindowTF1  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPWindowTF2")) {
				    			stream.SPWindowTF2  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPSilenceTF")) {
				    			stream.SPSilenceTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("singleFileCB")) {
				    			stream.singleFileCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimeCB")) {
				    			stream.maxTimeCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("maxMBCB")) {
				    			stream.maxMBCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("sequenzCB")) {
				    			stream.sequenzCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("patternCB")) {
				    			stream.patternCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("cutSongIncompleteCB")) {
				    			stream.cutSongIncompleteCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("neverOverIncompCB")) {
				    			stream.neverOverIncompCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("noDirEveryStreamCB")) {
				    			stream.noDirEveryStreamCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("noIndiviSongsCB")) {
				    			stream.noIndiviSongsCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("createReayCB")) {
				    			stream.createReayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("connectToRelayCB")) {
				    			stream.connectToRelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("createPlaylistRelayCB")) {
				    			stream.createPlaylistRelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("dontSearchAltPortCB")) {
				    			stream.dontSearchAltPortCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("dontAutoReconnectCB")) {
				    			stream.dontAutoReconnectCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("timeoutReconnectCB")) {
				    			stream.timeoutReconnectCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("proxyCB")) {
				    			stream.proxyCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("useragentCB")) {
				    			stream.useragentCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("countBeforStartCB")) {
				    			stream.countBeforStartCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("metaDataCB")) {
				    			stream.metaDataCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("interfaceCB")) {
				    			stream.interfaceCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("externMetaDataCB")) {
				    			stream.externMetaDataCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("extraArgsCB")) {
				    			stream.extraArgsCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSRelayCB")) {
				    			stream.CSRelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSMetaCB")) {
				    			stream.CSMetaCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSIDTagCB")) {
				    			stream.CSIDTagCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSFileSysCB")) {
				    			stream.CSFileSysCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("XS2CB")) {
				    			stream.XS2CB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPDelayCB")) {
				    			stream.SPDelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPExtraCB")) {
				    			stream.SPExtraCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPWindowCB")) {
				    			stream.SPWindowCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPSilenceCB")) {
				    			stream.SPSilenceCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("IDV1CB")) {
				    			stream.IDV1CB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("IDV2CB")) {
				    			stream.IDV2CB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} 
				    		
				    		else if (parser.getAttributeLocalName( i ).equals("send_bitrate")) {
				    			stream.send_bitrate  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("send_metainvervall")) {
				    			stream.send_metainvervall  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("send_name")) {
				    			stream.send_name  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("send_serverName")) {
				    			stream.send_serverName  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("exeCommand")) {
				    			stream.setExeCommand(parser.getAttributeValue( i ));
				    		}
				    	}
				    	//an ID lower 1 is invalid
				    	if(stream.id > 0) {
				    		addStreamToVector(stream);
				    	}
				    	break; 
				 
				    default: 
				    	break; 
				  }
				parser.next(); 
			}

		} catch (FileNotFoundException e) {
			System.err.println("No configuartion file found: Streams.xml");
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get the default stream from xml-file and create
	 * the stream object
	 */
	public void loadDefaultStreamOnStart() {
		String loadPath =  new Control_GetPath().getStreamRipStarPath();
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser;
			parser = factory.createXMLStreamReader( new FileInputStream(loadPath+"/DefaultStream.xml" ) );
			while ( parser.hasNext() ) { 
	 
				switch ( parser.getEventType() ) { 
					case XMLStreamConstants.START_DOCUMENT: 
						System.out.println( "Loading file Streams.xml" );
						parser.getEventType();
						break; 
				 
				    case XMLStreamConstants.END_DOCUMENT: 
				    	System.out.println( "End of read settings " ); 
				    	parser.close(); 
				    	break; 
				 
				    case XMLStreamConstants.START_ELEMENT:
				    	//create an stream
				    	Stream stream = new Stream("",0);

				    	//get all attributes from file and set the options
				    	for ( int i = 0; i < parser.getAttributeCount(); i++ ) {
				    		if(parser.getAttributeLocalName( i ).equals("id")) {
				    			stream.id = Integer.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("name")) {
				    			stream.name = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("completeCB")) {
				    			stream.completeCB  = Short.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("address")) {
				    			stream.address  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("website")) {
				    			stream.website  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("genre")) {
				    			stream.genre  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("comment")) {
				    			stream.comment  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("singleFileTF")) {
				    			stream.singleFileTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimeHHTF")) {
				    			stream.maxTimeHHTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimeMMTF")) {
				    			stream.maxTimeMMTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimessTF")) {
				    			stream.maxTimessTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxMBTF")) {
				    			stream.maxMBTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("sequenzTF")) {
				    			stream.sequenzTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("patternTF")) {
				    			stream.patternTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("relayServerPortTF")) {
				    			stream.relayServerPortTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("maxConnectRelayTF")) {
				    			stream.maxConnectRelayTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("relayPlayListTF")) {
				    			stream.relayPlayListTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("timeOutReonTF")) {
				    			stream.timeOutReonTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("proxyTF")) {
				    			stream.proxyTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("useragentTF")) {
				    			stream.useragentTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("sciptSongsTF")) {
				    			stream.sciptSongsTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("metaDataFileTF")) {
				    			stream.metaDataFileTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("interfaceTF")) {
				    			stream.interfaceTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("externTF")) {
				    			stream.externTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("extraArgsTF")) {
				    			stream.extraArgsTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSRelayTF")) {
				    			stream.CSRelayTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSMetaDataTF")) {
				    			stream.CSMetaDataTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSIDTF")) {
				    			stream.CSIDTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("CSFileSysTF")) {
				    			stream.CSFileSysTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPDelayTF")) {
				    			stream.SPDelayTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPExtraTF1")) {
				    			stream.SPExtraTF1  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPExtraTF2")) {
				    			stream.SPExtraTF2  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPWindowTF1")) {
				    			stream.SPWindowTF1  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPWindowTF2")) {
				    			stream.SPWindowTF2  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("SPSilenceTF")) {
				    			stream.SPSilenceTF  = parser.getAttributeValue( i );
				    		} else if (parser.getAttributeLocalName( i ).equals("singleFileCB")) {
				    			stream.singleFileCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("maxTimeCB")) {
				    			stream.maxTimeCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("maxMBCB")) {
				    			stream.maxMBCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("sequenzCB")) {
				    			stream.sequenzCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("patternCB")) {
				    			stream.patternCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("cutSongIncompleteCB")) {
				    			stream.cutSongIncompleteCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("neverOverIncompCB")) {
				    			stream.neverOverIncompCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("noDirEveryStreamCB")) {
				    			stream.noDirEveryStreamCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("noIndiviSongsCB")) {
				    			stream.noIndiviSongsCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("createReayCB")) {
				    			stream.createReayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("connectToRelayCB")) {
				    			stream.connectToRelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("createPlaylistRelayCB")) {
				    			stream.createPlaylistRelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("dontSearchAltPortCB")) {
				    			stream.dontSearchAltPortCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("dontAutoReconnectCB")) {
				    			stream.dontAutoReconnectCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("timeoutReconnectCB")) {
				    			stream.timeoutReconnectCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("proxyCB")) {
				    			stream.proxyCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("useragentCB")) {
				    			stream.useragentCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("countBeforStartCB")) {
				    			stream.countBeforStartCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("metaDataCB")) {
				    			stream.metaDataCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("interfaceCB")) {
				    			stream.interfaceCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("externMetaDataCB")) {
				    			stream.externMetaDataCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("extraArgsCB")) {
				    			stream.extraArgsCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSRelayCB")) {
				    			stream.CSRelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSMetaCB")) {
				    			stream.CSMetaCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSIDTagCB")) {
				    			stream.CSIDTagCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("CSFileSysCB")) {
				    			stream.CSFileSysCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("XS2CB")) {
				    			stream.XS2CB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPDelayCB")) {
				    			stream.SPDelayCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPExtraCB")) {
				    			stream.SPExtraCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPWindowCB")) {
				    			stream.SPWindowCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("SPSilenceCB")) {
				    			stream.SPSilenceCB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("IDV1CB")) {
				    			stream.IDV1CB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		} else if (parser.getAttributeLocalName( i ).equals("IDV2CB")) {
				    			stream.IDV2CB  = Boolean.valueOf(parser.getAttributeValue( i ));
				    		}
				    	}
				    	this.setDefaultStream(stream);
				    	break; 
				 
				    default: 
				    	break; 
				  }
				parser.next(); 
			}

		} catch (FileNotFoundException e) {
			System.err.println("No configuartion file found: DefaultStream.xml");
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Add a stream to the vector of all streams
	 * @param newStream
	 */
	public void addStreamToVector(Stream newStream) {
		streamVector.add(newStream);
	}
	
	/**
	 * Return the vector with all streams in it
	 * @return
	 */
	public Vector<Stream> getStreamVector() {
		return streamVector;
	}
	
	/**
	 * Collect all path together an pack it in a String[]
	 * @return: String[] with all paths in it
	 */
	public String[] getPaths() {
		String[] path = new String[5];
		
		path[0] = streamripperPath;
		path[1] = mp3PlayerPath;
		path[2] = generalPath;
		path[3] = webBrowserPath;
		path[4] = fileBrowserPath;
		
		return path;
	}
	
	/**
	 * update all path in this class
	 * @param path: all path in a array
	 */
	public void setPaths(String[] path) {
		streamripperPath = path[0];
		mp3PlayerPath = path[1];
		generalPath = path[2];
		webBrowserPath = path[3];
		fileBrowserPath = path[4];
	}
	
	/**
	 * Removes the stream, with the given id, from the vector
	 * of all streams
	 * @param id: The id representing the stream in xml-file
	 */
	public void removeStreamFromVector(int id) {
		for(int i=0; i < streamVector.capacity() ;i++) {
			if(streamVector.get(i).id == id) {
				streamVector.remove(i);
				streamVector.trimToSize();
				System.gc();
				break;
			}
		}
	}
	
	/**
	 * Get the path to streamripper
	 * @return: String with path in it
	 */
	public String getStreamripperPath() {
		return streamripperPath;
	}
	
	/**
	 * Get the path to where all stream should be saved
	 * @return: String with path in it
	 */
	public String getGeneralPath() {
		return generalPath;
	}
	
	/**
	 * Get the path to the webbrowser
	 * @return: String with path in it
	 */
	public String getWebBrowserPath() {
		return webBrowserPath;
	}
	
	/**
	 * Get the path to the filebrowser
	 * @return: String with path in it
	 */
	public String getFileBrowserPath() {
		return fileBrowserPath;
	}
	
	/**
	 * Get the path to the mp3Player
	 * @return: String with path in it
	 */
	public String getMP3PlayerPath() {
		return mp3PlayerPath;
	}
	
	
	public void startMp3Player(String content) {
		String path = getMP3PlayerPath();
		if(path == null)
			System.err.println("Can't find mp3Player");
		else
			new Control_RunExternProgram(path + " " + content).run();
	}
	
	public void startWebBrowser(String url) {
		String path = getWebBrowserPath();
		if(path == null || path.equals("")) {
			System.err.println("Can't find the webbrowser");
			JOptionPane.showMessageDialog(mainGui,
						trans.getString("setBrowserOnly"));
		}
		else
			new Control_RunExternProgram(path + " " + url).run();
	}
	
	public Process startStreamripper(Stream stream) {
		Process streamProcess = null;

		String address = stream.address;
		Vector<String> options = collectOptions(stream);
		String streamripperPath = getStreamripperPath();
		options.add(0,address);
		options.add(0,streamripperPath);

		try {
			System.out.println("trying to start with command: \n");
			System.out.println(options);
			streamProcess = new ProcessBuilder(options).start();
		} catch (IOException e) {
		    System.err.println("Error while executing streamripper"+e);
		}
		
		//put all option from the vector into an String
		int count = options.capacity();
		String fullCommand = "";
		for(int i=0; i<count; i++) {
			fullCommand += options.elementAt(i)+" ";
		}
		stream.setExeCommand(fullCommand);
		return streamProcess;
	}
	
	//give the longest name from all streams back
	//this is needed for the max column width
	public String getlongestName() {
		String name = "";
		for(int i=0;i<streamVector.capacity();i++) {
			if(streamVector.get(i).name.length() > name.length())
				name = streamVector.get(i).name;
		}
		return name;
	}
	
	/**
	 * Look in der StreamVector, witch collects all streams
	 * for an stream with the given ID
	 * @param id: the ID for the stream in xml-file
	 * @return The Stream with the given ID
	 */
	public Stream getStreamByID(int id) {
		for(int i=0; i< streamVector.capacity(); i++) {
			if(streamVector.get(i).id == id) {
				return streamVector.get(i);
			}
		}
		return null;
	}
	
	/**
	 * looks on witch position in the vector a stream is
	 * @param id: the stream ID in xml-file
	 * @return the position; -1 for not found
	 */
	public int getPlaceInStreamVector(int id) {
		for(int i=0; i< streamVector.capacity(); i++) {
			if(streamVector.get(i).id == id) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * looks if a Url in the streamVector already exist
	 * @param Url: to test url
	 * @return true if exist, else false
	 */
	public Boolean existURL(String Url){
		for(int i=0; i< streamVector.capacity(); i++) {
			if(streamVector.get(i).address.equals(Url))
				return true;
		}
		return false;
	}
	
	/**
	 * stops all streams in the streamvector if the are recording
	 */
	public void stopAllStreams() {
		for(int i=0; i< streamVector.capacity(); i++) {
			if(streamVector.get(i).getStatus())
				streamVector.get(i).setStop();
		}
	}
	
	/**
	 * disable all options that are newer than streamripper 1.63.5 in all streams
	 */
	public void disableAllNewOptionsForAllStreams() {
		for(int i=0; i< streamVector.capacity(); i++) {
			
			//if "-o version" is set switch to "-o larger" 
			if(streamVector.get(i).completeCB == 0) {
				streamVector.get(i).completeCB = 1;
			}
				
		}
	}
	
	/**
	 * return all options for a stream in the way streamripper wants it
	 * @param id: the ID of the stream in xml-file
	 * @return: String with all options
	 */
	public Vector<String> collectOptions(Stream stream) {
		
		Vector<String> options = new Vector<String>(0,1);
		
		//First: Try to get the general path if one exist
		String tmp = getGeneralPath();
		if(!(tmp == null || tmp.equals(""))) {
			options.add("-d");
			options.add(tmp);
		}
		
		//test, witch is activated 
		//SINGLEFILE_CB
		if(stream.singleFileCB) {
			options.add("-a");
			options.add(stream.singleFileTF);
		}
		
		//MAXTIME_CB
		if(stream.maxTimeCB) {
			int length = 0;
			try {
				length += Integer.parseInt(stream.maxTimeHHTF)*60*60;
				length += Integer.parseInt(stream.maxTimeMMTF)*60;
				length += Integer.parseInt(stream.maxTimessTF);
			} catch (NumberFormatException e) {
				System.err.println("Wrong Number in options" +
						"Please use numbers only in lengthfields");
			}
			options.add("-l");
			options.add(""+length);
		}
		
		//MAXMB_CB
		if(stream.maxMBCB) {
			options.add("-M");
			options.add(stream.maxMBTF);
		}
		
		//FIELSEQUENZ_CB
		if(stream.sequenzCB) {
			options.add("-q");
			options.add(stream.sequenzTF);
		}
		
		//PATTERN_CB
		if(stream.patternCB) {
			options.add("-D");
			options.add(stream.patternTF);
		}
		
		//NEVER_OVER_COMP_RENAME_CB
		if(stream.completeCB == 0) {
			options.add("-o");
			options.add("version");
		}
		
		//EVER_OVER_COMP_CB
		if(stream.completeCB == 1) {
			options.add("-o");
			options.add("always");
		}
		
		//NEVER_OVER_COMP_CB
		if(stream.completeCB == 2) {
			options.add("-o");
			options.add("never");
		}
		
		//OVER_BIGGER_COMP_CB
		if(stream.completeCB == 3) {
			options.add("-o");
			options.add("larger");
		}
		
		//TRUNCATE_INCOMP_CB
		if(stream.cutSongIncompleteCB) {
			options.add("-T");
		}
		
		//NEVER_OVER_INCOMP_CB
		if(stream.neverOverIncompCB) {
			options.add("-t");
		}
		
		//DONT_CREATE_DIR_STREAM_CB
		if(stream.noDirEveryStreamCB) {
			options.add("-s");
		}
		
		//DONT_CREATE_INDV_SONGs_CB
		if(stream.noIndiviSongsCB) {
			options.add("-A");
		}
		
		//CREATE_RELAY_SERVER_CB
		if(stream.createReayCB) {
			options.add("-r");
			options.add(stream.relayServerPortTF);	//port for the relay server
			options.add("-R");						//max connection to relay
			options.add(stream.maxConnectRelayTF);
			
			//CREATE_PLAYLIST_CB
			if(stream.createPlaylistRelayCB) {
				options.add("-L");
				options.add(stream.relayPlayListTF);
			}
		}

		//DONT_SEARCH_ALT_PORT_CB
		if(stream.dontSearchAltPortCB) {
			options.add("-z");
		}
		
		//DONT_AUTO_RECONNEC_CB
		if(stream.dontAutoReconnectCB) {
			options.add("-c");
		}
		
		//TIMEOUT_CB
		if(stream.timeoutReconnectCB) {
			options.add("-m");
			options.add(stream.timeOutReonTF);
		}
		
		//HTTP_PROXY_URL_CB
		if(stream.proxyCB) {
			options.add("-p");
			options.add(stream.proxyTF);
		}
		
		//USERAGENT_CB
		if(stream.useragentCB) {
			options.add("-u");
			options.add(stream.useragentTF);
		}
		
		//COUNT_SONGS_START_CB
		if(stream.countBeforStartCB) {
			options.add("-k");
			options.add(stream.sciptSongsTF);
		}
		
		//META_FILE_CB
		if(stream.metaDataCB) {
			options.add("-w");
			options.add(stream.metaDataFileTF);
		}
		
		//INTERFACE_CB
		if(stream.interfaceCB) {
			options.add("-I");
			options.add(stream.interfaceTF);
		}
		
		//EXTERN_PROG_FETCH_META_CB
		if(stream.externMetaDataCB) {
			options.add("-E");
			options.add(stream.externTF);
		}
		
		//EXTRA_STREAMRIPPER_ARGS_CB
		if(stream.extraArgsCB) {
			//split where a space is found
			Pattern p = Pattern.compile( "[ ]" );
			String[] args = p.split(stream.extraArgsTF);
			for ( int i = 0; i < args.length; i++ ) {
				options.add(args[i]);
			}
		}
		
		//CS_RELAY_CB
		if(stream.CSRelayCB) {
			options.add("--codeset-relay="+stream.CSRelayTF);
		}
		
		//CS_METADATA_CB
		if(stream.CSMetaCB) {
			options.add("--codeset-metadata="+stream.CSMetaDataTF);
		}
		
		//CS_IDTAG_CB
		if(stream.CSIDTagCB) {
			options.add("--codeset-id3="+stream.CSIDTF);
		}
		
		//CS_FILESYS_CB
		if(stream.CSFileSysCB) {
			options.add("--codeset-filesys="+stream.CSFileSysTF);
		}
		
		//USE_XS2_CB
		if(stream.XS2CB) {
			options.add("--xs2");
		}
		
		//TIME_SHIFT_METADATA_CB
		if(stream.SPDelayCB) {
			options.add("--xs-offset="+stream.SPDelayTF);
		}
		
		//TIME_SHIFT_EXTRA_CB
		if(stream.SPExtraCB) {
			options.add("--xs-padding="+stream.SPExtraTF1+":"+stream.SPExtraTF2);
		}
		
		//TIME_SEARCH_REL_META_CB
		if(stream.SPWindowCB) {
			options.add("--xs-search-window="+stream.SPWindowTF1+":"+stream.SPWindowTF2);
		}
		
		//TIME_SILENCE_CB
		if(stream.SPSilenceCB) {
			options.add("--xs-silence-length="+stream.SPSilenceTF);
		}
		
		//ID1_CB
		if(stream.IDV1CB) {
			options.add("--with-id3v1");
		}
		
		//ID2_CB
		if(!stream.IDV2CB) {
			options.add("--without-id3v2");
		}
		
		return options;
	}
}
	