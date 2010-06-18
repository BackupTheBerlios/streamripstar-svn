package control;

import java.util.Vector;

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
		String[][] genres = new String[3][];
		
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
		return genres;
	}
}
