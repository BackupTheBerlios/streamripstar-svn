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
	 * streamInfo[0] = Name 
	 * streamInfo[1] = now Playing 
	 * streamInfo[2] = Listeners
	 * streamInfo[3] = Bitrate 
	 * streamInfo[4] = Format
	 * streamInfo[5] = ID
	 * streamInfo[6] = Genres
	 * streamInfo[7] = Website
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
				//new shoutcast
				URL shoutcast = new URL("http://www.shoutcast.com/genre-ajax/"+genre+"/?strIndex="+(currentPage*maxResults)+
						"&count="+maxResults+"&ajax=true&mode=listeners&order=desc");
				
				readGenresStream = shoutcast.openStream();
				bw = new BufferedReader(new InputStreamReader(readGenresStream));
	
				// create a stream to save the info from the website
				String[] streamInfo = new String[8];
				
				while (!stopSearching && (text = bw.readLine()) != null) {
					try {
						//from here we need all from the source code
						//Look for the number of results
						if(text.contains("	<input type=\"hidden\" class=\"numfound\" ")){
							int results = Integer.valueOf(text.substring(
									text.indexOf("value=")+7, text.lastIndexOf("\"")));
							totalPages = (results / maxResults);
							
						}
						
						//here starts a stream
						if(text.contains("class=\"stationcol\"")) {
							//next line starts a stream
							text = readNextHtmlLine(true);
					
							//now find the ID for the stream
							streamInfo[5] = text.substring(text.indexOf("\" id=\"")+6, text.indexOf("\" title=\""));

							//the name
							streamInfo[0] = text.substring(text.indexOf("\" title=\"")+9,text.indexOf("\" href=\""));

							//The genres
							streamInfo[6] = readNextHtmlLine(false).substring(5).trim();
							
							//The website
							text = readNextHtmlLine(true);
							streamInfo[7] = text.substring(text.indexOf(" href=\"")+7,text.indexOf("\" target="));
							
							//The current title (now playing...)
							text = readNextHtmlLine(true);
							streamInfo[1] = text.substring(text.indexOf("\" title=\"")+9,text.lastIndexOf("\""));
							
							//now the max listeners
							streamInfo[2] = readNextHtmlLine(false).trim();
							
							//now have a look at the bitrate
							streamInfo[3] = readNextHtmlLine(false).trim();
							
							//which Format do we use?
							streamInfo[4] = readNextHtmlLine(false).trim();
							
							//This stream has all information
							streams.add(streamInfo);
							
							//create an new for the next one
							streamInfo = new String[8];					
						}

					} catch (NullPointerException e) {
						System.out.println("Error while loading from shoutcast website 1");
					} catch (StringIndexOutOfBoundsException e) {
						System.out.println("Error while loading from shoutcast website 2");
						e.printStackTrace();
					} catch (NumberFormatException e) {
						System.err.println("Controled Crash in StreamBrowser");
						e.printStackTrace();
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
	 * streaminfo[1] = now Playing 
	 * streaminfo[2] = Listeners
	 * streaminfo[3] = Bitrate 
	 * streaminfo[4] = Format
	 * streaminfo[5] = ID
	 * streamInfo[6] = Genres
	 * streamInfo[7] = Website
	 * 
	 * @param genre the keyword for searching
	 * @param keyword true, if the search should be with keywords 
	 */
	public void getStreamsPerKeyword(String keyword) {
		// make sure, that the Vector of streams is empty
		streams.removeAllElements();
		streams.trimToSize();
		try {

			URL shoutcast = new URL("http://www.shoutcast.com/search-ajax/"+keyword+"?strIndex="+(currentPage*maxResults)+
					"&count="+maxResults+"&ajax=true");
			
			readGenresStream = shoutcast.openStream();
			
			bw = new BufferedReader(new InputStreamReader(readGenresStream));

			// create a stream to save the info from the website
			String[] streamInfo = new String[8];
			
			while (!stopSearching && (text = bw.readLine()) != null) {
				try {
					//from here we need all from the source code
					//Look for the number of results
					if(text.contains("	<input type=\"hidden\" class=\"numfound\" ")){
						int results = Integer.valueOf(text.substring(
								text.indexOf("value=")+7, text.lastIndexOf("\"")));
						totalPages = (results / maxResults);
						
					}
					
					//here starts a stream
					if(text.contains("class=\"stationcol\"")) {
						//next line starts a stream
						text = readNextHtmlLine(true);
				
						//now find the ID for the stream
						streamInfo[5] = text.substring(text.indexOf("\" id=\"")+6, text.indexOf("\" title=\""));

						//the name
						streamInfo[0] = text.substring(text.indexOf("\" title=\"")+9,text.indexOf("\" href=\""));

						//The genres
						streamInfo[6] = readNextHtmlLine(false).substring(5).trim();
						
						//The website
						text = readNextHtmlLine(true);
						streamInfo[7] = text.substring(text.indexOf(" href=\"")+7,text.indexOf("\" target="));
						
						//The current title (now playing...)
						text = readNextHtmlLine(true);
						streamInfo[1] = text.substring(text.indexOf(" title=\"")+8,text.lastIndexOf("\""));

						readNextHtmlLine(false);
						
						//now the max listeners
						streamInfo[2] = readNextHtmlLine(false).trim();
						
						//now have a look at the bitrate
						streamInfo[3] = readNextHtmlLine(false).trim();
						
						//which Format do we use?
						streamInfo[4] = readNextHtmlLine(false).trim();
						
						//This stream has all information
						streams.add(streamInfo);
						
						//create an new for the next one
						streamInfo = new String[8];					
					}

				} catch (NullPointerException e) {
					System.out.println("Error while loading from shoutcast website 1");
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Error while loading from shoutcast website 2");
				} catch (NumberFormatException e) {
					System.err.println("Controled Crash in StreamBrowser");
					e.printStackTrace();
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
	 * @param getNoHTMLText: if true, is returns the no filtert text, instead of the filtered
	 */
	public String readNextHtmlLine(boolean getNoHTMLText) {
		String next = "";
		
		try {
			while(!stopSearching && (text = bw.readLine()) != null) {
				while(!stopSearching  && !text.trim().endsWith(">")) {
					text += bw.readLine();
				}
				String noHTML = text.replaceAll("\\<.*?>","").trim();
				if(noHTML.length() > 0) {
					if(getNoHTMLText) {
						return text;
					} else {
						return noHTML;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			return "";
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
