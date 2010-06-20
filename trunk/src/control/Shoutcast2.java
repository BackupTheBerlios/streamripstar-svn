package control;

/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 


public class Shoutcast2 {
	
	/**
	 * Get the genres with all subgenres back
	 * @return the Genres with all subgenres. Every Line of the array
	 * starts with the genre. All other are the subgenres for this genre
	 */
	public String[][] getGenres() {
		String[][] genres = new String[12][];
		
		String[] tmpGenres = new String[23];
		
		tmpGenres[0] = "Alternative";
		tmpGenres[1] = "Adult Altergenres";
		tmpGenres[2] = "nativeBritpop";
		tmpGenres[3] = "Classic Alternative";
		tmpGenres[4] = "College";
		tmpGenres[5] = "Dancepunk";
		tmpGenres[6] = "Dream Pop";
		tmpGenres[7] = "Emo";
		tmpGenres[8] = "Goth";
		tmpGenres[9] = "Grunge";
		tmpGenres[10] = "Hardcore";
		tmpGenres[11] = "Indie Pop";
		tmpGenres[12] = "Indie Rock";
		tmpGenres[13] = "Industrial";
		tmpGenres[14] = "Lo-Fi";
		tmpGenres[15] = "Modern Rock";
		tmpGenres[16] = "New Wave";
		tmpGenres[17] = "Noise Pop";
		tmpGenres[18] = "Post-Punk";
		tmpGenres[19] = "Power Pop";
		tmpGenres[20] = "Punk";
		tmpGenres[21] = "Ska";
		tmpGenres[22] = "Xtreme";
		
		genres[0] = tmpGenres;
		tmpGenres = new String[8];
		
		tmpGenres[0] = "Blues";
		tmpGenres[1] = "Acoustic Blues";
		tmpGenres[2] = "Cajun/Zydeco";
		tmpGenres[3] = "Chicago Blues";
		tmpGenres[4] = "Contemporary Blues";
		tmpGenres[5] = "Country Blues";
		tmpGenres[6] = "Delta Blues";
		tmpGenres[7] = "Electric Blues";
		
		genres[1] = tmpGenres;
		tmpGenres = new String[12];
		
		tmpGenres[0] = "Classical";
		tmpGenres[1] = "Baroque";
		tmpGenres[2] = "Chamber";
		tmpGenres[3] = "Choral";
		tmpGenres[4] = "Classical Period";
		tmpGenres[5] = "Early Classical";
		tmpGenres[6] = "Impressionist";
		tmpGenres[7] = "Modern";
		tmpGenres[8] = "Opera";
		tmpGenres[9] = "Piano";
		tmpGenres[10] = "Romantic";
		tmpGenres[11] = "Symphony";
		
		genres[2] = tmpGenres;
		tmpGenres = new String[10];
		
		tmpGenres[0] = "Country";
		tmpGenres[1] = "Alt-Country";
		tmpGenres[2] = "Americana";
		tmpGenres[3] = "Bluegrass";
		tmpGenres[4] = "Classic Country";
		tmpGenres[5] = "Contemporary Bluegrass";
		tmpGenres[6] = "Contemporary Country";
		tmpGenres[7] = "Honky Tonk";
		tmpGenres[8] = "Hot Country Hits";
		tmpGenres[9] = "Western";
		
		genres[3] = tmpGenres;
		tmpGenres = new String[8];
		
		tmpGenres[0] = "Decades";
		tmpGenres[1] = "30s";
		tmpGenres[2] = "40s";
		tmpGenres[3] = "50s";
		tmpGenres[4] = "60s";
		tmpGenres[5] = "70s";
		tmpGenres[6] = "80s";
		tmpGenres[7] = "90s";

		genres[4] = tmpGenres;
		tmpGenres = new String[7];
		
		tmpGenres[0] = "Easy Listening";
		tmpGenres[1] = "Exotica";
		tmpGenres[2] = "Light Rock";
		tmpGenres[3] = "Lounge";
		tmpGenres[4] = "Orchestral Pop";
		tmpGenres[5] = "Polka";
		tmpGenres[6] = "Space Age Pop";
		
		genres[5] = tmpGenres;
		tmpGenres = new String[20];
		
		tmpGenres[0] = "Electronic";
		tmpGenres[1] = "Acid House";
		tmpGenres[2] = "Ambient";
		tmpGenres[3] = "Big Beat";
		tmpGenres[4] = "Breakbeat";
		tmpGenres[5] = "Dance";
		tmpGenres[6] = "Demo";
		tmpGenres[7] = "Disco";
		tmpGenres[8] = "Downtempo";
		tmpGenres[9] = "Drum and Bass";
		tmpGenres[10] = "Electro";
		tmpGenres[11] = "Garage";
		tmpGenres[12] = "Hard House";
		tmpGenres[13] = "HouseIDM";
		tmpGenres[14] = "Jungle";
		tmpGenres[15] = "Progressive";
		tmpGenres[16] = "Techno";
		tmpGenres[17] = "Trance";
		tmpGenres[18] = "Tribal";
		tmpGenres[19] = "Trip Hop";
		
		genres[6] = tmpGenres;
		tmpGenres = new String[7];
		
		tmpGenres[0] = "Folk";
		tmpGenres[1] = "Alternative Folk";
		tmpGenres[2] = "Contemporary Folk";
		tmpGenres[3] = "Folk Rock";
		tmpGenres[4] = "New Acoustic";
		tmpGenres[5] = "Traditional Folk";
		tmpGenres[6] = "World Folk";

		genres[7] = tmpGenres;
		tmpGenres = new String[16];
		
		tmpGenres[0] = "Inspirational";
		tmpGenres[1] = "Christian";
		tmpGenres[2] = "Christian Metal";
		tmpGenres[3] = "Christian Rap";
		tmpGenres[4] = "Christian Rock";
		tmpGenres[5] = "Classic Christian";
		tmpGenres[6] = "Contemporary Gospel";
		tmpGenres[7] = "Gospel";
		tmpGenres[8] = "Praise/Worship";
		tmpGenres[9] = "Sermons/Services";
		tmpGenres[10] = "Southern Gospel";
		tmpGenres[11] = "Traditional Gospel";
		tmpGenres[12] = "";
		tmpGenres[13] = "";
		tmpGenres[14] = "";
		tmpGenres[15] = "";
		
		genres[8] = tmpGenres;
		tmpGenres = new String[29];
		
		tmpGenres[0] = "International";
		tmpGenres[1] = "African";
		tmpGenres[2] = "Arabic";
		tmpGenres[3] = "Asian";
		tmpGenres[4] = "Bollywood";
		tmpGenres[5] = "Brazilian";
		tmpGenres[6] = "Caribbean";
		tmpGenres[7] = "Celtic";
		tmpGenres[8] = "Chinese";
		tmpGenres[9] = "European";
		tmpGenres[10] = "Filipino";
		tmpGenres[11] = "French";
		tmpGenres[12] = "Greek";
		tmpGenres[13] = "Hawaiian/Pacific";
		tmpGenres[14] = "Hindi";
		tmpGenres[15] = "Indian";
		tmpGenres[16] = "Japanese";
		tmpGenres[17] = "Jewish";
		tmpGenres[18] = "Klezmer";
		tmpGenres[19] = "Korean";
		tmpGenres[20] = "Mediterranean";
		tmpGenres[21] = "Middle Eastern";
		tmpGenres[22] = "North American";
		tmpGenres[23] = "Soca";
		tmpGenres[24] = "South American";
		tmpGenres[25] = "Tamil";
		tmpGenres[26] = "World";
		tmpGenres[27] = "Worldbeat";
		tmpGenres[28] = "Zouk";
		
		genres[9] = tmpGenres;
		tmpGenres = new String[14];
		
		tmpGenres[0] = "Jazz";
		tmpGenres[1] = "Acid Jazz";
		tmpGenres[2] = "Avant Garde";
		tmpGenres[3] = "Big Band";
		tmpGenres[4] = "Bop";
		tmpGenres[5] = "Classic Jazz";
		tmpGenres[6] = "Cool Jazz";
		tmpGenres[7] = "Fusion";
		tmpGenres[8] = "Hard Bop";
		tmpGenres[9] = "Latin Jazz";
		tmpGenres[10] = "Smooth Jazz";
		tmpGenres[11] = "Swing";
		tmpGenres[12] = "Vocal Jazz";
		tmpGenres[13] = "World Fusion";

		genres[10] = tmpGenres;
		tmpGenres = new String[18];
		
		tmpGenres[0] = "Latin";
		tmpGenres[1] = "Bachata";
		tmpGenres[2] = "Banda";
		tmpGenres[3] = "Bossa Nova";
		tmpGenres[4] = "Cumbia";
		tmpGenres[5] = "Latin Dance";
		tmpGenres[6] = "Latin Pop";
		tmpGenres[7] = "Latin Rap/Hip-Hop";
		tmpGenres[8] = "Latin Rock";
		tmpGenres[9] = "Mariachi";
		tmpGenres[10] = "Merengue";
		tmpGenres[11] = "Ranchera";
		tmpGenres[12] = "Reggaeton";
		tmpGenres[13] = "Regional Mexican";
		tmpGenres[14] = "Salsa";
		tmpGenres[15] = "Tango";
		tmpGenres[16] = "Tejano";
		tmpGenres[17] = "Tropicalia";

		genres[11] = tmpGenres;
		
		return genres;
		
		
		
		 
		
		
	}
}
