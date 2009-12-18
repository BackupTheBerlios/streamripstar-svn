package StreamRipStar;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;

public class Control_http_Shoutcast {
	private BufferedReader bw = null;
	private InputStream readGenresStream = null;
	private InputStream readStream = null;
	private String text = "";
	private Vector<Vector<String>> addGenre = new Vector<Vector<String>>(0, 1);
	private Vector<String> tmpGenres = new Vector<String>(0, 1);
	// inlcude all streams for a genre
	private Vector<String[]> streams = new Vector<String[]>(0, 1);
	private String subpage = ""; // include the mainpage
	private Boolean stopSearching = false; // stop an action
	private int numberOfStreams = 1;

	// streaminfo[0] = Name
	// streaminfo[1] = Website
	// streaminfo[2] = Genre
	// streaminfo[3] = now Playing
	// streaminfo[4] = Listeners/MaxListeners
	// streaminfo[5] = Bitrate
	// streaminfo[6] = Format
	// streaminfo[7] = Link

	// Constructor
	public Control_http_Shoutcast() {

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
	 * Get all Genres from the website www.shoutcast.com save it into a String
	 * Vector in this class
	 */
	public void getGenresFromWebsite() {
		try {
			URL shoutcast = new URL("http://classic.shoutcast.com");
			readGenresStream = shoutcast.openStream();
			bw = new BufferedReader(new InputStreamReader(readGenresStream));
			while (!stopSearching && (text = bw.readLine()) != null) {
				// look in the source for the SearchBox including the genres
				// if found looks for the genres
				if (text.trim().startsWith("<td class=\"SearchBox\"")) {
					while (!stopSearching && (text = bw.readLine()) != null) {

						// form action: look for the page in with
						// the request must be send
						if (text.contains("<form action=")) {
							Scanner f = new Scanner(text)
									.useDelimiter("\\s*\"\\s*");
							f.next();
							subpage = f.next();
							f.close();
						}

						// here we look for the genres itself
						// and extract it into a vector of Strings
						if (text.contains("<OPTION VALUE=")) {
							Scanner f = new Scanner(text)
									.useDelimiter("\\s*\"\\s*");
							f.next();
							f.next();
							String tmp = f.next();
							if (!tmp.startsWith("> - ")) {
								// add collected Genres + Subgenres
								// to one Genre
								if (tmpGenres.capacity() > 0)
									addGenre.add(tmpGenres);
								tmpGenres = new Vector<String>(0, 1);
								tmpGenres.add(tmp.substring(1));
							} else {
								tmpGenres.add(tmp.substring(4));
							}
							f.close();
						}

						// end of SearchBox
						// stop searching
						if (text.contains("</SELECT>")) {
							stopSearching = true;
						}
					}
				}
			}
			// add last genre to stream
			if (tmpGenres.capacity() > 0) {
				addGenre.add(tmpGenres);
			}
			
			if(addGenre.get(0).get(0).equals("lass=")) {
				addGenre.remove(0);
				addGenre.trimToSize();
			}
				
			
			tmpGenres = new Vector<String>(0, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
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
	 * Browse the list of stream on shoutcast.com in the given genre and save it
	 * into an Array of Strings into an vector called streaminfo. streaminfo
	 * contains following information: streaminfo[0] = Name streaminfo[1] =
	 * Website streaminfo[2] = Genre streaminfo[3] = now Playing streaminfo[4] =
	 * Listeners/MaxListeners streaminfo[5] = Bitrate streaminfo[6] = Format
	 * streaminfo[7] = Link
	 * 
	 * @param genre
	 */
	public void getStreamsPerGenre(String genre) {
		// make sure, that the Vector of streams is empty
		streams.removeAllElements();
		streams.trimToSize();
		try {
			// for testing
			URL shoutcast = new URL("http://classic.shoutcast.com" + subpage
					+ "?sgenre=" + genre + "&numresult=100");
			readGenresStream = shoutcast.openStream();
			bw = new BufferedReader(new InputStreamReader(readGenresStream));

			// look for the stream with the number "numberOfStreams"
			String[] streamInfo = new String[8];

			while (!stopSearching && (text = bw.readLine()) != null) {
				if (text.contains("<b>" + numberOfStreams + "</b>")) {
					// only write in vector, if have the right Data
					// witch only can happen, if numberOfStreams > 1
					if (numberOfStreams > 1) {
						// send a copy in vector
						streams.add(streamInfo);
						streamInfo = new String[8];
					}

					// clear data
					int x = streamInfo.length;
					for (int fx = 0; fx < x; fx++) {
						streamInfo[fx] = "";
					}
					numberOfStreams++;
				}

				// look for the link
				try {
					if (text.contains("/sbin/shoutcast-playlist")) {
						Scanner f = new Scanner(text)
								.useDelimiter("\\s*href=\"\\s*");
						f.next();
						String tmpA = f.next();
						f = new Scanner(tmpA).useDelimiter("\\s*\"\\s*");
						streamInfo[7] = f.next();
						f.close();
					}

					// look for Genre'
					if (text.contains("<b>[")) {
						Scanner f = new Scanner(text)
								.useDelimiter("\\s*<b>\\s*");
						f.next();
						String tmpA = f.next();
						f = new Scanner(tmpA).useDelimiter("\\s*]\\s*");
						streamInfo[2] = f.next().substring(1);
						f.close();
					}

					// look for the Website and strean name
					if (text.contains("_scurl\"")) {

						// website
						Scanner f = new Scanner(text)
								.useDelimiter("\\s*href=\"\\s*");
						f.next();
						String tmpA = f.next();
						f = new Scanner(tmpA).useDelimiter("\\s*\"\\s*");
						streamInfo[1] = f.next();
						f.close();

						// name
						f = new Scanner(text).useDelimiter("\\s*<a\\s*");
						f.next();
						tmpA = f.next();
						f = new Scanner(tmpA).useDelimiter("\\s*\">\\s*");
						f.next();
						tmpA = f.next();
						f = new Scanner(tmpA).useDelimiter("\\s*</a\\s*");
						streamInfo[0] = f.next();
						f.close();
					}

					// actual title
					// then reads twice a line and fetching Listeners
					// then reads twice a line and fetching the bitrate
					// then reads five times a line and fetching the format
					if (text.contains("Now Playing")) {
						// now playing:
						Scanner f = new Scanner(text)
								.useDelimiter("\\s*</font>\\s*");
						f.next();
						streamInfo[3] = f.next();
						f.close();

						// listeners/Max listeners
						bw.readLine();
						text = bw.readLine();
						f = new Scanner(text).useDelimiter("\\s*font\\s*");
						f.next();
						String tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
						streamInfo[4] = f.next();

						// bitrate
						bw.readLine();
						text = bw.readLine();
						f = new Scanner(text).useDelimiter("\\s*font\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
						streamInfo[5] = f.next();
						f.close();

						// Format
						// read the fifth line
						bw.readLine();
						bw.readLine();
						bw.readLine();
						bw.readLine();
						text = bw.readLine();
						f = new Scanner(text).useDelimiter("\\s*font\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
						streamInfo[6] = f.next();
						f.close();
					}
					
					if(text.trim().equals("</font></td>")) {
						// listeners/Max listeners
						bw.readLine();
						text = bw.readLine();
						Scanner f = new Scanner(text).useDelimiter("\\s*font\\s*");
						f.next();
						String tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
						streamInfo[4] = f.next();

						// bitrate
						bw.readLine();
						text = bw.readLine();
						f = new Scanner(text).useDelimiter("\\s*font\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
						streamInfo[5] = f.next().trim();
						f.close();

						// Format
						// read the fifth line
						bw.readLine();
						bw.readLine();
						bw.readLine();
						bw.readLine();
						text = bw.readLine();
						f = new Scanner(text).useDelimiter("\\s*font\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
						f.next();
						tmpB = f.next();
						f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
						streamInfo[6] = f.next();
						f.close();
					}
				} catch (NoSuchElementException f) {
					System.out.println("Cant find everything the the html");
				}
			}

			// add last stream to vector
//			streams.add(streamInfo);
		} catch (Exception e) {
			System.out.println("HHHIIIIIIIIERRR");
			if (e.getMessage().startsWith("stream is closed")) {
				stopSearching = true;
			} else
				e.printStackTrace();
		} finally {
			// reset for new run
			stopSearching = false;
			numberOfStreams = 1;

			if (readGenresStream != null) {
				try {
					readGenresStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * This Method return an Vector of Strings witch contains all genres roots
	 * from site
	 * 
	 * @return
	 */
	public Vector<Vector<String>> getAllGenres() {
		return addGenre;
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
		String shoutcast = "http://classic.shoutcast.com";
		return shoutcast;
	}
}
