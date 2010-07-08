package control;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

public class Control_http_Shoutcast {
	private BufferedReader bw = null;
	private InputStream readGenresStream = null;
	private InputStream readStream = null;
	private String text = "";
	private Vector<String[]> streams = new Vector<String[]>(0, 1);
	private Boolean stopSearching = false; // stop an action
	private int currentPage = 0;
	private int totalPages = 0;
	private int maxResults = 100;
	
	// streaminfo[0] = Name
	// streaminfo[1] = Website
	// streaminfo[2] = Genre
	// streaminfo[3] = now Playing
	// streaminfo[4] = Listeners/MaxListeners
	// streaminfo[5] = Bitrate
	// streaminfo[6] = Format
	// streaminfo[7] = Link

	/**
	 * the default constructor. Here is nothing to do at the moment
	 */
	public Control_http_Shoutcast() {

	}
	
	/**
	 * set the to laod page of the first one
	 */
	public void resetPages() {
		currentPage=0;
		totalPages=0;
	}
	
	/**
	 * Increases the to load page by one. This will only happen
	 * if the current page is not the last one
	 */
	public void nextPage() {
		if(totalPages > currentPage) {
			currentPage++;
		}
	}
	
	/**
	 * decreases the to load page by one. This will only happen
	 * if the current page is not the first
	 */
	public void prevoiousPage() {
		if( currentPage >= 1) {
			currentPage--;
		}
	}
	
	/**
	 * gives the current page back, which is loaded from shoutcast
	 * @return the current page for the current loaded search
	 */
	public int getCurrentPage() {
		return currentPage+1;
	}
	
	/**
	 * gives the total amount of pages back, which represents this
	 * search which all results
	 * @return All pages as an integer
	 */
	public int getTotalPages() {
		return totalPages+1;
	}
	
	/**
	 * Returns the number of pages available with streams if 
	 * this search. 
	 * @return [0] returns the current page
	 * [1] returns the max available pages
	 */
	public int[] getPages() {
		int[] pages = new int[2];
		pages[0] = currentPage;
		pages[1] = totalPages;
		
		return pages;
	}
	
	/**
	 * This method look for the stream address + port in a .pls or .m3u file and
	 * return the first one found. If it found a stream than it returns it. else
	 * it returns an empty string
	 * 
	 * @param streamURL
	 * @return
	 */
	public String getfirstStreamFromURL(String streamURL) {
		String url = "";
		String tmp = "";
		Boolean breakLook = false;

		try {
			// create URL
			URL stream = new URL(streamURL);

			// create Stream and open it to url
			readStream = stream.openStream();

			// create an buffered reader
			bw = new BufferedReader(new InputStreamReader(readStream));

			// read data and look for first address
			while (!breakLook && (tmp = bw.readLine()) != null) {

				// if it a .pls simply look for line File
				if (tmp.contains("File")) {
					// after = start the address
					int startAddress = tmp.indexOf("=");

					// read into url
					url = tmp.substring(startAddress + 1);

					// stop -> run finally() and return url
					breakLook = true;
				}
				// if a line contains the line http
				// StreamRipStar think its the address
				else if (tmp.contains("http://")) {
					url = tmp;
					// stop -> run finally() and return url
					breakLook = true;
				}
			}
		} catch (Exception e) {
			System.out.println("Could not get the playlist file from server");
			return null;
		} finally {
			if (readStream != null) {
				try {
					readStream.close();
				} catch (IOException e) {
				}
			}
		}
		return url;
	}

	/**
	 * Browse the list of stream on shoutcast.com in the given genre and save it
	 * into an Array of Strings into an vector called streaminfo. streaminfo
	 * contains following information: 
	 * 
	 * streaminfo[0] = Name 
	 * streaminfo[1] = Website 
	 * streaminfo[2] = Genre 
	 * streaminfo[3] = now Playing 
	 * streaminfo[4] = Listeners
	 * streaminfo[5] = Bitrate 
	 * streaminfo[6] = Format
	 * streaminfo[7] = ID
	 * 
	 * @param genre the keyword (most cases the genre) 
	 * @param keyword true, if the search should be with keywords 
	 */
	public void getStreamsPerGenre(String genre, boolean keyword) {
		if(keyword) {
			getStreamsPerKeyword(genre);
		} else {
			// make sure, that the Vector of streams is empty
			streams.removeAllElements();
			streams.trimToSize();
			try {
				// for testing
				URL shoutcast = new URL("http://shoutcast.com/directory/genreSearchResult.jsp?startIndex="+((currentPage*maxResults)+1)+
						"&sgenre="+ genre + "&numresult="+maxResults);
				readGenresStream = shoutcast.openStream();
				bw = new BufferedReader(new InputStreamReader(readGenresStream));
	
				// create a stream to save the info from the website
				String[] streamInfo = new String[8];
				Boolean firstStationFound = false;
				
				while (!stopSearching && (text = bw.readLine()) != null) {
					try {
						//from here we need all from the source code
						//Look for the number of results
						if(text.contains("totalResults=")){
							int results = Integer.valueOf(text.substring(
									text.indexOf("totalResults=")+13, text.indexOf(";")));
							totalPages = (results / maxResults);
							
						}
						
						if(text.contains("onClick=\"holdStationID('")) {
							streamInfo[7] = text.substring(
									text.indexOf("onClick")+24,text.indexOf("')"));
						}
						
						
						String noHTMLtext = text.toString().replaceAll("\\<.*?>","").trim();
						
						if(noHTMLtext.length() > 0) {
							
							//here we get the name of this stream
							if(noHTMLtext.startsWith("Station:")) {
								if(!firstStationFound) {
									//name of this stream
									String tmp = readNextHtmlLine();
									streamInfo[0] = tmp.substring(tmp.indexOf("title")+7);
									
									//current title
									readNextHtmlLine();
									readNextHtmlLine();
									streamInfo[3] = readNextHtmlLine();
									
									//the genre
									readNextHtmlLine();
									streamInfo[2] = readNextHtmlLine();
									
									//the amout of current Listener
									streamInfo[4] = readNextHtmlLine().replace(",", "");
	
	
									readNextHtmlLine();
									readNextHtmlLine();
									readNextHtmlLine();
									readNextHtmlLine();
									
									//the link to the website
									streamInfo[1] = text.substring(text.indexOf("href=\"")+6, text.indexOf("target=\"_")-2);
									           
									//name of this stream
									streamInfo[0] = readNextHtmlLine();
									readNextHtmlLine();
									
									//der Link zur Website
	//								streamInfo[1] = text.substring(text.indexOf("href=")+6, text.indexOf("target=\"")-2);
									
									//the Bitrate
									while(!text.startsWith("Bitrate:")){
										text = readNextHtmlLine().trim();
									}
									streamInfo[5] = text.substring(8, text.indexOf("kbps")).trim();
									
									//in which format the stream is send
									streamInfo[6] = readNextHtmlLine().substring(5).trim();
									
									//der stream ist damit komplett
									streams.add(streamInfo);
									
									//erzeuge einen neuen für den nächsten Stream
									streamInfo = new String[8];
									
									firstStationFound = false;
									
								} else {
									firstStationFound = true;
									
								}
							}
						}
					} catch (NullPointerException e) {
						System.out.println("Error while loading from shoutcast website");
					} catch (StringIndexOutOfBoundsException e) {
						System.out.println("Error while loading from shoutcast website");
						firstStationFound = false;
					}
				}
			} catch (Exception e) {
				if (e.getMessage().startsWith("stream is closed")) {
					stopSearching = true;
				} else {
					e.printStackTrace();
				}
			} finally {
				// reset for new run
				stopSearching = false;
	
				if (readGenresStream != null) {
					try {
						readGenresStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	/**
	 * Browse the list of stream on shoutcast.com in the given genre and save it
	 * into an Array of Strings into an vector called streaminfo. streaminfo
	 * contains following information: 
	 * 
	 * streaminfo[0] = Name 
	 * streaminfo[1] = Website 
	 * streaminfo[2] = Genre 
	 * streaminfo[3] = now Playing 
	 * streaminfo[4] = Listeners
	 * streaminfo[5] = Bitrate 
	 * streaminfo[6] = Format
	 * streaminfo[7] = ID
	 * 
	 * @param genre the keyword for searching
	 * @param keyword true, if the search should be with keywords 
	 */
	public void getStreamsPerKeyword(String keyword) {
		// make sure, that the Vector of streams is empty
		streams.removeAllElements();
		streams.trimToSize();
		try {
			// for testing
			URL shoutcast = new URL("http://shoutcast.com/directory/searchKeyword.jsp?startIndex="+((currentPage*maxResults)+1)+
					"&s=" + keyword + "&numresult="+maxResults);
			readGenresStream = shoutcast.openStream();
			bw = new BufferedReader(new InputStreamReader(readGenresStream));

			// create a stream to save the info from the website
			String[] streamInfo = new String[8];
			Boolean firstStationFound = false;
			
			while (!stopSearching && (text = bw.readLine()) != null) {
				try {
					//from here we need all from the source code
					//Look for the number of results
					if(text.contains("totalResults=")){
						int results = Integer.valueOf(text.substring(
								text.indexOf("totalResults=")+13, text.indexOf(";")));
						totalPages = (results / maxResults);
						
					}
					
					if(text.contains("onClick=\"holdStationID('")) {
						streamInfo[7] = text.substring(
								text.indexOf("onClick")+24,text.indexOf("')"));
					}
					
					
					String noHTMLtext = text.toString().replaceAll("\\<.*?>","").trim();
					
					if(noHTMLtext.length() > 0) {
						
						//here we get the name of this stream
						if(noHTMLtext.startsWith("Station:")) {
							if(!firstStationFound) {
//								//name of this stream
//								String tmp = readNextHtmlLine();
//								streamInfo[0] = tmp.substring(tmp.indexOf("title")+7);
								//name of this stream
								streamInfo[0] = readNextHtmlLine();
								
								//current title
								readNextHtmlLine();
								streamInfo[3] = readNextHtmlLine();
								
								//the genre
								readNextHtmlLine();
								streamInfo[2] = readNextHtmlLine();
								
								//the amout of current Listener
								streamInfo[4] = readNextHtmlLine().replace(",", "");

								//the Bitrate
								readNextHtmlLine();
								streamInfo[5] = readNextHtmlLine();
								
								//Typ : Mp3 or a++
								readNextHtmlLine();
								streamInfo[6] = readNextHtmlLine();
								
								//name of this stream
								readNextHtmlLine();
								bw.readLine();
								text = bw.readLine();
								streamInfo[1] = text.substring(text.indexOf("href=\"")+6, text.indexOf("target=\"_")-2);
								streamInfo[0] = readNextHtmlLine();

								//der stream ist damit komplett
								streams.add(streamInfo);
								
								//erzeuge einen neuen für den nächsten Stream
								streamInfo = new String[8];
								
								firstStationFound = false;
								
							} else {
								firstStationFound = true;
								
							}
						}
					}
				} catch (NullPointerException e) {
					System.err.println("Error while loading from shoutcast website");
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Error while loading from shoutcast website");
					firstStationFound = false;
				}
			}
		} catch (Exception e) {
			System.out.println("HHHIIIIIIIIERRR");
			if (e.getMessage().startsWith("stream is closed")) {
				stopSearching = true;
			} else
				e.printStackTrace();
		} finally {
			// reset for new run
			stopSearching = false;

			if (readGenresStream != null) {
				try {
					readGenresStream.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * Read as long as the representing no-html String is empty from the 
	 * incoming streamreader
	 * @return the text after the empty line
	 */
	public String readNextHtmlLine() {
		String next = "";
		
		try {
			while(!stopSearching && (text = bw.readLine()) != null) {
				
				String noHTML = text.toString().replaceAll("\\<.*?>","").trim();
				if(noHTML.length() > 0) {
					return noHTML;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return next;
	}
	
	/**
	 * This Method return an Vector of Strings witch contains all streams from a
	 * specific genre
	 * 
	 * @return
	 */
	public Vector<String[]> getStreams() {
		return streams;
	}

	public String getBaseAddress() {
		String shoutcast = "http://yp.shoutcast.com/sbin/tunein-station.pls?id=";
		return shoutcast;
	}
}
